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
 * @author Serg V. Zhdanovskih, Ruslan N. Garipov
 * @since 0.3.0
 */
public final class BalanceSolver extends BaseObject
{
    public static final int RES_OK = 0;
    public static final int RES_NoUniqueSolution = 1;
    public static final int RES_EquationCanNotBeBalanced = 2;

    private final int[][] fData = new int[9][10];
    private int[] fFactors;

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
        return this.fElementsCount++;
    }

    public final int getFactor(int reagentIndex)
    {
        return this.fFactors[reagentIndex];
    }

    public final void setData(int elemIndex, int reagentIndex, boolean isProduct, int value)
    {
        if (isProduct) {
            this.fData[elemIndex][reagentIndex] -= value;
        } else {
            this.fData[elemIndex][reagentIndex] += value;
        }
    }

    public final int balanceByLeastSquares()
    {
        int[][] A = new int[this.fReagentsCount - 1][this.fReagentsCount - 1];
        int[] b = new int[A.length];

        for (int i = 0; i < A.length; i++) {
            // 'Cos `A` is a square matrix I use `A.length` field for the both sizes.
            for (int j = 0; j < A.length; j++) {
                for (int k = 0; k < this.fElementsCount; ++k) {
                    A[i][j] += this.fData[k][i + 1] * this.fData[k][j + 1];
                    if (0 == j)
                    {
                        b[i] -= this.fData[k][i + 1] * this.fData[k][this.fReagentsCount];
                    }
                }
            }
        }

        // You're gonna cancel out the matrix `A` and vector `b`, aren't you?
        int G = 0;
        for (int i = 0; i < A.length; i++) {
            int min = ExtMath.gcd(ExtMath.gcd(A[i], 0), b[i]);
            for (int j = 0; j < A.length; j++) {
                A[i][j] /= min;
            }
            b[i] /= min;
        }

        RefObject<Integer> refG = new RefObject<>(G);
        A = this.inversion(A, refG);
        G = refG.argValue;

        if (G == 0) {
            return BalanceSolver.RES_NoUniqueSolution;
        }

        b = this.multiply(A, b);

        // WOW! The following is array resizing in Java?
        int[] bTemp = new int[b.length + 1];
        System.arraycopy(b, 0, bTemp, 0, b.length);
        b = bTemp;
        b[b.length - 1] = G;

        simplify(b);
        fFactors = b.clone();

        int result = 0;
        for (int i = 0; i < this.fElementsCount; i++) {
            int r = 0;
            for (int j = 0; j < this.fReagentsCount; j++) {
                r += this.fData[i][j + 1] * this.fFactors[j];
            }
            result += Math.abs(r);
        }
        if (0 != result) {
            java.util.Arrays.fill(fFactors, 1);
            return BalanceSolver.RES_EquationCanNotBeBalanced;
        }

        return result;
    }

    /**
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

    /**
     * Finds a next possible permutations of the given sequence.
     * @param permutation
     * On input this is a previous permutation of the sequence. On return this is the next one.
     * On the first call to the method this array must be a sequence with ascending order.
     * On each consecutive call the method generates the next permutation in-place.
     * @param parity
     * parity of the transpositions of the previous permutation ("parity of the permutation").
     * @return parity of the transpositions made during this permutation (-1 for odd and 1 for even).
     *
     * Remarks
     * The method implements generation in lexicographic order (https://en.wikipedia.org/wiki/Permutation#Generation_in_lexicographic_order).
     *
     * The sign of a permutation σ is defined as +1 if σ is even and −1 if σ is odd.
     *
     * [Ruslan N. Garipov]: Why is the method called 'dijkstra'? Dijkstra's algorithm is an algorithm for finding
     * the shortest paths between nodes in a graph. Not for finding possible commutation of elements. Do I confuse
     * with that?
     */
    private static int dijkstra(int[] permutation, int parity)
    {
        /*
         * Search inversions.
         *
         * Search for an ordered subrange, starting from the last element in the array.
         */
        int i = permutation.length - 1;
        while (permutation[i - 1] >= permutation[i]) {
            --i;
        }
        /*
         * Find a replacement for the "bad" element -- one that breaks ascending order after the subrange [{i}; {last}).
         * Here {i} is an index of array element and "ascending order" is when you read the array from right to left.
         *
         * Since the caller of this method limits number of such calls to `permutation.length!` (length factorial)
         * neither `i` not `j` may be negative number after subtractions made by `--` op.
         */
        int j = permutation.length - 1;
        while (permutation[i - 1] >= permutation[j]) {
            --j;
        }
        parity = -parity;  // zsv: I like it better
        /*
         * Move a bigger element to the left. And because initially, at the first call to `dijkstra`, the `permutation` was
         * an ordered sequence, now, after the swap, subrange [{i}, {last}) has descending order, if you look on it from
         * left to right (it also was such before the swap, but now the difference between each pair of neighbouring
         * elements is always one).
         */
        swap(permutation, i - 1, j);
        /*
         * Reverse elements order in the [{i}, {last}) range.
         */
        j = permutation.length - 1;
        while (i < j) {
            parity = -parity;
            swap(permutation, i, j);
            ++i;
            --j;
        }
        return parity;
    }

    // The method returns the matrix of cofactors not the inverse of `mtx`!
    private int[][] inversion(int[][] mtx, RefObject<Integer> det)
    {
        if (mtx.length < 1) {
            throw new RuntimeException("Inversion: wrong input");
        }

        int[][] invert = new int[mtx.length][mtx.length];

        det.argValue = 0;
        for (int i = 0; i < invert.length; i++) {
            for (int j = 0; j < invert.length; j++) {
                invert[i][j] = getCofactor(j, i, mtx, mtx.length);
            }
            // Now, here, the first row in `invert` matrix is initialized (invert[0][{any}]).
            det.argValue += mtx[i][0] * invert[0][i];
        }

        return invert;
    }

    private int[] multiply(int[][] een, int[] twee)
    {
        int[] uit = new int[een.length];
        for (int i = 0; i < uit.length; i++) {
            for (int j = 0; j < uit.length; j++) {
                uit[i] += een[i][j] * twee[j];
            }
        }
        return uit;
    }

    private void simplify(int[] b)
    {
        int from = 0;
        int gcd = ExtMath.gcd(b, from);
        for (int i = from; i < b.length; i++) {
            b[i] = Math.abs(b[i] / gcd);
        }
    }

    /**
     * Finds matrix cofactor of the specified entry.
     * @param row
     * Row index of entry for which the method calculates cofactor.
     * @param column
     * Column index of entry for which the method calculates cofactor.
     * @param mtx
     * Source matrix for which cofactors are calculated. It's a square matrix of size `size` x `size`.
     * @param size
     * Size of `mtx`. Can be removed from the code; it can use `mtx.length` property instead.
     * @return 
     * Cofactor 'C' of the entry (`row`, `column`).
     */
    private static int getCofactor(int row, int column, int[][] mtx, int size)
    {
        if ((size < 1) || (size > 8)) {
            // Why such limits?
            throw new RuntimeException("subdet: input invalid");
        }

         // We're about to calculate determinant of the `mtx` _sub_matrix (a matrix without one row and column).
        int[] permutation = new int[size - 1];
         /*
          * Number of all possible commutations of numbers in `permutation` is factorial of `permutation` length.
          */
        int numberOfPermutations = 1;
        for (int k = 0; k < permutation.length; k++) {
            permutation[k] = k;
            numberOfPermutations *= k + 1;
        }
        int minor = 0;
        // The identity permutation is an even permutation (its signature is +1).
        int sgn = 1;
        for (int m = 0; m < numberOfPermutations; ++m) {
            if (0 < m) {
                // Find a permutation; for the first iteration use `permutation` as the first possible permutation.
                sgn = dijkstra(permutation, sgn);
            }

            int value = sgn;
            for (int k = 0; k < permutation.length; k++) {
                // "delete" the `row`-th row and `column`-th column.
                int i = (k >= row) ? k + 1 : k;
                int j = (permutation[k] >= column) ? permutation[k] + 1 : permutation[k];
                value *= mtx[i][j];
            }
            minor += value;
        }
        // Get cofactor of the entry in the `row`-th row and `column`-th column (it's the product of 
        // (-1)^(`row` + `column`) and `minor`).
        return 0 != ((row + column) % 2) ? -minor : minor;
    }
}
