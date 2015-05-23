package chemlab.core.controls.decay;

import bslib.common.AuxUtils;
import bslib.common.RefObject;
import chemlab.core.chemical.CLData;
import chemlab.core.chemical.DecayMode;
import chemlab.core.chemical.DecayModeSet;
import chemlab.refbooks.DecayRecord;
import chemlab.refbooks.NuclideRecord;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class DecayViewer extends JPanel
{
    private NuclideNode fRootNuclide;

    private int fMaxHeight;
    private int fMaxWidth;
    
    public DecayViewer()
    {
        super();
        this.setBackground(Color.white);
        this.setLayout(null);
    }

    /*@Override
    protected void Dispose(boolean disposing)
    {
        if (disposing) {
            this.destroyDecay(this.fRootNuclide);
        }
        super.Dispose(disposing);
    }*/

    public final void preview(String rootNuclide)
    {
        try {
            this.destroyDecay(this.fRootNuclide);

            this.fRootNuclide = addNode(rootNuclide);

            this.createDecay(this.fRootNuclide);

            int y = 32;
            RefObject<Integer> refX = new RefObject<>(40);
            RefObject<Boolean> refIsInc = new RefObject<>(false);

            this.setNode(this.fRootNuclide, refX, y, refIsInc);
            
            this.setPreferredSize(new Dimension(this.fMaxWidth + 40, this.fMaxHeight + 32));
            this.revalidate();
        } finally {
            this.repaint();
        }
    }

    private static NuclideNode addNode(String nuclide)
    {
        NuclideNode result = new NuclideNode();
        result.Nuclide = nuclide;
        
        for (DecayMode dm : DecayMode.values()) {
            result.Descendants[dm.getValue()] = null;
        }
        return result;
    }

    private void createDecay(NuclideNode node)
    {
        NuclideRecord nucRec = CLData.NuclidesBook.getNuclideBySign(node.Nuclide);
        node.Record = nucRec;

        ArrayList<DecayRecord> decays = CLData.DecayBook.getDecayByNuclide(nucRec.NuclideId);
        for (DecayRecord decRec : decays) {
            DecayMode dm = decRec.Mode;

            NuclideRecord descRec = CLData.NuclidesBook.getNuclideById(decRec.DescendantId);
            
            node.Descendants[dm.getValue()] = addNode(descRec.Sign);

            this.createDecay(node.Descendants[dm.getValue()]);
        }
    }

    private void destroyDecay(NuclideNode node)
    {
        if (node != null) {
            for (DecayMode dm : DecayMode.values()) {
                if (node.Descendants[dm.getValue()] != null) {
                    this.destroyDecay(node.Descendants[dm.getValue()]);
                }
            }
        }

        if (node != null) {
            node.dispose();
        }
    }

    private void setRite(int X1, int X2, int Y)
    {
        int i = X1;
        if (i <= X2) {
            do {
                DecayShape shape = this.createShape(i, Y);
                shape.Id = ShapeId.RiteLine;
                if (i == X1) {
                    shape.Id = ShapeId.RiteTail;
                }
                if (i == X2) {
                    shape.Id = ShapeId.RiteHead;
                }
                shape.setType(DecayShape.ShapeType.dskRite);
                i += 32;
            } while (i <= X2);
        }
    }

    private void createLabel(int left, int top, String caption)
    {
        Graphics gr = this.getGraphics();
        FontMetrics metrics = gr.getFontMetrics();
        int tw = metrics.stringWidth(caption);
        int th = metrics.getHeight();

        JLabel result = new JLabel();
        result.setForeground(Color.black);
        result.setFont(this.getFont());
        result.setText(caption);
        result.setLocation(left - (int) tw, top - (int) (th / 2)); // 
        this.add(result);
    }

    private DecayShape createShape(int left, int top)
    {
        DecayShape shape = new DecayShape(this, left, top);
        this.add(shape);
        return shape;
    }

    private void checkBounds(DecayShape shape)
    {
        int x2 = shape.getX() + shape.getWidth();
        int y2 = shape.getY() + shape.getHeight();
        
        if (this.fMaxWidth < x2) this.fMaxWidth = x2;
        if (this.fMaxHeight < y2) this.fMaxHeight = y2;
    }
    
    private void setNode(NuclideNode node, RefObject<Integer> refX, int Y, RefObject<Boolean> refIsInc)
    {
        int descCnt = 0;
        int iPreX = 0;
        if (node != null) {
            DecayModeSet decayModes = node.Record.DecayModes;
            if (decayModes.contains(DecayMode.dmStable)) {
                DecayShape shape = this.createShape(refX.argValue, Y);
                shape.NuclideNode = node;
                shape.setType(DecayShape.ShapeType.dskNuclide);

                String text = AuxUtils.FloatToStr(node.Record.Weight);
                String text2 = AuxUtils.FloatToStr(node.Record.Spin);
                String hint = String.format("Вес: %1$s; Спин: %2$s; Период полураспада: %3$s", text, text2, node.Record.HalfLife);
                shape.setToolTipText(hint);

                this.checkBounds(shape);
                
                this.createLabel(refX.argValue, Y, node.Nuclide);
                refIsInc.argValue = false;
            } else {
                for (DecayMode dm : DecayMode.values()) {
                    if (node.Descendants[dm.getValue()] != null) {
                        descCnt++;
                        if (descCnt > 1 && iPreX < refX.argValue) {
                            this.setRite(iPreX + 32, refX.argValue - 32, Y);
                        }

                        DecayShape shape = this.createShape(refX.argValue + 32, Y - 32);
                        shape.Mode = dm;
                        shape.setType(DecayShape.ShapeType.dskDecayMode);
                        shape.setToolTipText(CLData.DecayMode[dm.getValue()].Name);
                        this.checkBounds(shape);
                        
                        shape = this.createShape(refX.argValue, Y);
                        shape.NuclideNode = node;
                        shape.setType(DecayShape.ShapeType.dskNuclide);
                        this.checkBounds(shape);

                        String text3 = AuxUtils.FloatToStr(node.Record.Weight);
                        String text4 = AuxUtils.FloatToStr(node.Record.Spin);
                        String hint = String.format("Вес: %1$s; Спин: %2$s; Период полураспада: %3$s", text3, text4, node.Record.HalfLife);
			shape.setToolTipText(hint);

                        this.createLabel(refX.argValue, Y, node.Nuclide);

                        shape = this.createShape(refX.argValue, Y + shape.getHeight());
                        shape.Id = ShapeId.DownShrt;
                        shape.setType(DecayShape.ShapeType.dskDown);
                        iPreX = refX.argValue;
                        this.checkBounds(shape);

                        this.setNode(node.Descendants[dm.getValue()], refX, Y + 64, refIsInc);
                        
                        int cnt = node.getDescendantsCount();
                        if (cnt > 1 && !refIsInc.argValue) {
                            refX.argValue += 96;
                            refIsInc.argValue = !refIsInc.argValue;
                        }
                    }
                }
            }
        }
    }
}
