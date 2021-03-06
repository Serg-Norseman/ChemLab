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
package chemlab.core.controls.experiment;

import bslib.common.BaseObject;
import bslib.common.Bitmap;
import bslib.common.ColorUtil;
import bslib.common.Rect;
import bslib.math.DoubleHelper;
import chemlab.core.CLUtils;
import chemlab.core.chemical.ChemConsts;
import chemlab.core.chemical.ReactionSolver;
import chemlab.core.chemical.Substance;
import chemlab.core.chemical.SubstanceState;
import chemlab.core.controls.experiment.devices.Beaker;
import chemlab.core.controls.experiment.devices.BunsenBurner;
import chemlab.core.controls.experiment.effects.BoilingEffect;
import chemlab.core.controls.experiment.effects.IDeviceEffect;
import chemlab.core.controls.experiment.effects.VaporEffect;
import chemlab.core.controls.experiment.matter.Liquid;
import chemlab.core.controls.experiment.matter.Matter;
import chemlab.core.controls.experiment.matter.Solid;
import chemlab.core.controls.experiment.matter.Steam;
import chemlab.core.measure.ChemUnits;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.measure.Measure;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.5.0
 */
public class LabDevice extends BaseObject
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private static final int FRAME_CHANGE_TICKS = 5;
    private static final int DEV_CAPACITY_COLOR = -1;

    private final ReactionSolver fReactionSolver;

    protected final ExperimentBench fBench;
    protected final ArrayList<Matter> fSubstances;

    protected boolean fActive;
    protected boolean fDecant;
    protected long fPrevTime;

    private DeviceId fID;

    private Bitmap fContentsImage;
    private final Bitmap[] fDevImages = new Bitmap[5];
    private Bitmap fInternalImage;

    private int fFrames;
    private int fFrameIndex;
    private int fUpdateTicks;
    
    private int fAbstVolume;
    private int fFillVolume;
    private int fLiquidLevel;
    private Color fLiquidColor;
    private DevBottom fBottom;

    private int fHeight;
    private int fLeft;
    private int fWidth;
    private int fTop;
        
    private final ArrayList<IDeviceEffect> fEffects;
    private boolean fBoiling;
    private boolean fVapor;
    private long fSteamLastTime;
    
    public final int getLeft()
    {
        return this.fLeft;
    }

    public final void setLeft(int value)
    {
        this.fLeft = value;
    }

    public final int getTop()
    {
        return this.fTop;
    }

    public final void setTop(int value)
    {
        this.fTop = value;
    }

    public final int getHeight()
    {
        return this.fHeight;
    }

    public final int getWidth()
    {
        return this.fWidth;
    }

    public final int getBottom()
    {
        return this.fTop + this.fHeight - 1;
    }

    public final int getRight()
    {
        return this.fLeft + this.fWidth - 1;
    }

    public final Rect getRect()
    {
        return new Rect(fLeft, fTop, this.fLeft + this.fWidth - 1, this.fTop + this.fHeight - 1);
    }
    
    protected LabDevice(ExperimentBench owner, int x, int y, DeviceId deviceId)
    {
        this.fBench = owner;

        this.fSubstances = new ArrayList<>();
        
        this.fReactionSolver = new ReactionSolver();

        this.fEffects = new ArrayList<>();
        
        this.fActive = false;
        this.fDecant = true;
        this.fFrameIndex = 0;
        this.setLeft(x);
        this.setTop(y);
        this.setID(deviceId);
    }

    /*@Override
    protected void dispose(boolean disposing)
    {
        if (disposing) {
            this.fContentsImage.Dispose();
            if (this.fInternalImage != null) {
                this.fInternalImage.Dispose();
            }

            for (int i = 0; i < this.fFrames; i++) {
                if (this.fDevImages[i] != null) {
                    this.fDevImages[i].Dispose();
                }
            }

            this.fReactionMaster.dispose();
            this.fSubstances = null;
        }
        super.dispose(disposing);
    }*/

    public final DeviceId getID()
    {
        return this.fID;
    }

    public final void setID(DeviceId value)
    {
        this.fID = value;
        this.fActive = false;
        this.fFrames = this.fID.Frames;

        String dev = this.fID.toString().toUpperCase();
        String Int = "INT_" + dev.substring(0, 0) + dev.substring(0 + 4);

        if (this.fFrames > 1) {
            for (int i = 0; i < this.fFrames; i++) {
                this.fDevImages[i] = loadImage(dev + "_" + String.valueOf(i), ColorUtil.BGRToRGB(0));
            }
        } else {
            this.fDevImages[0] = loadImage(dev, ColorUtil.BGRToRGB(0));

            this.fInternalImage = loadImage(Int, false);
        }

        this.fHeight = this.fDevImages[0].getHeight();
        this.fWidth = this.fDevImages[0].getWidth();

        this.fContentsImage = new Bitmap(this.fWidth, this.fHeight);
        this.fAbstVolume = this.getAbstractVolume();

        this.fBench.repaint();
    }

    private static Bitmap loadImage(String resName, boolean transparent)
    {
        return CLUtils.loadBitmap("devices/" + resName + ".bmp", transparent);
    }

    private static Bitmap loadImage(String resName, Color transparentColor)
    {
        return CLUtils.loadBitmap("devices/" + resName + ".bmp", transparentColor);
    }
    
    public final boolean isContainer()
    {
        return (this.fID.Type == DeviceType.Container);
    }

    public boolean isActivable()
    {
        return false;
    }

    public final boolean getActive()
    {
        return this.fActive;
    }

    public final void setActive(boolean value)
    {
        this.fActive = value;
        this.fFrameIndex = 0;
        this.fBench.repaint();
        
        if (value) {
            this.activate();
        } else {
            this.deactivate();
        }
    }

    protected void activate()
    {
        
    }

    protected void deactivate()
    {
        
    }

    public final void tickTime(long time)
    {
        this.updateFrame();
        this.updateState(time);

        for (IDeviceEffect effect : this.fEffects) {
            effect.doStep();
        }

        this.fPrevTime = time;
    }

    private void updateFrame()
    {
        if (this.fActive) {
            fUpdateTicks++;

            if (fUpdateTicks == FRAME_CHANGE_TICKS) {
                if (this.fID == DeviceId.dev_Bunsen_Burner) {
                    if (this.fFrameIndex < 4) {
                        this.fFrameIndex++;
                    } else {
                        this.fFrameIndex = 1;
                    }
                }

                fUpdateTicks = 0;
            }
        }
    }

    protected void updateState(long time)
    {
        // update internal state
        if (this.isContainer()) {
            boolean boil = this.isBoiling();
            this.setBoiling(boil);

            boolean vapor = this.isVapor();
            this.setVapor(vapor);

            for (int i = this.fSubstances.size() - 1; i >= 0; i--) {
                Matter subst = this.fSubstances.get(i);
                if (DoubleHelper.equals(subst.getMass(), 0.0D, 0.00001)) {
                    this.fSubstances.remove(i);
                }
            }

            this.changeContents();
        }

        // update external influence
        switch (this.fID.Type) {
            case Container:
                break;

            case Heater:
                break;

            case Meter:
                break;

            case Connector:
                break;

            case Condenser:
                break;
        }
    }

    public final boolean isBoiling()
    {
        boolean result = false;

        for (Matter subst : this.fSubstances) {
            if (subst.getState() == SubstanceState.Liquid) {
                Liquid liquid = (Liquid)subst;

                if (liquid.getTemperature() >= Liquid.getBoilingTemperature(ChemConsts.STD_ATMOSPHERIC_PRESSURE)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public final boolean isVapor()
    {
        long dTime = this.fPrevTime - this.fSteamLastTime;

        return (dTime < 5000);
    }

    public final int getFillVolume()
    {
        return this.fFillVolume;
    }

    public final short getRealVolume()
    {
        return this.fID.RealVolume;
    }

    public final DeviceType getType()
    {
        return this.fID.Type;
    }

    // Rubbish!
    public final Measure<Double, Temperature> getTemperature()
    {
        /*if (this.fID.Type == DeviceType.Heater && this.fActive) {
            if (this.fID == DeviceId.dev_Bunsen_Burner) {
                // Methane, t=2043 °С
                return 2043.0f;
            } else if (this.fID == DeviceId.dev_Heater) {
                // controllable temperature
            }
        }*/
        
        double result = 0.0d;
        
        if (this.isContainer()) {
            double temp = 0;
            for (Matter subst : this.fSubstances) {
                temp += subst.getTemperature();
            }
            result = temp / this.fSubstances.size();
        }
        
        return Measure.valueOf(result, ChemUnits.KELVIN);
    }

    public final void setTemperature(float value)
    {
    }

    /**
     * Returns the temperature that this might add/substract from the chemical
     * inside it (for Condensers).
     */
    public float getTemperatureDifference()
    {
        return 0.0f;
    }

    public final Measure<Double, Pressure> getPressure()
    {
        return Measure.valueOf(101.325D, ChemUnits.PASCAL);
    }

    public final void setPressure(float value)
    {
    }

    public final float getPH()
    {
        return 0f;
    }

    public final Matter getSubstance(int index)
    {
        Matter result = null;
        if (index >= 0 && index < this.fSubstances.size()) {
            result = this.fSubstances.get(index);
        }
        return result;
    }

    public final int getSubstancesCount()
    {
        return this.fSubstances.size();
    }

    public final Measure<Double, Mass> getSubstancesMass()
    {
        double result = 0.0;
        for (Matter subst : this.fSubstances) {
            result = (result + subst.getMass());
        }

        return Measure.valueOf(result, ChemUnits.GRAM);
    }

    private int getAbstractVolume()
    {
        int result = 0;
        if (this.isContainer()) {
            for (int y = this.fHeight - 1; y >= 0; y--) {
                int d = 0;
                for (int x = 0; x < this.fWidth; x++) {
                    if (this.fInternalImage.getRGB(x, y) == DEV_CAPACITY_COLOR) {
                        d++;
                    }
                }

                if (d != 0) {
                    float r = d / 2.0f;
                    int sq = (int) (Math.round((Math.PI * (double) (r * r))));
                    result += sq;
                }
            }
        }
        return result;
    }

    public boolean isValidPoint(int x, int y)
    {
        return (this.fInternalImage.getRGB(x, y) == DEV_CAPACITY_COLOR);
    }

    private void drawContentLine(Bitmap contentsImage, int y, Color color)
    {
        int lineColor = color.getRGB();
        for (int x = 0; x < this.fWidth; x++) {
            if (this.fInternalImage.getRGB(x, y) == DEV_CAPACITY_COLOR) {
                contentsImage.setRGB(x, y, lineColor);
            }
        }
    }

    public final void changeContents()
    {
        this.fFillVolume = 0;

        int solidVolume = 0;
        double subVol = 0.0;

        Color solColor = Color.gray;
        Color liqColor = Color.cyan;

        for (int i = 0; i < this.fSubstances.size(); i++) {
            Matter subst = this.fSubstances.get(i);
            SubstanceState state = subst.getState();
            
            double substVol = (subst.getMass() / subst.getDensity(state));
            Color substColor = subst.getColor(state);

            switch (state) {
                case Solid:
                    subVol = substVol;
                    solidVolume = (int) (solidVolume + ((long) Math.round(subVol)));
                    solColor = (i == 0) ? substColor : ColorUtil.blend(solColor, substColor, 0.5);
                    break;

                case Liquid:
                    subVol = substVol;
                    liqColor = (i == 0) ? substColor : ColorUtil.blend(liqColor, substColor, 0.5);
                    break;

                case Gas:
                    subVol = (substVol * 1000.0);
                    break;

                case Ion:
                    subVol = substVol;
                    liqColor = (i == 0) ? substColor : ColorUtil.blend(liqColor, substColor, 0.5);
                    break;
            }

            this.fFillVolume = (int) (this.fFillVolume + ((long) Math.round(subVol)));
        }
        
        this.fLiquidColor = liqColor;

        short realVolume = this.getRealVolume();
        int comVolume = (int) (Math.round((this.fAbstVolume * ((double) this.fFillVolume / realVolume))));
        int solVolume = (int) (Math.round((this.fAbstVolume * ((double) solidVolume / realVolume))));

        this.fContentsImage = new Bitmap(this.fWidth, this.fHeight);
        Graphics cntCanvas = this.fContentsImage.getGraphics();
        cntCanvas.setColor(Color.black);
        cntCanvas.fillRect(0, 0, this.fWidth, this.fHeight);

        int currV = 0;
        this.fLiquidLevel = -1;
        
        DevBottom db = new DevBottom();
        db.X1 = -1;
        db.X2 = -1;
        db.Y = -1;

        for (int y = this.fHeight - 1; y >= 0; y--) {
            int d = 0;
            for (int x = 0; x < this.fWidth; x++) {
                if (this.fInternalImage.getRGB(x, y) == DEV_CAPACITY_COLOR) {
                    d++;

                    if (db.Y == -1) {
                        if (db.X1 == -1) db.X1 = x;
                        db.X2 = x;
                    }
                }
            }

            if (db.Y == -1 && d > 0) db.Y = y;
            
            if (d != 0) {
                float r = d / 2.0f;
                currV = (int) (currV + (Math.round((Math.PI * (r * r)))));

                if (currV <= solVolume) {
                    drawContentLine(this.fContentsImage, y, solColor);
                } else if (currV <= comVolume) {
                    drawContentLine(this.fContentsImage, y, liqColor);
                } else if (this.fLiquidLevel == -1) {
                    this.fLiquidLevel = y;
                }
            }
        }

        this.fBottom = db;
        
        this.fContentsImage = Bitmap.makeTransparent(this.fContentsImage, Color.BLACK);

        this.fBench.repaint();
    }

    public final void paint(Graphics2D deskCanvas)
    {
        int devX = this.getLeft();
        int devY = this.getTop();

        deskCanvas.translate(devX, devY);

        if (this.isContainer() && this.getSubstancesMass().getValue() > 0.0f) {
            deskCanvas.drawImage(this.fContentsImage, 0, 0, null);

            for (IDeviceEffect effect : this.fEffects) {
                effect.draw(deskCanvas);
            }
        }

        deskCanvas.drawImage(this.fDevImages[this.fFrameIndex], 0, 0, null);
        deskCanvas.translate(-devX, -devY);
    }

    public final int getLiquidLevel()
    {
        return this.fLiquidLevel;
    }

    public final Color getLiquidColor()
    {
        return this.fLiquidColor;
    }

    public final DevBottom getDevTop()
    {
        return this.fBottom;
    }

    public final DevBottom getDevBottom()
    {
        return this.fBottom;
    }

    protected final Matter addMatter(Matter newMatter, boolean lost)
    {
        if (newMatter instanceof Steam) {
            this.fSteamLastTime = this.fPrevTime;
        }

        if (!lost) {
            Matter founded = null;

            for (Matter matter : this.fSubstances) {
                if (matter.Formula.equals(newMatter.Formula) && matter.getState() == newMatter.getState()) {
                    founded = matter;
                    break;
                }
            }

            if (founded != null) {
                founded.add(newMatter);

                return founded;
            } else {
                this.fSubstances.add(newMatter);

                return newMatter;
            }
        } else {
            return newMatter;
        }
    }

    public final Substance addSubstance(String formula, SubstanceState state, Measure<Double, ?> amount)
    {
        Matter result = Matter.createMatter(formula, state, amount);
        return this.addMatter(result, false);
    }

    public final Substance addSubstance(String formula, SubstanceState state, double mass)
    {
        Matter result = Matter.createMatter(formula, state, mass);
        return this.addMatter(result, false);
    }

    public final void deleteSubstance(int index)
    {
        this.fSubstances.remove(index);

        this.changeContents();
    }
    
    public final void clear()
    {
        for (Substance substance : this.fSubstances) {
            substance.dispose();
        }
        this.fSubstances.clear();

        this.changeContents();
    }

    public final boolean inArea(int x, int y)
    {
        return (x >= this.getLeft() && x < this.getLeft() + this.fWidth && y >= this.getTop() && y < this.getTop() + this.fHeight);
    }

    public final void read(InputStream stream)
    {
    }

    public final void write(OutputStream stream)
    {
    }

    public final boolean getBoiling()
    {
        return this.fBoiling;
    }

    public final void setBoiling(boolean value)
    {
        if (this.fBoiling == value) {
            return;
        }

        this.fBoiling = value;

        if (value) {
            IDeviceEffect effect = new BoilingEffect();
            effect.init(this);
            this.fEffects.add(effect);
        } else {
            for (IDeviceEffect effect : this.fEffects) {
                if (effect instanceof BoilingEffect) {
                    this.fEffects.remove(effect);
                    break;
                }
            }
        }
    }

    public final boolean getVapor()
    {
        return this.fBoiling;
    }

    public final void setVapor(boolean value)
    {
        if (this.fVapor == value) {
            return;
        }

        this.fVapor = value;

        if (value) {
            IDeviceEffect effect = new VaporEffect();
            effect.init(this);
            this.fEffects.add(effect);
        } else {
            for (IDeviceEffect effect : this.fEffects) {
                if (effect instanceof VaporEffect) {
                    this.fEffects.remove(effect);
                    break;
                }
            }
        }
    }

    public final boolean canCling(LabDevice target)
    {
        boolean result = false;
        
        return result;
    }

    public static LabDevice createDevice(ExperimentBench owner, int x, int y, DeviceId deviceId)
    {
        LabDevice device = null;
        
        switch (deviceId) {
            case dev_Bunsen_Burner:
                device = new BunsenBurner(owner, x, y, deviceId);
                break;

            case dev_Beaker_100:
            case dev_Beaker_250:
            case dev_Beaker_600:
                device = new Beaker(owner, x, y, deviceId);
                break;

            case dev_Conical_Flask_100:
            case dev_Conical_Flask_250:
            case dev_Roundbottom_Flask_100:
            case dev_TestTube_50:
            case dev_Buret_10:
            case dev_Buret_50:
            case dev_Electronic_Balance_250:
            case dev_Graduated_Cylinder_10:
            case dev_Graduated_Cylinder_100:
            case dev_Heater:
                device = new LabDevice(owner, x, y, deviceId);
                break;

            default:
                throw new AssertionError(deviceId.name());
        }
        
        return device;
    }
    
    public final boolean isClosedSystem()
    {
        return false;
    }
    
    /**
     *
     * @param energy in J
     */
    public void addHeatEnergy(double energy)
    {
        // Rubbish!
        if (this.isContainer()) {
            for (Matter matter : this.fSubstances) {
                if (matter instanceof Liquid) {
                    Steam steam = ((Liquid) matter).addEnergy(energy, ChemConsts.STD_ATMOSPHERIC_PRESSURE);

                    if (this.isClosedSystem()) {
                        this.addMatter(steam, false);
                    } else {
                        // lost
                        this.addMatter(steam, true);
                    }
                } else {
                    if (matter instanceof Solid) {
                        matter.addEnergy(energy);
                    } else {
                        // not steam
                    }
                }
            }
        }
    }
    
    public static final DeviceId findDevice(String name)
    {
        for (DeviceId dev : DeviceId.values()) {
            String devName = dev.Name;
            if (dev.Type == DeviceType.Container && dev.RealVolume > 0) {
                devName += " (" + String.valueOf(dev.RealVolume) + " ml)";
            }
            
            if (devName.equals(name)) {
                return dev;
            }
        }
        
        return null;
    }
    
    public final List<LabDevice> findConnections()
    {
        return this.fBench.findConnections(this);
    }
    
    public static final void loadNames()
    {
        DeviceId.dev_Beaker_100.Name = res_i18n.getString("CL_Dev_Beaker");
        DeviceId.dev_Beaker_250.Name = res_i18n.getString("CL_Dev_Beaker");
        DeviceId.dev_Beaker_600.Name = res_i18n.getString("CL_Dev_Beaker");
        DeviceId.dev_Conical_Flask_100.Name = res_i18n.getString("CL_Dev_ConicalFlask");
        DeviceId.dev_Conical_Flask_250.Name = res_i18n.getString("CL_Dev_ConicalFlask");
        DeviceId.dev_Roundbottom_Flask_100.Name = res_i18n.getString("CL_Dev_RoundbottomFlask");
        DeviceId.dev_TestTube_50.Name = res_i18n.getString("CL_Dev_TestTube");
        DeviceId.dev_Bunsen_Burner.Name = res_i18n.getString("CL_Dev_BunsenBurner");
        DeviceId.dev_Buret_10.Name = res_i18n.getString("CL_Dev_Buret");
        DeviceId.dev_Buret_50.Name = res_i18n.getString("CL_Dev_Buret");
        DeviceId.dev_Electronic_Balance_250.Name = res_i18n.getString("CL_Dev_ElectronicBalance");
        DeviceId.dev_Graduated_Cylinder_10.Name = res_i18n.getString("CL_Dev_GraduatedCylinder");
        DeviceId.dev_Graduated_Cylinder_100.Name = res_i18n.getString("CL_Dev_GraduatedCylinder");
        DeviceId.dev_Heater.Name = res_i18n.getString("CL_Dev_Heater");
    }
}
