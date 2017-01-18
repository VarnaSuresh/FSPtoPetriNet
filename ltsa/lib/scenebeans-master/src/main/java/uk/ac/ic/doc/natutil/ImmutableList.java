/** The Regent Distributed Programming Environment
 *  
 *  by Nat Pryce, 1998
 */

package uk.ac.ic.doc.natutil;

import java.util.Enumeration;


public abstract class ImmutableList
{
   /** Adds an element to the head of the list, returning the new list.
     *
     *  @param o
     *      The element to be added to the list.
     *  @return
     *      The list consisting of the element <var>o</var> followed by
     *      this list.
     */
    public final ImmutableList add( Object o ) {
        return new Node( o, this );
    }

    /** Removes the element <var>o</var> resulting in a new list which
     *  is returned to the caller.
     *
     *  @param o
     *      The object to be removed from the list.
     *
     *  @return
     *      A list consisting of this list with object <var>o</var> removed.
     */
    public abstract ImmutableList remove( Object o );

    /** Removes all elements for which the predicate <var>p</var> returns
     *  true, resulting in a new list which is returned to the caller.
     *
     *  @param p
     *      A predicate that returns <code>true</code> if the element is
     *      to be removed from the list, and <code>false</code> otherwise.
     *  @return
     *      A list consisting of this list with all elements for which the
     *      predicate <var>p</var> returned true removed.
     */
    public abstract ImmutableList removeIf( Predicate p );

    /** Applies the procedure <var>proc</var> to all elements in the list.
     */
    public abstract void forAll( Procedure proc );

    /** Creates a new list whose elements are the result of applying function
     *  <var>fn</var> to the elements of this list.
     */
    public abstract ImmutableList map( Function fn );

    /** Returns a "standard" enumeration over the elements of the list.
     */
    public Enumeration elements() {
        return new Enumeration() {
            public boolean hasMoreElements() {
                return _current != EMPTY;
            }

            public Object nextElement() {
                Object result = ((Node)_current)._element;
                _current = ((Node)_current)._next;
                return result;
            }

            private ImmutableList _current = ImmutableList.this;
        };
    }

    /** The empty list.  Variables of type ImmutableList should be
     *  initialised to this value to create new empty lists.
     */
    public static final ImmutableList EMPTY = new ImmutableList() {
        public ImmutableList removeIf( Predicate p ) {
            return this;
        }

        public ImmutableList remove( Object o ) {
            return this;
        }

        public void forAll( Procedure proc ) {
            return;
        }

        public ImmutableList map( Function fn ) {
            return this;
        }
    };

    static class Node extends ImmutableList {
        Node( Object element, ImmutableList next ) {
            _element = element;
            _next = next;
        }

        Node( Object element ) {
            _element = element;
            _next = EMPTY;
        }

        public ImmutableList removeIf( Predicate p ) {
            ImmutableList n = _next.remove(p);
            if( p.evaluate(_element) ) {
                return n;
            } else if( n == _next ) {
                return this;
            } else {
                return new Node( _element, n );
            }
        }

        public ImmutableList remove( Object old ) {
            if( _element == old ) {
                return _next;
            } else {
                ImmutableList n = _next.remove(old);
                if( n == _next ) {
                    return this;
                } else {
                    return new Node( _element, n );
                }
            }
        }

        public void forAll( Procedure proc ) {
            proc.execute(_element);
            _next.forAll(proc);
        }

        public ImmutableList map( Function fn ) {
            return new Node( fn.evaluate(_element), _next.map(fn) );
        }

        private Object _element;
        private ImmutableList _next;
    }
}


