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

import chemlab.database.CLDB;
import chemlab.refbooks.AllotropeRecord;
import chemlab.refbooks.DecayBook;
import chemlab.refbooks.ElementsBook;
import chemlab.refbooks.NuclidesBook;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public class CLData
{
    public static final String DecimNumbers = "0123456789.,";
    public static final String Numbers = "0123456789";
    public static final String SignedNumbers = "0123456789-";
    public static final String LatUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LatLower = "abcdefghijklmnopqrstuvwxyz";
    public static final String LatSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static char[] BondKinds;
    public static String[] Environment;
    public static String[] Bond;
    public static String[] ElementClasses;
    public static String[] Crystal;
    public static String[] ElementABProperty;
    public static DecayModeRecord[] DecayMode;
    public static String[] SubstanceStates;
    public static String[] SubstanceStateSigns;

    public static AllotropeRecord[] dbAllotropes;

    public static final ElementsBook ElementsBook;
    public static final NuclidesBook NuclidesBook;
    public static final DecayBook DecayBook;
    public static final CLDB Database = CLDB.getInstance();


    static {
        CLData.Environment = new String[]{"Кислая", "Нейтральная", "Основная"};

        CLData.Bond = new String[]{"Невозможно определить", "Ковалентная не полярная", "Ковалентная полярная", "Ионная"};

        CLData.ElementClasses = new String[]{"Щелочной металл", "Щелочноземельный металл", "Переходный металл", "Редкоземельный металл", "Металл", "Благородный газ", "Галоген", "Неметалл"};

        CLData.Crystal = new String[]{"Кубическая", "Куб. объемо-центрированная", "Куб. гранецентрированная", "Гексагональная", "Моноклинная", "Орторомбическая", "Ромбоэдральная", "Тетрагональная"};

        CLData.ElementABProperty = new String[]{"Кислый", "Амфотер", "Основной"};

        DecayModeRecord[] decays = new DecayModeRecord[8];
        decays[0] = new DecayModeRecord("Stable", "Стабилен");
        decays[1] = new DecayModeRecord("a", "Альфа-распад");
        decays[2] = new DecayModeRecord("b-b-", "Двойной бета-распад");
        decays[3] = new DecayModeRecord("b-", "Бета-распад");
        decays[4] = new DecayModeRecord("b+", "Позитронный распад");
        decays[5] = new DecayModeRecord("EC", "Захват электрона");
        decays[6] = new DecayModeRecord("IT", "Изомерный переход");
        decays[7] = new DecayModeRecord("SF", "Спонтанное деление ядер");
        CLData.DecayMode = decays;

        CLData.SubstanceStates = new String[]{"Твердое тело", "Жидкость", "Газ", "Ион"};
        CLData.SubstanceStateSigns = new String[]{" (s)", " (l)", " (g)", " (i)"};

        BondKinds = new char[]{'S', 'D', 'T', 'C', 'Q'};

        dbAllotropes = new AllotropeRecord[128];

        ElementsBook = new ElementsBook();
        NuclidesBook = new NuclidesBook();
        DecayBook = new DecayBook();
    }

    public static void transferData()
    {
        /*System.out.println("records: " + CompoundsBook.size());
        
        try {
            for (int i = 0; i < CompoundsBook.size(); i++) {
                CompoundRecord oldRec = CompoundsBook.get(i);

                CompoundRec newRec = Database.getCompound(oldRec.Formula, true);
                double mMass = oldRec.getMolecularMass();

                if (mMass == 0.0) {
                    try {
                        CompoundSolver fCompoundMaster = new CompoundSolver();
                        fCompoundMaster.Formula = oldRec.Formula;
                        fCompoundMaster.Charge = 0;
                        fCompoundMaster.analyse();
                        fCompoundMaster.loadData();
                        fCompoundMaster.calculateMolecularMass();
                        mMass = fCompoundMaster.getMolecularMass();
                    } catch (Exception ex) {
                    }
                }

                newRec.setMolecularMass(mMass);
                newRec.update();
                
                for (PhysicalState oldState : oldRec.fPhysicalStates) {
                    if (oldState == null) {
                        continue;
                    }
                    
                    PhysStateRec newPhys = Database.getPhysicalState(oldRec.Formula, oldState.State, true);

                    newPhys.setDensity(oldState.Density);
                    newPhys.setColor(oldState.Color);
                    newPhys.setHeatFormation(oldState.HeatFormation);
                    newPhys.setGibbsFreeEnergy(oldState.GibbsFreeEnergy);
                    newPhys.setStdEntropy(oldState.StdEntropy);
                    newPhys.setMolarHeatCapacity(oldState.MolarHeatCapacity);

                    newPhys.update();
                }
            }

            ResultSet rs = Database.execQuery("select count(*) as cnt from compounds");
            rs.next();
            System.out.println("records 2: " + rs.getString("cnt"));
        } catch (SQLException ex) {
            System.out.println("error2");
        }*/
        
        // to future
        // CompoundRecord compRec = CLData.CompoundsBook.checkCompound(formula);
        
        /*ExtData therm = new ExtData();
        for (ExtData.Elem tval : therm.data) {
            String formula = tval.name;
            SubstanceState state;
            
            if (formula.endsWith("(s)")) {
                formula = formula.replace("(s)", "");
                state = SubstanceState.Solid;
            } else if (formula.endsWith("(g)")) {
                formula = formula.replace("(g)", "");
                state = SubstanceState.Gas;
            } else if (formula.endsWith("(l)")) {
                formula = formula.replace("(l)", "");
                state = SubstanceState.Liquid;
            } else {
                continue;
            }
            
            CompoundRecord record = CLData.CompoundsBook.checkCompound(formula);
            PhysicalState physState = record.getPhysicalState(state, true);
            physState.HeatFormation = tval.heatFormation;
            physState.GibbsFreeEnergy = tval.gibbsFreeEnergy;
            physState.StdEntropy = tval.stdEntropy;
        }*/
    }
}
