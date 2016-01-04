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
package chemlab.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.7.0
 */
@DatabaseTable(tableName = "compound_names")
public class CompoundName extends BaseDaoEnabled<CompoundName, String>
{
    @DatabaseField(columnName = "id", generatedId = true)
    private int fId;

    @DatabaseField(columnName = "formula", foreign = true)
    private CompoundRecord fCompound;

    @DatabaseField(columnName = "name")
    private String fName;

    @DatabaseField(columnName = "lang")
    private String fLang;

    
    public CompoundName()
    {
        
    }

    public CompoundName(Dao<CompoundName, String> dao)
    {
        this.setDao(dao);
    }
    
    public CompoundRecord getCompound()
    {
        return this.fCompound;
    }
    
    public void setCompound(CompoundRecord value)
    {
        this.fCompound = value;
    }
    
    public String getName()
    {
        return this.fName;
    }
    
    public void setName(String value)
    {
        this.fName = value;
    }
    
    public String getLang()
    {
        return this.fLang;
    }
    
    public void setLang(String value)
    {
        this.fLang = value;
    }
    
}
