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

import bslib.common.Bitmap;
import bslib.common.Logger;
import bslib.common.Point;
import bslib.common.Rect;
import bslib.common.RefObject;
import bslib.common.StringHelper;
import chemlab.core.chemical.SubstanceState;
import chemlab.core.controls.EditorControl;
import chemlab.core.controls.experiment.misc.ClingHelper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

/**
 *
 * @author Serg V. Zhdanovskih
 * @since 0.5.0
 */
public class ExperimentBench extends EditorControl implements ActionListener
{
    private static final boolean DEBUG = true;
    
    private static final int TIMER_DELAY = 50; // milliseconds
    
    private static final BasicStroke FOCUS_STROKE = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{2.0f}, 0.0f);
    
    private LabDevice fCurrentDev;
    private LabDevice fMenuDev;
    
    private int FX;
    private int FY;
    private boolean fActive;
    private ExperimentHeader fHeader;
    private final ArrayList<LabDevice> fDevices;
    private Bitmap fBuffer;
    private Date fBegTime = new Date(0);
    private Date fCurTime = new Date(0);
    private Date fEndTime = new Date(0);
    private String fDebugInfo;
    private final ArrayList<Connection> fConnections;

    private final Timer fTimer;
    private final JPopupMenu fDeviceMenu;
    private final JMenuItem miActivationSwitch;
    private final JMenuItem miClear;
    private final JMenuItem miDelete;
    private final JMenuItem miProperties;

    private BenchListener fBenchListener;

    public ExperimentBench()
    {
        super();
        
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setBackground(Color.white);
        this.fBuffer = null;
        this.fDevices = new ArrayList<>();
        this.fConnections = new ArrayList<>();
        this.setDoubleBuffered(true);

        this.fTimer = new Timer(TIMER_DELAY, (ActionEvent e) -> {
            long time = System.currentTimeMillis();
            tickTime(time);
        });
        this.fTimer.setRepeats(true);

        this.fDeviceMenu = new JPopupMenu(null);

        this.miActivationSwitch = new JMenuItem("Включить");
        this.miActivationSwitch.addActionListener(this);
        this.miActivationSwitch.setActionCommand("mi_DeviceSwitch");

        this.miClear = new JMenuItem("Очистить");
        this.miClear.addActionListener(this);
        this.miClear.setActionCommand("mi_DeviceClear");

        this.miDelete = new JMenuItem("Удалить");
        this.miDelete.addActionListener(this);
        this.miDelete.setActionCommand("mi_DeviceDelete");

        this.miProperties = new JMenuItem("Свойства");
        this.miProperties.addActionListener(this);
        this.miProperties.setActionCommand("mi_DeviceProperties");

        this.fDeviceMenu.add(this.miActivationSwitch);
        this.fDeviceMenu.add(this.miClear);
        this.fDeviceMenu.add(this.miDelete);
        this.fDeviceMenu.add(this.miProperties);

        this.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                onMouseDrag(e);
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                onMouseMove(e);
            }
        });
        
        this.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                onMouseDown(e);
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                onMouseUp(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            }
        });
        
        this.start();
    }

    /*@Override
    protected void dispose(boolean disposing)
    {
        if (disposing) {
            this.Clear();
            this.fBuffer.dispose();
            this.fTimer.dispose();
        }
        super.dispose(disposing);
    }*/

    public final boolean getActive()
    {
        return this.fActive;
    }

    public final void setActive(boolean value)
    {
        if (this.fActive != value) {
            this.fActive = value;

            if (value) {
                this.start();
            } else {
                this.finish();
            }
        }
    }

    public final LabDevice getDevice(int index)
    {
        LabDevice result = null;
        if (index >= 0 && index < this.fDevices.size()) {
            result = this.fDevices.get(index);
        }
        return result;
    }

    public final int getDeviceCount()
    {
        return this.fDevices.size();
    }

    public final Date getBegTime()
    {
        return this.fBegTime;
    }

    public final void setBegTime(Date value)
    {
        this.fBegTime = value;
    }

    public final Date getCurTime()
    {
        return this.fCurTime;
    }

    public final void setCurTime(Date value)
    {
        this.fCurTime = value;
    }

    public final Date getEndTime()
    {
        return this.fEndTime;
    }

    public final void setEndTime(Date value)
    {
        this.fEndTime = value;
    }

    private String getAuthor()
    {
        return this.fHeader.Author;
    }

    private String getComments()
    {
        return this.fHeader.Comments;
    }

    private String getSubject()
    {
        return this.fHeader.Subject;
    }

    private String getTitle()
    {
        return this.fHeader.Title;
    }

    private void setAuthor(String value)
    {
        this.fHeader.Author = value;
    }

    private void setComments(String value)
    {
        this.fHeader.Comments = value;
    }

    private void setSubject(String value)
    {
        this.fHeader.Subject = value;
    }

    private void setTitle(String value)
    {
        this.fHeader.Title = value;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        try {
            this.fBuffer = new Bitmap(this.getWidth(), this.getHeight());
            Graphics2D canv = (Graphics2D) this.fBuffer.getGraphics();

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            canv.setRenderingHints(rh);
            
            canv.setColor(Color.white);
            canv.fillRect(0, 0, this.getWidth(), this.getHeight());

            for (int i = 0; i < this.getDeviceCount(); i++) {
                LabDevice dev = this.getDevice(i);
                dev.paint(canv);
            }

            if (this.fCurrentDev != null) {
                Rect rt = this.fCurrentDev.getRect();
                canv.setColor(Color.black);
                canv.setStroke(FOCUS_STROKE);
                canv.drawRect(rt.Left, rt.Top, rt.getWidth(), rt.getHeight());
            }

            g.drawImage(this.fBuffer, 0, 0, null);
            
            if (DEBUG && !StringHelper.isNullOrEmpty(fDebugInfo)) {
                g.drawString(this.fDebugInfo, 10, 10);
            }
        } catch (Exception ex) {
            Logger.write("ExperimentMaster.OnPaint(): " + ex.getMessage());
        }
    }

    private void tickTime(long time)
    {
        // every 50 milliseconds
        for (int i = 0; i < this.getDeviceCount(); i++) {
            LabDevice dev = this.getDevice(i);
            dev.tickTime(time);
        }

        this.repaint();
    }

    public LabDevice getDeviceByCoord(int x, int y)
    {
        for (int i = 0; i < this.getDeviceCount(); i++) {
            LabDevice dev = this.getDevice(i);
            if (dev.inArea(x, y)) {
                return dev;
            }
        }

        return null;
    }

    protected void onMouseDown(MouseEvent e)
    {
        this.fCurrentDev = this.getDeviceByCoord(e.getX(), e.getY());
        if (this.fCurrentDev != null && e.getButton() == MouseEvent.BUTTON1) {
            // don't change this two lines
            this.FX = e.getX() - this.fCurrentDev.getLeft();
            this.FY = e.getY() - this.fCurrentDev.getTop();

            this.invalidate();
        }
    }

    protected void onMouseUp(MouseEvent e)
    {
        if (this.fCurrentDev != null) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                this.FX = -1;
                this.FY = -1;
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                this.fMenuDev = this.fCurrentDev;

                boolean active = this.fMenuDev.getActive();
                if (!active) {
                    this.miActivationSwitch.setText("Включить");
                } else {
                    this.miActivationSwitch.setText("Выключить");
                }

                this.miActivationSwitch.setEnabled(this.fMenuDev.isActivable());
                this.miClear.setEnabled(this.fMenuDev.isContainer());
                this.miProperties.setEnabled(this.fMenuDev.isContainer());

                this.fDeviceMenu.show(this, e.getX(), e.getY());
            }

            this.fCurrentDev = null;
        }
    }
    
    protected void onMouseDrag(MouseEvent e)
    {
        //if (e.getButton() == MouseEvent.BUTTON1) {
            if (this.fCurrentDev != null) {
                if (this.FX != -1 || this.FY != -1) {
                    // don't change this two lines
                    int nX = (e.getX() - this.FX);
                    int nY = (e.getY() - this.FY);
                    
                    if (this.canBeDrag(this.fCurrentDev, nX, nY)) {
                        RefObject<LabDevice> refOther = new RefObject<>();
                        Point refPoint = ClingHelper.canCling(this.fDevices, this.fCurrentDev, nX, nY, refOther);
                        if (refPoint != null) {
                            nX = refPoint.X;
                            nY = refPoint.Y;
                        }

                        this.fCurrentDev.setLeft(nX);
                        this.fCurrentDev.setTop(nY);

                        this.checkCling(this.fCurrentDev, nX, nY, refOther.argValue);

                        this.repaint();
                    }
                }
            }
        //}
    }

    private void checkCling(LabDevice dev, int nX, int nY, LabDevice other)
    {
        boolean appendMode = (other != null);
        boolean connExists = false;
        
        List<LabDevice> disconns = new ArrayList<>();
        
        List<LabDevice> conns = this.findConnections(dev);
        for (LabDevice conDev : conns) {
            // check disconnections
            Point res = ClingHelper.canCling(dev, nX, nY, conDev);
            if (res == null) {
                disconns.add(conDev);
            }
            
            if (appendMode && conDev == other) {
                // check the possibility of adding a new connection
                connExists = true;
            }
        }
        
        // remove disconnections
        for (LabDevice dc : disconns) {
            this.removeConnection(dev, dc);
        }
        
        // append new connection
        if (appendMode && !connExists) {
            this.addConnection(dev, other);
        }
    }
    
    protected void onMouseMove(MouseEvent e)
    {
        if (e.getButton() == 0) {
            LabDevice dev = this.getDeviceByCoord(e.getX(), e.getY());
            String hint = null;

            if (dev != null) {
                String realVol = String.valueOf(dev.getRealVolume());
                String fillVol = String.valueOf(dev.getFillVolume());
                hint = String.format("<html>Вместимость: %s мл<br>Объем: %s мл<br>Масса: %s<br>Давление: %s<br>Температура: %s<br>pH: %s", 
                        new Object[]{realVol, fillVol, String.format("%5.5f г", dev.getSubstancesMass()), String.format("%5.5f кПа", dev.getPressure()), String.format("%5.5f °K", dev.getTemperature()), String.format("%5.5f", dev.getPH())});
                hint += "<br>Соединения: " + this.getConnectionsString(dev);
                hint += "</html>";
            }

            if (StringHelper.isNullOrEmpty(hint)) {
                this.setToolTipText(null);
            } else {
                this.setToolTipText(hint);
            }
        }
    }

    protected final boolean canBeDrag(LabDevice dev, int ax, int ay)
    {
        boolean result = (ax >= 2 && ay >= 2);
        return result && ax + dev.getWidth() < super.getWidth() - 2 && ay + dev.getHeight() < super.getHeight() - 2;
    }

    public final void clear()
    {
        for (LabDevice device : this.fDevices) {
            device.dispose();
        }
        this.fDevices.clear();
    }

    public final LabDevice addDevice(int left, int top, DeviceId deviceId)
    {
        LabDevice result = LabDevice.createDevice(this, left, top, deviceId);
        this.fDevices.add(result);

        if (result.getID() == DeviceId.dev_Beaker_100) {
            result.addSubstance("CaO", SubstanceState.Solid, 20);
            result.addSubstance("H2O", SubstanceState.Liquid, 40);

            result.changeContents();
            
            result.setBoiling(true);
        }

        this.repaint();

        return result;
    }

    public final void deleteDevice(LabDevice dev)
    {
        int ind = this.fDevices.indexOf(dev);
        if (ind >= 0) {
            this.fDevices.remove(ind);
        }
        dev.dispose();

        this.repaint();
    }

    public final Connection addConnection(LabDevice dev1, LabDevice dev2)
    {
        Connection result = new Connection(dev1, dev2);
        this.fConnections.add(result);
        return result;
    }

    public final void removeConnection(LabDevice dev1, LabDevice dev2)
    {
        for (Connection conn : this.fConnections) {
            if ((conn.Dev1 == dev1 && conn.Dev2 == dev2) || (conn.Dev1 == dev2 && conn.Dev2 == dev1)) {
                this.fConnections.remove(conn);
                return;
            }
        }
    }
    
    public final String getConnectionsString(LabDevice dev)
    {
        StringBuilder result = new StringBuilder();
        
        List<LabDevice> list = this.findConnections(dev);
        for (LabDevice other : list) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(other.getID().name());
        }
        
        return result.toString();
    }
    
    public final List<LabDevice> findConnections(LabDevice dev)
    {
        List<LabDevice> result = new ArrayList<>();
        
        for (Connection conn : this.fConnections) {
            if (conn.Dev1 == dev) {
                result.add(conn.Dev2);
            } else if (conn.Dev2 == dev) {
                result.add(conn.Dev1);
            }
        }
        
        return result;
    }
    
    @Override
    public void loadFromFile(String fileName)
    {
        /*this.Clear();

        String ext = Path.GetExtension(fileName).toLowerCase();

        if (TextUtils.equals(ext, ".edf")) {
            FileStream fileStream = new FileStream(fileName, FileMode.Open);
            this.LoadFromStream(fileStream);
            fileStream.Dispose();

            this.repaint();
        }*/
    }

    @Override
    public void saveToFile(String fileName)
    {
        /*FileStream fileStream = new FileStream(fileName, FileMode.CreateNew);
        this.SaveToStream(fileStream);
        fileStream.Dispose();*/
    }

    public final void loadFromStream(InputStream stream)
    {
    }

    public final void saveToStream(OutputStream stream)
    {
    }

    public final void start()
    {
        this.fTimer.start();

        this.fBegTime = new Date();
        //this.FEndTime = 0;
    }

    public final void finish()
    {
        this.fEndTime = new Date();

        this.fTimer.stop();
    }

    public final BenchListener getBenchListener()
    {
        return this.fBenchListener;
    }

    public final void setBenchListener(BenchListener value)
    {
        this.fBenchListener = value;
    }

    private void miDeviceActivationSwitchClick()
    {
        LabDevice device = this.fMenuDev;

        boolean active = device.getActive();
        if (active) {
            device.setActive(false);
        } else {
            device.setActive(true);
        }
    }

    private void miDeviceClearClick()
    {
        LabDevice device = this.fMenuDev;
        device.clear();
        this.invalidate();
    }

    private void miDeviceDeleteClick()
    {
        LabDevice device = this.fMenuDev;
        this.deleteDevice(device);
    }

    private void miDevicePropertiesClick()
    {
        LabDevice device = this.fMenuDev;
        if (this.fBenchListener != null) {
            this.fBenchListener.deviceProperties(device);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String actionPerformed = e.getActionCommand();
        //Object source = e.getSource();

        switch (actionPerformed) {
            case "mi_DeviceSwitch":
                miDeviceActivationSwitchClick();
                break;
            case "mi_DeviceClear":
                miDeviceClearClick();
                break;
            case "mi_DeviceDelete":
                miDeviceDeleteClick();
                break;
            case "mi_DeviceProperties":
                miDevicePropertiesClick();
                break;
        }
    }
}
