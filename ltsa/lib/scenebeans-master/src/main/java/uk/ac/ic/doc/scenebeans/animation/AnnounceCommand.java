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


/** A command that causes an {@link uk.ac.ic.doc.scenebeans.animation.Animation}
 *  to announce an {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent}.
 */
public class AnnounceCommand implements Command
{
    private Animation _animation;
    private String _event;
    
    /** Constructs the command.
     *
     *  @param animation
     *      The Animation that will announce the event.
     *  @param event
     *      The name of the event that will be announced.
     */
    public AnnounceCommand( Animation animation, String event ) {
        _animation = animation;
        _event = event;
    }
    
    /** Returns the animation that will announce the event.
     *
     *  @return
     *      The Animation that will announce the event.
     */
    public Animation getAnimation() {
        return _animation;
    }
    
    /** Sets the animation that will announce the event.
     *
     *  @param animation
     *      The Animation that will announce the event.
     */
    public void setAnimation( Animation animation ) {
        _animation = animation;
    }
    
    /** Returns the name of the event that will be announced.
     *
     *  @return
     *      The name of the event that will be announced.
     */
    public String getEventName() {
        return _event;
    }
    
    /** Sets the name of the event that will be announced.
     *
     *  @param event
     *      The name of the event that will be announced.
     */
    public void setEventName( String event ) {
        _event = event;
    }
    
    public void invoke() throws CommandException {
        _animation.announceAnimationEvent(_event);
    }
}

