/*
 *  "BSLib", Brainstorm Library.
 *  Copyright (C) 2017 by Sergey V. Zhdanovskih.
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
package bslib.components;

import bslib.common.AuxUtils;
import bslib.common.Logger;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public class JLinkLabel extends JLabel
{
    public JLinkLabel()
    {
        try {
            //setText("<HTML>Click the <FONT color=\"#000099\"><U>link</U></FONT> to go to the Java website.</HTML>");
            setHorizontalAlignment(SwingConstants.LEFT);
            setOpaque(false);
            setBackground(Color.WHITE);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() == 1) {
                        try {
                            String strURI = getText();
                            AuxUtils.browseURI(new URI(strURI));
                        } catch (URISyntaxException ex) {
                            Logger.write("JLinkLabel.actionPerformed()" + ex.getMessage());
                        }
                    }
                }
            });
        } catch (Exception ex) {
            Logger.write("JLinkLabel.cctor()" + ex.getMessage());
        }
    }
}
