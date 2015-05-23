package chemlab.core.controls.decay;

import bslib.common.BaseObject;
import chemlab.refbooks.NuclideRecord;

public class NuclideNode extends BaseObject
{
    public String Nuclide;
    public NuclideRecord Record;
    public NuclideNode[] Descendants;

    public NuclideNode()
    {
        this.Descendants = new NuclideNode[8];
    }
    
    public final int getDescendantsCount()
    {
        int result = 0;
        for (int dm = 0; dm < 8; dm++) {
            if (this.Descendants[dm] != null) {
                result++;
            }
        }
        return result;
    }
}
