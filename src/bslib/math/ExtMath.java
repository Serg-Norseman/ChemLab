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
package bslib.math;

import bslib.common.AuxUtils;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Serg V. Zhdanovskih
 */
public final class ExtMath
{
    public static int factorial(int n)
    {
        int result = 1;
        for (int k = 2; k <= n; k++) result *= k;
        return result;
    }

    /*private static int GreatestCommonDivisor_old(int aa, int bb)
    {
        int result = (bb == 0) ? aa : ExtMath.GreatestCommonDivisor(bb, aa % bb);
        return result;
    }*/

    public static int GreatestCommonDivisor(int aa, int bb)
    {
        while (bb > 0) {
            int tmp = bb;
            bb = aa % bb;
            aa = tmp;
        }
        return aa;
    }

    public static int GreatestCommonDivisor(int[] ia)
    {
        int res = ia[0];
        for (int i = 1; i < ia.length; i++) {
            res = GreatestCommonDivisor(res, ia[i]);
        }
        return res;
    }

    // FIXME
    public static int[] lcm(int[] ia)
    {
        int j = GreatestCommonDivisor(ia);
        if (j == 1) {
            return ia;
        }
        int i = 0;
        while (i < ia.length) {
            ia[i] /= j;
            i += 1;
        }
        return lcm(ia);
    }

    public static int LeastCommonDividend(ArrayList<Integer> numbers)
    {
        ArrayList<Integer> MV = new ArrayList<>();
        ArrayList<Integer> MC = new ArrayList<>();

        for (Integer num : numbers) {
            ArrayList<Integer> M = transform(num);
            optimize(M, MV, MC);
        }

        int result = 1;
        for (int i = 0; i < MV.size(); i++) {
            for (int k = 1; k <= MC.get(i); k++) {
                result *= MV.get(i);
            }
        }
        return result;
    }

    private static ArrayList<Integer> transform(int number)
    {
        ArrayList<Integer> result = new ArrayList<>();
        int N = number;
        while (N > 1) {
            for (int M = 2; M <= N; M++) {
                if (AuxUtils.Frac((N / (double) M)) == 0.0f) {
                    result.add(M);
                    N /= M;
                    break;
                }
            }
        }
        return result;
    }

    private static int optimize_indexOf(ArrayList<Integer> A, ArrayList<Integer> B, int value)
    {
        for (int i = 0; i < A.size(); i++) {
            if (A.get(i) == value) {
                return i;
            }
        }

        A.add(0);
        B.add(0);
        int result = A.size() - 1;
        return result;
    }

    private static void optimize(ArrayList<Integer> multiplier, ArrayList<Integer> mValue, ArrayList<Integer> mCount)
    {
        for (int i = 0; i < multiplier.size(); i++) {
            if (multiplier.get(i) == 0) {
                continue;
            }

            int count = 1;
            for (int k = i + 1; k < multiplier.size(); k++) {
                if (Objects.equals(multiplier.get(k), multiplier.get(i))) {
                    count++;
                    multiplier.set(k, 0);
                }
            }

            int ind = optimize_indexOf(mValue, mCount, multiplier.get(i));
            if (mCount.get(ind) < count) {
                mCount.set(ind, count);
            }
            mValue.set(ind, multiplier.get(i));
        }
    }
}
