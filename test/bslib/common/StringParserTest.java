/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015 by Sergey V. Zhdanovskih.
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
package bslib.common;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public class StringParserTest
{
    
    public StringParserTest()
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
     * Test of checkToken method, of class StringParser.
     */
    @Test
    public void testSimpleFormula1()
    {
        System.out.println("testSimpleFormula1");
        StringParser instance = new StringParser("C2H5OH");
        
        instance.nextToken();
        assertEquals("C", instance.getStringToken());
        instance.nextToken();
        assertEquals("2", instance.getStringToken());
        instance.nextToken();
        assertEquals("H", instance.getStringToken());
        instance.nextToken();
        assertEquals("5", instance.getStringToken());
        instance.nextToken();
        assertEquals("OH", instance.getStringToken());
    }

    /**
     * Test of checkToken method, of class StringParser.
     */
    @Test
    public void testSimpleFormula2()
    {
        System.out.println("testSimpleFormula2");
        StringParser instance = new StringParser("C2H15OH");
        
        assertEquals(StringParser.cptString, instance.nextToken());
        assertEquals("C", instance.getStringToken());
        assertEquals(StringParser.cptInteger, instance.nextToken());
        assertEquals("2", instance.getStringToken());
        assertEquals(StringParser.cptString, instance.nextToken());
        assertEquals("H", instance.getStringToken());
        assertEquals(StringParser.cptInteger, instance.nextToken());
        assertEquals("15", instance.getStringToken());
        assertEquals(StringParser.cptString, instance.nextToken());
        assertEquals("OH", instance.getStringToken());
    }

    /**
     * Test of checkToken method, of class StringParser.
     */
    @Test
    public void testSimpleFormula3()
    {
        System.out.println("testSimpleFormula3");
        StringParser instance = new StringParser("12H2O");
        
        assertEquals(StringParser.cptInteger, instance.nextToken());
        assertEquals("12", instance.getStringToken());
        assertEquals(StringParser.cptString, instance.nextToken());
        assertEquals("H", instance.getStringToken());
        assertEquals(StringParser.cptInteger, instance.nextToken());
        assertEquals("2", instance.getStringToken());
        assertEquals(StringParser.cptString, instance.nextToken());
        assertEquals("O", instance.getStringToken());
    }

    /**
     * Test of checkToken method, of class StringParser.
     */
    @Test
    public void testSimpleFormula4()
    {
        System.out.println("testSimpleFormula4");
        StringParser instance = new StringParser("Ca(OH)2");
        
        assertEquals(StringParser.cptString, instance.nextToken());
        assertEquals("Ca", instance.getStringToken());
        assertEquals(StringParser.cptSymbol, instance.nextToken());
        assertEquals("(", instance.getStringToken());
        assertEquals(StringParser.cptString, instance.nextToken());
        assertEquals("OH", instance.getStringToken());
        assertEquals(StringParser.cptSymbol, instance.nextToken());
        assertEquals(")", instance.getStringToken());
        assertEquals(StringParser.cptInteger, instance.nextToken());
        assertEquals("2", instance.getStringToken());
    }

    /**
     * Test of checkToken method, of class StringParser.
     */
    @Test
    public void testCheckToken()
    {
        /*System.out.println("checkToken");
        char T = ' ';
        StringParser instance = null;
        instance.checkToken(T);
        // TODO review the generated test code and remove the default call to fail.*/
        fail("The test case is a prototype.");
    }

    /**
     * Test of nextToken method, of class StringParser.
     */
    @Test
    public void testNextToken()
    {
        /*System.out.println("nextToken");
        StringParser instance = null;
        char expResult = ' ';
        char result = instance.nextToken();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.*/
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFloatToken method, of class StringParser.
     */
    @Test
    public void testGetFloatToken()
    {
        /*System.out.println("getFloatToken");
        StringParser instance = null;
        double expResult = 0.0;
        double result = instance.getFloatToken();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.*/
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIntToken method, of class StringParser.
     */
    @Test
    public void testGetIntToken()
    {
        /*System.out.println("getIntToken");
        StringParser instance = null;
        int expResult = 0;
        int result = instance.getIntToken();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.*/
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStringToken method, of class StringParser.
     */
    @Test
    public void testGetStringToken()
    {
        /*System.out.println("getStringToken");
        StringParser instance = null;
        String expResult = "";
        String result = instance.getStringToken();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.*/
        fail("The test case is a prototype.");
    }
    
}
