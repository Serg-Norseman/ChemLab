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
        setOpaque(false);
    }

    public Image getImage()
    {
        return fBackgroundImage;
    }

    public void setImage(Image image)
    {
        if (fBackgroundImage != image) {
            fBackgroundImage = image;

            if (fBackgroundImage != null) {
                Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
                setPreferredSize(size);
                setMinimumSize(size);
                setMaximumSize(size);
                setSize(size);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        if (fBackgroundImage != null) {
            g.drawImage(fBackgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
