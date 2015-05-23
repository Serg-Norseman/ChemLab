package chemlab.forms;

import bslib.common.FramesHelper;
import bslib.common.ImageHelper;
import bslib.components.ImagePanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.1.0
 */
public final class CLAboutDialog extends JDialog
{
    private JLabel LabelTitle;
    private JLabel LabelVer;
    private ImagePanel Image1;
    private JButton btnClose;
    private JLabel LabelCopyright;
    private JLabel LabelMail;

    public CLAboutDialog(Frame owner)
    {
        super(owner, true);
        this.initializeComponents();
    }

    private void initializeComponents()
    {
        this.LabelTitle = new JLabel();
        this.LabelVer = new JLabel();
        this.Image1 = new ImagePanel();
        this.LabelCopyright = new JLabel();
        this.LabelMail = new JLabel();
        this.btnClose = new JButton();

        this.setLayout(null);
        FramesHelper.setClientSize(this, 640, 340);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.setResizable(false);
        this.setTitle("О программе");
        
        this.LabelTitle.setText("Химическая Лаборатория");
        this.LabelTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        this.LabelTitle.setLocation(236, 8);
        this.LabelTitle.setSize(322, 31);

        this.LabelVer.setText("Версия 0.6.0");
        this.LabelVer.setFont(this.getFont());
        this.LabelVer.setLocation(236, 48);
        this.LabelVer.setSize(116, 20);

        this.Image1.setLocation(8, 8);
        this.Image1.setSize(214, 320);
        this.Image1.setImage(ImageHelper.loadImage("CLLogo.png"));
        this.Image1.setBorder(BorderFactory.createEtchedBorder());

        this.LabelCopyright.setText("Copyright © 1995-2001, 2015 by Serg V. Zhdanovskih");
        this.LabelCopyright.setFont(this.getFont());
        this.LabelCopyright.setLocation(236, 80);
        this.LabelCopyright.setSize(380, 20);

        this.LabelMail.setText("mailto:serg.alchemist@gmail.com");
        this.LabelMail.setForeground(Color.blue);
        this.LabelMail.setFont(this.getFont());
        this.LabelMail.setLocation(236, 111);
        this.LabelMail.setSize(240, 20);
        this.LabelMail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.btnClose.setText("Закрыть");
        this.btnClose.setFont(this.getFont());
        this.btnClose.setLocation(540, 305);
        this.btnClose.setSize(90, 25);
        this.btnClose.addActionListener((ActionEvent e) -> {
            this.setVisible(false);
        });

        this.add(this.LabelTitle);
        this.add(this.LabelVer);
        this.add(this.Image1);
        this.add(this.LabelCopyright);
        this.add(this.LabelMail);
        this.add(this.btnClose);
    }
}
