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
