/*
 *  "GEDKeeper", the personal genealogical database editor.
 *  Copyright (C) 2017 by Sergey V. Zhdanovskih.
 *
 *  This file is part of "GEDKeeper".
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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public class RangeTest
{
    @Test(expected = IllegalArgumentException.class)
    public void testCctorException()
    {
        System.out.println("cctorException");

        Range instance = new Range(2, 1);
    }

    /**
     * Test of isOverlapped method, of class Range.
     */
    @Test
    public void testIsOverlapped()
    {
        System.out.println("isOverlapped");

        assertTrue(new Range(1, 2).isOverlapped(new Range(1, 2))); // true
        assertTrue(new Range(1, 3).isOverlapped(new Range(2, 4))); // true
        assertTrue(new Range(2, 4).isOverlapped(new Range(1, 3))); // true
        assertFalse(new Range(3, 4).isOverlapped(new Range(1, 2))); // false
        assertFalse(new Range(1, 2).isOverlapped(new Range(3, 4))); // false
        assertTrue(new Range(2, 3).isOverlapped(new Range(1, 4))); // true
        assertTrue(new Range(1, 4).isOverlapped(new Range(2, 3))); // true

        assertTrue(new Range(1, 2).isOverlapped(new Range(1, 4))); // true
        assertTrue(new Range(1, 4).isOverlapped(new Range(1, 2))); // true
        assertTrue(new Range(1, 4).isOverlapped(new Range(3, 4))); // true
        assertTrue(new Range(3, 4).isOverlapped(new Range(1, 4))); // true
    }

    /**
     * Test of clone method, of class Range.
     */
    @Test
    public void testClone()
    {
        System.out.println("clone");

        Range instance = new Range(2, 3);
        Range copy = instance.clone();

        assertEquals(2, copy.Start);
        assertEquals(3, copy.End);
    }
}
