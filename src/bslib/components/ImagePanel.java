package bslib.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
    private Image fBackgroundImage;

    public ImagePanel()
    {

    }

    public Image getImage()
    {
        return fBackgroundImage;
    }

    public void setImage(Image image)
    {
        this.fBackgroundImage = image;
        Dimension size = new Dimension(this.getWidth(), this.getHeight());
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        this.setSize(size);
        this.setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if (this.fBackgroundImage != null) {
            g.drawImage(fBackgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }
}
