package chemlab.core.controls.experiment;

import bslib.common.AuxUtils;
import bslib.common.BaseObject;
import bslib.common.Bitmap;
import bslib.common.ImageHelper;
import chemlab.core.chemical.CLData;
import chemlab.core.chemical.ReactionSolver;
import chemlab.core.chemical.Substance;
import java.awt.Color;
import java.awt.Graphics;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class LabDevice extends BaseObject
{
    private final ExperimentMaster fExpMaster;
    private final ReactionSolver fReactionMaster;
    private DeviceRecord fRecord;

    private boolean fActive;
    private boolean fFocused;
    private DeviceId FID = DeviceId.values()[0];

    private Bitmap fContentsImage;
    private final Bitmap[] fDevImages = new Bitmap[5];
    private Bitmap fInternalImage;

    private int fFrames;
    private int fFrameIndex;

    private final ArrayList<Substance> fSubstances;
    private int fAbstVolume;
    private int fFillVolume;
    private int fLiquidLevel;

    private int fHeight;
    private int fLeft;
    private int fWidth;
    private int fTop;

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

    public LabDevice(ExperimentMaster owner, int x, int y, DeviceId deviceId)
    {
        this.fExpMaster = owner;
        this.fSubstances = new ArrayList<>();
        this.fReactionMaster = new ReactionSolver();

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
        return this.FID;
    }

    public final void setID(DeviceId value)
    {
        this.FID = value;
        this.fRecord = CLData.Devices.get(this.FID.getValue());
        this.fActive = false;
        this.fFrames = this.fRecord.Frames;

        String dev = this.FID.toString().toUpperCase();
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
        return this.fRecord.Substances_Container;
    }

    public final boolean isActivable()
    {
        return (this.FID == DeviceId.dev_Bunsen_Burner);
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

    public final void tickTime()
    {
        if (this.fActive) {
            if (this.FID == DeviceId.dev_Bunsen_Burner) {
                if (this.fFrameIndex < 4) {
                    this.fFrameIndex++;
                } else {
                    this.fFrameIndex = 1;
                }
            }
        }
    }

    public final int getFillVolume()
    {
        return this.fFillVolume;
    }

    public final short getRealVolume()
    {
        return this.fRecord.RealVolume;
    }

    public final float getTemperature()
    {
        return 0f;
    }

    public final void setTemperature(float value)
    {
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
        if (this.fRecord.Substances_Container) {
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

            switch (subst.State) {
                case Solid:
                    subVol = substVol;
                    solidVolume = (int) (solidVolume + ((long) Math.round(subVol)));
                    solColor = (i == 0) ? subst.Color : AuxUtils.blend(solColor, subst.Color, 0.5);
                    break;

                case Liquid:
                    subVol = substVol;
                    liqColor = (i == 0) ? subst.Color : AuxUtils.blend(liqColor, subst.Color, 0.5);
                    break;

                case Gas:
                    subVol = (substVol * 1000.0);
                    break;

                case Ion:
                    subVol = substVol;
                    liqColor = (i == 0) ? subst.Color : AuxUtils.blend(liqColor, subst.Color, 0.5);
                    break;
            }

            this.fFillVolume = (int) (this.fFillVolume + ((long) Math.round(subVol)));
        }

        short realVolume = this.getRealVolume();
        int comVolume = (int) (Math.round((this.fAbstVolume * ((double) this.fFillVolume / realVolume))));
        int solVolume = (int) (Math.round((this.fAbstVolume * ((double) solidVolume / realVolume))));

        this.fContentsImage = new Bitmap(this.fWidth, this.fHeight);
        Graphics cntCanvas = this.fContentsImage.getGraphics();
        cntCanvas.setColor(Color.black);
        cntCanvas.fillRect(0, 0, this.fWidth, this.fHeight);

        int currV = 0;
        this.fLiquidLevel = -1;

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

        this.fContentsImage = Bitmap.makeTransparent(this.fContentsImage, Color.BLACK);

        this.fExpMaster.repaint();
    }

    public final void paint(Graphics deskCanvas)
    {
        if (this.fRecord.Substances_Container && this.getSubstancesMass() > 0.0f) {
            deskCanvas.drawImage(this.fContentsImage, this.getLeft(), this.getTop(), null);
        }

        deskCanvas.drawImage(this.fDevImages[this.fFrameIndex], this.getLeft(), this.getTop(), null);

        if (this.fFocused) {
            //Canvas.DrawFocusRect(Types.Rect(this.Left, this.Top, this.Left + this.Width, this.Top + this.Height));
        }
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
}
