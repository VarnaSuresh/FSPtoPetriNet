package custom;

import java.awt.Frame;
import lts.Animator;
import lts.Relation;
import java.io.File;

public abstract class CustomAnimator extends Frame {

    public abstract void init(Animator a, File xml, 
    	                        Relation actionMap, Relation controlMap,
    	                        boolean replay);

    public abstract void stop();

    public void dispose() {
        stop();
        super.dispose();
    }

}