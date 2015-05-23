/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
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

import java.util.Arrays;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public final class StringHelper
{
    public static boolean equals(String str1, String str2)
    {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    public static boolean isNullOrEmpty(String str)
    {
        return (str == null || str.length() == 0);
    }

    public static int compareStr(String S1, String S2)
    {
        return S1.compareTo(S2);
    }

    public static int compareText(String S1, String S2)
    {
        return S1.compareToIgnoreCase(S2);
    }

    public static String upperFirst(String val)
    {
        if (StringHelper.isNullOrEmpty(val)) {
            return "";
        }

        StringBuilder str = new StringBuilder(val);
        str.setCharAt(0, Character.toUpperCase(str.charAt(0)));
        return str.toString();
    }

    public static String repeat(char ch, int repeat)
    {
        char[] chars = new char[repeat];
        Arrays.fill(chars, ch);
        return new String(chars);
    }

    public static String concat(String... stringarray)
    {
        if (stringarray == null) {
            return null;
        } else {
            return join("", stringarray, 0, stringarray.length);
        }
    }

    public static String join(String separator, String[] stringarray)
    {
        if (stringarray == null) {
            return null;
        } else {
            return join(separator, stringarray, 0, stringarray.length);
        }
    }

    public static String join(String separator, String[] stringarray, int startindex, int count)
    {
        StringBuilder result = new StringBuilder();

        if (stringarray == null) {
            return null;
        }

        for (int index = startindex; index < stringarray.length && index - startindex < count; index++) {
            if (separator != null && index > startindex) {
                result.append(separator);
            }

            if (stringarray[index] != null) {
                result.append(stringarray[index]);
            }
        }

        return result.toString();
    }
}
