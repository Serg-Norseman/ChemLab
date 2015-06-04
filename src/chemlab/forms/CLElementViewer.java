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
        this.tblElementProps.addColumn(res_i18n.getString("CL_Property"), 185);
        this.tblElementProps.addColumn(res_i18n.getString("CL_VALUE"), 323);

        this.setTitle(String.format(res_i18n.getString("CL_EV_TITLE"), eSymbol));
        this.setLayout(new BorderLayout());
        this.add(this.tblElementProps, BorderLayout.CENTER);
        FramesHelper.setClientSize(this, 530, 340);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        CommonUtils.changeFont(this);

        ElementRecord elem = CLData.ElementsBook.findElement(eSymbol);

        this.addProperty(res_i18n.getString("CL_Number"), (new Integer(elem.FNumber)).toString());
        this.addProperty(res_i18n.getString("CL_Symbol"), elem.FSymbol);
        this.addProperty(res_i18n.getString("CL_Name"), elem.FRus_Name);
        this.addProperty(res_i18n.getString("CL_Class"), CLData.ElementClasses[elem.FClass.getValue()]);
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
