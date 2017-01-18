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






package uk.ac.ic.doc.scenebeans.activity;

import java.util.*;


/** The base class of finite activities.
 */
public abstract class FiniteActivityBase extends ActivityBase
{
    private String _activity_name = null;
    
    /** Constructs a FiniteActivityBase.
     */
    protected FiniteActivityBase() {
        super();
    }
    
    /** Returns <code>true</code> indicating that the activity is finite.
     *
     *  @param return
     *      <code>true</code>.
     */
    public boolean isFinite() {
        return true;
    }
    
    /** Returns the name of this activity, that is reported in the
     *  {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent}s announced
     *  when the activity completes.
     *
     *  @return
     *      The name of the activity.
     */
    public String getActivityName() {
        return _activity_name;
    }
    
    /** Sets the name of this activity, that is reported in the
     *  {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent}s announced
     *  when the activity completes.
     *
     *  @param name
     *      The name of the activity.
     */
    public void setActivityName( String name ) {
        _activity_name = name;
    }
    
    /** Posts an {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent} indicating
     *  that the activity is complete.  The name of the event is initialised as
     *  the name of this activity.
     */
    protected synchronized void postActivityComplete() {
        postActivityComplete( _activity_name );
    }
}

