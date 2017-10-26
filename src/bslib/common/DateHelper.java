/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2017 by Sergey V. Zhdanovskih.
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

public final class DateHelper
{
    public static int datePart(int calendarDatePart, java.util.Date date)
    {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        if (calendarDatePart == java.util.Calendar.MONTH) //Month in java.util.Calendar is 0-based, so add 1 to simulate .NET:
        {
            return c.get(calendarDatePart) + 1;
        } else {
            return c.get(calendarDatePart);
        }
    }

    public static int daysInMonth(int year, int month)
    {
        //Month in java.util.Calendar is 0-based, so subtract 1:
        java.util.Calendar cal = new java.util.GregorianCalendar(year, month - 1, 1);
        return cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
    }

    public static java.util.Date today()
    {
        java.util.Calendar now = java.util.Calendar.getInstance();
        //Month in java.util.Calendar is 0-based, so add 1 to simulate .NET:
        return dateForYMDHMS(now.get(java.util.Calendar.YEAR), now.get(java.util.Calendar.MONTH) + 1, now.get(java.util.Calendar.DATE), 0, 0, 0);
    }

    public static java.util.Date dateForYMD(int year, int month, int day)
    {
        return dateForYMDHMS(year, month, day, 0, 0, 0);
    }

    public static java.util.Date dateForYMDHMS(int year, int month, int day, int hour, int minute, int second)
    {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.clear();
        //Month in java.util.Calendar is 0-based, so subtract 1:
        cal.set(year, month - 1, day, hour, minute, second);
        return cal.getTime();
    }
}
