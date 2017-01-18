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

import uk.ac.ic.doc.scenebeans.activity.*;



/** A Command that resets an {@link uk.ac.ic.doc.scenebeans.activity.Activity}
 *  when it is invoked.
 *
 *  @see uk.ac.ic.doc.scenebeans.activity.Activity#reset
 */
public class ResetActivityCommand implements Command
{
    private Activity _activity;
    
    
    /** Constructs a ResetActivityCommand.
     *
     *  @param a
     *      The Activity to reset.
     */
    public ResetActivityCommand( Activity a ) {
        _activity = a;
    }
    
    /** Returns the activity that is reset by this command.
     *
     *  @return
     *      The activity reset by this command.
     */
    public Activity getActivity() {
        return _activity;
    }
    
    /** Sets the activity that is reset by this command.
     *
     *  @param a
     *      The activity reset by this command.
     */
    public void setActivity( Activity a ) {
        _activity = a;
    }
    
    public void invoke() throws CommandException {
        _activity.reset();
    }
}

