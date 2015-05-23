package chemlab.forms;

import bslib.common.AuxUtils;
import bslib.common.FramesHelper;
import bslib.common.StringHelper;
import chemlab.core.chemical.CLData;
import chemlab.refbooks.UnitRecord;
import chemlab.refbooks.ValueRecord;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public final class CLUnitMaster extends JFrame implements ActionListener
{
    private JLabel Label1;
    private JTextField eInputValue;
    private JLabel Label2;
    private JComboBox cbInputUnit;
    private JLabel Label3;
    private JComboBox cbOutputUnit;
    private JLabel Label4;
    private JTextField eOutputValue;
    private JButton btnAccept;
    private JButton btnClose;
    private JLabel Label5;
    private JComboBox cbValue;

    public CLUnitMaster()
    {
        super();
        this.initializeComponents();

        //this.cbValue.BeginUpdate();
        for (int i = 0; i < CLData.ValuesTable.size(); i++) {
            ValueRecord valRec = CLData.ValuesTable.get(i);
            if (valRec.Value == 0f) {
                this.cbValue.addItem(new ComboItem(valRec.Name, i));
            }
        }
        //this.cbValue.EndUpdate();
    }

    private void initializeComponents()
    {
        this.Label1 = new JLabel();
        this.eInputValue = new JTextField();
        this.Label2 = new JLabel();
        this.cbInputUnit = new JComboBox();
        this.Label3 = new JLabel();
        this.cbOutputUnit = new JComboBox();
        this.Label4 = new JLabel();
        this.eOutputValue = new JTextField();
        this.btnAccept = new JButton();
        this.btnClose = new JButton();
        this.Label5 = new JLabel();
        this.cbValue = new JComboBox();

        this.setLayout(null);

        this.Label5.setText("Величина");
        this.Label5.setLocation(8, 8);
        this.Label5.setSize(100, 12);

        this.Label2.setText("Данная ед. изм.");
        this.Label2.setLocation(8, 52);
        this.Label2.setSize(141, 12);

        this.Label1.setText("Данное значение");
        this.Label1.setLocation(8, 96);
        this.Label1.setSize(141, 12);

        this.Label3.setText("Необходимая ед. изм.");
        this.Label3.setLocation(155, 52);
        this.Label3.setSize(141, 12);

        this.Label4.setText("Необходимое значение");
        this.Label4.setLocation(155, 96);
        this.Label4.setSize(141, 12);

        this.eInputValue.setText("0");
        this.eInputValue.setLocation(8, 111);
        this.eInputValue.setSize(141, 20);

        this.cbInputUnit.setLocation(8, 67);
        this.cbInputUnit.setSize(141, 20);

        this.cbOutputUnit.setLocation(155, 67);
        this.cbOutputUnit.setSize(141, 20);

        this.eOutputValue.setText("0");
        this.eOutputValue.setLocation(155, 111);
        this.eOutputValue.setSize(141, 20);

        this.btnAccept.setLocation(59, 148);
        this.btnAccept.setSize(90, 23);
        this.btnAccept.setText("Расчет");
        this.btnAccept.addActionListener(this);
        this.btnAccept.setActionCommand("ACTION_CALC");

        this.btnClose.setLocation(155, 148);
        this.btnClose.setSize(90, 23);
        this.btnClose.setText("Закрыть");
        this.btnClose.addActionListener(this);
        this.btnClose.setActionCommand("ACTION_CLOSE");

        cbValue.setLocation(8, 23);
        cbValue.setSize(288, 20);
        cbValue.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ComboItem item = (ComboItem) e.getItem();
                    int idx = (int) item.Data;
                    updateUnits(cbInputUnit, idx);
                    updateUnits(cbOutputUnit, idx);
                }
            }
        });

        this.add(this.Label1);
        this.add(this.eInputValue);
        this.add(this.Label2);
        this.add(this.cbInputUnit);
        this.add(this.Label3);
        this.add(this.cbOutputUnit);
        this.add(this.Label4);
        this.add(this.eOutputValue);
        this.add(this.btnAccept);
        this.add(this.btnClose);
        this.add(this.Label5);
        this.add(this.cbValue);

        FramesHelper.setClientSize(this, 304, 182);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Мастер единиц измерения");
        
        CommonUtils.changeFont(this);
    }

    private static void updateUnits(JComboBox combo, int vid)
    {
        //combo.BeginUpdate();
        combo.removeAllItems();
        for (UnitRecord unitRec : CLData.UnitsTable) {
            if (unitRec.ValId == CLData.ValuesTable.get(vid).Id && !StringHelper.equals(unitRec.Sign, "-")) {
                combo.addItem(unitRec.Name);
            }
        }
        //combo.EndUpdate();
    }

    public void actCalc()
    {
        try {
            int inId = CLData.getUnitIDByName((String) this.cbInputUnit.getSelectedItem());
            int outId = CLData.getUnitIDByName((String) this.cbOutputUnit.getSelectedItem());
            double inf = CLData.UnitsTable.get(inId).Factor;
            double outf = CLData.UnitsTable.get(outId).Factor;
            double inv = AuxUtils.ParseFloat(this.eInputValue.getText(), 0);
            double outv = (inv * inf / outf);
            this.eOutputValue.setText(String.format("%5.5f", outv));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        //Object source = e.getSource();

        switch (actionPerformed) {
            case "ACTION_CALC":
                this.actCalc();
                break;

            case "ACTION_CLOSE":
                this.setVisible(false);
                break;
        }
    }
}
