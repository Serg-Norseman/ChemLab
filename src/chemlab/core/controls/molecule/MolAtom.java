package chemlab.core.controls.molecule;

import chemlab.core.chemical.CLData;
import bslib.common.AuxUtils;
import bslib.common.BaseObject;
import bslib.common.Bitmap;
import bslib.common.EnumSet;
import bslib.common.Rect;
import java.awt.Color;
import java.awt.Graphics2D;

public class MolAtom extends BaseObject
{
    private final Molecule fOwner;
    private String fSign;
    protected double FX;
    protected double FY;
    protected double FZ;
    private int FTX;
    private int FTY;
    private int FTZ;
    private boolean fSelected;
    private int fElementNumber;
    private byte _fcharge;
    private float _pcharge;
    private Color FColor;

    public Matrix3D mat;
    public Matrix3D amat;
    public Matrix3D tmat;

    public final String getSign()
    {
        return this.fSign;
    }

    public final void setSign(String value)
    {
        this.fSign = value;
        this.fOwner.getMaster().DoChange();
        this.fOwner.getMaster().Repaint();
    }

    public final int getElementNumber()
    {
        return this.fElementNumber;
    }

    public final void setElementNumber(int value)
    {
        this.fElementNumber = value;
        this.initBalls();
        this.fOwner.getMaster().Repaint();
    }

    public final void initBalls()
    {
        int num = this.fElementNumber;
        this.FColor = AuxUtils.BGRToRGB(CLData.ElementsBook.get(num - -1).FColor);

        BallsBuffer[] ballsBuffers = this.fOwner.getMaster().FAtomsHash;
        int j = 2;
        do {
            BallsBuffer buffer = ballsBuffers[this.fElementNumber - 1];
            if (buffer.Balls[j - 2] == null) {
                Bitmap ball = MoleculeMaster.createSphere(j, this.FColor);
                buffer.Balls[j - 2] = ball;
            }
            j++;
        } while (j != 16);
    }

    public final double getX()
    {
        return this.FX;
    }

    public final void setX(double value)
    {
        this.FX = value;
    }

    public final double getY()
    {
        return this.FY;
    }

    public final void setY(double value)
    {
        this.FY = value;
    }

    public final double getZ()
    {
        return this.FZ;
    }

    public final void setZ(double value)
    {
        this.FZ = value;
    }

    public final int getTX()
    {
        return this.FTX;
    }

    public final void setTX(int value)
    {
        this.FTX = value;
    }

    public final int getTY()
    {
        return this.FTY;
    }

    public final void setTY(int value)
    {
        this.FTY = value;
    }

    public final int getTZ()
    {
        return this.FTZ;
    }

    public final void setTZ(int value)
    {
        this.FTZ = value;
    }

    public final boolean getSelected()
    {
        return this.fSelected;
    }

    public final void setSelected(boolean value)
    {
        if (this.fSelected != value) {
            this.fSelected = value;
            this.fOwner.getMaster().Repaint();
            this.fOwner.getMaster().DoChange();
        }
    }

    public final Molecule getOwner()
    {
        return this.fOwner;
    }

    public final byte getFormalCharge()
    {
        return this._fcharge;
    }

    public final void setFormalCharge(int value)
    {
        this._fcharge = (byte)value;
    }

    public final float getPartialCharge()
    {
        return this._pcharge;
    }

    public final void setPartialCharge(float value)
    {
        this._pcharge = value;
    }

    public final Color getColor()
    {
        return this.FColor;
    }

    public MolAtom(Molecule owner)
    {
        this.fOwner = owner;
        this.mat = new Matrix3D();
        this.amat = new Matrix3D();
        this.tmat = new Matrix3D();
        this.setElementNumber(1);
    }

    @Override
    protected void dispose(boolean disposing)
    {
        if (disposing) {
            this.tmat = null;
            this.amat = null;
            this.mat = null;
        }
        super.dispose(disposing);
    }

    public final void draw(Graphics2D canvas)
    {
        //Pen pen;
        //Brush brush;

        //Canvas.Brush.Color = Color.FromArgb(0);
        if (this.fSelected) {
            //brush = new SolidBrush(AuxUtils.BGRToRGB(16776960));
            canvas.setColor(AuxUtils.BGRToRGB(16776960)); // 2
        } else {
            //brush = new SolidBrush(AuxUtils.BGRToRGB(16777215));
            canvas.setColor(AuxUtils.BGRToRGB(16777215)); // 1
        }

        int sx = (int) (Math.round((double) this.FTX));
        int sy = (int) (Math.round((double) this.FTY));
        int R = (int) (Math.round((double) this.FTZ));
        if (R < 2) {
            R = 2;
        }
        if (R > 15) {
            R = 15;
        }

        if (this.fSelected) {
            Rect rt = new Rect(sx - R - 1, sy - R - 1, sx + R + 1, sy + R + 1);
            canvas.drawOval(rt.Left, rt.Top, rt.getWidth(), rt.getHeight());
        }

        Bitmap ball = this.fOwner.getMaster().FAtomsHash[this.fElementNumber - 1].Balls[R - 2];

        canvas.drawImage(ball, sx - R, sy - R, null);

	//Canvas.Brush.Style = TBrushStyle.bsClear;
        String hint = "";
        EnumSet<ViewOption> opts = this.fOwner.getMaster().getViewOptions();
        if (opts.contains(ViewOption.mvoAtomSign)) {
            hint += this.fSign;
        }

        if (opts.contains(ViewOption.mvoAtomName)) {
            hint = hint + "/" + CLData.ElementsBook.get(this.fElementNumber - -1).FRus_Name;
        }

        if (opts.contains(ViewOption.mvoAtomNumber)) {
            int idx = this.getIndex();
            String str = (new Integer(idx)).toString();
            hint = hint + "/" + str;
        }

        if (opts.contains(ViewOption.mvoAtomMass)) {
            double atomicMass = CLData.ElementsBook.get(this.fElementNumber - -1).FAtomic_Mass;
            String str2 = AuxUtils.FloatToStr(atomicMass);
            hint = hint + "/" + str2;
        }

        canvas.drawString(hint.trim(), sx, sy);
    }

    public final void transform()
    {
        Point3D pt = this.mat.transformPoint(this.FX, this.FY, this.FZ);
        this.FTX = (int) (Math.round(pt.X));
        this.FTY = (int) (Math.round(pt.Y));
        this.FTZ = (int) (Math.round(pt.Z));
    }

    public final int getIndex()
    {
        return this.fOwner.indexOfAtom(this);
    }
}
