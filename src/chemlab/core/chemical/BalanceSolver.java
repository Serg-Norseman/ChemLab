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
        int N = (permutation != null) ? permutation.length : 0;
        /*
         * `N` can be zero here. If so you _can_ get an "access violation" if you would write that in native C++:
         * `int i = N - 1;`
         * `... permutation[i - 1] ...` == `... permutation[-2] ...` -- how do Java handle this?
         */
        int i = N - 1;
        while (permutation[i - 1] >= permutation[i]) {
            i--;
        }
        int j = N;
        while (permutation[j - 1] <= permutation[i - 1]) {
            j--;
        }
        teken *= -1;  // is `teken = -teken` better?
        swap(permutation, i - 1, j - 1);
        i++;
        j = N;
        if (i < j) {
            do {
                teken *= -1
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

        for (int i = 1; i <= this.fDimension; i++) {
            for (int j = 1; j <= this.fDimension; j++) {
                invert[i][j] = subdeterminant(j, i, this.fDimension, mtx);
            }
        }

        det.argValue = 0;
        for (int i = 1; i <= this.fDimension; i++) {
            det.argValue += mtx[i][1] * invert[1][i];
        }

        return invert;
    }

    private int[] multiply(int[][] een, int[] twee)
    {
        int dim = this.fDimension;
        int[] uit = new int[dim + 1];

        for (int i = 1; i <= dim; i++) {
            int h = 0;
            for (int j = 1; j <= dim; j++) {
                h += een[i][j] * twee[j];
            }
            uit[i] = h;
        }

        return uit;
    }

    private void simplify(int[] b)
    {
        if (b[this.fReagentsCount] < 0) {
            for (int i = 1; i <= this.fReagentsCount; i++) {
                b[i] = -b[i];
            }
        }

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
        boolean fout = ndm < 1 || ndm > 8;
        if (fout) {
            throw new RuntimeException("subdet: input invalid");
        }

        int[] rij = new int[ndm - 1];
        for (int k = 0; k <= ndm - 2; k++) {
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

        for (int i = 1; i < this.fReagentsCount; i++) {
            for (int j = 1; j < this.fReagentsCount; j++) {
                A[i][j] = 0;

                for (int k = 1; k <= this.fElementsCount; k++) {
                    A[i][j] += this.fData[k - 1][i] * this.fData[k - 1][j];
                }
            }
        }

        for (int i = 1; i < this.fReagentsCount; i++) {
            b[i] = 0;

            for (int k = 1; k <= this.fElementsCount; k++) {
                b[i] -= this.fData[k - 1][i] * this.fData[k - 1][this.fReagentsCount];
            }
        }

        int G = 0;
        for (int i = 1; i < this.fReagentsCount; i++) {
            int min = 0;
            int twee = 0;
            int een;

            for (int j = 1; j < this.fReagentsCount; j++) {
                if (A[i][j] != 0) {
                    een = Math.abs(A[i][j]);
                    if (twee > 0) {
                        G = ExtMath.gcd(een, twee);
                        boolean vgl = min == 0 || G < min;
                        if (vgl) {
                            min = G;
                        }
                    }
                    twee = een;
                }
            }

            een = Math.abs(b[i]);
            if (een > 0) {
                G = ExtMath.gcd(een, twee);
                boolean vgl = min == 0 || G < min;
                if (vgl) {
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

        int[] bTemp = new int[this.fReagentsCount + 1];
        System.arraycopy(b, 0, bTemp, 0, this.fReagentsCount);
        b = bTemp;
        b[this.fReagentsCount] = G;

        this.simplify(b);

        this.fFactors = new int[this.fReagentsCount + 1];
        for (int i = 1; i <= this.fReagentsCount; i++) {
            this.fFactors[i] = b[i];
        }

        for (int i = 1; i <= this.fElementsCount; i++) {
            int r = 0;
            for (int j = 1; j <= this.fReagentsCount; j++) {
                r += this.fData[i - 1][j] * this.fFactors[j];
            }
            result += Math.abs(r);
        }

        if (result > 0) {
            for (int i = 1; i <= this.fReagentsCount; i++) {
                this.fFactors[i] = 1;
            }
            
            return BalanceSolver.RES_EquationCanNotBeBalanced;
        }

        return result;
    }
}
