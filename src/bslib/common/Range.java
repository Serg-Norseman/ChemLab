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

public final class Range<T extends java.lang.Comparable<T>>
{
    public T Start;
    public T End;

    public Range()
    {
    }

    public Range(T start, T end)
    {
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException("End must be greater than Start");
        }

        Start = start;
        End = end;
    }

    public boolean isOverlapped(Range<T> other)
    {
        if (Start.compareTo(other.Start) == 0) {
            return true;
        }

        if (Start.compareTo(other.Start) > 0) {
            return Start.compareTo(other.End) <= 0;
        }

        return other.Start.compareTo(End) <= 0;
    }

    @Override
    public Range clone()
    {
        Range varCopy = new Range();
        varCopy.Start = this.Start;
        varCopy.End = this.End;

        return varCopy;
    }
}
