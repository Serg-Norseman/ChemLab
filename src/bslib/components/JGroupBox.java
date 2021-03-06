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

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public class JGroupBox extends JPanel
{
    private Border fBorder;
    private String fTitle;

    public JGroupBox()
    {
        this("Group Box title");
    }

    public JGroupBox(String title)
    {
        setLayout(new BorderLayout());
        setText(title);
    }

    public final String getText()
    {
        return fTitle;
    }

    public final void setText(String text)
    {
        fTitle = text;
        fBorder = BorderFactory.createTitledBorder(fTitle);
        setBorder(fBorder);
    }
}
