/** SceneBeans, a Java API for animated 2D graphics.
 *  
 *  Copyright (C) 2000 Nat Pryce and Imperial College
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 *  USA.
 *  
 */






package uk.ac.ic.doc.scenebeans.animation;

import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.activity.*;


/** A Command that, when invoked, starts an 
 *  {@link uk.ac.ic.doc.scenebeans.activity.Activity} by adding it to an
 *  {@link uk.ac.ic.doc.scenebeans.activity.ActivityRunner}
 */
public class StartActivityCommand implements Command
{
    private Activity _activity;
    private ActivityRunner _runner;
    
    /** Constructs a StartActivityCommand.
     *
     *  @param a
     *      The Activity to start.
     *  @param r
     *      The ActivityRunner that is to run the Activity.
     */
    public StartActivityCommand( Activity a, ActivityRunner r ) {
        _activity = a;
        _runner = r;
    }
    
    /** Returns the activity that is started by this command.
     *
     *  @return
     *      The activity started by this command.
     */
    public Activity getActivity() {
        return _activity;
    }
    
    /** Sets the activity that is started by this command.
     *
     *  @param a
     *      The activity started by this command.
     */
    public void setActivity( Activity a ) {
        _activity = a;
    }
    
    
    /** Returns the ActivityRunner that will manage the activity when it
     *  is started.
     *
     *  @return
     *      The ActivityRunner that will manage the activity.
     */
    public ActivityRunner getActivityRunner() {
        return _runner;
    }
    
    /** Sets the ActivityRunner that will manage the activity when it
     *  is started.
     *
     *  @param ar
     *      The ActivityRunner that will manage the activity.
     */
    public void setActivityRunner( ActivityRunner ar ) {
        _runner = ar;
    }
    
    public void invoke() throws CommandException {
        _runner.addActivity(_activity);
    }
}

