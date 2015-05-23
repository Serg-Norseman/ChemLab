package chemlab.core.controls.molecule;

import bslib.common.AuxUtils;
import bslib.common.Bitmap;
import bslib.common.INotifyHandler;
import bslib.common.Logger;
import bslib.common.Point;
import bslib.common.Rect;
import bslib.common.RefObject;
import bslib.common.StringHelper;
import chemlab.core.chemical.BondKind;
import chemlab.core.chemical.CompoundSolver;
import chemlab.core.controls.EditorControl;
import chemlab.core.chemical.CLData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

public class MoleculeMaster extends EditorControl
{
    public final class Selector
    {
        public boolean Active;
        public Rect Region = new Rect();
    }

    private static short CurrentVersion;
    private static int cur_Edit;
    private static int cur_Scale;
    private static int cur_PlaneMove;
    private static int cur_SpaceMove;
    private static int cur_SpaceRotate;
    private static int cur_PlaneRotate;
    private static int cur_NOne;
    private static int cur_NTwo;
    private static int cur_NThree;
    private static int cur_POne;
    private static int cur_PTwo;
    private static int cur_PThree;

    private final ArrayList<Molecule> fMolecules;
    private Point cp;
    private int prevx;
    private int prevy;
    private double xtheta;
    private double ytheta;
    private Bitmap FBuffer;
    private MoleculeHeader fHeader;

    public BallsBuffer[] FAtomsHash;

    private MolAtom Current_Atom;
    private MolBond Current_Bond;
    private boolean fUpdating;
    private final Selector fSelector;
    private MasterMode fMasterMode = getMasterMode().values()[0];

    private JPopupMenu pmMaster;
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
    private Timer fTimer;
    private boolean fAutoRotate;

    private int fActiveMoleculeIndex;
    private JComboBox fMoleculesList;

    public INotifyHandler fMoleculeProperties;
    private AtomPropertiesHandler fAtomProperties;
    private BondPropertiesHandler fBondProperties;

    private CompoundSolver fCompound;
    private ViewOptionSet fViewOptions;

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

    private String GetAuthor()
    {
        return this.fHeader.Author;
    }

    private String GetComments()
    {
        return this.fHeader.Comments;
    }

    private String GetSubject()
    {
        return this.fHeader.Subject;
    }

    private String GetTitle()
    {
        return this.fHeader.Title;
    }

    private void SetAuthor(String value)
    {
        this.fHeader.Author = value;
    }

    private void SetComments(String value)
    {
        this.fHeader.Comments = value;
    }

    private void SetSubject(String value)
    {
        this.fHeader.Subject = value;
    }

    private void SetTitle(String value)
    {
        this.fHeader.Title = value;
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

    protected void OnMouseDown(MouseEvent e)
    {
        MasterMode fMasterMode = this.fMasterMode;
        if (fMasterMode != MasterMode.mm_Edit) {
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

    protected void OnMouseMove(MouseEvent e)
    {
        
    }

    protected void OnMouseDrag(MouseEvent e)
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

    protected void OnMouseUp(MouseEvent e)
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
        
        this.fTimer = new Timer(50, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                tickTime();
            }
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
                OnMouseDrag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                OnMouseMove(e);
            }
        });
        
        this.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                OnMouseDown(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                OnMouseUp(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
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
        this.getActiveMolecule().clear();
        this.beginUpdate();

        String ext = AuxUtils.getFileExt(fileName).toLowerCase();
        switch (ext) {
            case ".xyz":
                this.loadFromXYZ(fileName);
                break;
            case ".mm1gp":
                this.loadFromMM1GP(fileName);
                break;
            case ".hin":
                this.loadFromHIN(fileName);
                break;
            case ".mcm":
                this.loadFromMCM(fileName);
                break;
            case ".sdf":
            case ".sd":
            case ".mdl":
            case ".mol":
                this.loadFromMDL(fileName);
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

    private static double convertXYZCoord(String coord) throws ParseException
    {
        StringBuilder sb = new StringBuilder(coord);
        if (sb.indexOf("-.") == 0) {
            sb.insert(1, "0");
        }
        return (AuxUtils.ParseFloat(sb.toString(), 0.0f));
    }

    public final void loadFromXYZ(String fileName)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line) && line.charAt(0) != '#') {
                        line = AuxUtils.prepareString(line);
                        String[] parts = line.trim().split("[ ]", -1);

                        String sym = parts[0];
                        double xx = convertXYZCoord(parts[1]);
                        double yy = convertXYZCoord(parts[2]);
                        double zz = convertXYZCoord(parts[3]);

                        MolAtom curAtom = this.getActiveMolecule().addAtom();
                        curAtom.setSign(sym);
                        curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(sym));
                        curAtom.setX(xx);
                        curAtom.setY(yy);
                        curAtom.setZ(zz);
                    }

                    line = tf.readLine();
                }

                String title = (new java.io.File(fileName)).getName();
                this.getActiveMolecule().setName(title.substring(0, title.indexOf(".")));
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    public final void loadFromMM1GP(String fileName)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                int aCnt = 0;
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line)) {
                        line = AuxUtils.prepareString(line);
                        String[] params;

                        if (line.indexOf("!Atoms") == 0) {
                            params = AuxUtils.getParams(line);
                            aCnt = AuxUtils.ParseInt(params[1], 0);

                            for (int i = 0; i < aCnt; i++) {
                                line = tf.readLine();
                                params = AuxUtils.getParams(line);

                                int eNum = AuxUtils.ParseInt(params[1], 0);
                                if (eNum >= -1 && eNum <= 118) {
                                    MolAtom curAtom = this.getActiveMolecule().addAtom();
                                    curAtom.setSign(CLData.ElementsBook.get(eNum - -1).FSymbol);
                                    curAtom.setElementNumber(eNum);
                                }
                            }
                        }

                        if (line.indexOf("!Bonds") == 0) {
                            params = AuxUtils.getParams(line);
                            int bCnt = AuxUtils.ParseInt(params[1], 0);
                            if (bCnt > 0) {
                                for (int i = 0; i < bCnt; i++) {
                                    line = tf.readLine();

                                    params = AuxUtils.getParams(line);
                                    int aNum = AuxUtils.ParseInt(params[0], 0);
                                    int aNum2 = AuxUtils.ParseInt(params[1], 0);
                                    MolBond curBond = this.getActiveMolecule().addBond(this.getActiveMolecule().getAtom(aNum), this.getActiveMolecule().getAtom(aNum2), BondKind.bkSingle);
                                    curBond.setKindByChar(params[2].charAt(0));
                                }
                            }
                        }

                        if (line.indexOf("!Coord") == 0) {
                            for (int i = 0; i < aCnt; i++) {
                                line = tf.readLine();
                                params = AuxUtils.getParams(line);

                                int aNum = AuxUtils.ParseInt(params[0], 0);
                                MolAtom atom = this.getActiveMolecule().getAtom(aNum);
                                atom.setX(AuxUtils.ParseFloat(params[1], 0));
                                atom.setY(AuxUtils.ParseFloat(params[2], 0));
                                atom.setZ(AuxUtils.ParseFloat(params[3], 0));
                            }
                        }

                        if (line.indexOf("!Charges") == 0) {
                            for (int i = 0; i < aCnt; i++) {
                                line = tf.readLine();

                                params = AuxUtils.getParams(line);
                                int aNum = AuxUtils.ParseInt(params[0], 0);
                                this.getActiveMolecule().getAtom(aNum).setPartialCharge((float) (AuxUtils.ParseFloat(params[1], 0)));
                            }
                        }
                    }

                    line = tf.readLine();
                }

                String title = (new java.io.File(fileName)).getName();
                this.getActiveMolecule().setName(title.substring(0, title.indexOf(".")));
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    public final void loadFromHIN(String fileName)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                Molecule molecule = this.getActiveMolecule();
                ArrayList<HINBond> tmpBonds = new ArrayList<>();
                int i;
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line)) {
                        line = AuxUtils.prepareString(line);
                        String[] params;

                        if (line.indexOf("mol") == 0) {
                            params = AuxUtils.getParams(line);
                            int mol = AuxUtils.ParseInt(params[1], 0);
                            if (mol > 1) {
                                break; // файлы с двумя и более молекулами не отрабатываем
                            }
                        }

                        if (line.indexOf("endmol") == 0) {
                            break;
                        }

                        if (line.indexOf("atom") == 0) {
                            params = AuxUtils.getParams(line);
                            if (params.length <= 11) {
                                break;
                            }

                            MolAtom curAtom = this.getActiveMolecule().addAtom();
                            curAtom.setSign(params[3]);
                            curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(params[3]));
                            curAtom.setX(AuxUtils.ParseFloat(params[7], 0));
                            curAtom.setY(AuxUtils.ParseFloat(params[8], 0));
                            curAtom.setZ(AuxUtils.ParseFloat(params[9], 0));

                            int bonds = AuxUtils.ParseInt(params[10], 0);
                            i = 11;
                            for (int k = 1; k <= bonds; k++) {
                                HINBond hinBond = new HINBond();
                                tmpBonds.add(hinBond);
                                char c = params[i + 1].charAt(0);
                                switch (c) {
                                    case 'a':
                                        hinBond.bt = BondKind.bkConjugated;
                                        break;
                                    case 'd':
                                        hinBond.bt = BondKind.bkDouble;
                                        break;
                                    case 's':
                                        hinBond.bt = BondKind.bkSingle;
                                        break;
                                    case 't':
                                        hinBond.bt = BondKind.bkTriple;
                                        break;
                                }
                                hinBond.F = AuxUtils.ParseInt(params[1], 0);
                                hinBond.T = AuxUtils.ParseInt(params[i], 0);
                                i += 2;
                            }
                        }
                    }
                    
                    line = tf.readLine();
                }

                for (i = 0; i < tmpBonds.size(); i++) {
                    molecule.addBond(molecule.getAtom(tmpBonds.get(i).F - 1), molecule.getAtom(tmpBonds.get(i).T - 1), tmpBonds.get(i).bt);
                }

                String title = (new java.io.File(fileName)).getName();
                this.getActiveMolecule().setName(title.substring(0, title.indexOf(".")));
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    public final void loadFromMDL(String fileName)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                Molecule molecule = this.getActiveMolecule();
                int line = 0;
                int aCnt = 0;
                int bCnt = 0;
                String sline = tf.readLine();
                while (sline != null) {
                    sline = sline.trim();
                    line++;

                    if (!StringHelper.isNullOrEmpty(sline)) {
                        sline = AuxUtils.prepareString(sline);

                        if (line == 4) {
                            String[] params;
                            params = AuxUtils.getParams(sline);
                            aCnt = AuxUtils.ParseInt(params[0], 0);
                            bCnt = AuxUtils.ParseInt(params[1], 0);
                        } else {
                            if (line > 4 && line <= 4 + aCnt) {
                                String[] params;
                                params = AuxUtils.getParams(sline);

                                MolAtom curAtom = molecule.addAtom();
                                curAtom.setSign(params[3]);
                                curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(params[3]));
                                curAtom.setX(AuxUtils.ParseFloat(params[0], 0));
                                curAtom.setY(AuxUtils.ParseFloat(params[1], 0));
                                curAtom.setZ(AuxUtils.ParseFloat(params[2], 0));

                                String tempVar = params[4];
                                if (StringHelper.equals(tempVar, "1")) {
                                    curAtom.setFormalCharge(3);
                                } else if (StringHelper.equals(tempVar, "2")) {
                                    curAtom.setFormalCharge(2);
                                } else if (StringHelper.equals(tempVar, "3")) {
                                    curAtom.setFormalCharge(1);
                                } else if (StringHelper.equals(tempVar, "5")) {
                                    curAtom.setFormalCharge(-1);
                                } else if (StringHelper.equals(tempVar, "6")) {
                                    curAtom.setFormalCharge(-2);
                                } else if (StringHelper.equals(tempVar, "7")) {
                                    curAtom.setFormalCharge(-3);
                                }
                            } else if (line > 4 + aCnt && line <= 4 + aCnt + bCnt) {
                                String[] params;
                                params = AuxUtils.getParams(sline);
                                int F = AuxUtils.ParseInt(params[0], 0);
                                int T = AuxUtils.ParseInt(params[1], 0);
                                BondKind bt = BondKind.bkSingle;
                                int num = AuxUtils.ParseInt(params[2], 0);
                                switch (num) {
                                    case 1:
                                        bt = BondKind.bkSingle;
                                        break;
                                    case 2:
                                        bt = BondKind.bkDouble;
                                        break;
                                    case 3:
                                        bt = BondKind.bkTriple;
                                        break;
                                    case 4:
                                        bt = BondKind.bkConjugated;
                                        break;
                                }

                                molecule.addBond(molecule.getAtom(F - 1), molecule.getAtom(T - 1), bt);
                            } else if (line > 4 + aCnt + bCnt) {
                                String[] params;
                                params = AuxUtils.getParams(sline);
                                if (StringHelper.equals(params[0], ">") && StringHelper.equals(params[1], "<MOLNAME>")) {
                                    sline = tf.readLine();

                                    line++;
                                    this.getActiveMolecule().setName(sline);
                                }
                            }
                        }
                    }
                    
                    sline = tf.readLine();
                }
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    private static MCMLineKind detectMCMKind(String s)
    {
        MCMLineKind result = MCMLineKind.lkND;

        int p = s.indexOf("-");
        int p2 = s.indexOf(":");

        if (p >= 0 && p < s.length() && CLData.LatSymbols.indexOf(s.charAt(p + 1)) >= 0) {
            result = MCMLineKind.lkBond;
        } else if (p2 >= 0 && p2 < s.length() && CLData.SignedNumbers.indexOf(s.charAt(p2 + 1)) >= 0) {
            result = MCMLineKind.lkAtom;
        }

        return result;
    }

    private static void divMCMAtom(String atom, RefObject<String> element, RefObject<Integer> number)
    {
        int i = 1;
        element.argValue = "";
        String num = "";

        while (i <= ((atom != null) ? atom.length() : 0)) {
            if (CLData.Numbers.indexOf(atom.charAt(i - 1)) >= 0) {
                num += (atom.charAt(i - 1));
            } else {
                element.argValue += (atom.charAt(i - 1));
            }
            i++;
        }

        number.argValue = AuxUtils.ParseInt(num, 0);
    }

    private void prepareMCMAtom(String s)
    {
        try {
            int p = s.indexOf(";");
            String needs = s.substring(0, p);

            p = needs.indexOf(":");
            String atom = needs.substring(0, p);
            String coords = needs.substring(p + 1, p + 1 + needs.length() - (p + 1)).trim();

            String element = null;
            int number = 0;
            RefObject<String> tempRef_element = new RefObject<>(element);
            RefObject<Integer> tempRef_number = new RefObject<>(number);
            divMCMAtom(atom, tempRef_element, tempRef_number);
            element = tempRef_element.argValue;
            number = tempRef_number.argValue;

            String[] params;
            params = AuxUtils.getParams(coords);

            if (number > this.getActiveMolecule().getAtomCount()) {
                this.getActiveMolecule().allocateAtoms(number);
            }

            MolAtom curAtom = this.getActiveMolecule().getAtom(number - 1);
            curAtom.setSign(element);
            curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(element));
            curAtom.setX(AuxUtils.ParseFloat(params[0], 0));
            curAtom.setY(AuxUtils.ParseFloat(params[1], 0));
            curAtom.setZ(AuxUtils.ParseFloat(params[2], 0));
        } catch (ParseException ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    private void prepareMCMBond(String s)
    {
        String[] parts = s.split("[-]", -1);
        String eL = null;
        int nL = 0;
        RefObject<String> tempRef_eL = new RefObject<>(eL);
        RefObject<Integer> tempRef_nL = new RefObject<>(nL);
        divMCMAtom(parts[0], tempRef_eL, tempRef_nL);
        eL = tempRef_eL.argValue;
        nL = tempRef_nL.argValue;

        String eR = null;
        int nR = 0;
        RefObject<String> tempRef_eR = new RefObject<>(eR);
        RefObject<Integer> tempRef_nR = new RefObject<>(nR);
        divMCMAtom(parts[1], tempRef_eR, tempRef_nR);
        eR = tempRef_eR.argValue;
        nR = tempRef_nR.argValue;

        Molecule activeMolecule = this.getActiveMolecule();
        activeMolecule.addBond(activeMolecule.getAtom(nL - 1), activeMolecule.getAtom(nR - 1), BondKind.bkSingle);
    }

    public final void loadFromMCM(String fileName)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line) && line.charAt(0) != ';') {
                        if (line.charAt(0) == '\\') {
                            break;
                        }

                        if (line.indexOf("file") < 0 && line.indexOf("atoms") < 0 && line.indexOf("bonds") < 0 && line.indexOf(" = ") < 0) {
                            MCMLineKind kind = detectMCMKind(line);

                            switch (kind) {
                                case lkAtom:
                                    this.prepareMCMAtom(line);
                                    break;

                                case lkBond:
                                    this.prepareMCMBond(line);
                                    break;
                            }
                        }
                    }

                    line = tf.readLine();
                }
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
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

    public final INotifyHandler get_MoleculeProperties()
    {
        return this.fMoleculeProperties;
    }

    public final void set_MoleculeProperties(INotifyHandler value)
    {
        this.fMoleculeProperties = value;
    }

    public final BondPropertiesHandler get_BondProperties()
    {
        return this.fBondProperties;
    }

    public final void set_BondProperties(BondPropertiesHandler value)
    {
        this.fBondProperties = value;
    }

    public final AtomPropertiesHandler get_AtomProperties()
    {
        return this.fAtomProperties;
    }

    public final void set_AtomProperties(AtomPropertiesHandler value)
    {
        this.fAtomProperties = value;
    }

    static {
        MoleculeMaster.CurrentVersion = 2;
        MoleculeMaster.cur_Edit = 11;
        MoleculeMaster.cur_Scale = 12;
        MoleculeMaster.cur_PlaneMove = 13;
        MoleculeMaster.cur_SpaceMove = 14;
        MoleculeMaster.cur_SpaceRotate = 15;
        MoleculeMaster.cur_PlaneRotate = 16;
        MoleculeMaster.cur_NOne = 21;
        MoleculeMaster.cur_NTwo = 22;
        MoleculeMaster.cur_NThree = 23;
        MoleculeMaster.cur_POne = 24;
        MoleculeMaster.cur_PTwo = 25;
        MoleculeMaster.cur_PThree = 26;
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
}
