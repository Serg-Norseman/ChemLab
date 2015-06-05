/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-1998, 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package chemlab.core.controls.molecule;

import bslib.common.AuxUtils;
import bslib.common.Bitmap;
import bslib.common.INotifyHandler;
import bslib.common.Logger;
import bslib.common.Point;
import bslib.common.Rect;
import chemlab.core.chemical.CompoundSolver;
import chemlab.core.controls.EditorControl;
import chemlab.core.controls.molecule.loader.FilesLoader;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

/**
 * 
 * @author Serg V. Zhdanovskih
 * @since 0.4.0
 */
public class MoleculeMaster extends EditorControl
{
    public final class Selector
    {
        public boolean Active;
        public Rect Region = new Rect();
    }

    private final ArrayList<Molecule> fMolecules;
    private final Timer fTimer;

    private Point cp;
    private int prevx;
    private int prevy;
    private double xtheta;
    private double ytheta;
    private Bitmap fBuffer;

    protected BallsBuffer[] FAtomsHash;

    private MolAtom Current_Atom;
    private MolBond Current_Bond;
    private boolean fUpdating;
    private final Selector fSelector;
    private MasterMode fMasterMode = MasterMode.mm_Edit;

    private final JPopupMenu pmMaster;
    private JMenuItem miAtomAdd;
    private JMenuItem miN2;
    private JMenuItem miChainAdd;
    private JMenuItem miRingAdd;
    private JMenuItem miGroupAdd;
    private JMenuItem miBondAdd;
    private JMenuItem miN3;
    private JMenuItem miAtomDelete;
    private JMenuItem miN4;
    private JMenuItem miAtomProperties;
    private JMenuItem miBondDelete;
    private JMenuItem miN5;
    private JMenuItem miBondProperties;
    private JMenuItem miBendAngleProperties;
    private JMenuItem miTorsionAngleProperties;

    private boolean fAxisVisible;
    private boolean fAutoRotate;

    private int fActiveMoleculeIndex;
    private JComboBox fMoleculesList;

    private CompoundSolver fCompound;
    private ViewOptionSet fViewOptions;
    
    protected MasterListener fMasterListener;

    public final Molecule getActiveMolecule()
    {
        if (this.fActiveMoleculeIndex < 0 || this.fActiveMoleculeIndex >= this.fMolecules.size()) {
            return null;
        }
        
        return this.fMolecules.get(this.fActiveMoleculeIndex);
    }

    public final int getActiveMoleculeIndex()
    {
        return this.fActiveMoleculeIndex;
    }

    public final void setActiveMoleculeIndex(int value)
    {
        this.fActiveMoleculeIndex = value;
        this.Repaint();
        this.changeContent();
    }

    public final boolean getAutoRotate()
    {
        return this.fAutoRotate;
    }

    public final void setAutoRotate(boolean value)
    {
        if (this.fAutoRotate != value) {
            this.fAutoRotate = value;

            if (value) {
                this.fTimer.start();
            } else {
                this.fTimer.stop();
            }
        }
    }

    public final boolean getAxisVisible()
    {
        return this.fAxisVisible;
    }

    public final void setAxisVisible(boolean value)
    {
        this.fAxisVisible = value;
        this.Repaint();
    }

    public final CompoundSolver getCompoundMaster()
    {
        return this.fCompound;
    }

    public final void setCompoundMaster(CompoundSolver value)
    {
        this.fCompound = value;
    }

    public final MasterMode getMasterMode()
    {
        return this.fMasterMode;
    }

    public final void setMasterMode(MasterMode value)
    {
        this.fMasterMode = value;

        switch (this.fMasterMode) {
            case mm_Edit:
                //base.Cursor = (TCursor)TMoleculeMaster.cur_Edit;
                break;
            case mm_Scale:
                //base.Cursor = (TCursor)TMoleculeMaster.cur_Scale;
                break;
            case mm_SpaceMove:
                //base.Cursor = (TCursor)TMoleculeMaster.cur_SpaceMove;
                break;
            case mm_PlaneMove:
                //base.Cursor = (TCursor)TMoleculeMaster.cur_PlaneMove;
                break;
            case mm_SpaceRotate:
                //base.Cursor = (TCursor)TMoleculeMaster.cur_SpaceRotate;
                break;
            case mm_PlaneRotate:
                //base.Cursor = (TCursor)TMoleculeMaster.cur_PlaneRotate;
                break;
        }

        this.prevx = 0;
        this.prevy = 0;
        this.Repaint();
    }

    public final JComboBox getMoleculesList()
    {
        return this.fMoleculesList;
    }

    public final void setMoleculesList(JComboBox value)
    {
        this.fMoleculesList = value;
    }

    private static JMenuItem addMenuItem(JPopupMenu menu, String caption, INotifyHandler clickHandler, int tag)
    {
        JMenuItem result = new JMenuItem();
        menu.add(result);

        result.setText(caption);
        //result.Click += clickHandler;
        //result.Tag = tag;

        return result;
    }

    private void miAtomAddClick(Object sender)
    {
    }

    private void miChainAddClick(Object sender)
    {
    }

    private void miRingAddClick(Object sender)
    {
    }

    private void miGroupAddClick(Object sender)
    {
    }

    private void miAtomDeleteClick(Object sender)
    {
    }

    private void miAtomPropertiesClick(Object sender)
    {
    }

    private void miBondAddClick(Object sender)
    {
    }

    private void miBondDeleteClick(Object sender)
    {
    }

    private void miBondPropertiesClick(Object sender)
    {
    }

    private void pmAtomVisible(boolean value)
    {
        /*this.miBondAdd.Visible = value;
        this.miN3.Visible = value;
        this.miAtomDelete.Visible = value;
        this.miN4.Visible = value;
        this.miAtomProperties.Visible = value;
        this.miBondAdd.Enabled = (this.getActiveMolecule().GetSelectedAtomsCount() == 2);
        this.miAtomProperties.Enabled = (this.getActiveMolecule().GetSelectedAtomsCount() == 1);*/
    }

    private void pmBondVisible(boolean Value)
    {
        /*this.miBondDelete.Visible = Value;
        this.miN5.Visible = Value;
        this.miBondProperties.Visible = Value;
        this.miBondProperties.Enabled = (this.getActiveMolecule().GetSelectedBondsCount() == 1);*/
    }

    private void pmMasterVisible(boolean Value)
    {
        /*this.miAtomAdd.Visible = Value;
        this.miN2.Visible = Value;
        this.miChainAdd.Visible = Value;
        this.miRingAdd.Visible = Value;
        this.miGroupAdd.Visible = Value;
        this.miChainAdd.Enabled = false;
        this.miGroupAdd.Enabled = false;*/
    }

    private void pmBendAngleVisible(boolean Value)
    {
        //this.miBendAngleProperties.Visible = Value;
    }

    private void pmTorsionAngleVisible(boolean Value)
    {
        //this.miTorsionAngleProperties.Visible = Value;
    }

    private void tickTime()
    {
        if (this.fAutoRotate) {
            Molecule molecule = this.getActiveMolecule();
            
            molecule.tmat.init();
            molecule.tmat.rotX(5.0);
            molecule.tmat.rotY(5.0);

            for (int i = 0; i < molecule.fAtoms.size(); i++) {
                molecule.getAtom(i).amat.mult(molecule.tmat);
            }

            this.Repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D) g;

        try {
            if (this.getActiveMolecule().fAtoms.size() > 0) {
                this.getActiveMolecule().draw(gr);
            }

            if (this.fSelector.Active) {
                gr.setColor(AuxUtils.BGRToRGB(16776960));
                gr.setStroke(new BasicStroke(1));
                //pen.DashStyle = DashStyle.Dot;
                Rect rt = this.fSelector.Region;
                gr.drawRect(rt.Left, rt.Top, rt.getWidth(), rt.getHeight());
            }
        } catch (Exception ex) {
            Logger.write("MoleculeMaster.OnPaint(): " + ex.getMessage());
        }
    }

    private void pmMasterPopup(Object sender)
    {
        Molecule activeMolecule = this.getActiveMolecule();
        this.pmMasterVisible(activeMolecule.getSelectedAtomsCount() == 0 && activeMolecule.getSelectedBondsCount() == 0);
        this.pmAtomVisible(activeMolecule.getSelectedAtomsCount() > 0 && activeMolecule.getSelectedBondsCount() == 0);
        this.pmBondVisible(activeMolecule.getSelectedAtomsCount() == 0 && activeMolecule.getSelectedBondsCount() > 0);
        this.pmBendAngleVisible(activeMolecule.getSelectedAtomsCount() == 3);
        this.pmTorsionAngleVisible(activeMolecule.getSelectedAtomsCount() == 4);
    }

    private void changeSelector()
    {
        /*Rect rt = this.fSelector.Region.clone();
        if (rt.Left > rt.Right) {
            int t = rt.Left;
            rt.setLocation(rt.Right;
            rt.Right = t;
        }
        if (rt.Top > rt.Bottom) {
            int t = rt.Top;
            rt.Top = rt.Bottom;
            rt.Bottom = t;
        }

        this.BeginUpdate();

        Molecule activeMolecule = this.getActiveMolecule();

        for (int i = 0; i < activeMolecule.FAtoms.size(); i++) {
            MolAtom atom = activeMolecule.GetAtom(i);

            int tx = atom.getTX();
            int ty = atom.getTY();
            if (tx >= rt.Left && tx <= rt.Right && ty >= rt.Top && ty <= rt.Bottom) {
                atom.setSelected(true);
            } else {
                atom.setSelected(false);
            }
        }

        this.EndUpdate();*/
    }

    public final ViewOptionSet getViewOptions()
    {
        return this.fViewOptions;
    }

    public final void setViewOptions(ViewOptionSet value)
    {
        if (!this.fViewOptions.equals(value)) {
            this.fViewOptions = value;
            this.Repaint();
        }
    }

    private void clearBallsBuffer()
    {
        int i = 1;
        do {
            int j = 2;
            do {
                Bitmap ball = this.FAtomsHash[i - 1].Balls[j - 2];
                if (ball != null) {
                    //ball.dispose();
                }
                j++;
            } while (j != 16);
            i++;
        } while (i != 112);
    }

    public final Molecule getMolecule(int index)
    {
        Molecule result = null;
        if (index >= 0 && index < this.fMolecules.size()) {
            result = this.fMolecules.get(index);
        }
        return result;
    }

    public final int getMoleculesCount()
    {
        return this.fMolecules.size();
    }

    @Override
    public void DoChange()
    {
        if (!this.fUpdating) {
            super.DoChange();
        }
    }

    protected void onMouseDown(MouseEvent e)
    {
        if (this.fMasterMode != MasterMode.mm_Edit) {
            this.Current_Atom = null;
            this.prevx = e.getX();
            this.prevy = e.getY();
        } else {
            this.Current_Atom = this.getActiveMolecule().findAtomByCoord(e.getX(), e.getY());
            if (this.Current_Atom == null) {
                this.Current_Bond = this.getActiveMolecule().findBondByCoord(e.getX(), e.getY());
            } else {
                this.Current_Bond = null;
                this.prevx = e.getX();
                this.prevy = e.getY();
            }

            if (this.Current_Atom != null || this.Current_Bond != null) {
                if (this.Current_Atom != null) {
                    this.Current_Atom.setSelected(true);
                }

                if (this.Current_Bond != null) {
                    this.Current_Bond.setSelected(true);
                }
            } else {
                this.getActiveMolecule().clearSelected();
            }

            if (this.Current_Atom == null && this.Current_Bond == null && e.getButton() == MouseEvent.BUTTON1) {
                this.fSelector.Active = true;
                this.fSelector.Region = new Rect(e.getX(), e.getY(), e.getX(), e.getY());
                this.Repaint();
            }
        }
    }

    protected void onMouseMove(MouseEvent e)
    {
        
    }

    protected void onMouseDrag(MouseEvent e)
    {
        Molecule activeMolecule = this.getActiveMolecule();
        switch (this.fMasterMode) {
            case mm_Edit:
                if (this.Current_Atom != null && this.prevx != 0 && this.prevy != 0) {
                    int dX = e.getX() - this.prevx;
                    int dY = e.getY() - this.prevy;
                    activeMolecule.tmat.init();
                    activeMolecule.tmat.translate((double) dX, (double) dY, 0.0);
                    if (activeMolecule.getSelectedAtomsCount() == 0) {
                        this.Current_Atom.amat.mult(activeMolecule.tmat);
                    } else {
                        for (int i = 0; i < activeMolecule.getSelectedAtomsCount(); i++) {
                            activeMolecule.getSelectedAtom(i).amat.mult(activeMolecule.tmat);
                        }
                    }

                    this.Repaint();
                    this.prevx = e.getX();
                    this.prevy = e.getY();
                }
                /*if (this.fSelector.Active) {
                    this.fSelector.Region.Right = e.X;
                    this.fSelector.Region.Bottom = e.Y;
                    this.SelectorChange();
                    this.Repaint();
                }*/
                break;

            case mm_Scale:
                if (this.prevx != 0 && this.prevy != 0) {
                    double scFactor;
                    if (Math.abs(e.getY() - this.prevy) > Math.abs(e.getX() - this.prevx)) {
                        scFactor = ((double) e.getY() / (double) this.prevy);
                    } else {
                        scFactor = ((double) e.getX() / (double) this.prevx);
                    }
                    activeMolecule.tmat.init();
                    activeMolecule.tmat.scale(scFactor);

                    for (int i = 0; i < activeMolecule.fAtoms.size(); i++) {
                        activeMolecule.getAtom(i).amat.mult(activeMolecule.tmat);
                    }

                    activeMolecule.axmat.mult(activeMolecule.tmat);
                    this.Repaint();
                    this.prevx = e.getX();
                    this.prevy = e.getY();
                }
                break;

            case mm_SpaceMove:
                if (this.prevx != 0 && this.prevy != 0) {
                    int dY = e.getY() - this.prevy;
                    activeMolecule.tmat.init();
                    activeMolecule.tmat.translate(0.0, 0.0, (double) dY);

                    for (int i = 0; i < activeMolecule.fAtoms.size(); i++) {
                        activeMolecule.getAtom(i).amat.mult(activeMolecule.tmat);
                    }

                    activeMolecule.axmat.mult(activeMolecule.tmat);
                    this.Repaint();
                    this.prevx = e.getX();
                    this.prevy = e.getY();
                }
                break;

            case mm_PlaneMove:
                if (this.prevx != 0 && this.prevy != 0) {
                    int dX = e.getX() - this.prevx;
                    int dY = e.getY() - this.prevy;
                    activeMolecule.tmat.init();
                    activeMolecule.tmat.translate((double) dX, (double) dY, 0.0);

                    for (int i = 0; i < activeMolecule.fAtoms.size(); i++) {
                        activeMolecule.getAtom(i).amat.mult(activeMolecule.tmat);
                    }

                    activeMolecule.axmat.mult(activeMolecule.tmat);
                    this.Repaint();
                    this.prevx = e.getX();
                    this.prevy = e.getY();
                }
                break;

            case mm_SpaceRotate:
                if (this.prevx != 0 && this.prevy != 0) {
                    activeMolecule.tmat.init();
                    this.xtheta = ((double) (e.getY() - this.prevy) * (360.0 / (double) super.getWidth()));
                    this.ytheta = ((double) (e.getX() - this.prevx) * (360.0 / (double) super.getHeight()));
                    activeMolecule.tmat.rotX(this.xtheta);
                    activeMolecule.tmat.rotY(this.ytheta);

                    for (int i = 0; i < activeMolecule.fAtoms.size(); i++) {
                        activeMolecule.getAtom(i).amat.mult(activeMolecule.tmat);
                    }

                    activeMolecule.axmat.mult(activeMolecule.tmat);
                    this.Repaint();
                    this.prevx = e.getX();
                    this.prevy = e.getY();
                }
                break;

            case mm_PlaneRotate:
                if (this.prevx != 0 && this.prevy != 0) {
                    activeMolecule.tmat.init();
                    this.ytheta = ((double) (e.getX() - this.prevx) * (360.0 / (double) super.getHeight()));
                    activeMolecule.tmat.rotZ(this.ytheta);

                    for (int i = 0; i < activeMolecule.fAtoms.size(); i++) {
                        activeMolecule.getAtom(i).amat.mult(activeMolecule.tmat);
                    }

                    activeMolecule.axmat.mult(activeMolecule.tmat);
                    this.Repaint();
                    this.prevx = e.getX();
                    this.prevy = e.getY();
                }
                break;
        }
    }

    protected void onMouseUp(MouseEvent e)
    {
        if (this.fMasterMode != MasterMode.mm_Edit) {
            this.prevx = 0;
            this.prevy = 0;
        } else {
            if (e.getButton() != MouseEvent.BUTTON1) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    this.Current_Atom = this.getActiveMolecule().findAtomByCoord(e.getX(), e.getY());
                    this.Current_Bond = this.getActiveMolecule().findBondByCoord(e.getX(), e.getY());

                    this.prevx = e.getX();
                    this.prevy = e.getY();
                    this.pmMaster.show(this, e.getX(), e.getY());
                }
            } else {
                this.fSelector.Active = false;
                this.fSelector.Region = new Rect(0, 0, 0, 0);
                this.Repaint();
            }

            this.Current_Atom = null;
            this.Current_Bond = null;

            this.Repaint();
        }
    }

    public MoleculeMaster()
    {
        super();
        this.fViewOptions = new ViewOptionSet();
        this.fSelector = new Selector();
        this.cp = new Point();

        BallsBuffer[] ballsBuffers = new BallsBuffer[111];
        for (int num = 0; num < ballsBuffers.length; num++) {
            ballsBuffers[num] = new BallsBuffer();
        }
        this.FAtomsHash = ballsBuffers;

        this.fMolecules = new ArrayList<>();

        //this.FBuffer = new Bitmap();
        
        this.fTimer = new Timer(50, (ActionEvent e) -> {
            tickTime();
        });

        this.setDoubleBuffered(true);
        this.setBackground(Color.black);

	//Forms.Screen.set_Cursors(TMoleculeMaster.cur_Edit, Windows.LoadCursor(WinUtils.HInstance(), "cur_Edit"));
        //Forms.Screen.set_Cursors(TMoleculeMaster.cur_Scale, Windows.LoadCursor(WinUtils.HInstance(), "cur_Scale"));
        //Forms.Screen.set_Cursors(TMoleculeMaster.cur_PlaneMove, Windows.LoadCursor(WinUtils.HInstance(), "cur_Plane_Move"));
        //Forms.Screen.set_Cursors(TMoleculeMaster.cur_SpaceMove, Windows.LoadCursor(WinUtils.HInstance(), "cur_Space_Move"));
        //Forms.Screen.set_Cursors(TMoleculeMaster.cur_SpaceRotate, Windows.LoadCursor(WinUtils.HInstance(), "cur_Space_Rotate"));
        //Forms.Screen.set_Cursors(TMoleculeMaster.cur_PlaneRotate, Windows.LoadCursor(WinUtils.HInstance(), "cur_Plane_Rotate"));
        super.setSize(105, 105);

        this.setViewOptions(new ViewOptionSet(ViewOption.mvoAtomSign, ViewOption.mvoBalls, ViewOption.mvoSticks));

        this.pmMaster = new JPopupMenu();
        //this.pmMaster.Popup += this.pmMasterPopup;

        /*this.miAtomAdd = AddMenuItem(this.pmMaster, "Добавить атом", this.miAtomAddClick, 0);
        this.miAtomAdd.Enabled = false;
        this.miN2 = AddMenuItem(this.pmMaster, "-", null, 0);
        this.miChainAdd = AddMenuItem(this.pmMaster, "Добавить цепь", this.miChainAddClick, 0);
        this.miChainAdd.Enabled = false;
        this.miRingAdd = AddMenuItem(this.pmMaster, "Добавить кольцо", this.miRingAddClick, 0);
        this.miRingAdd.Enabled = false;
        this.miGroupAdd = AddMenuItem(this.pmMaster, "Добавить группу", this.miGroupAddClick, 0);
        this.miGroupAdd.Enabled = false;
        this.miBondAdd = AddMenuItem(this.pmMaster, "Добавить связь", this.miBondAddClick, 0);
        this.miBondAdd.Enabled = false;
        this.miN3 = AddMenuItem(this.pmMaster, "-", null, 0);
        this.miAtomDelete = AddMenuItem(this.pmMaster, "Удалить атом", this.miAtomDeleteClick, 0);
        this.miN4 = AddMenuItem(this.pmMaster, "-", null, 0);
        this.miAtomProperties = AddMenuItem(this.pmMaster, "Свойства атома", this.miAtomPropertiesClick, 0);
        this.miAtomProperties.Enabled = false;
        this.miBondDelete = AddMenuItem(this.pmMaster, "Удалить связь", this.miBondDeleteClick, 0);
        this.miN5 = AddMenuItem(this.pmMaster, "-", null, 0);
        this.miBondProperties = AddMenuItem(this.pmMaster, "Свойства связи", this.miBondPropertiesClick, 0);
        this.miBondProperties.Enabled = false;*/

        this.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                onMouseDrag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                onMouseMove(e);
            }
        });
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                onMouseDown(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                onMouseUp(e);
            }
        });

        this.addMolecule().setName("default molecule");
    }

    /*@Override
    protected void dispose(boolean disposing)
    {
        if (disposing) {
            this.pmMaster.Dispose();
            this.fTimer.Dispose();
            if (this.FBuffer != null) {
                this.FBuffer.Dispose();
            }

            this.clearBallsBuffer();
            this.Clear();

            this.fMolecules = null;
        }
        super.dispose(disposing);
    }*/

    public final void beginUpdate()
    {
        this.fUpdating = true;
    }

    public final void clear()
    {
        for (int i = 0; i < this.fMolecules.size(); i++) {
            this.getMolecule(i).dispose();
        }
    }

    public final void changeContent()
    {
        /*if (this.fMoleculesList != null) {
            this.fMoleculesList.BeginUpdate();
            this.fMoleculesList.Items.Clear();

            for (int i = 0; i < this.fMolecules.size(); i++) {
                this.fMoleculesList.Items.Add(this.getMolecule(i).getName());
            }
            this.fMoleculesList.EndUpdate();
            //this.FMoleculesList.ItemIndex = this.FActiveMoleculeIndex;
        }*/
        
        this.Repaint();
    }

    public final void endUpdate()
    {
        this.fUpdating = false;
        this.Repaint();
    }

    @Override
    public void loadFromFile(String fileName)
    {
        this.beginUpdate();

        Molecule activeMolecule = this.getActiveMolecule();
        activeMolecule.clear();

        String ext = AuxUtils.getFileExt(fileName).toLowerCase();
        switch (ext) {
            case ".xyz":
                FilesLoader.loadFromXYZ(fileName, activeMolecule);
                break;
            case ".mm1gp":
                FilesLoader.loadFromMM1GP(fileName, activeMolecule);
                break;
            case ".hin":
                FilesLoader.loadFromHIN(fileName, activeMolecule);
                break;
            case ".mcm":
                FilesLoader.loadFromMCM(fileName, activeMolecule);
                break;
            case ".sdf":
            case ".sd":
            case ".mdl":
            case ".mol":
                FilesLoader.loadFromMDL(fileName, activeMolecule);
                break;
        }

        this.fitToWnd();
        this.endUpdate();
        this.changeContent();
    }

    @Override
    public void saveToFile(String fileName)
    {
    }

    public final void Repaint()
    {
        if (!this.fUpdating) {
            super.repaint();
        }
    }

    public final Molecule addMolecule()
    {
        Molecule result = new Molecule(this);
        this.fMolecules.add(result);
        this.setActiveMoleculeIndex(this.fMolecules.size() - 1);
        this.changeContent();
        return result;
    }

    public final void deleteMolecule(int index)
    {
        this.fMolecules.get(index).dispose();
        this.fMolecules.remove(index);
        this.changeContent();
    }

    public void fitToWnd()
    {
        Molecule molecule = this.getActiveMolecule();
        if (molecule != null) {
            molecule.fitToWnd(this.getWidth(), this.getHeight());
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);

        this.cp.X = width / 2;
        this.cp.Y = height / 2;

        Molecule molecule = this.getActiveMolecule();
        if (molecule != null) {
            //molecule.fitToWnd(width, height);
        }
    }

    private static int blend(int fg, int bg, double fgfactor)
    {
        return (int) (bg + (fg - bg) * fgfactor);
    }

    public static Bitmap createSphere(int rad, Color color)
    {
        Bitmap ball = null;

        if (rad > 0) {
            Color[] colors = new Color[101];
            int dd = rad * 2;
            int h = (int) (Math.round((rad * 0.375f)));

            int rl = color.getRed();
            int gl = color.getGreen();
            int bl = color.getBlue();

            int[] data = new int[dd * dd];
            int maxr = 0;

            for (int yy = dd; yy >= 0; yy--) {
                int x0 = (int) (Math.round(Math.sqrt(rad * rad - (yy - rad) * (yy - rad))));
                int p = yy * dd + rad - x0;

                for (int xx = -x0 + 1; xx <= x0 - 1; xx++) {
                    int x = xx + h;
                    int y = yy - rad + h;
                    int r = (int) Math.round((Math.sqrt(x * x + y * y) + 0.5f));
                    if (r > maxr) {
                        maxr = r;
                    }
                    if (r <= 0) {
                        r = 0;
                    }
                    p++;
                    data[p] = r;
                }
            }

            colors[0] = new Color(192, 192, 192);

            for (int i = maxr; i >= 1; i--) {
                double d = (i / (double) maxr);
                colors[i] = AuxUtils.newColor(
                        blend(blend(rl, 255, d), 192, 1.0), 
                        blend(blend(gl, 255, d), 192, 1.0), 
                        blend(blend(bl, 255, d), 192, 1.0));
            }

            Bitmap ballImage = new Bitmap(dd, dd);
            Graphics2D canvas = ballImage.createGraphics();
            canvas.setColor(Color.black);
            canvas.fillRect(0, 0, dd, dd);

            Color rr = colors[maxr / 2];
            canvas.setColor(rr);
            canvas.fillOval(0, 0, dd, dd);

            for (int xx = 0; xx < dd; xx++) {
                for (int yy = 0; yy < dd; yy++) {
                    ballImage.setRGB(xx, yy, colors[data[yy * dd + xx]].getRGB());
                }
            }

            ball = Bitmap.makeTransparent(ballImage);
        }
        
        return ball;
    }

    public final MasterListener getMasterListener()
    {
        return this.fMasterListener;
    }

    public final void setMasterListener(MasterListener value)
    {
        this.fMasterListener = value;
    }
}
