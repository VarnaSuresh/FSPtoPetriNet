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


package uk.ac.ic.doc.scenebeans.animation.parse;

/** A BehaviourLink represents a link from a behaviour to a behaviour 
 *  listener.
 */
public class BehaviourLink
{
    private Object _behaviour;
    private String _behaviour_id;
    private Object _facet;
    private String _facet_name;
    private Object _animated;
    private Object _listener;
    private String _property_name;
    
    /** Constructs a BehaviourLink for a non-facetted behaviour.
     *
     *  @param behaviour
     *      The behaviour bean.
     *  @param behaviour_id
     *      The symbol that identifies the behaviour in the XML document.
     *  @param animated
     *      The bean being animated by the behaviour.
     *  @param listener
     *      The listener interface registered with the behaviour.
     *  @param property_name
     *      The name of the property being animated by the behaviour.
     */
    public BehaviourLink( Object behaviour, String behaviour_id,
                          Object animated, 
                          Object listener, String property_name )
    {
        _behaviour = behaviour;
        _behaviour_id = behaviour_id;
        _facet = behaviour;
        _facet_name = null;
        _animated = animated;
        _listener = listener;
        _property_name = property_name;
    }
    
    /** Constructs a BehaviourLink for a facetted behaviour.
     *
     *  @param behaviour
     *      The behaviour bean.
     *  @param behaviour_id
     *      The symbol that identifies the behaviour in the XML document.
     *  @param facet
     *      The facet object that implements the behaviour interface for
     *      the behaviour bean.
     *  @param facet_name
     *      The name of the facet.
     *  @param animated
     *      The bean being animated by the behaviour.
     *  @param listener
     *      The listener interface registered with the behaviour.
     *  @param property_name
     *      The name of the property being animated by the behaviour.
     */
    public BehaviourLink( Object behaviour, String behaviour_id,
                          Object facet, String facet_name,
                          Object animated, 
                          Object listener, String property_name )
    {
        _behaviour = behaviour;
        _behaviour_id = behaviour_id;
        _facet = facet;
        _facet_name = facet_name;
        _animated = animated;
        _listener = listener;
        _property_name = property_name;
    }
    
    /** Returns the behaviour bean.
     */
    public Object getBehaviour() {
        return _behaviour;
    }
    
    /** Returns the symbol that identifies the behaviour in the XML document.
     *
     *  @return
     *      The symbol that identifies the behaviour in the XML document.
     */
    public String getBehaviourID() {
        return _behaviour_id;
    }
    
    /** Queries whether the link is to a facet of a behaviour or to an
     *  unfacetted behaviour.
     *
     *  @return
     *      <code>true</code> if the link is to a facet, <code>false</code>
     *      if it is not.
     */
    public boolean isBehaviourFacetted() {
        return _facet_name != null;
    }
    
    /** Returns the facet of the behaviour, if the link is to a facet, or
     *  the behaviour itself, if it is not facetted.
     */
    public Object getFacet() {
        return _facet;
    }
    
    /** Returns the name of the facet, or <code>null</code> if the behaviour
     *  is not facetted.
     */
    public String getFacetName() {
        return _facet_name;
    }
    
    /** Returns the bean being animated by the behaviour.
     */
    public Object getAnimated() {
        return _animated;
    }
    
    /** Returns the listener that routes behaviour updates to a property
     *  of the animated bean.
     */
    public Object getListener() {
        return _listener;
    }
    
    /** Returns the name of the property being animated.
     */
    public String getPropertyName() {
        return _property_name;
    }
}
