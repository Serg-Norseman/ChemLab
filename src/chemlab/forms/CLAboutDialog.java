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
package chemlab.forms;

import bslib.common.FramesHelper;
import bslib.common.ImageHelper;
import bslib.components.ImagePanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
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
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

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
        this.setTitle(res_i18n.getString("CL_ABOUT"));
        
        this.LabelTitle.setText(res_i18n.getString("CL_APPNAME"));
        this.LabelTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        this.LabelTitle.setLocation(236, 8);
        this.LabelTitle.setSize(322, 31);

        this.LabelVer.setText(res_i18n.getString("CL_VERSION"));
        this.LabelVer.setFont(this.getFont());
        this.LabelVer.setLocation(236, 48);
        this.LabelVer.setSize(116, 20);

        this.Image1.setLocation(8, 8);
        this.Image1.setSize(214, 320);
        this.Image1.setImage(ImageHelper.loadImage("ui/logo.png"));
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

        this.btnClose.setText(res_i18n.getString("CL_Close"));
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
