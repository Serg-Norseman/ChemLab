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
package chemlab.core.controls.experiment;

import chemlab.core.chemical.CLData;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.6.0
 */
public class PanelDropTarget implements DropTargetListener
{
    protected JPanel pane;
    protected DropTarget dropTarget;
    protected boolean acceptableType; // Indicates whether data is acceptable
    protected DataFlavor targetFlavor; // Flavor to use for transfer

    public PanelDropTarget(JPanel pane)
    {
        this.pane = pane;

        // Create the DropTarget and register it with the JPanel.
        this.dropTarget = new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE,
                this, true, null);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde)
    {
        // Get the type of object being transferred and determine
        // whether it is appropriate.
        checkTransferType(dtde);

        // Accept or reject the drag.
        acceptOrRejectDrag(dtde);
    }

    @Override
    public void dragExit(DropTargetEvent dte)
    {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde)
    {
        // Accept or reject the drag
        acceptOrRejectDrag(dtde);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde)
    {
        // Accept or reject the drag
        acceptOrRejectDrag(dtde);
    }

    @Override
    public void drop(DropTargetDropEvent dtde)
    {
        // Check the drop action
        if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
            // Accept the drop and get the transfer data
            dtde.acceptDrop(dtde.getDropAction());
            Transferable transferable = dtde.getTransferable();

            try {
                boolean result = dropComponent(transferable, dtde.getLocation());

                dtde.dropComplete(result);
            } catch (Exception ex) {
                dtde.dropComplete(false);
            }
        } else {
            dtde.rejectDrop();
        }
    }

    protected boolean dropComponent(Transferable transferable, Point location)
            throws IOException, UnsupportedFlavorException
    {
        Object data = transferable.getTransferData(targetFlavor);
        if (data instanceof String) {
            ExperimentMaster master = (ExperimentMaster) this.pane;
            DeviceRecord dev = CLData.findDevice((String) data);
            int idx = CLData.Devices.indexOf(dev);
            DeviceId devId = DeviceId.forValue(idx);

            if (dev != null) {
                master.addDevice((int) location.getX(), (int) location.getY(), devId);
            }

            return true;
        }

        return false;
    }

    protected boolean acceptOrRejectDrag(DropTargetDragEvent dtde)
    {
        int dropAction = dtde.getDropAction();
        int sourceActions = dtde.getSourceActions();
        boolean acceptedDrag = false;

        // Reject if the object being transferred
        // or the operations available are not acceptable.
        if (!acceptableType || (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            dtde.rejectDrag();
        } else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
            // Not offering copy or move - suggest a copy
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
            acceptedDrag = true;
        } else {
            // Offering an acceptable operation: accept
            dtde.acceptDrag(dropAction);
            acceptedDrag = true;
        }

        return acceptedDrag;
    }

    protected void checkTransferType(DropTargetDragEvent dtde)
    {
        // Only accept a flavor that returns a Component
        acceptableType = false;
        DataFlavor[] fl = dtde.getCurrentDataFlavors();
        for (int i = 0; i < fl.length; i++) {
            Class dataClass = fl[i].getRepresentationClass();

            if (dataClass == String.class) {
                // This flavor returns a Component - accept it.
                targetFlavor = fl[i];
                acceptableType = true;
                break;
            }
        }
    }
}
