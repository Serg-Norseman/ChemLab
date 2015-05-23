package chemlab.core.controls.decay;

import bslib.common.AuxUtils;
import bslib.common.Bitmap;
import bslib.common.ImageHelper;
import chemlab.core.chemical.DecayMode;
import chemlab.core.chemical.CLData;
import chemlab.forms.CommonUtils;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public final class DecayShape extends JComponent
{
    public enum ShapeType
    {
        dskDown,
        dskRite,
        dskDecayMode,
        dskNuclide;
    }

    private Bitmap fImage;
    private ShapeType fType = ShapeType.dskDown;

    public ShapeId Id = ShapeId.DownHead;
    public DecayMode Mode = DecayMode.dmStable;
    public NuclideNode NuclideNode;

    public DecayShape(JComponent parent, int left, int top)
    {
        super();
        this.setOpaque(false);
        this.fImage = new Bitmap(32, 32);
        this.setLocation(left, top);
        this.setSize(32, 32);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
    }

    /*@Override
     protected void Dispose(boolean disposing)
     {
     if (disposing) {
     this.fImage.Dispose();
     }
     super.Dispose(disposing);
     }*/
    public ShapeType getType()
    {
        return this.fType;
    }

    public void setType(ShapeType value)
    {
        this.fType = value;

        String resName = "";
        switch (this.fType) {
            case dskDown:
                resName = this.Id.toString();
                break;

            case dskRite:
                resName = this.Id.toString();
                break;

            case dskDecayMode:
                resName = this.Mode.toString();
                StringBuilder sb = new StringBuilder(resName);
                sb.insert(2, "_");
                resName = sb.toString();
                break;

            case dskNuclide: {
                String sValue = this.NuclideNode.Nuclide;

                int i = 0;
                StringBuilder sbv = new StringBuilder(sValue);
                while (i < sbv.length()) {
                    if (CLData.Numbers.indexOf(sbv.charAt(i)) < 0) {
                        sbv.deleteCharAt(i);
                    } else {
                        i++;
                    }
                }
                sValue = sbv.toString();
                
                int iValue = (int) (AuxUtils.ParseInt(sValue, 0) / 4.0f + 2.75f);

                for (NuclideId nid : NuclideId.values()) {
                    sValue = nid.toString().substring(3);
                    int iNID = AuxUtils.ParseInt(sValue, 0);

                    if (iValue == iNID || (iNID >= iValue - 2 && iNID <= iValue + 2)) {
                        break;
                    }
                }

                resName = "N" + sValue;
            }
            break;
        }

        String lpBitmapName = resName.toLowerCase() + ".bmp";
        this.fImage = ImageHelper.loadBitmap(lpBitmapName, true);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(this.fImage, 0, 0, null);

        g.setColor(Color.black);
        switch (this.fType) {
            case dskNuclide: {
                String sValue = this.NuclideNode.Nuclide;
                g.drawString(sValue, 0, 0);
                break;
            }
        }
    }
}
