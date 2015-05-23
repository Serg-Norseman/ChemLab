package chemlab.forms;

import bslib.common.AuxUtils;
import bslib.common.FramesHelper;
import chemlab.core.chemical.CLData;
import chemlab.refbooks.ElementRecord;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public final class CLElementViewer extends JFrame
{
    private VirtualTable lvElement;

    public CLElementViewer(String eSymbol)
    {
        super();

        this.initializeComponents();

        this.setTitle("Элемент [" + eSymbol + "]");

        ElementRecord elem = CLData.ElementsBook.findElement(eSymbol);

        this.addProperty("Номер", (new Integer(elem.FNumber)).toString());
        this.addProperty("Символ", elem.FSymbol);
        this.addProperty("Название", elem.FRus_Name);
        this.addProperty("Класс", CLData.ElementClasses[elem.FClass.getValue()]);
        this.addProperty("Атомная масса", AuxUtils.FloatToStr(elem.FAtomic_Mass));
        this.addProperty("Атомный радиус", AuxUtils.FloatToStr(elem.FAtomic_Radius));
        this.addProperty("Атомный объем", AuxUtils.FloatToStr(elem.FAtomic_Volume));
        this.addProperty("Ковалент. радиус", AuxUtils.FloatToStr(elem.FCovalent_Radius));
        this.addProperty("Электроотрицательность", AuxUtils.FloatToStr(elem.FElectronegativity));
        this.addProperty("Степени окисления", elem.FOxidation_Degree.getDegreeStr());
        this.addProperty("Валентности", elem.FValency.getValencyStr());
        this.addProperty("Сродство к электрону", AuxUtils.FloatToStr(elem.FElectron_Affinity));
        this.addProperty("Твердость", AuxUtils.FloatToStr(elem.FHardness));
        this.addProperty("Ван дер Ваальса радиус", AuxUtils.FloatToStr(elem.FVDW_Radius));
        this.addProperty("Точка кипения", AuxUtils.FloatToStr(elem.FBoiling_Point));
        this.addProperty("Точка плавления", AuxUtils.FloatToStr(elem.FMelting_Point));
        this.addProperty("Плотность", AuxUtils.FloatToStr(elem.FDensity));
        this.addProperty("Теплота парообразования", AuxUtils.FloatToStr(elem.FVaporization_Heat));
        this.addProperty("Теплота слияния", AuxUtils.FloatToStr(elem.FFusion_Heat));
        this.addProperty("Электропроводность", AuxUtils.FloatToStr(elem.FElectric_Conductivity));
        this.addProperty("Теплопроводность", AuxUtils.FloatToStr(elem.FThermal_Conductivity));
        this.addProperty("Уд. теплоемкость", AuxUtils.FloatToStr(elem.FSpecific_Heat_Capacity));
        
        this.lvElement.packColumns(10);
    }

    private void initializeComponents()
    {
        this.setLayout(new BorderLayout());

        FramesHelper.setClientSize(this, 530, 340);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        this.lvElement = new VirtualTable();
        this.lvElement.setLocation(0, 0);
        this.lvElement.setSize(529, 343);
        this.lvElement.addColumn("Свойство", 185);
        this.lvElement.addColumn("Значение", 323);

        this.add(this.lvElement, BorderLayout.CENTER);
        
        CommonUtils.changeFont(this);
    }

    private void addProperty(String name, String value)
    {
        Object[] rowData = new Object[]{name, value};
        this.lvElement.addRow(rowData);

        /*ListViewItem item = this.lvElement.Items.Add(name);
         item.SubItems.Add(value);*/
    }
}
