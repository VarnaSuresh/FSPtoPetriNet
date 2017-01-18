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


/** A SequentialActivity that runs multiple sub-activities sequentially.
 *  A ConcurrentActivity is finite - it completes when all its last sub-activity
 *  complete - and can only run activities that are themselves finite.
 */
public class SequentialActivity
    extends CompositeActivity
    implements AnimationListener
{
    private List _activities = new ArrayList();
    int _current = 0;
    
    public SequentialActivity() {
        super();
    }
    
    public synchronized void reset() {
        _current = 0;
        
        /*  Reset sub-activities in reverse order so that the last activity
         *  to be reset updates its listeners to the initial state of the first
         *  activity to be performed.
         */
        for( int i = _activities.size()-1; i >= 0; i-- ) {
            ((Activity)_activities.get(i)).reset();
        }
    }
    
    public synchronized void addActivity( Activity a ) {
        if( !a.isFinite() ) {
            throw new IllegalArgumentException(
                "infinite activity added to sequence" ); 
        }
        
        a.setActivityRunner(this);
        a.addAnimationListener(this);
        
        _activities.add(a);
    }
    
    public synchronized void removeActivity( Activity a ) {
        a.setActivityRunner(null);
        _activities.remove(a);
    }
    
    public synchronized void performActivity( double t ) {
        if( _current < _activities.size() ) {
            ((Activity)_activities.get(_current)).performActivity(t);
        }
    }
    
    public void animationEvent( AnimationEvent ev ) {
        _current++;
        if( _current == _activities.size() ) {
            postActivityComplete();
        }
    }
}
