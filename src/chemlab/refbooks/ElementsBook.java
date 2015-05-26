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
package chemlab.refbooks;

import chemlab.core.chemical.CrystalKind;
import chemlab.core.chemical.ElementABPropertyId;
import chemlab.core.chemical.ElementClass;
import bslib.common.AuxUtils;
import bslib.common.Logger;
import bslib.common.StringHelper;
import java.text.ParseException;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public final class ElementsBook extends RefBook
{
    private final ArrayList<ElementRecord> fList;

    public ElementsBook()
    {
        this.fList = new ArrayList<>();
        this.load();
    }

    @Override
    public final ElementRecord get(int index)
    {
        if (index < 0 || index >= this.fList.size()) {
            return null;
        }
        return this.fList.get(index);
    }

    @Override
    public final int size()
    {
        return this.fList.size();
    }
    
    @Override
    public final Iterable<ElementRecord> getList()
    {
        return this.fList;
    }
    
    public final int findElementNumber(String symbol)
    {
        String sym = symbol.toLowerCase();

        for (ElementRecord eRec : fList) {
            if (StringHelper.equals(sym, eRec.FSymbol.toLowerCase())) {
                return eRec.FNumber;
            }
        }

        throw new RuntimeException("Элемент неизвестен: " + symbol);
    }

    public final ElementRecord findElement(String symbol)
    {
        String sym = symbol.toLowerCase();

        for (ElementRecord eRec : fList) {
            if (StringHelper.equals(sym, eRec.FSymbol.toLowerCase())) {
                return eRec;
            }
        }

        throw new RuntimeException("Элемент неизвестен: " + symbol);
    }

    private void load()
    {
        RefBook.loadResource("/resources/data/ElementsTable.xml", "elements", "element", new IElementLoader<ElementRecord>()
        {
            @Override
            public int load(Element el) throws ParseException
            {
                ElementRecord elRec = new ElementRecord();

                try {
                    elRec.FNumber = AuxUtils.ParseInt(el.getAttribute("ID"), 0);

                    elRec.FClass = ElementClass.valueOf(readElement(el, "Class"));
                    elRec.FColor = AuxUtils.ParseInt(readElement(el, "Color"), 0);
                    elRec.FSymbol = readElement(el, "Symbol");
                    elRec.FRus_Name = readElement(el, "Rus_Name");
                    elRec.FLat_Name = readElement(el, "Lat_Name");
                    elRec.FEng_Name = readElement(el, "Eng_Name");
                    elRec.FGer_Name = readElement(el, "Ger_Name");
                    elRec.FFre_Name = readElement(el, "Fre_Name");
                    elRec.FSwd_Name = readElement(el, "Swd_Name");
                    elRec.FNor_Name = readElement(el, "Nor_Name");
                    elRec.FOriginal_Name = readElement(el, "Original_Name");
                    elRec.FAtomic_Mass = AuxUtils.ParseFloat(readElement(el, "Atomic_Mass"), 0);
                    elRec.FAtomic_Radius = AuxUtils.ParseFloat(readElement(el, "Atomic_Radius"), 0);
                    elRec.FAtomic_Volume = AuxUtils.ParseFloat(readElement(el, "Atomic_Volume"), 0);
                    elRec.FCovalent_Radius = AuxUtils.ParseFloat(readElement(el, "Covalent_Radius"), 0);
                    elRec.FElectronegativity = AuxUtils.ParseFloat(readElement(el, "Electronegativity"), 0);
                    elRec.FOxidation_Degree.parse(readElement(el, "Oxidation_Degrees"), true);
                    elRec.FValency.parse(readElement(el, "Valencies"), true);
                    elRec.FStructure = readElement(el, "Structure");
                    elRec.FDiscovery_Date = readElement(el, "Discovery_Date");
                    elRec.FDiscoverers = readElement(el, "Discoverers");
                    elRec.FDiscovery_Particularities = readElement(el, "Discovery_Particularities");
                    elRec.FTerrestrial_Cortex = AuxUtils.ParseFloat(readElement(el, "Terrestrial_Cortex"), 0);
                    elRec.FDescription = readElement(el, "Description");
                    elRec.FCrystal_Structure = CrystalKind.valueOf(readElement(el, "Crystal_Structure"));
                    elRec.FSources = readElement(el, "Sources");
                    elRec.FUses = readElement(el, "Uses");
                    elRec.FIP_1ST = AuxUtils.ParseFloat(readElement(el, "IP_1ST"), 0);
                    elRec.FIP_2ND = AuxUtils.ParseFloat(readElement(el, "IP_2ND"), 0);
                    elRec.FIP_3RD = AuxUtils.ParseFloat(readElement(el, "IP_3RD"), 0);
                    elRec.FElectron_Affinity = AuxUtils.ParseFloat(readElement(el, "Electron_Affinity"), 0);
                    elRec.FHardness = AuxUtils.ParseFloat(readElement(el, "Hardness"), 0);
                    elRec.FCritical_Temperature = AuxUtils.ParseFloat(readElement(el, "Critical_Temperature"), 0);
                    elRec.FCritical_Density = AuxUtils.ParseFloat(readElement(el, "Critical_Density"), 0);
                    elRec.FCritical_Pressure = AuxUtils.ParseFloat(readElement(el, "Critical_Pressure"), 0);
                    elRec.FVDW_Radius = AuxUtils.ParseFloat(readElement(el, "VDW_Radius"), 0);
                    elRec.FBoiling_Point = AuxUtils.ParseFloat(readElement(el, "Boiling_Point"), 0);
                    elRec.FMelting_Point = AuxUtils.ParseFloat(readElement(el, "Melting_Point"), 0);
                    elRec.FDensity = AuxUtils.ParseFloat(readElement(el, "Density"), 0);
                    elRec.FVaporization_Heat = AuxUtils.ParseFloat(readElement(el, "Vaporization_Heat"), 0);
                    elRec.FFusion_Heat = AuxUtils.ParseFloat(readElement(el, "Fusion_Heat"), 0);
                    elRec.FElectric_Conductivity = AuxUtils.ParseFloat(readElement(el, "Electric_Conductivity"), 0);
                    elRec.FThermal_Conductivity = AuxUtils.ParseFloat(readElement(el, "Thermal_Conductivity"), 0);
                    elRec.FSpecific_Heat_Capacity = AuxUtils.ParseFloat(readElement(el, "Specific_Heat_Capacity"), 0);
                    elRec.FABProperty = ElementABPropertyId.valueOf(readElement(el, "ABProperty"));
                    elRec.FAbsorption_Spectrum = readElement(el, "Absorption_Spectrum");
                    elRec.FEmission_Spectrum = readElement(el, "Emission_Spectrum");

                    fList.add(elRec);
                } catch (Exception ex) {
                    Logger.write("ElementsBook.load(): " + String.valueOf(elRec.FNumber) + ", " + ex.getMessage());
                }

                return 0;
            }
        });
    }
}
