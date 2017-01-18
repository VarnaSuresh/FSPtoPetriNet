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

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.*;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.activity.*;


/** An Animation is the basic unit of animation design.  It encapsulates a
 *  scene graph and the activities animating that graph, and can itself be
 *  embedded in a scene graph and run as an activity.
 */
public class Animation
    extends ActivityBase
    implements CompositeNode, Serializable, ActivityRunner
{
    private ActivityList _activities = ActivityList.EMPTY;
    private Layered _layers = new Layered();
    private Map _commands = new HashMap();
    private Set _event_names = new HashSet();
    private double _width = 0.0;
    private double _height = 0.0;
    private boolean _is_animated = false;
    private boolean _is_dirty = false;
    
    /** Returns the width of the animation.  This is <em>not</em> calculated from
     *  the scene graph, because the graph may be animated in arbitrary ways,
     *  but must be precalculated by the animation designer.
     *
     *  @return
     *      The width of the animation.
     */
    public double getWidth() {
        return _width;
    }
    
    /** Sets the width of the animation.
     *
     *  @param width
     *      The width of the animation.
     */
    public void setWidth( double width ) {
        _width = width;
    }
    
    /** Returns the height of the animation.  This is <em>not</em> calculated from
     *  the scene graph, because the graph may be animated in arbitrary ways,
     *  but must be precalculated by the animation designer.
     *
     *  @return
     *      The height of the animation.
     */
    public double getHeight() {
        return _height;
    }
    
    /** Sets the height of the animation.
     *
     *  @param height
     *      The height of the animation.
     */
    public void setHeight( double height ) {
        _height = height;
    }
    
    public boolean isDirty() {
        return _is_dirty;
    }
    
    public void setDirty( boolean b ) {
        _is_dirty = b;
    }
    
    
    /** Calls back to the {@link uk.ac.ic.doc.scenebeans.SceneGraphProcessor}
     *  <var>p</var> to be processed as a 
     *  {@link uk.ac.ic.doc.scenebeans.CompositeNode}.
     *
     *  @param p
     *      A SceneGraphProcessor that is traversing the scene graph.
     */
    public void accept( SceneGraphProcessor p ) {
        p.process(this);
    }
    
    public int getSubgraphCount() {
        return _layers.getSubgraphCount();
    }
    
    public SceneGraph getSubgraph( int n ) {
        return _layers.getSubgraph(n);
    }
    
    public int getVisibleSubgraphCount() {
        return _layers.getVisibleSubgraphCount();
    }
    
    public SceneGraph getVisibleSubgraph( int n ) {
        return _layers.getVisibleSubgraph(n);
    }
    
    public int getLastDrawnSubgraphCount() {
        return _layers.getLastDrawnSubgraphCount();
    }
    
    public SceneGraph getLastDrawnSubgraph( int n ) {
        return _layers.getLastDrawnSubgraph(n);
    }
    
    public void addSubgraph( SceneGraph sg ) {
        _layers.addSubgraph(sg);
    }
    
    public void removeSubgraph( SceneGraph sg ) {
        _layers.removeSubgraph(sg);
    }
    
    public void removeSubgraph( int n ) {
        _layers.removeSubgraph(n);
    }
    
    public void draw( Graphics2D g ) {
        _layers.draw(g);
    }
    
    public synchronized void addActivity( Activity a ) {
        if( a.getActivityRunner() != this ) {
            a.setActivityRunner(this);
            _activities = _activities.add(a);
        }
    }
    
    public synchronized void removeActivity( Activity a ) {
        if( a.getActivityRunner() == this ) {
            a.setActivityRunner(null);
            _activities = _activities.remove(a);
        }
    }
    
    public boolean isFinite() {
        return false;
    }
    
    public void reset() {
        for( Iterator i = _activities.iterator(); i.hasNext(); ) {
            ((Activity)i.next()).reset();
        }
    }
    
    public void performActivity( double t ) {
        _activities.performActivities(t);
    }
    
    /** Adds a command to the Animation.  The command can then be invoked by
     *  the {@link #invokeCommand} method.
     *
     *  @param name
     *      The name of the command.
     *  @param command
     *      The object that implements the command.
     */
    public synchronized void addCommand( String name, Command command ) {
        _commands.put( name, command );
    }
    
    /** Removes a command from the Animation.
     *
     *  @param name
     *      The name of the command to be invoked.
     */
    public synchronized void removeCommand( String name ) {
        _commands.remove(name);
    }
    
    /** Returns the set of set command names that can be invoked on this 
     *  Animation.
     *  
     *  @return
     *     An immutable set of Strings, each of which is the name of a command
     *     that can be invoked upon the Animation.
     */
    public synchronized Set getCommandNames() {
        return Collections.unmodifiableSet(_commands.keySet());
    }
    
    /** Finds the object that implements a command that can be invoked upon
     *  this Animation.
     *
     *  @param name
     *      The name of the command.
     *  @return
     *      The object implementing the command, or <code>null</code> if the
     *      Animation does not have a command of that name.
     */
    public synchronized Command getCommand( String name ) {
        return (Command)_commands.get(name);
    }
    
    /** Invokes a command on the Animation by name.
     *
     *  @param name
     *      The name of the command.
     *  @exception uk.ac.ic.doc.scenebeans.animation.CommandException
     *      An error occurred in processing the command, or the Animation
     *      does not have a command of that name.
     */
    public synchronized void invokeCommand( String name )
        throws CommandException
    {
        Command cmd = (Command)_commands.get(name);
        if( cmd != null ) {
            cmd.invoke();
        } else {
            throw new CommandException( "unknown command \"" + name + "\"" );
        }
    }
    
    /** Returns all the names of 
     *  {@link uk.ac.ic.doc.scenebeans.event.AnimationEvent}s that will be 
     *  announced by this Animation.
     *
     *  @return
     *      An immutable set of Strings, each of which is the name of an
     *      AnimationEvent that will be announced by this Animation.
     */
    public Set getEventNames() {
        return Collections.unmodifiableSet(_event_names);
    }
    
    /** Adds an name to the set of names of AnimationEvents that will 
     *  be posted by this Animation.
     *
     *  @param name
     *      The name of to be added.
     */
    public void addEventName( String name ) {
        _event_names.add(name);
    }
    
    /** Removes an name from the set of names of AnimationEvents that will 
     *  be posted by this Animation.
     *
     *  @param name
     *      The name to be removed.
     */
    public void removeEventName( String name ) {
        _event_names.remove(name);
    }
    
    final void announceAnimationEvent( String event ) {
        postActivityComplete(event);
    }
}
