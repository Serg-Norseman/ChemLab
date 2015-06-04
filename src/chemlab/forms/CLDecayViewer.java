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
import chemlab.core.controls.decay.DecayViewer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * @author Serg V. Zhdanovskih
 * @since 0.2.0
 */
public final class CLDecayViewer extends JFrame
{
    private static final ResourceBundle res_i18n = ResourceBundle.getBundle("resources/res_i18n");

    private final DecayViewer fDecayViewer;

    public CLDecayViewer(String nuclide)
    {
        super();
        
        this.setLayout(new BorderLayout());
        FramesHelper.setClientSize(this, 640, 480);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle(String.format(res_i18n.getString("CL_DECAY_TITLE"), nuclide));

        this.fDecayViewer = new DecayViewer();
        this.fDecayViewer.setFont(CommonUtils.DEFAULT_UI_FONT);
        this.fDecayViewer.setForeground(Color.black);
        
        JScrollPane scrollPane = new JScrollPane(this.fDecayViewer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(this.fDecayViewer);
        this.add(scrollPane, BorderLayout.CENTER);
        
        CommonUtils.changeFont(this);
        
        this.fDecayViewer.preview(nuclide);
    }
}
