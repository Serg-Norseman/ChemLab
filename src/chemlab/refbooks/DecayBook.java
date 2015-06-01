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
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class DecayBook extends RefBook
{
    private final ArrayList<DecayRecord> fList;

    public DecayBook()
    {
        this.fList = new ArrayList<>();
        this.load();
    }

    @Override
    public final DecayRecord get(int index)
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
    public final Iterable<DecayRecord> getList()
    {
        return this.fList;
    }

    public final ArrayList<DecayRecord> getDecayByNuclide(int nuclideId)
    {
        ArrayList<DecayRecord> result = new ArrayList<>();
        for (DecayRecord decRec : fList) {
            if (decRec.NuclideId == nuclideId) {
                result.add(decRec);
            }
        }
        return result;
    }
    
    private void load()
    {
        RefBook.loadResource("/resources/data/DecayTable.xml", "decays", "decay", new IElementLoader<DecayRecord>()
        {
            @Override
            public void processRoot(Element root)
            {                
            }
            
            @Override
            public int load(Element el)
            {
                DecayRecord decRec = new DecayRecord();

                decRec.DecayId = AuxUtils.ParseInt(el.getAttribute("ID"), 0);
                decRec.NuclideId = AuxUtils.ParseInt(el.getAttribute("Nuclide_ID"), 0);
                decRec.Mode = chemlab.core.chemical.DecayMode.valueOf(el.getAttribute("DecayMode"));
                decRec.DescendantId = AuxUtils.ParseInt(el.getAttribute("DescendantID"), 0);

                fList.add(decRec);

                return 0;
            }
        });
    }
}
