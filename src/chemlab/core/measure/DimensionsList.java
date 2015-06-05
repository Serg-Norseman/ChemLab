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
package chemlab.core.measure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class DimensionsList
{
    public static final class DimRecord
    {
        public final String Name;
        public final Unit<?> BaseUnit;
        
        public DimRecord(String name, Unit<?> baseUnit)
        {
            this.Name = name;
            this.BaseUnit = baseUnit;
        }
    }
    
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private static final DimensionsList INSTANCE = new DimensionsList();
    private final List<DimRecord> fList;

    private DimensionsList()
    {
        this.fList = new ArrayList<>();

        add(res_i18n.getString("CL_Temperature"), SI.KELVIN);
        add(res_i18n.getString("CL_Mass"), SI.KILOGRAM);
        add(res_i18n.getString("CL_Length"), SI.METRE);
        add(res_i18n.getString("CL_AmountOfSubstance"), SI.MOLE);
        add(res_i18n.getString("CL_Duration"), SI.SECOND);
        add(res_i18n.getString("CL_Force"), SI.NEWTON);
        add(res_i18n.getString("CL_Pressure"), SI.PASCAL);
        add(res_i18n.getString("CL_Square"), SI.SQUARE_METRE);
        add(res_i18n.getString("CL_Volume"), SI.CUBIC_METRE);
        add(res_i18n.getString("CL_Density"), ChemUnits.KG_M3);
    }

    public static DimensionsList getInstance()
    {
        return INSTANCE;
    }

    public List<DimRecord> getList()
    {
        return Collections.unmodifiableList(fList);
    }

    private void add(String name, Unit<?> baseUnit)
    {
        fList.add(new DimRecord(name, baseUnit));
    }
}
