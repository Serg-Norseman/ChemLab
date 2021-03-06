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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public final class AuxUtils
{
    private static final String Nums = "0123456789+-";

    public static final boolean binEndian = false;
    public static final int MaxInt = 2147483647;
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final String CRLF = "\r\n";
    public static final String NewLine = "\r\n";

    public static int RandSeed;
    private static int LastRandSeed;
    private static Random RandomEngine;

    static {
        AuxUtils.randomize();
    }

    public static void randomize()
    {
        AuxUtils.RandSeed = 0;
        AuxUtils.LastRandSeed = -1;

        if (AuxUtils.LastRandSeed != AuxUtils.RandSeed) {
            if (AuxUtils.RandSeed == 0) {
                AuxUtils.RandomEngine = new Random();
            } else {
                AuxUtils.RandomEngine = new Random(AuxUtils.RandSeed);
            }
            AuxUtils.LastRandSeed = AuxUtils.RandSeed;
        }
    }

    public static int getRandom(int range)
    {
        return AuxUtils.RandomEngine.nextInt(range);
    }

    public static int getBoundedRnd(int low, int high)
    {
        if (low > high) {
            int temp = low;
            low = high;
            high = temp;
        }

        return low + AuxUtils.getRandom(high - low + 1);
    }

    public static Point getRandomPoint(Rect area)
    {
        int x = AuxUtils.getBoundedRnd(area.Left, area.Right);
        int y = AuxUtils.getBoundedRnd(area.Top, area.Bottom);
        return new Point(x, y);
    }

    public static int getRandomArrayInt(int... array)
    {
        int idx = AuxUtils.getRandom(array.length);
        return array[idx];
    }

    public static <T> T getRandomItem(List<T> list)
    {
        int size = list.size();

        if (size < 1) {
            return null;
        } else if (size == 1) {
            return list.get(0);
        } else {
            return list.get(AuxUtils.getRandom(size));
        }
    }

    public static <T extends Enum<T>> T getRandomEnum(Class<T> enumType)
    {
        T[] vals = enumType.getEnumConstants();
        int idx = AuxUtils.getRandom(vals.length);
        return vals[idx];
    }

    public static String getFileExt(String fileName)
    {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i);
        }
        return extension;
    }
    
    public static String[] getParams(String s)
    {
        s = prepareString(s);
        return s.split("[ ]");
    }

    public static String prepareString(String s)
    {
        StringBuilder sb = new StringBuilder(s);
        while (sb.indexOf("  ") >= 0) {
            sb.deleteCharAt(sb.indexOf("  "));
        }
        return sb.toString().trim();
    }

    public static String FloatToStr(double value)
    {
        return (new Double(value)).toString();
    }

    public static boolean isValidInt(String value)
    {
        if (value == null || value.length() == 0) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            if (AuxUtils.Nums.indexOf(value.charAt(i)) < 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValueBetween(int value, int lowLimit, int topLimit, boolean includeLimits)
    {
        if (lowLimit > topLimit) {
            int temp = lowLimit;
            lowLimit = topLimit;
            topLimit = temp;
        }

        if (!includeLimits) {
            lowLimit++;
            topLimit--;
        }

        return value >= lowLimit && value <= topLimit;
    }

    public static int distance(int x1, int y1, int x2, int y2)
    {
        int dX = x2 - x1;
        int dY = y2 - y1;
        return (int) Math.round(Math.sqrt((double) (dX * dX + dY * dY)));
    }

    public static int distance(Point pt1, Point pt2)
    {
        return AuxUtils.distance(pt1.X, pt1.Y, pt2.X, pt2.Y);
    }

    public static String adjustString(String val, int up)
    {
        String result = val;
        if (result.length() < up) {
            StringBuilder sb = new StringBuilder(result);
            while (sb.length() < up) {
                sb.append(' ');
            }
            result = sb.toString();
        }
        return result;
    }

    public static String adjustNumber(int val, int up)
    {
        String result = Integer.toString(val);
        if (result.length() < up) {
            StringBuilder sb = new StringBuilder(result);
            while (sb.length() < up) {
                sb.insert(0, '0');
            }
            result = sb.toString();
        }
        return result;
    }

    public static boolean chance(int percent)
    {
        return AuxUtils.getRandom(101) < percent;
    }

    public static String changeExtension(String originalName, String newExtension)
    {
        int lastDot = originalName.lastIndexOf('.');
        if (lastDot != -1) {
            return originalName.substring(0, lastDot) + newExtension;
        } else {
            return originalName + newExtension;
        }
    }

    public static <T> int indexOf(T item, T[] array)
    {
        for (int i = 0; i < array.length; i++) {
            if ((array[i] != null && array[i].equals(item)) || (item == null && array[i] == null)) {
                return i;
            }
        }

        return -1;
    }

    public static int indexOfInt(int item, int[] array)
    {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }

        return -1;
    }

    public static final int getSystemModel()
    {
        String sys = System.getProperty("sun.arch.data.model");
        return Integer.parseInt(sys);
    }

    public static double Int(double value)
    {
        return ((value > (double) 0f) ? Math.floor(value) : Math.ceil(value));
    }

    public static double Frac(double value)
    {
        return (value - Int(value));
    }

    public static String getAppPath()
    {
        return System.getProperty("user.dir") + File.separatorChar;
    }
    
    public static String getAppPath(Class aClass)
    {
        String applicationDir = aClass.getProtectionDomain().getCodeSource().getLocation().getPath();

        String result = applicationDir;
        if (result.endsWith("classes/")) {
            result = result + "../../";
        } else if (result.endsWith(".jar")) {
            result = result.substring(0, result.lastIndexOf("/"));
        }

        File file = new File(result);
        try {
            result = file.getCanonicalPath() + File.separatorChar;
        } catch (IOException ex) {
        }

        return result;
    }

    public static int parseInt(String str, int Default)
    {
        if (StringHelper.isNullOrEmpty(str)) {
            return Default;
        }

        //try {
        return Integer.parseInt(str);
        //} catch (Exception ex) {
        //    return Default;
        //}
    }

    public static double parseFloat(String S, double Default) throws ParseException
    {
        //try {
        if (StringHelper.isNullOrEmpty(S)) {
            return Default;
        }

        char decimalSeparator = (S.indexOf(',') >= 0) ? ',' : '.';

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(decimalSeparator);
        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(symbols);
        double result = format.parse(S).doubleValue();
        return result;
        /*} catch (ParseException ex) {
         //return Default;
         //throw;
         Logger.write("AuxUtils.ParseFloat()" + ex.getMessage());
         return 0;
         }*/
    }

    public static int setBit(int changeValue, int position, int value)
    {
        int result = changeValue;

        int bt = (1 << position);
        if (value == 1) {
            result |= bt;
        } else {
            result &= (bt ^ 255);
        }

        return result;
    }

    public static boolean hasBit(int testValue, int position)
    {
        int bt = (1 << position);
        return (bt & testValue) > 0;
    }

    public static void browseURI(URI uri)
    {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                Logger.write("AuxUtils.browseURI.1(): " + ex.getMessage());
            }
        } else {
            Logger.write("AuxUtils.browseURI.2(): desktop isn't supported");
        }
    }
}
