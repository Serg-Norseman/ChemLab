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
package bslib.math;

/**
 *
 * @author Sergey V. Zhdanovskih, Ruslan N. Garipov
 */
public final class ExtMath
{
    public static int factorial(int n)
    {
        int result = 1;
        for (int k = 2; k <= n; k++) {
            result *= k;
        }
        return result;
    }

    /**
     * Calculate the Greatest Common Divisor of two numbers.
     *
     * @param aa
     * @param bb
     * @return
     */
    public static int gcd(int aa, int bb)
    {
        while (0 != bb) {
            int tmp = bb;
            bb = aa % bb;
            aa = tmp;
        }
        //  The greatest common divisor is the largest positive integer.
        return Math.abs(aa);
    }

    /**
     * Finds the greatest common divisor (GCD) of integers in the specified part
     * of an array.
     *
     * @param numbers Source array of integers.
     * @param first Index of the first element in 'numbers' where the method
     * begins calculation of the GCD. The method calculates the GCD till the
     * last element in the 'numbers'.
     * @return The greatest common divisor of integers through the range [first,
     * {last}] in the 'numbers'.
     */
    public static int gcd(int[] numbers, int first)
    {
        int result = numbers[first];
        for (int i = first + 1; i < numbers.length; i++) {
            result = ExtMath.gcd(result, numbers[i]);
        }
        return result;
    }

    /**
     * Calculate Least Common Multiple of two numbers.
     *
     * @param a
     * @param b
     * @return
     */
    public static int lcm(int a, int b)
    {
        return a * (b / ExtMath.gcd(a, b));
    }

    /**
     * Calculate Least Common Multiple of numbers array.
     *
     * @param ia
     * @return
     */
    public static int lcm(int... ia)
    {
        int result = ia[0];
        for (int i = 1; i < ia.length; i++) {
            result = ExtMath.lcm(result, ia[i]);
        }

        return result;
    }

    public static double truncate(double value)
    {
        return truncate(value, 0);
    }

    public static double truncate(double value, int places)
    {
        double multiplier = Math.pow(10, places);
        return Math.floor(multiplier * value) / multiplier;
    }

    public static double round(double value, int places)
    {
        double multiplicationFactor = Math.pow(10, places);
        double interestedInZeroDPs = value * multiplicationFactor;
        return Math.round(interestedInZeroDPs) / multiplicationFactor;
    }

    public static double safeDiv(double dividend, double divisor)
    {
        return (divisor == 0.0d) ? 0.0d : (dividend / divisor);
    }

    @Deprecated
    public static double degreesToRadians(double degrees)
    {
        return degrees * (Math.PI / 180);
    }

    @Deprecated
    public static double radiansToDegrees(double radians)
    {
        return radians * 180 / Math.PI;
    }

    public static double log(double a, double newBase)
    {
        if (newBase == 1.0) {
            return Double.NaN;
        }
        if ((a != 1.0) && (newBase == 0.0 || (newBase == Double.POSITIVE_INFINITY))) {
            return Double.NaN;
        }
        return Math.log(a) / Math.log(newBase);
    }

    public static int signum(int f)
    {
        return (f == 0) ? 0 : (f < 0 ? -1 : +1);
    }
}
