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

import bslib.common.AuxUtils;
import bslib.common.StringHelper;
import java.text.ParseException;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public class NuclidesBook extends RefBook
{
    private final ArrayList<NuclideRecord> fList;

    public NuclidesBook()
    {
        this.fList = new ArrayList<>();
        this.load();
    }

    @Override
    public final NuclideRecord get(int index)
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
    public final Iterable<NuclideRecord> getList()
    {
        return this.fList;
    }

    public final NuclideRecord getNuclideBySign(String nuclideSign)
    {
        for (NuclideRecord nRec : fList) {
            if (StringHelper.equals(nRec.Sign, nuclideSign)) {
                return nRec;
            }
        }

        throw new RuntimeException("Изотоп неизвестен: " + nuclideSign);
    }

    public final NuclideRecord getNuclideById(int nuclideId)
    {
        for (NuclideRecord nRec : fList) {
            if (nRec.NuclideId == nuclideId) {
                return nRec;
            }
        }

        throw new RuntimeException("Изотоп неизвестен: " + String.valueOf(nuclideId));
    }
    
    private void load()
    {
        RefBook.loadResource("/resources/data/NuclideTable.xml", "nuclides", "nuclide", new IElementLoader<NuclideRecord>()
        {
            @Override
            public void processRoot(Element root)
            {                
            }
            
            @Override
            public int load(Element el) throws ParseException
            {
                NuclideRecord nucRec = new NuclideRecord();

                nucRec.NuclideId = AuxUtils.parseInt(el.getAttribute("ID"), 0);
                nucRec.ElementId = AuxUtils.parseInt(el.getAttribute("ElementID"), 0);
                nucRec.Sign = el.getAttribute("Sign");
                nucRec.Abundance = AuxUtils.parseFloat(el.getAttribute("Abundance"), 0);
                nucRec.Weight = AuxUtils.parseFloat(el.getAttribute("Weight"), 0);
                nucRec.Spin = AuxUtils.parseFloat(el.getAttribute("Spin"), 0);
                nucRec.HalfLife = el.getAttribute("HalfLife");
                nucRec.DecayModes.parse(el.getAttribute("DecayModes"), true);

                fList.add(nucRec);

                return 0;
            }
        });
    }
}
