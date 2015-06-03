package chemlab.forms;

import bslib.common.AuxUtils;
import bslib.common.FramesHelper;
import chemlab.core.chemical.CLData;
import chemlab.refbooks.ElementRecord;
import chemlab.vtable.VirtualTable;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public final class CLElementViewer extends JDialog
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private final VirtualTable tblElementProps;

    public CLElementViewer(Frame owner, String eSymbol)
    {
        super(owner);

        this.tblElementProps = new VirtualTable();
        this.tblElementProps.addColumn(res_i18n.getString("CL_PROPERTY"), 185);
        this.tblElementProps.addColumn(res_i18n.getString("CL_VALUE"), 323);

        this.setTitle(String.format(res_i18n.getString("CL_EV_TITLE"), eSymbol));
        this.setLayout(new BorderLayout());
        this.add(this.tblElementProps, BorderLayout.CENTER);
        FramesHelper.setClientSize(this, 530, 340);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        CommonUtils.changeFont(this);

        ElementRecord elem = CLData.ElementsBook.findElement(eSymbol);

        this.addProperty(res_i18n.getString("CL_NUMBER"), (new Integer(elem.FNumber)).toString());
        this.addProperty(res_i18n.getString("CL_SYMBOL"), elem.FSymbol);
        this.addProperty(res_i18n.getString("CL_NAME"), elem.FRus_Name);
        this.addProperty(res_i18n.getString("CL_CLASS"), CLData.ElementClasses[elem.FClass.getValue()]);
        this.addProperty(res_i18n.getString("CL_ATOMIC_MASS"), AuxUtils.FloatToStr(elem.FAtomic_Mass));
        this.addProperty(res_i18n.getString("CL_ATOMIC_RADIUS"), AuxUtils.FloatToStr(elem.FAtomic_Radius));
        this.addProperty(res_i18n.getString("CL_ATOMIC_VOLUME"), AuxUtils.FloatToStr(elem.FAtomic_Volume));
        this.addProperty(res_i18n.getString("CL_COVALENT_RADIUS"), AuxUtils.FloatToStr(elem.FCovalent_Radius));
        this.addProperty(res_i18n.getString("CL_ENEGATIVITY"), AuxUtils.FloatToStr(elem.FElectronegativity));
        this.addProperty(res_i18n.getString("CL_OXIDATION_DEGREES"), elem.FOxidation_Degree.getDegreeStr());
        this.addProperty(res_i18n.getString("CL_VALENCIES"), elem.FValency.getValencyStr());
        this.addProperty(res_i18n.getString("CL_ELECTRON_AFFINITY"), AuxUtils.FloatToStr(elem.FElectron_Affinity));
        this.addProperty(res_i18n.getString("CL_HARDNESS"), AuxUtils.FloatToStr(elem.FHardness));
        this.addProperty(res_i18n.getString("CL_VDW_RADIUS"), AuxUtils.FloatToStr(elem.FVDW_Radius));
        this.addProperty(res_i18n.getString("CL_BOILING_POINT"), AuxUtils.FloatToStr(elem.FBoiling_Point));
        this.addProperty(res_i18n.getString("CL_MELTING_POINT"), AuxUtils.FloatToStr(elem.FMelting_Point));
        this.addProperty(res_i18n.getString("CL_DENSITY"), AuxUtils.FloatToStr(elem.FDensity));
        this.addProperty(res_i18n.getString("CL_VAPORIZATION_HEAT"), AuxUtils.FloatToStr(elem.FVaporization_Heat));
        this.addProperty(res_i18n.getString("CL_FUSION_HEAT"), AuxUtils.FloatToStr(elem.FFusion_Heat));
        this.addProperty(res_i18n.getString("CL_ELECTRIC_CONDUCTIVITY"), AuxUtils.FloatToStr(elem.FElectric_Conductivity));
        this.addProperty(res_i18n.getString("CL_THERMAL_CONDUCTIVITY"), AuxUtils.FloatToStr(elem.FThermal_Conductivity));
        this.addProperty(res_i18n.getString("CL_SPECIFIC_HEAT_CAPACITY"), AuxUtils.FloatToStr(elem.FSpecific_Heat_Capacity));
        
        this.tblElementProps.packColumns(10);
        
        this.setLocationRelativeTo(owner);
    }

    private void addProperty(String name, String value)
    {
        Object[] rowData = new Object[]{name, value};
        this.tblElementProps.addRow(rowData);
    }
}
