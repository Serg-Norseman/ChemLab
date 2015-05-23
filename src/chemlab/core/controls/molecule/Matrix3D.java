package chemlab.core.controls.molecule;

public final class Matrix3D
{
    private double xx;
    private double xy;
    private double xz;
    private double xo;
    private double yx;
    private double yy;
    private double yz;
    private double yo;
    private double zx;
    private double zy;
    private double zz;
    private double zo;

    public Matrix3D()
    {
        this.xx = 1.0;
        this.yy = 1.0;
        this.zz = 1.0;
    }

    public final void init()
    {
        this.xo = 0.0;
        this.xx = 1.0;
        this.xy = 0.0;
        this.xz = 0.0;
        this.yo = 0.0;
        this.yx = 0.0;
        this.yy = 1.0;
        this.yz = 0.0;
        this.zo = 0.0;
        this.zx = 0.0;
        this.zy = 0.0;
        this.zz = 1.0;
    }

    public final void scale(double f)
    {
        this.xx = (this.xx * f);
        this.xy = (this.xy * f);
        this.xz = (this.xz * f);
        this.xo = (this.xo * f);
        this.yx = (this.yx * f);
        this.yy = (this.yy * f);
        this.yz = (this.yz * f);
        this.yo = (this.yo * f);
        this.zx = (this.zx * f);
        this.zy = (this.zy * f);
        this.zz = (this.zz * f);
        this.zo = (this.zo * f);
    }

    public final void translate(double x, double y, double z)
    {
        this.xo = (this.xo + x);
        this.yo = (this.yo + y);
        this.zo = (this.zo + z);
    }

    public final void mult(Matrix3D rhs)
    {
        double lxx = (this.xx * rhs.xx + this.yx * rhs.xy + this.zx * rhs.xz);
        double lxy = (this.xy * rhs.xx + this.yy * rhs.xy + this.zy * rhs.xz);
        double lxz = (this.xz * rhs.xx + this.yz * rhs.xy + this.zz * rhs.xz);
        double lxo = (this.xo * rhs.xx + this.yo * rhs.xy + this.zo * rhs.xz + rhs.xo);
        double lyx = (this.xx * rhs.yx + this.yx * rhs.yy + this.zx * rhs.yz);
        double lyy = (this.xy * rhs.yx + this.yy * rhs.yy + this.zy * rhs.yz);
        double lyz = (this.xz * rhs.yx + this.yz * rhs.yy + this.zz * rhs.yz);
        double lyo = (this.xo * rhs.yx + this.yo * rhs.yy + this.zo * rhs.yz + rhs.yo);
        double lzx = (this.xx * rhs.zx + this.yx * rhs.zy + this.zx * rhs.zz);
        double lzy = (this.xy * rhs.zx + this.yy * rhs.zy + this.zy * rhs.zz);
        double lzz = (this.xz * rhs.zx + this.yz * rhs.zy + this.zz * rhs.zz);
        double lzo = (this.xo * rhs.zx + this.yo * rhs.zy + this.zo * rhs.zz + rhs.zo);
        this.xx = lxx;
        this.xy = lxy;
        this.xz = lxz;
        this.xo = lxo;
        this.yx = lyx;
        this.yy = lyy;
        this.yz = lyz;
        this.yo = lyo;
        this.zx = lzx;
        this.zy = lzy;
        this.zz = lzz;
        this.zo = lzo;
    }

    public final void rotY(double theta)
    {
        theta = (theta * 0.017453292519943295);
        double ct = Math.cos(theta);
        double st = Math.sin(theta);
        double Nxx = (this.xx * ct + this.zx * st);
        double Nxy = (this.xy * ct + this.zy * st);
        double Nxz = (this.xz * ct + this.zz * st);
        double Nxo = (this.xo * ct + this.zo * st);
        double Nzx = (this.zx * ct - this.xx * st);
        double Nzy = (this.zy * ct - this.xy * st);
        double Nzz = (this.zz * ct - this.xz * st);
        double Nzo = (this.zo * ct - this.xo * st);
        this.xo = Nxo;
        this.xx = Nxx;
        this.xy = Nxy;
        this.xz = Nxz;
        this.zo = Nzo;
        this.zx = Nzx;
        this.zy = Nzy;
        this.zz = Nzz;
    }

    public final void rotX(double theta)
    {
        theta = (theta * 0.017453292519943295);
        double ct = Math.cos(theta);
        double st = Math.sin(theta);
        double Nyx = (this.yx * ct + this.zx * st);
        double Nyy = (this.yy * ct + this.zy * st);
        double Nyz = (this.yz * ct + this.zz * st);
        double Nyo = (this.yo * ct + this.zo * st);
        double Nzx = (this.zx * ct - this.yx * st);
        double Nzy = (this.zy * ct - this.yy * st);
        double Nzz = (this.zz * ct - this.yz * st);
        double Nzo = (this.zo * ct - this.yo * st);
        this.yo = Nyo;
        this.yx = Nyx;
        this.yy = Nyy;
        this.yz = Nyz;
        this.zo = Nzo;
        this.zx = Nzx;
        this.zy = Nzy;
        this.zz = Nzz;
    }

    public final void rotZ(double theta)
    {
        theta = (theta * 0.017453292519943295);
        double ct = Math.cos(theta);
        double st = Math.sin(theta);
        double Nyx = (this.yx * ct + this.xx * st);
        double Nyy = (this.yy * ct + this.xy * st);
        double Nyz = (this.yz * ct + this.xz * st);
        double Nyo = (this.yo * ct + this.xo * st);
        double Nxx = (this.xx * ct - this.yx * st);
        double Nxy = (this.xy * ct - this.yy * st);
        double Nxz = (this.xz * ct - this.yz * st);
        double Nxo = (this.xo * ct - this.yo * st);
        this.yo = Nyo;
        this.yx = Nyx;
        this.yy = Nyy;
        this.yz = Nyz;
        this.xo = Nxo;
        this.xx = Nxx;
        this.xy = Nxy;
        this.xz = Nxz;
    }

    public final Point3D transformPoint(double x, double y, double z)
    {
        Point3D pt = new Point3D();
        pt.X = (x * this.xx + y * this.xy + z * this.xz + this.xo);
        pt.Y = (x * this.yx + y * this.yy + z * this.yz + this.yo);
        pt.Z = (x * this.zx + y * this.zy + z * this.zz + this.zo);
        return pt;
    }
}
