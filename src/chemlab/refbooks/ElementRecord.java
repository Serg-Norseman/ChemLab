package chemlab.refbooks;

import chemlab.core.chemical.CrystalKind;
import chemlab.core.chemical.DegreeSet;
import chemlab.core.chemical.ElementABPropertyId;
import chemlab.core.chemical.ElementClass;
import chemlab.core.chemical.ValencySet;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public final class ElementRecord
{
    public ElementClass FClass = ElementClass.OtherNonmetals;
    public int FNumber;
    public int FColor;
    public String FSymbol;
    public String FRus_Name;
    public String FLat_Name;
    public String FEng_Name;
    public String FGer_Name;
    public String FFre_Name;
    public String FSwd_Name;
    public String FNor_Name;
    public String FOriginal_Name;
    public double FAtomic_Mass;
    public double FAtomic_Radius;
    public double FAtomic_Volume;
    public double FCovalent_Radius;
    public double FElectronegativity;
    public DegreeSet FOxidation_Degree;
    public ValencySet FValency;
    public String FStructure;
    public String FDiscovery_Date;
    public String FDiscoverers;
    public String FDiscovery_Particularities;
    public double FTerrestrial_Cortex;
    public String FDescription;
    public CrystalKind FCrystal_Structure = CrystalKind.values()[0];
    public String FSources;
    public String FUses;
    public double FIP_1ST;
    public double FIP_2ND;
    public double FIP_3RD;
    public double FElectron_Affinity;
    public double FHardness;
    public double FCritical_Temperature;
    public double FCritical_Density;
    public double FCritical_Pressure;
    public double FVDW_Radius;
    public double FBoiling_Point;
    public double FMelting_Point;
    public double FDensity;
    public double FVaporization_Heat;
    public double FFusion_Heat;
    public double FElectric_Conductivity;
    public double FThermal_Conductivity;
    public double FSpecific_Heat_Capacity;
    public ElementABPropertyId FABProperty = ElementABPropertyId.eabAmphoteric;
    public String FAbsorption_Spectrum;
    public String FEmission_Spectrum;

    public ElementRecord()
    {
        this.FOxidation_Degree = new DegreeSet();
        this.FValency = new ValencySet();
    }
}
