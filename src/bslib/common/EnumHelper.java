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

/**
 *
 * @author Serg V. Zhdanovskih
 */
public final class EnumHelper
{
    /**
     * Finds the predecessor of the given enum value.
     *
     * @param enumType the {@code Class} object of the enum type from which to
     * return a value.
     * @param value the value whose predecessor should be returned
     * @return the predecessor of the given value, {@code null} if none.
     */
    public static <T extends Enum<T>> T pred(final Class<T> enumType, final T value)
    {
        T[] values = enumType.getEnumConstants();
        return value.ordinal() == 0 ? null : values[value.ordinal() - 1];
    }

    /**
     * Finds the successor of the given enum value.
     *
     * @param enumType the {@code Class} object of the enum type from which to
     * return a value.
     * @param value the value whose successor should be returned
     * @return the successor of the given value, {@code null} if none.
     */
    public static <T extends Enum<T>> T succ(final Class<T> enumType, final T value)
    {
        T[] values = enumType.getEnumConstants();
        return value.ordinal() == (values.length - 1) ? null : values[value.ordinal() + 1];
    }
}
