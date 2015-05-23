package chemlab.core.controls;

import bslib.common.Bitmap;
import bslib.common.ImageHelper;
import chemlab.core.chemical.CrystalKind;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public final class CrystalViewer extends JPanel
{
    private CrystalKind fCrystal;
    private final Bitmap[] fImages;

    public CrystalViewer()
    {
        super();
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setSize(40, 40);

        this.fImages = new Bitmap[8];
        this.fCrystal = CrystalKind.Cubic;

        for (CrystalKind i : CrystalKind.values()) {
            String name = "cs_" + i.toString() + ".bmp";
            Bitmap bmp = (Bitmap)ImageHelper.loadBitmap(name.toLowerCase(), true);
            this.fImages[i.getValue()] = bmp;
        }
    }

    public final void setCrystal(CrystalKind value)
    {
        if (this.fCrystal != value) {
            this.fCrystal = value;
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Bitmap image = this.fImages[this.fCrystal.getValue()];
        int dx = (this.getWidth() - image.getWidth()) / 2;
        int dy = (this.getHeight() - image.getHeight()) / 2;

        g.drawImage(image, dx, dy, null);
    }
}
