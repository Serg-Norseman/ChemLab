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
package chemlab.core.chemical;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class DegreeSetTest
{
    
    public DegreeSetTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of initTypeHandler method, of class DegreeSet.
     */
    @Test
    public void testCommon()
    {
        System.out.println("testCommon");
        DegreeSet instance = new DegreeSet();
        instance.parse("[N5,N1,P2,P6]", true);
        assertEquals(true, instance.contains(DegreeId.N5));
        assertEquals(true, instance.contains(DegreeId.N1));
        assertEquals(true, instance.contains(DegreeId.P2));
        assertEquals(true, instance.contains(DegreeId.P6));
        assertEquals(false, instance.contains(DegreeId.Zero));
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
