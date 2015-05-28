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
package chemlab.core.controls.molecule.loader;

import bslib.common.AuxUtils;
import bslib.common.Logger;
import bslib.common.RefObject;
import bslib.common.StringHelper;
import chemlab.core.chemical.BondKind;
import chemlab.core.chemical.CLData;
import chemlab.core.controls.molecule.MolAtom;
import chemlab.core.controls.molecule.MolBond;
import chemlab.core.controls.molecule.Molecule;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class FilesLoader
{
    private static double convertXYZCoord(String coord) throws ParseException
    {
        StringBuilder sb = new StringBuilder(coord);
        if (sb.indexOf("-.") == 0) {
            sb.insert(1, "0");
        }
        return (AuxUtils.ParseFloat(sb.toString(), 0.0f));
    }

    public static final void loadFromXYZ(String fileName, Molecule activeMolecule)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line) && line.charAt(0) != '#') {
                        line = AuxUtils.prepareString(line);
                        String[] parts = line.trim().split("[ ]", -1);

                        String sym = parts[0];
                        double xx = convertXYZCoord(parts[1]);
                        double yy = convertXYZCoord(parts[2]);
                        double zz = convertXYZCoord(parts[3]);

                        MolAtom curAtom = activeMolecule.addAtom();
                        curAtom.setSign(sym);
                        curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(sym));
                        curAtom.setX(xx);
                        curAtom.setY(yy);
                        curAtom.setZ(zz);
                    }

                    line = tf.readLine();
                }

                String title = (new java.io.File(fileName)).getName();
                activeMolecule.setName(title.substring(0, title.indexOf(".")));
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    public static final void loadFromMM1GP(String fileName, Molecule activeMolecule)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                int aCnt = 0;
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line)) {
                        line = AuxUtils.prepareString(line);
                        String[] params;

                        if (line.indexOf("!Atoms") == 0) {
                            params = AuxUtils.getParams(line);
                            aCnt = AuxUtils.ParseInt(params[1], 0);

                            for (int i = 0; i < aCnt; i++) {
                                line = tf.readLine();
                                params = AuxUtils.getParams(line);

                                int eNum = AuxUtils.ParseInt(params[1], 0);
                                if (eNum >= -1 && eNum <= 118) {
                                    MolAtom curAtom = activeMolecule.addAtom();
                                    curAtom.setSign(CLData.ElementsBook.get(eNum - -1).FSymbol);
                                    curAtom.setElementNumber(eNum);
                                }
                            }
                        }

                        if (line.indexOf("!Bonds") == 0) {
                            params = AuxUtils.getParams(line);
                            int bCnt = AuxUtils.ParseInt(params[1], 0);
                            if (bCnt > 0) {
                                for (int i = 0; i < bCnt; i++) {
                                    line = tf.readLine();

                                    params = AuxUtils.getParams(line);
                                    int aNum = AuxUtils.ParseInt(params[0], 0);
                                    int aNum2 = AuxUtils.ParseInt(params[1], 0);
                                    MolBond curBond = activeMolecule.addBond(activeMolecule.getAtom(aNum), activeMolecule.getAtom(aNum2), BondKind.bkSingle);
                                    curBond.setKindByChar(params[2].charAt(0));
                                }
                            }
                        }

                        if (line.indexOf("!Coord") == 0) {
                            for (int i = 0; i < aCnt; i++) {
                                line = tf.readLine();
                                params = AuxUtils.getParams(line);

                                int aNum = AuxUtils.ParseInt(params[0], 0);
                                MolAtom atom = activeMolecule.getAtom(aNum);
                                atom.setX(AuxUtils.ParseFloat(params[1], 0));
                                atom.setY(AuxUtils.ParseFloat(params[2], 0));
                                atom.setZ(AuxUtils.ParseFloat(params[3], 0));
                            }
                        }

                        if (line.indexOf("!Charges") == 0) {
                            for (int i = 0; i < aCnt; i++) {
                                line = tf.readLine();

                                params = AuxUtils.getParams(line);
                                int aNum = AuxUtils.ParseInt(params[0], 0);
                                activeMolecule.getAtom(aNum).setPartialCharge((float) (AuxUtils.ParseFloat(params[1], 0)));
                            }
                        }
                    }

                    line = tf.readLine();
                }

                String title = (new java.io.File(fileName)).getName();
                activeMolecule.setName(title.substring(0, title.indexOf(".")));
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    public static final void loadFromHIN(String fileName, Molecule activeMolecule)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                ArrayList<HINBond> tmpBonds = new ArrayList<>();
                int i;
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line)) {
                        line = AuxUtils.prepareString(line);
                        String[] params;

                        if (line.indexOf("mol") == 0) {
                            params = AuxUtils.getParams(line);
                            int mol = AuxUtils.ParseInt(params[1], 0);
                            if (mol > 1) {
                                break; // файлы с двумя и более молекулами не отрабатываем
                            }
                        }

                        if (line.indexOf("endmol") == 0) {
                            break;
                        }

                        if (line.indexOf("atom") == 0) {
                            params = AuxUtils.getParams(line);
                            if (params.length <= 11) {
                                break;
                            }

                            MolAtom curAtom = activeMolecule.addAtom();
                            curAtom.setSign(params[3]);
                            curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(params[3]));
                            curAtom.setX(AuxUtils.ParseFloat(params[7], 0));
                            curAtom.setY(AuxUtils.ParseFloat(params[8], 0));
                            curAtom.setZ(AuxUtils.ParseFloat(params[9], 0));

                            int bonds = AuxUtils.ParseInt(params[10], 0);
                            i = 11;
                            for (int k = 1; k <= bonds; k++) {
                                HINBond hinBond = new HINBond();
                                tmpBonds.add(hinBond);
                                char c = params[i + 1].charAt(0);
                                switch (c) {
                                    case 'a':
                                        hinBond.bt = BondKind.bkConjugated;
                                        break;
                                    case 'd':
                                        hinBond.bt = BondKind.bkDouble;
                                        break;
                                    case 's':
                                        hinBond.bt = BondKind.bkSingle;
                                        break;
                                    case 't':
                                        hinBond.bt = BondKind.bkTriple;
                                        break;
                                }
                                hinBond.F = AuxUtils.ParseInt(params[1], 0);
                                hinBond.T = AuxUtils.ParseInt(params[i], 0);
                                i += 2;
                            }
                        }
                    }
                    
                    line = tf.readLine();
                }

                for (i = 0; i < tmpBonds.size(); i++) {
                    activeMolecule.addBond(activeMolecule.getAtom(tmpBonds.get(i).F - 1), activeMolecule.getAtom(tmpBonds.get(i).T - 1), tmpBonds.get(i).bt);
                }

                String title = (new java.io.File(fileName)).getName();
                activeMolecule.setName(title.substring(0, title.indexOf(".")));
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    public static final void loadFromMDL(String fileName, Molecule activeMolecule)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                int line = 0;
                int aCnt = 0;
                int bCnt = 0;
                String sline = tf.readLine();
                while (sline != null) {
                    sline = sline.trim();
                    line++;

                    if (!StringHelper.isNullOrEmpty(sline)) {
                        sline = AuxUtils.prepareString(sline);

                        if (line == 4) {
                            String[] params;
                            params = AuxUtils.getParams(sline);
                            aCnt = AuxUtils.ParseInt(params[0], 0);
                            bCnt = AuxUtils.ParseInt(params[1], 0);
                        } else {
                            if (line > 4 && line <= 4 + aCnt) {
                                String[] params;
                                params = AuxUtils.getParams(sline);

                                MolAtom curAtom = activeMolecule.addAtom();
                                curAtom.setSign(params[3]);
                                curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(params[3]));
                                curAtom.setX(AuxUtils.ParseFloat(params[0], 0));
                                curAtom.setY(AuxUtils.ParseFloat(params[1], 0));
                                curAtom.setZ(AuxUtils.ParseFloat(params[2], 0));

                                String tempVar = params[4];
                                if (StringHelper.equals(tempVar, "1")) {
                                    curAtom.setFormalCharge(3);
                                } else if (StringHelper.equals(tempVar, "2")) {
                                    curAtom.setFormalCharge(2);
                                } else if (StringHelper.equals(tempVar, "3")) {
                                    curAtom.setFormalCharge(1);
                                } else if (StringHelper.equals(tempVar, "5")) {
                                    curAtom.setFormalCharge(-1);
                                } else if (StringHelper.equals(tempVar, "6")) {
                                    curAtom.setFormalCharge(-2);
                                } else if (StringHelper.equals(tempVar, "7")) {
                                    curAtom.setFormalCharge(-3);
                                }
                            } else if (line > 4 + aCnt && line <= 4 + aCnt + bCnt) {
                                String[] params;
                                params = AuxUtils.getParams(sline);
                                int F = AuxUtils.ParseInt(params[0], 0);
                                int T = AuxUtils.ParseInt(params[1], 0);
                                BondKind bt = BondKind.bkSingle;
                                int num = AuxUtils.ParseInt(params[2], 0);
                                switch (num) {
                                    case 1:
                                        bt = BondKind.bkSingle;
                                        break;
                                    case 2:
                                        bt = BondKind.bkDouble;
                                        break;
                                    case 3:
                                        bt = BondKind.bkTriple;
                                        break;
                                    case 4:
                                        bt = BondKind.bkConjugated;
                                        break;
                                }

                                activeMolecule.addBond(activeMolecule.getAtom(F - 1), activeMolecule.getAtom(T - 1), bt);
                            } else if (line > 4 + aCnt + bCnt) {
                                String[] params;
                                params = AuxUtils.getParams(sline);
                                if (StringHelper.equals(params[0], ">") && StringHelper.equals(params[1], "<MOLNAME>")) {
                                    sline = tf.readLine();

                                    line++;
                                    activeMolecule.setName(sline);
                                }
                            }
                        }
                    }
                    
                    sline = tf.readLine();
                }
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    private static MCMLineKind detectMCMKind(String s)
    {
        MCMLineKind result = MCMLineKind.lkND;

        int p = s.indexOf("-");
        int p2 = s.indexOf(":");

        if (p >= 0 && p < s.length() && CLData.LatSymbols.indexOf(s.charAt(p + 1)) >= 0) {
            result = MCMLineKind.lkBond;
        } else if (p2 >= 0 && p2 < s.length() && CLData.SignedNumbers.indexOf(s.charAt(p2 + 1)) >= 0) {
            result = MCMLineKind.lkAtom;
        }

        return result;
    }

    private static void divMCMAtom(String atom, RefObject<String> element, RefObject<Integer> number)
    {
        int i = 1;
        element.argValue = "";
        String num = "";

        while (i <= ((atom != null) ? atom.length() : 0)) {
            if (CLData.Numbers.indexOf(atom.charAt(i - 1)) >= 0) {
                num += (atom.charAt(i - 1));
            } else {
                element.argValue += (atom.charAt(i - 1));
            }
            i++;
        }

        number.argValue = AuxUtils.ParseInt(num, 0);
    }

    private static void prepareMCMAtom(Molecule molecule, String s)
    {
        try {
            int p = s.indexOf(";");
            String needs = s.substring(0, p);

            p = needs.indexOf(":");
            String atom = needs.substring(0, p);
            String coords = needs.substring(p + 1, p + 1 + needs.length() - (p + 1)).trim();

            String element = null;
            int number = 0;
            RefObject<String> tempRef_element = new RefObject<>(element);
            RefObject<Integer> tempRef_number = new RefObject<>(number);
            divMCMAtom(atom, tempRef_element, tempRef_number);
            element = tempRef_element.argValue;
            number = tempRef_number.argValue;

            String[] params;
            params = AuxUtils.getParams(coords);

            if (number > molecule.getAtomCount()) {
                molecule.allocateAtoms(number);
            }

            MolAtom curAtom = molecule.getAtom(number - 1);
            curAtom.setSign(element);
            curAtom.setElementNumber(CLData.ElementsBook.findElementNumber(element));
            curAtom.setX(AuxUtils.ParseFloat(params[0], 0));
            curAtom.setY(AuxUtils.ParseFloat(params[1], 0));
            curAtom.setZ(AuxUtils.ParseFloat(params[2], 0));
        } catch (ParseException ex) {
            Logger.write("" + ex.getMessage());
        }
    }

    private static void prepareMCMBond(Molecule molecule, String s)
    {
        String[] parts = s.split("[-]", -1);
        String eL = null;
        int nL = 0;
        RefObject<String> tempRef_eL = new RefObject<>(eL);
        RefObject<Integer> tempRef_nL = new RefObject<>(nL);
        divMCMAtom(parts[0], tempRef_eL, tempRef_nL);
        eL = tempRef_eL.argValue;
        nL = tempRef_nL.argValue;

        String eR = null;
        int nR = 0;
        RefObject<String> tempRef_eR = new RefObject<>(eR);
        RefObject<Integer> tempRef_nR = new RefObject<>(nR);
        divMCMAtom(parts[1], tempRef_eR, tempRef_nR);
        eR = tempRef_eR.argValue;
        nR = tempRef_nR.argValue;

        molecule.addBond(molecule.getAtom(nL - 1), molecule.getAtom(nR - 1), BondKind.bkSingle);
    }

    private enum MCMLineKind { lkND, lkAtom, lkBond }
    
    public static final void loadFromMCM(String fileName, Molecule activeMolecule)
    {
        try {
            BufferedReader tf = new BufferedReader(new FileReader(fileName));
            try {
                String line = tf.readLine();
                while (line != null) {
                    if (!StringHelper.isNullOrEmpty(line) && line.charAt(0) != ';') {
                        if (line.charAt(0) == '\\') {
                            break;
                        }

                        if (line.indexOf("file") < 0 && line.indexOf("atoms") < 0 && line.indexOf("bonds") < 0 && line.indexOf(" = ") < 0) {
                            MCMLineKind kind = detectMCMKind(line);

                            switch (kind) {
                                case lkAtom:
                                    prepareMCMAtom(activeMolecule, line);
                                    break;

                                case lkBond:
                                    prepareMCMBond(activeMolecule, line);
                                    break;
                            }
                        }
                    }

                    line = tf.readLine();
                }
            } finally {
                tf.close();
            }
        } catch (Exception ex) {
            Logger.write("" + ex.getMessage());
        }
    }
}
