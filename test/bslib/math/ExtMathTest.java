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
package bslib.math;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public class ExtMathTest
{
    public ExtMathTest()
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
     * Test of factorial method, of class ExtMath.
     */
    @Test
    public void testFactorial()
    {
        System.out.println("factorial");
        assertEquals(120, ExtMath.factorial(5));
    }

    /**
     * Test of gcd method, of class ExtMath.
     */
    @Test
    public void testGCD_I2()
    {
        System.out.println("gcd");
        assertEquals(35, ExtMath.gcd(70, 105));
    }

    /**
     * Test of gcd method, of class ExtMath.
     */
    @Test
    public void testGCD_IA()
    {
        System.out.println("gcd");
        assertEquals(35, ExtMath.gcd(new int[] {70, 105}));
    }

    /**
     * Test of LCM method, of class ExtMath.
     */
    @Test
    public void testLCM_I2()
    {
        System.out.println("LCM");
        assertEquals(80, ExtMath.lcm(16, 20));
    }

    /**
     * Test of LCM method, of class ExtMath.
     */
    @Test
    public void testLCM_IA()
    {
        System.out.println("LCM");
        assertEquals(80, ExtMath.lcm(new int[] {16, 20}));
    }
}
