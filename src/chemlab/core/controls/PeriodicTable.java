package chemlab.core.controls;

import bslib.common.AuxUtils;
import bslib.common.INotifyHandler;
import bslib.common.StringHelper;
import chemlab.core.chemical.CLData;
import chemlab.refbooks.ElementRecord;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;

public final class PeriodicTable extends JPanel implements ActionListener
{
    public enum Colouration
    {
        ByClass,
        ByABP;
    }

    private static final int PTRows = 9;
    private static final int PTCols = 18;

    private static final String[][] PTable;
    private static final Color[] ElementClassColors;
    private static final Color[] ElementABPColors;

    private final JToggleButton[][] fButtons = new JToggleButton[9][18];
    private int fBtnH;
    private int fBtnW;
    private Colouration fColouration = Colouration.ByClass;
    private String fElement;
    private INotifyHandler fOnChange;

    private JPopupMenu fPopupMenu;
    private JMenuItem miColourationByABP;
    private JMenuItem miColourationByClass;

    public PeriodicTable()
    {
        super();

        this.setLayout(null);
        this.fOnChange = null;

        ButtonGroup group = new ButtonGroup();
        
        Font btnFont = new Font("Verdana", Font.BOLD, 10);
        
        for (int p = 1; p <= PTRows; p++) {
            for (int g = 1; g <= PTCols; g++) {
                String sE = PeriodicTable.PTable[p - 1][g - 1].trim();

                if ((!StringHelper.equals(sE, ""))) {
                    JToggleButton btn = new JToggleButton();
                    btn.setText(sE);
                    btn.setFont(btnFont);
                    btn.addActionListener(this);
                    btn.setActionCommand("ELEM_SELECT");
                    btn.setMargin(new Insets(0, 0, 0, 0));

                    this.fButtons[p - 1][g - 1] = btn;
                    this.add(btn);
                    group.add(btn);

                    ElementRecord elRec = CLData.ElementsBook.findElement(sE);
                    String sClass = CLData.ElementClasses[elRec.FClass.getValue()];
                    String sABP = CLData.ElementABProperty[elRec.FABProperty.getValue()];

                    String text = String.valueOf(elRec.FNumber);
                    btn.setToolTipText(StringHelper.concat(text, "/", elRec.FSymbol, "/", elRec.FRus_Name, "/", sClass, "/", sABP));
                }
            }
        }

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    showContextMenu(e.getX(), e.getY());
                }
            }
        });
        
        this.fPopupMenu = new JPopupMenu();
        ButtonGroup menuGroup = new ButtonGroup();

        this.miColourationByClass = new JRadioButtonMenuItem();
        this.miColourationByClass.setText("Окраска по классу");
        this.miColourationByClass.addActionListener(this);
        this.miColourationByClass.setActionCommand("ColByClass");
        this.miColourationByClass.setSelected(true);

        this.miColourationByABP = new JRadioButtonMenuItem();
        this.miColourationByABP.setText("Окраска по Кислотно-Основности");
        this.miColourationByABP.addActionListener(this);
        this.miColourationByABP.setActionCommand("ColByABP");

        menuGroup.add(this.miColourationByClass);
        menuGroup.add(this.miColourationByABP);
        
        this.fPopupMenu.add(this.miColourationByClass);
        this.fPopupMenu.add(this.miColourationByABP);

        this.refreshButtons();
    }

    private void showContextMenu(int x, int y)
    {
        this.fPopupMenu.show(this, x, y);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String actionPerformed = ae.getActionCommand();
        Object source = ae.getSource();
        
        if (actionPerformed.equals("ELEM_SELECT")) {
            JToggleButton elBtn = (JToggleButton) source;
            this.fElement = elBtn.getText();
            if (this.fOnChange != null) {
                this.fOnChange.invoke(this);
            }
        }
        
        if (actionPerformed.equals("ColByClass")) {
            //this.miColourationByClass.setSelected(true);
            //this.miColourationByABP.setSelected(false);
            this.setColouration(Colouration.ByClass);
        }

        if (actionPerformed.equals("ColByABP")) {
            //this.miColourationByClass.setSelected(false);
            //this.miColourationByABP.setSelected(true);
            this.setColouration(Colouration.ByABP);
        }
    }

    /*@Override
     protected void Dispose(boolean disposing)
     {
     if (disposing) {
     for (int p = 1; p <= PTRows; p++) {
     for (int g = 1; g <= PTCols; g++) {
     ToggleButton btn = this.fButtons[p - 1][g - 1];
     if (btn != null) {
     btn.Dispose();
     }
     }
     }
     this.fButtons = null;
     }
     super.Dispose(disposing);
     }*/
    public INotifyHandler getOnChange()
    {
        return this.fOnChange;
    }

    public void setOnChange(INotifyHandler value)
    {
        this.fOnChange = value;
    }

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);

        this.fBtnW = width / PeriodicTable.PTCols;
        this.fBtnH = height / PeriodicTable.PTRows;

        for (int p = 1; p <= PTRows; p++) {
            for (int g = 1; g <= PTCols; g++) {
                JToggleButton btn = this.fButtons[p - 1][g - 1];
                if (btn != null) {
                    btn.setBounds((g - 1) * this.fBtnW, (p - 1) * this.fBtnH, this.fBtnW, this.fBtnH);
                }
            }
        }
    }
    
    public String getElement()
    {
        return this.fElement;
    }

    public void setElement(String value)
    {
        for (int p = 1; p <= PTRows; p++) {
            for (int g = 1; g <= PTCols; g++) {
                String cell = PeriodicTable.PTable[p - 1][g - 1].trim();
                if (StringHelper.equals(cell, value)) {
                    this.fElement = value;
                    this.fButtons[p - 1][g - 1].setSelected(true);
                    return;
                }
            }
        }
    }

    public Colouration getColouration()
    {
        return this.fColouration;
    }

    public void setColouration(Colouration value)
    {
        if (this.fColouration != value) {
            this.fColouration = value;
            this.refreshButtons();
        }
    }

    private void refreshButtons()
    {
        for (int p = 1; p <= PTRows; p++) {
            for (int g = 1; g <= PTCols; g++) {
                JToggleButton btn = this.fButtons[p - 1][g - 1];

                String sE = PeriodicTable.PTable[p - 1][g - 1].trim();

                if ((!StringHelper.equals(sE, ""))) {
                    ElementRecord elRec = CLData.ElementsBook.findElement(sE);

                    switch (this.fColouration) {
                        case ByClass:
                            btn.setForeground(PeriodicTable.ElementClassColors[elRec.FClass.getValue()]);
                            break;

                        case ByABP:
                            btn.setForeground(PeriodicTable.ElementABPColors[elRec.FABProperty.getValue()]);
                            break;
                    }
                }
            }
        }
    }

    static {
        String[][] array = new String[9][18];
        array[0] = new String[] { "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "H  ", "He " };
        array[1] = new String[] { "Li ", "Be ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "   ", "B  ", "C  ", "N  ", "O  ", "F  ", "Ne " };

        array[2][0] = "Na ";
        array[2][1] = "Mg ";
        array[2][2] = "   ";
        array[2][3] = "   ";
        array[2][4] = "   ";
        array[2][5] = "   ";
        array[2][6] = "   ";
        array[2][7] = "   ";
        array[2][8] = "   ";
        array[2][9] = "   ";
        array[2][10] = "   ";
        array[2][11] = "   ";
        array[2][12] = "Al ";
        array[2][13] = "Si ";
        array[2][14] = "P  ";
        array[2][15] = "S  ";
        array[2][16] = "Cl ";
        array[2][17] = "Ar ";
        array[3][0] = "K  ";
        array[3][1] = "Ca ";
        array[3][2] = "Sc ";
        array[3][3] = "Ti ";
        array[3][4] = "V  ";
        array[3][5] = "Cr ";
        array[3][6] = "Mn ";
        array[3][7] = "Fe ";
        array[3][8] = "Co ";
        array[3][9] = "Ni ";
        array[3][10] = "Cu ";
        array[3][11] = "Zn ";
        array[3][12] = "Ga ";
        array[3][13] = "Ge ";
        array[3][14] = "As ";
        array[3][15] = "Se ";
        array[3][16] = "Br ";
        array[3][17] = "Kr ";
        array[4][0] = "Rb ";
        array[4][1] = "Sr ";
        array[4][2] = "Y  ";
        array[4][3] = "Zr ";
        array[4][4] = "Nb ";
        array[4][5] = "Mo ";
        array[4][6] = "Tc ";
        array[4][7] = "Ru ";
        array[4][8] = "Rh ";
        array[4][9] = "Pd ";
        array[4][10] = "Ag ";
        array[4][11] = "Cd ";
        array[4][12] = "In ";
        array[4][13] = "Sn ";
        array[4][14] = "Sb ";
        array[4][15] = "Te ";
        array[4][16] = "I  ";
        array[4][17] = "Xe ";
        array[5][0] = "Cs ";
        array[5][1] = "Ba ";
        array[5][2] = "La ";
        array[5][3] = "Hf ";
        array[5][4] = "Ta ";
        array[5][5] = "W  ";
        array[5][6] = "Re ";
        array[5][7] = "Os ";
        array[5][8] = "Ir ";
        array[5][9] = "Pt ";
        array[5][10] = "Au ";
        array[5][11] = "Hg ";
        array[5][12] = "Tl ";
        array[5][13] = "Pb ";
        array[5][14] = "Bi ";
        array[5][15] = "Po ";
        array[5][16] = "At ";
        array[5][17] = "Rn ";
        array[6][0] = "Fr ";
        array[6][1] = "Ra ";
        array[6][2] = "Ac ";
        array[6][3] = "Rf ";
        array[6][4] = "Db ";
        array[6][5] = "Sg ";
        array[6][6] = "Bh ";
        array[6][7] = "Hs ";
        array[6][8] = "Mt ";
        array[6][9] = "Ds ";
        array[6][10] = "Rg ";
        array[6][11] = "Cn ";
        array[6][12] = "Uut";
        array[6][13] = "Uuq";
        array[6][14] = "Uup";
        array[6][15] = "Uuh";
        array[6][16] = "Uus";
        array[6][17] = "Uuo";
        array[7][0] = "   ";
        array[7][1] = "   ";
        array[7][2] = "   ";
        array[7][3] = "   ";
        array[7][4] = "Ce ";
        array[7][5] = "Pr ";
        array[7][6] = "Nd ";
        array[7][7] = "Pm ";
        array[7][8] = "Sm ";
        array[7][9] = "Eu ";
        array[7][10] = "Gd ";
        array[7][11] = "Tb ";
        array[7][12] = "Dy ";
        array[7][13] = "Ho ";
        array[7][14] = "Er ";
        array[7][15] = "Tm ";
        array[7][16] = "Yb ";
        array[7][17] = "Lu ";
        array[8][0] = "   ";
        array[8][1] = "   ";
        array[8][2] = "   ";
        array[8][3] = "   ";
        array[8][4] = "Th ";
        array[8][5] = "Pa ";
        array[8][6] = "U  ";
        array[8][7] = "Np ";
        array[8][8] = "Pu ";
        array[8][9] = "Am ";
        array[8][10] = "Cm ";
        array[8][11] = "Bk ";
        array[8][12] = "Cf ";
        array[8][13] = "Es ";
        array[8][14] = "Fm ";
        array[8][15] = "Md ";
        array[8][16] = "No ";
        array[8][17] = "Lr ";
        PTable = array;

        ElementClassColors = new Color[]{AuxUtils.BGRToRGB(16711680), AuxUtils.BGRToRGB(32768), AuxUtils.BGRToRGB(8388736), AuxUtils.BGRToRGB(8421376), AuxUtils.BGRToRGB(0), AuxUtils.BGRToRGB(33023), AuxUtils.BGRToRGB(8388863), AuxUtils.BGRToRGB(255)};

        ElementABPColors = new Color[]{AuxUtils.BGRToRGB(65280), AuxUtils.BGRToRGB(4227327), AuxUtils.BGRToRGB(16711935)};
    }
}
