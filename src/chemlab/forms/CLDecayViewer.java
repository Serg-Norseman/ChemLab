package chemlab.forms;

import bslib.common.FramesHelper;
import chemlab.core.controls.decay.DecayViewer;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public final class CLDecayViewer extends JFrame
{
    private final DecayViewer fDecayViewer;

    public CLDecayViewer(String nuclide)
    {
        super();
        
        this.setLayout(new BorderLayout());
        FramesHelper.setClientSize(this, 640, 480);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Радиоактивный распад нуклида [" + nuclide + "]");

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