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

import bslib.common.AuxUtils;
import bslib.common.BaseObject;
import bslib.common.Bitmap;
import bslib.common.ImageHelper;
import bslib.common.Rect;
import chemlab.core.chemical.ReactionSolver;
import chemlab.core.chemical.Substance;
import chemlab.core.controls.experiment.effects.BoilingEffect;
import chemlab.core.controls.experiment.effects.IDeviceEffect;
import chemlab.core.controls.experiment.effects.VaporEffect;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.5.0
 */
public class LabDevice extends BaseObject
{
    private static final int FRAME_CHANGE_TICKS = 5;

    private final ExperimentBench fExpMaster;
    private final ReactionSolver fReactionSolver;

    private boolean fActive;
    private boolean fFocused;
    private DeviceId fID;

    private Bitmap fContentsImage;
    private final Bitmap[] fDevImages = new Bitmap[5];
    private Bitmap fInternalImage;

    private int fFrames;
    private int fFrameIndex;
    private int fUpdateTicks;
    private long fPrevTime;

    private final ArrayList<Substance> fSubstances;
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
    
    public LabDevice(ExperimentBench owner, int x, int y, DeviceId deviceId)
    {
        this.fExpMaster = owner;
        this.fSubstances = new ArrayList<>();
        this.fReactionSolver = new ReactionSolver();

        this.fEffects = new ArrayList<>();
        
        this.fActive = false;
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
                this.fDevImages[i] = loadImage(dev + "_" + String.valueOf(i), AuxUtils.BGRToRGB(0));
            }
        } else {
            this.fDevImages[0] = loadImage(dev, AuxUtils.BGRToRGB(0));

            this.fInternalImage = loadImage(Int, false);
        }

        this.fHeight = this.fDevImages[0].getHeight();
        this.fWidth = this.fDevImages[0].getWidth();

        this.fContentsImage = new Bitmap(this.fWidth, this.fHeight);
        this.fAbstVolume = this.getAbstractVolume();

        this.fExpMaster.repaint();
    }

    private static Bitmap loadImage(String resName, boolean transparent)
    {
        return ImageHelper.loadBitmap("devices/" + resName + ".bmp", transparent);
    }

    private static Bitmap loadImage(String resName, Color transparentColor)
    {
        return ImageHelper.loadBitmap("devices/" + resName + ".bmp", transparentColor);
    }
    
    public final boolean isContainer()
    {
        return (this.fID.Type == DeviceType.Container);
    }

    public final boolean isActivable()
    {
        return (this.fID == DeviceId.dev_Bunsen_Burner);
    }

    public final boolean getActive()
    {
        return this.fActive;
    }

    public final void setActive(boolean value)
    {
        this.fActive = value;
        this.fFrameIndex = 0;
        this.fExpMaster.repaint();
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
    
    private void updateState(long time)
    {
        // update internal state
        
        // update external influence
        switch (this.fID.Type) {
            case Container:
                break;

            case Heater:
                float curTemp = this.getTemperature();
                long dt = time - this.fPrevTime;
                break;

            case Meter:
                break;

            case Connector:
                break;

            case Condenser:
                break;
        }
    }
    
    public final int getFillVolume()
    {
        return this.fFillVolume;
    }

    public final short getRealVolume()
    {
        return this.fID.RealVolume;
    }

    public final float getTemperature()
    {
        if (this.fID.Type == DeviceType.Heater && this.fActive) {
            if (this.fID == DeviceId.dev_Bunsen_Burner) {
                // Methane, t=2043 °С
                return 2043.0f;
            } else if (this.fID == DeviceId.dev_Heater) {
                // controllable temperature
            }
        }
        
        return 0f;
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

    public final float getPressure()
    {
        return 101.325f;
    }

    public final void setPressure(float value)
    {
    }

    public final float getPH()
    {
        return 0f;
    }

    public final Substance getSubstance(int index)
    {
        Substance result = null;
        if (index >= 0 && index < this.fSubstances.size()) {
            result = this.fSubstances.get(index);
        }
        return result;
    }

    public final int getSubstancesCount()
    {
        return this.fSubstances.size();
    }

    public final double getSubstancesMass()
    {
        double result = 0.0;
        for (Substance subst : this.fSubstances) {
            result = (result + subst.Mass);
        }
        return result;
    }

    private int getAbstractVolume()
    {
        int result = 0;
        if (this.isContainer()) {
            int check = AuxUtils.BGRToRGB(16777215).getRGB();

            for (int y = this.fHeight - 1; y >= 0; y--) {
                int d = 0;
                for (int x = 0; x < this.fWidth; x++) {
                    if (this.fInternalImage.getRGB(x, y) == check) {
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

    private void drawContentLine(Bitmap internalImage, Bitmap contentsImage, int y, Color color)
    {
        int check = AuxUtils.BGRToRGB(16777215).getRGB();
        for (int x = 0; x < this.fWidth; x++) {
            if (internalImage.getRGB(x, y) == check) {
                contentsImage.setRGB(x, y, color.getRGB());
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
            Substance subst = this.fSubstances.get(i);
            double substVol = (subst.Mass / subst.Density);
            Color substColor;

            switch (subst.State) {
                case Solid:
                    substColor = (subst.Color == null) ? Color.gray : subst.Color;
                    subVol = substVol;
                    solidVolume = (int) (solidVolume + ((long) Math.round(subVol)));
                    solColor = (i == 0) ? substColor : AuxUtils.blend(solColor, substColor, 0.5);
                    break;

                case Liquid:
                    substColor = (subst.Color == null) ? Color.cyan : subst.Color;
                    subVol = substVol;
                    liqColor = (i == 0) ? substColor : AuxUtils.blend(liqColor, substColor, 0.5);
                    break;

                case Gas:
                    subVol = (substVol * 1000.0);
                    break;

                case Ion:
                    substColor = (subst.Color == null) ? Color.cyan : subst.Color;
                    subVol = substVol;
                    liqColor = (i == 0) ? substColor : AuxUtils.blend(liqColor, substColor, 0.5);
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

        int check = AuxUtils.BGRToRGB(16777215).getRGB();
        for (int y = this.fHeight - 1; y >= 0; y--) {
            int d = 0;
            for (int x = 0; x < this.fWidth; x++) {
                if (this.fInternalImage.getRGB(x, y) == check) {
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
                    drawContentLine(this.fInternalImage, this.fContentsImage, y, solColor);
                } else if (currV <= comVolume) {
                    drawContentLine(this.fInternalImage, this.fContentsImage, y, liqColor);
                } else if (this.fLiquidLevel == -1) {
                    this.fLiquidLevel = y;
                }
            }
        }

        this.fBottom = db;
        
        this.fContentsImage = Bitmap.makeTransparent(this.fContentsImage, Color.BLACK);

        this.fExpMaster.repaint();
    }

    public final void paint(Graphics2D deskCanvas)
    {
        Bitmap intImage = new Bitmap(this.fWidth, this.fHeight);
        Graphics2D intCanvas = (Graphics2D) intImage.getGraphics();

        if (this.isContainer() && this.getSubstancesMass() > 0.0f) {
            //deskCanvas.drawImage(this.fContentsImage, this.getLeft(), this.getTop(), null);
            intCanvas.drawImage(this.fContentsImage, 0, 0, null);
        }

        intCanvas.setXORMode(Color.black);
        for (IDeviceEffect effect : this.fEffects) {
            effect.draw(intCanvas);
        }
        intCanvas.setPaintMode();

        deskCanvas.drawImage(intImage, this.getLeft(), this.getTop(), null);
        deskCanvas.drawImage(this.fDevImages[this.fFrameIndex], this.getLeft(), this.getTop(), null);

        if (this.fFocused) {
            //Canvas.DrawFocusRect(Types.Rect(this.Left, this.Top, this.Left + this.Width, this.Top + this.Height));
        }
    }

    public final int getLiquidLevel()
    {
        return this.fLiquidLevel;
    }

    public final Color getLiquidColor()
    {
        return this.fLiquidColor;
    }
    
    public final DevBottom getDevBottom()
    {
        return this.fBottom;
    }
    
    public final Substance addSubstance()
    {
        Substance result = new Substance();
        this.fSubstances.add(result);

        //this.changeContents();
        return result;
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
}
