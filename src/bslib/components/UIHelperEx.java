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

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Sergey V. Zhdanovskih
 */
public class UIHelperEx
{
    @Deprecated
    public static String getComboBoxText(JComboBox comboBox)
    {
        return ((JTextComponent) comboBox.getEditor().getEditorComponent()).getText();
        // String item = box.getEditor().getItem().toString();
        // comboBox.getSelectedItem();
    }

    @Deprecated
    public static void setComboBoxText(JComboBox comboBox, String text)
    {
        ((JTextComponent) comboBox.getEditor().getEditorComponent()).setText(text);
        // box.getEditor().setItem("Text Has Changed");
        // comboBox.setSelectedItem("text has changed");
    }

    public static void setAction(AbstractButton btn, ActionListener listener, String actionCommand)
    {
        btn.addActionListener(listener);
        btn.setActionCommand(actionCommand);
    }

    public static void setAction(JComboBox cmb, ActionListener listener, String actionCommand)
    {
        cmb.addActionListener(listener);
        cmb.setActionCommand(actionCommand);
    }

    public static JComponent createHBox(JComponent... components)
    {
        return createBox(0, components);
    }

    public static JComponent createVBox(JComponent... components)
    {
        return createBox(1, components);
    }

    public static JComponent createBox(int orientation, JComponent... components)
    {
        // hgap, vgap of Flow influences to padding of container
        Box box;
        if (orientation == 0) {
            box = Box.createHorizontalBox();
        } else {
            box = Box.createVerticalBox();
        }

        int num = components.length;
        for (int i = 0; i < num; i++) {
            JComponent comp = components[i];

            if (comp == null) {
                box.add(Box.createHorizontalGlue());
            } else {
                box.add(comp);
            }

            if (i < num - 1) {
                box.add(Box.createHorizontalStrut(10));
            }
        }
        return box;
    }

    public static ButtonGroup createButtonGroup(AbstractButton... buttons)
    {
        ButtonGroup result = new ButtonGroup();
        for (AbstractButton btn : buttons) {
            result.add(btn);
        }
        return result;
    }

    public static FlowLayout createFlowLayout(Container container, int align, Component... components)
    {
        FlowLayout layout = new FlowLayout(align);
        for (Component comp : components) {
            container.add(comp);
        }
        return layout;
    }

    public static BoxLayout createBoxLayout(Container container, int axis, Component... components)
    {
        BoxLayout layout = new BoxLayout(container, axis);
        for (Component comp : components) {
            container.add(comp);
        }
        return layout;
    }

    public static void addMenuItems(JPopupMenu menu, JComponent... items)
    {
        for (JComponent item : items) {
            if (item == null) {
                menu.addSeparator();
            } else {
                menu.add(item);
            }
        }
    }

    public static void addMenuItems(JMenu menu, JComponent... items)
    {
        for (JComponent item : items) {
            menu.add(item);
        }
    }

    public static void addMenuItems(JMenuBar menu, JComponent... items)
    {
        for (JComponent item : items) {
            menu.add(item);
        }
    }

    public static void addToolItems(JToolBar toolBar, JComponent... items)
    {
        for (JComponent item : items) {
            if (item == null) {
                toolBar.addSeparator();
            } else {
                toolBar.add(item);
            }
        }
    }

    public static void addComponents(JPanel panel, JComponent... comps)
    {
        for (JComponent comp : comps) {
            panel.add(comp);
        }
    }

    public static void setFileChooserFilters(JFileChooser fileDlg, String filters, int selectIndex)
    {
        if (fileDlg == null) {
            throw new IllegalArgumentException("fileDlg");
        }

        FileFilter selectFilter = null;
        String[] filterParts = filters.split("[|]", -1);
        int filtersNum = filterParts.length / 2;
        for (int i = 0; i < filtersNum; i++) {
            int idx = i * 2;
            String name = filterParts[idx];
            String exts = filterParts[idx + 1];
            String[] extensions = exts.split("[,]", -1);

            String[] newExts = new String[extensions.length];
            for (int k = 0; k < extensions.length; k++) {
                String ext = extensions[k];
                if (ext.startsWith("*.")) {
                    ext = ext.substring(2);
                }
                newExts[k] = ext;
            }

            FileFilter fneFilter = new FileNameExtensionFilter(name, newExts);
            fileDlg.addChoosableFileFilter(fneFilter);
            if (selectIndex > 0 && (i == selectIndex - 1)) {
                selectFilter = fneFilter;
            }
        }

        if (selectFilter != null) {
            fileDlg.setFileFilter(selectFilter);
        }
    }
}
