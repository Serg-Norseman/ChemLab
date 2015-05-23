/*
 *  "ChemLab", Desktop helper application for chemists.
 *  Copyright (C) 1996-2001 by Serg V. Zhdanovskih (aka Alchemist, aka Norseman).
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
package chemlab.core.controls.experiment;

import bslib.common.ImageHelper;
import chemlab.core.chemical.CLData;
import chemlab.forms.CommonUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class DevicesList extends JPanel
{
    private final Map<String, ImageIcon> fImageMap;
    private final JList fList;

    public DevicesList()
    {
        super();

        this.fImageMap = new HashMap<>();
        String[] nameList = new String[DeviceId.values().length];

        int idx = 0;
        for (DeviceId dev : DeviceId.values()) {
            String tempVar = dev.toString();
            String id = "icon" + tempVar.substring(0, 0) + tempVar.substring(0 + 3);
            ImageIcon bmp = ImageHelper.loadIcon("devices/" + id + ".bmp");

            nameList[idx] = CLData.Devices.get(dev.getValue()).Name;
            this.fImageMap.put(nameList[idx], bmp);
            idx++;
        }

        this.fList = new JList(nameList);
        this.fList.setCellRenderer(new ListRenderer());
        this.fList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.fList.setDragEnabled(true);
        this.fList.setTransferHandler(new ListTransferHandler());

        JScrollPane scroll = new JScrollPane(fList);

        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);
    }

    public final int getSelectedIndex()
    {
        return this.fList.getSelectedIndex();
    }

    public class ListRenderer extends DefaultListCellRenderer
    {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setIcon((ImageIcon) fImageMap.get((String) value));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(CommonUtils.DEFAULT_UI_FONT);
            return label;
        }
    }

    public class ListTransferHandler extends TransferHandler
    {
        private int[] indices = null;
        private int addIndex = -1; //Location where items were added
        private int addCount = 0;  //Number of items added.

        @Override
        public boolean canImport(TransferHandler.TransferSupport info)
        {
            // Check for String flavor
            if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }
            return true;
        }

        @Override
        protected Transferable createTransferable(JComponent c)
        {
            return new StringSelection(exportString(c));
        }

        @Override
        public int getSourceActions(JComponent c)
        {
            return TransferHandler.COPY;
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport info)
        {
            if (!info.isDrop()) {
                return false;
            }

            JList list = (JList) info.getComponent();
            DefaultListModel listModel = (DefaultListModel) list.getModel();
            JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
            int index = dl.getIndex();
            boolean insert = dl.isInsert();

            // Get the string that is being dropped.
            Transferable t = info.getTransferable();
            String data;
            try {
                data = (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            // Perform the actual import.  
            if (insert) {
                listModel.add(index, data);
            } else {
                listModel.set(index, data);
            }
            return true;
        }

        @Override
        protected void exportDone(JComponent c, Transferable data, int action)
        {
            cleanup(c, action == TransferHandler.MOVE);
        }

        //Bundle up the selected items in the list
        //as a single string, for export.
        protected String exportString(JComponent c)
        {
            JList list = (JList) c;
            indices = list.getSelectedIndices();
            Object[] values = list.getSelectedValues();

            StringBuffer buff = new StringBuffer();

            for (int i = 0; i < values.length; i++) {
                Object val = values[i];
                buff.append(val == null ? "" : val.toString());
                if (i != values.length - 1) {
                    buff.append("\n");
                }
            }

            return buff.toString();
        }

        //Take the incoming string and wherever there is a
        //newline, break it into a separate item in the list.
        protected void importString(JComponent c, String str)
        {
            JList target = (JList) c;
            DefaultListModel listModel = (DefaultListModel) target.getModel();
            int index = target.getSelectedIndex();

            //Prevent the user from dropping data back on itself.
            //For example, if the user is moving items #4,#5,#6 and #7 and
            //attempts to insert the items after item #5, this would
            //be problematic when removing the original items.
            //So this is not allowed.
            if (indices != null && index >= indices[0] - 1
                    && index <= indices[indices.length - 1]) {
                indices = null;
                return;
            }

            int max = listModel.getSize();
            if (index < 0) {
                index = max;
            } else {
                index++;
                if (index > max) {
                    index = max;
                }
            }
            addIndex = index;
            String[] values = str.split("\n");
            addCount = values.length;
            for (int i = 0; i < values.length; i++) {
                listModel.add(index++, values[i]);
            }
        }

        //If the remove argument is true, the drop has been
        //successful and it's time to remove the selected items 
        //from the list. If the remove argument is false, it
        //was a Copy operation and the original list is left
        //intact.
        protected void cleanup(JComponent c, boolean remove)
        {
            if (remove && indices != null) {
                JList source = (JList) c;
                DefaultListModel model = (DefaultListModel) source.getModel();
                //If we are moving items around in the same list, we
                //need to adjust the indices accordingly, since those
                //after the insertion point have moved.
                if (addCount > 0) {
                    for (int i = 0; i < indices.length; i++) {
                        if (indices[i] > addIndex) {
                            indices[i] += addCount;
                        }
                    }
                }
                for (int i = indices.length - 1; i >= 0; i--) {
                    model.remove(indices[i]);
                }
            }
            indices = null;
            addCount = 0;
            addIndex = -1;
        }
    }
}
