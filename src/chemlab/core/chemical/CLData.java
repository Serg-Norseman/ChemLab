package chemlab.core.chemical;

import bslib.common.AuxUtils;
import bslib.common.StringHelper;
import chemlab.core.controls.experiment.DeviceRecord;
import chemlab.refbooks.AllotropeRecord;
import chemlab.refbooks.CompoundsBook;
import chemlab.refbooks.DecayBook;
import chemlab.refbooks.ElementsBook;
import chemlab.refbooks.IElementLoader;
import chemlab.refbooks.NuclidesBook;
import chemlab.refbooks.RefBook;
import chemlab.refbooks.UnitRecord;
import chemlab.refbooks.ValueRecord;
import java.text.ParseException;
import java.util.ArrayList;
import org.w3c.dom.Element;

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

    public static AllotropeRecord[] dbAllotropes;

    public static ArrayList<ValueRecord> ValuesTable;
    public static ArrayList<UnitRecord> UnitsTable;
    public static ArrayList<DeviceRecord> Devices;
    
    public static final ElementsBook ElementsBook;
    public static final NuclidesBook NuclidesBook;
    public static final DecayBook DecayBook;
    public static final CompoundsBook CompoundsBook;


    public static DeviceRecord findDevice(String name)
    {
        for (DeviceRecord devRec : Devices) {
            if (StringHelper.equals(name, devRec.Name)) {
                return devRec;
            }
        }
        return null;
    }

    public static int getUnitIDByName(String name)
    {
        for (int i = 0; i < UnitsTable.size(); i++) {
            UnitRecord unitRec = UnitsTable.get(i);
            if (StringHelper.equals(unitRec.Name, name)) {
                return i;
            }
        }
        return -1;
    }

    public static double convertValue(double aValue, int aFromUnit, int aToUnit)
    {
        double inf = UnitsTable.get(aFromUnit).Factor;
        double outf = UnitsTable.get(aToUnit).Factor;
        return (aValue * inf / outf);
    }

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

        BondKinds = new char[]{'S', 'D', 'T', 'C', 'Q'};

        dbAllotropes = new AllotropeRecord[128];

        ValuesTable = new ArrayList<>();
        UnitsTable = new ArrayList<>();
        Devices = new ArrayList<>();

        loadXML_Values("");
        loadXML_Units("");
        loadXML_Devices("");
        
        ElementsBook = new ElementsBook();
        NuclidesBook = new NuclidesBook();
        DecayBook = new DecayBook();
        
        CompoundsBook = new CompoundsBook();
        CompoundsBook.loadXML();
        
        importExternalData();
    }

    public static void loadXML_Values(String fileName)
    {
        RefBook.loadResource("/resources/data/ValuesTable.xml", "values", "value", new IElementLoader<ValueRecord>()
        {
            @Override
            public int load(Element el) throws ParseException
            {
                String vId = el.getAttribute("ID");
                String vSign = el.getAttribute("Sign");
                String vName = el.getAttribute("Name");
                String vUnitId = el.getAttribute("UnitID");
                String vValue = el.getAttribute("Value");

                ValueRecord valRec = new ValueRecord();
                valRec.Id = ValueId.valueOf(vId);
                valRec.Sign = vSign;
                valRec.Name = vName;
                valRec.UnitId = AuxUtils.ParseInt(vUnitId, 0);
                valRec.Value = AuxUtils.ParseFloat(vValue, 0);

                ValuesTable.add(valRec);

                return 0;
            }
        });
    }

    public static void loadXML_Units(String fileName)
    {
        RefBook.loadResource("/resources/data/UnitsTable.xml", "units", "unit", new IElementLoader<UnitRecord>()
        {
            @Override
            public int load(Element el) throws ParseException
            {
                String vId = el.getAttribute("ValueID");
                String vSign = el.getAttribute("Sign");
                String vName = el.getAttribute("Name");
                String vFactor = el.getAttribute("Factor");

                UnitRecord unitRec = new UnitRecord();
                unitRec.ValId = ValueId.valueOf(vId);
                unitRec.Sign = vSign;
                unitRec.Name = vName;
                unitRec.Factor = AuxUtils.ParseFloat(vFactor, 0);

                UnitsTable.add(unitRec);

                return 0;
            }
        });
    }

    public static void loadXML_Devices(String fileName)
    {
        RefBook.loadResource("/resources/data/DevicesTable.xml", "devices", "device", new IElementLoader<DeviceRecord>()
        {
            @Override
            public int load(Element el)
            {
                DeviceRecord decRec = new DeviceRecord();

                decRec.Name = el.getAttribute("Name");

                Devices.add(decRec);

                return 0;
            }
        });
    }
    
    private static void importExternalData()
    {
        // to future
        // CompoundRecord compRec = CLData.CompoundsBook.checkCompound(formula);
    }
}
