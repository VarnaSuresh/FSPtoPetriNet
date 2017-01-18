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

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/** An ActivityThread can be executed by a Thread and simulates the
 *  behaviour of a set of {@link uk.ac.ic.doc.scenebeans.activity.Activity} 
 *  objects in real-time.
 */
public class ActivityThread
    implements ActivityRunner, Runnable
{
    Object _perform_lock;
    ActivityList _activities = ActivityList.EMPTY;
    Object _list_lock = new Object();
    long _start;
    Thread _thread = null;
    boolean _stop = false;
    long _sleep = 100;
    
    public ActivityThread() {
        _perform_lock = this;
    }
    
    public ActivityThread( Object perform_lock ) {
        _perform_lock = perform_lock;
    }
    
    public void addActivity( Activity a ) {
        synchronized(_list_lock) {
            a.setActivityRunner(this);
            _activities = _activities.add(a);
        }
    }
    
    public void removeActivity( Activity a ) {
        synchronized(_list_lock) {
            a.setActivityRunner(null);
            _activities = _activities.remove(a);
        }
    }
    
    public long getSleepDelay() {
        synchronized(_perform_lock) {
            return _sleep;
        }
    }
    
    public void setSleepDelay( long millis ) {
        synchronized(_perform_lock) {
            _sleep = millis;
        }
    }
    
    public void start() {
        synchronized(_perform_lock) {
            _start = System.currentTimeMillis();
            _stop = false;
            _thread = new Thread(this);
            _thread.start();
        }
    }
    
    public void stop() throws InterruptedException {
        synchronized(_perform_lock) {
            _stop = true;
            _thread.interrupt();
            _thread.join();
            _thread = null;
        }
    }
    
    public void run() {
        long sleep;
        
        try {
            while( !_stop ) {
                
                synchronized(_perform_lock) {
                    long now = System.currentTimeMillis();
                    long delta = now - _start;
                    double t = (double)delta/1000.0;
                    
                    _activities.performActivities(t);
                    
                    _start = now;
                    sleep = _sleep;
                }
                
                if( sleep != 0 ) Thread.sleep(sleep);
            }
        }
        catch( InterruptedException ex ) {
        }
    }
}

