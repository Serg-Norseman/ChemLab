/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-1998, 2015 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
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
package chemlab.core.chemical;

import bslib.common.BaseObject;
import bslib.common.RefObject;
import bslib.math.ExtMath;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.3.0
 */
public final class BalanceSolver extends BaseObject
{
    public static final int RES_OK = 0;
    public static final int RES_NoUniqueSolution = 1;
    public static final int RES_EquationCanNotBeBalanced = 2;

    private final int[][] fData = new int[9][10];
    private int[] fFactors;

    private int fDimension;
    private int fElementsCount;
    private int fReagentsCount;

    public final void setReagentsCount(int value)
    {
        this.fReagentsCount = value;
    }

    public final int addElement(int elementId)
    {
        for (int i = 0; i < this.fElementsCount; i++) {
            if (this.fData[i][0] == elementId) {
                return i;
            }
        }

        this.fData[this.fElementsCount][0] = elementId;
        return ++this.fElementsCount;
    }

    public final int getFactor(int reagentIndex)
    {
        return this.fFactors[reagentIndex];
    }

    public final void setData(int elemIndex, int reagentIndex, boolean isProduct, int value)
    {
        if (isProduct) {
            this.fData[elemIndex - 1][reagentIndex] -= value;
        } else {
            this.fData[elemIndex - 1][reagentIndex] += value;
        }
    }

    private static int teken(int L)
    {
        return 1 - 2 * (L % 2);
    }

    /*
     * `private static int swap(int[] permutation, int k, int m, int teken)`
     * 'teken' ain't used in the swap operation; I believe it must be removed from the argument list. I mean 'teken' is
     * independent of swap op itself, it is some kind of invariant from point of view of the "swap".
     */
    private static void swap(int[] permutation, int k, int m)
    {
        int tmp = permutation[m];
        permutation[m] = permutation[k];
        permutation[k] = tmp;
    }

    private static int dijkstra(int[] permutation, int teken)
    {
        /*
         * The only place where `dijkstra` is called is `subdeterminant` method. `permutation` argument there can't
         * be zero. Therefore I believe the following `?:` operator is unnecessary.
         * Moreover, see the comment below...
         */
//        int N = (permutation != null) ? permutation.length : 0;
        /*
         * `N` can be zero here. If so you _can_ get an "access violation" if you would write the following code
         * in native C++:
         *
         * `int i = N - 1;`  // `N` is zero!
         * `... permutation[i - 1] ...` IT IS `... permutation[-2] ...` -- how do Java handle this?
         *
         * May be it would be better to check length of `permutation`? Something like `assert(1 < permutation.length);`
         */
//        int i = N - 1;
        int i = permutation.length - 1;
        while (permutation[i - 1] >= permutation[i]) {
            i--;
        }
        int j = permutation.length;
        while (permutation[j - 1] <= permutation[i - 1]) {
            j--;
        }
        teken *= -1;  // is `teken = -teken` better?
        swap(permutation, i - 1, j - 1);
        i++;
        j = permutation.length;
        if (i < j) {
            do {
                teken *= -1;
                swap(permutation, i - 1, j - 1);
                i++;
                j--;
            } while (i < j);
        }

        return teken;
    }

    private int[][] inversion(int[][] mtx, RefObject<Integer> det)
    {
        if (this.fDimension < 1) {
            throw new RuntimeException("Inversion: wrong input");
        }

        int[][] invert = new int[this.fDimension + 1][this.fDimension + 1];

        det.argValue = 0;
        for (int i = 1; i <= this.fDimension; i++) {
            for (int j = 1; j <= this.fDimension; j++) {
                invert[i][j] = subdeterminant(j, i, this.fDimension, mtx);
            }
            // Now, here, the first row in `invert` matrix is initialized (invert[1][{any}]).
            det.argValue += mtx[i][1] * invert[1][i];
        }

        return invert;
    }

    private int[] multiply(int[][] een, int[] twee)
    {
        int[] uit = new int[this.fDimension + 1];

        for (int i = 1; i <= this.fDimension; i++) {
            uit[i] = 0;
            for (int j = 1; j <= this.fDimension; j++) {
                uit[i] += een[i][j] * twee[j];
            }
        }

        return uit;
    }

    private void simplify(int[] b)
    {
        // I would replace the following line with `if (0 > b[b.length - 1])` -- it's more obvious.
        // There are too many bindings to the `fReagentsCount` all over the code.
        if (b[this.fReagentsCount] < 0) {
            // `for (int i = 1; i < b.length; i++)` or `for ({type} value: {container})`
            for (int i = 1; i <= this.fReagentsCount; i++) {
                b[i] = -b[i];
            }
        }
        /*
         * You've inverted elements in `b` array above ^... because implementation of `ExtMath.gcd` has a bug?
         *  `ExtMath.gcd` fails on negative integers:
         * (a) `ExtMath.gcd(10, -4)` returns -10,
         * (b) `ExtMath.gcd(-10, 4)` returns 4.
         * While both calls must return 2.
         *
         * If we'll fix it and allow negative integers in `ExtMath.gcd`, we can improve performance
         * in many places I believe. But it looks like this will require updating of all algorithms here.
         * I mean, why `b` has negative integers? What it means?..
         *
         * Can we use `ExtMath.gcd` overloaded for arrays here?
         */
        int min = ExtMath.gcd(b[this.fReagentsCount], b[this.fReagentsCount - 1]);

        for (int i = 1; i <= this.fReagentsCount - 2; i++) {
            int u = ExtMath.gcd(b[this.fReagentsCount], b[i]);
            if (u < min) {
                min = u;
            }
        }

        for (int i = 1; i <= this.fReagentsCount; i++) {
            b[i] /= min;
        }
    }

    private static int subdeterminant(int ii, int jj, int ndm, int[][] mtx)
    {
        if ((ndm < 1) || (ndm > 8)) {
            throw new RuntimeException("subdet: input invalid");
        }

        int[] rij = new int[ndm - 1];
        for (int k = 0; k < rij.length; k++) {
            rij[k] = k + 1;
        }

        int det = 0;
        int even = 1;

        int num8 = ExtMath.factorial(ndm - 1);
        for (int m = 1; m <= num8; m++) {
            if (m > 1) {
                even = dijkstra(rij, even);
            }

            int term = even;

            for (int k = 1; k <= ndm - 1; k++) {
                int i = k + ((k >= ii) ? 1 : 0);
                int j = rij[k - 1] + (int) ((rij[k - 1] >= jj) ? 1 : 0);
                term *= mtx[i][j];
            }
            det += term;
        }

        int result = teken(ii + jj) * det;

        return result;
    }

    public final int balanceByLeastSquares()
    {
        int[][] A = new int[this.fReagentsCount][this.fReagentsCount];
        int[] b = new int[this.fReagentsCount];

        for (int i = 1; i < A.length; i++) {
//            b[i] = 0; -- Java do this by itself.
            // 'Cos `A` is a square matrix I use `A.length` field for the both sizes.
            for (int j = 1; j < A.length; j++) {
//                A[i][j] = 0; -- Java guarantees `A` was initialized with 0s!
                for (int k = 0; k < this.fElementsCount; ++k) {
                    A[i][j] += this.fData[k][i] * this.fData[k][j];
                    if (1 == j)
                    {
                        b[i] -= this.fData[k][i] * this.fData[k][this.fReagentsCount];
                    }
                }
            }
        }

        /*
         * The first column and the first row in `A` matrix and the first element in `b` vector are all zeros.
         * Can we remove them and change sizes of the object? This requires more deeply code analysis.
         */

        // You're gonna cancel out the matrix `A` and vector `b`, aren't you?
        int G = 0;
        for (int i = 1; i < this.fReagentsCount; i++) {
            int min = 0;
            int twee = 0;
            int een;

            /*
             * The following `for` loop is modified GCD algorithm for vectors. You already have the implementation
             * of `ExtMath.gcd` method overloaded for arrays. Can we use it? Does the "balance-by-least-squares"
             * require the modified GCD?
             */
            for (int j = 1; j < this.fReagentsCount; j++) {
                if (0 != A[i][j]) {
                    if (0 != twee) {
                        // Here we have slightly modified GCD calculation that doesn't allow zeros in any arguments.
                        // (Is it required by the algorithm? If no we can remove one of `if's` above).
                        G = ExtMath.gcd(A[i][j], twee);
                        if ((min == 0) || (G < min)) {
                            min = G;
                        }
                    }
                    twee = A[i][j];
                }
            }

            if (0 != b[i]) {
                // `twee` can be zero here and we don't check it. Therefore I believe we can allow one zero-value
                // argument in the code above as we do here (thus removing `if (0 != twee)` statement).
                G = ExtMath.gcd(b[i], twee);
                if ((min == 0) || (G < min)) {
                    min = G;
                }
            }

            for (int j = 1; j < this.fReagentsCount; j++) {
                A[i][j] /= min;
            }
            b[i] /= min;
        }

        int result = 0;
        this.fDimension = this.fReagentsCount - 1;

        RefObject<Integer> refG = new RefObject<>(G);
        A = this.inversion(A, refG);
        G = refG.argValue;

        if (G == 0) {
            return BalanceSolver.RES_NoUniqueSolution;
        }

        b = this.multiply(A, b);

        // WOW! The following is array resizing in Java?
        int[] bTemp = new int[this.fReagentsCount + 1];
        System.arraycopy(b, 0, bTemp, 0, b.length);
        b = bTemp;
        b[b.length - 1] = G;

        this.simplify(b);

        // What about using of `b.clone` method?
        // `fFactors = b.clone();`
        this.fFactors = new int[this.fReagentsCount + 1];
        for (int i = 1; i <= this.fReagentsCount; i++) {
            this.fFactors[i] = b[i];
        }

        // `result` is zero here...
        for (int i = 1; i <= this.fElementsCount; i++) {
            int r = 0;
            for (int j = 1; j <= this.fReagentsCount; j++) {
                r += this.fData[i - 1][j] * this.fFactors[j];
            }
            result += Math.abs(r);
        }
        // ... you have been adding NON-NEGATIVE numbers (`result += Math.abs(r)`)...
        // How `result` may become a negative number? Why do you check it below? Did you intent to check for 0?
        if (result > 0) {
            for (int i = 1; i <= this.fReagentsCount; i++) {
                this.fFactors[i] = 1;
            }
            
            return BalanceSolver.RES_EquationCanNotBeBalanced;
        }

        return result;
    }
}
