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

import java.util.Iterator;

/** An ActivityList holds Activity objects and allows the concurrent
 *  invocation of their activities and modification of the collection.
 */
public abstract class ActivityList
{
    private ActivityList() {
    }
    
    /** Invokes performActivity on the Activity objects in the list.
     */
    public abstract void performActivities( double t );
    
    
    /** Adds an Activity to the head of the list, returning the new list.
     *
     *  @param o
     *      The element to be added to the list.
     *  @return
     *      The list consisting of the element <var>o</var> followed by
     *      this list.
     */
    public final ActivityList add( Activity a ) {
        return new Node( a, this );
    }
    
    
    /** Removes the element <var>o</var> resulting in a new list which
     *  is returned to the caller.
     *
     *  @param o
     *      The object to be removed from the list.
     *  @return
     *      A list consisting of this list with object <var>o</var> removed.
     */
    public abstract ActivityList remove( Activity o );
    
    
    /** Returns an {@link java.util.Enumeration} over the elements of the list.
     */
    public Iterator iterator() {
        return new Iterator() {
            public boolean hasNext() {
                return _current != EMPTY;
            }

            public Object next() {
                Object result = ((Node)_current)._element;
                _current = ((Node)_current)._next;
                return result;
            }
            
            public void remove() {
                throw new UnsupportedOperationException(
                    "attempt to remove an elements from an ActivityList" );
            }
            
            private ActivityList _current = ActivityList.this;
        };
    }
    
    
    /** The empty list.  Variables of type ActivityList should be initialised
     *  to this value to create new empty lists.
     */
    public static final ActivityList EMPTY = new EmptyActivityList();
    private static class EmptyActivityList extends ActivityList
    {
        public ActivityList remove( Activity o ) {
            return this;
        }
        
        public void performActivities( double t ) {
            // Do nothing
        }
    };
    
    
    /** A non-empty list.
     */
    static class Node extends ActivityList {
        Node( Activity element, ActivityList next ) {
            _element = element;
            _next = next;
        }
        
        Node( Activity element ) {
            _element = element;
            _next = EMPTY;
        }
        
        public void performActivities( double t ) {
            _element.performActivity(t);
            _next.performActivities(t);
        }
        
        public ActivityList remove( Activity old ) {
            if( _element == old ) {
                return _next;
            } else {
                ActivityList n = _next.remove(old);
                if( n == _next ) {
                    return this;
                } else {
                    return new Node( _element, n );
                }
            }
        }
        
        private Activity _element;
        private ActivityList _next;
    }
}


