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
import uk.ac.ic.doc.scenebeans.event.*;


/** A CompositeActivity that runs multiple sub-activities concurrently.
 *  A ConcurrentActivity is finite - it completes when all its sub-activities
 *  complete - and can only run activities that are themselves finite.
 */
public class ConcurrentActivity
    extends CompositeActivity
    implements AnimationListener
{
    private ActivityList _activities = ActivityList.EMPTY;
    int _finite_count = 0;
    int _complete = 0;
    
    public ConcurrentActivity() {
        super();
    }
    
    public synchronized void reset() {
        _complete = 0;
        for( Iterator i = _activities.iterator(); i.hasNext(); ) {
            ((Activity)i.next()).reset();
        }
    }
    
    public synchronized void addActivity( Activity a ) {
        a.setActivityRunner(this);
        if( a.isFinite() ) {
            _finite_count++;
            a.addAnimationListener(this);
        }
        
        _activities = _activities.add(a);
    }
    
    public synchronized void removeActivity( Activity a ) {
        if( a.isFinite() ) {
            _finite_count--;
            a.removeAnimationListener(this);
        }
        
        _activities = _activities.remove(a);
        a.setActivityRunner(null);
    }
    
    public void performActivity( double t ) {
        _activities.performActivities(t);
    }
    
    public void animationEvent( AnimationEvent ev ) {
        _complete++;
        if( _complete == _finite_count ) postActivityComplete();
    }
}
