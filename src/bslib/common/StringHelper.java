/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2015, 2017 by Sergey V. Zhdanovskih.
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
 * @author Sergey V. Zhdanovskih
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

    @Deprecated
    public static int compareStr(String S1, String S2)
    {
        return S1.compareTo(S2);
    }

    @Deprecated
    public static int compareText(String S1, String S2)
    {
        return S1.compareToIgnoreCase(S2);
    }

    public static String substringL(String s, int i, int len)
    {
        String res = s.substring(i, Math.min(i + len, s.length()));
        return res;
    }

    public static String upperFirst(String val)
    {
        if (isNullOrEmpty(val)) {
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

    public static String concat(String... args)
    {
        if (args == null) {
            return null;
        } else {
            StringBuilder str = new StringBuilder();
            for (String s : args) {
                str.append(s);
            }
            return str.toString();
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

    public static String trimEnd(String string, Character... charsToTrim)
    {
        if (string == null || charsToTrim == null) {
            return string;
        }

        int lengthToKeep = string.length();
        for (int index = string.length() - 1; index >= 0; index--) {
            boolean removeChar = false;
            if (charsToTrim.length == 0) {
                if (Character.isWhitespace(string.charAt(index))) {
                    lengthToKeep = index;
                    removeChar = true;
                }
            } else {
                for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
                    if (string.charAt(index) == charsToTrim[trimCharIndex]) {
                        lengthToKeep = index;
                        removeChar = true;
                        break;
                    }
                }
            }
            if (!removeChar) {
                break;
            }
        }
        return string.substring(0, lengthToKeep);
    }

    public static String trimStart(String string, Character... charsToTrim)
    {
        if (string == null || charsToTrim == null) {
            return string;
        }

        int startingIndex = 0;
        for (int index = 0; index < string.length(); index++) {
            boolean removeChar = false;
            if (charsToTrim.length == 0) {
                if (Character.isWhitespace(string.charAt(index))) {
                    startingIndex = index + 1;
                    removeChar = true;
                }
            } else {
                for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++) {
                    if (string.charAt(index) == charsToTrim[trimCharIndex]) {
                        startingIndex = index + 1;
                        removeChar = true;
                        break;
                    }
                }
            }
            if (!removeChar) {
                break;
            }
        }
        return string.substring(startingIndex);
    }

    public static String trim(String string, Character... charsToTrim)
    {
        return trimEnd(trimStart(string, charsToTrim), charsToTrim);
    }

    public static int compareStrings(String s1, String s2, boolean caseSensitive)
    {
        if (caseSensitive) {
            return s1.compareTo(s2);
        } else {
            return s1.compareToIgnoreCase(s2);
        }
    }

    public static int indexOfAny(String str, char[] chars, int startIndex)
    {
        for (char chr : chars) {
            int index = str.indexOf(chr, startIndex);
            if (index >= startIndex) {
                return index;
            }
        }
        return -1;
    }

    public static String padLeft(int val, char pad, int length)
    {
        return padLeft(String.valueOf(val), pad, length);
    }

    public static String padLeft(String val, char pad, int length)
    {
        String result = val;
        if (result.length() < length) {
            StringBuilder sb = new StringBuilder(result);
            while (sb.length() < length) {
                sb.insert(0, pad);
            }
            result = sb.toString();
        }
        return result;
    }
}
