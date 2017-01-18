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







package uk.ac.ic.doc.scenebeans;

import java.awt.Graphics2D;
import java.util.*;


/** The CompositeBase provides default implementations of most of the methods
 *  of the {@link uk.ac.ic.doc.scenebeans.CompositeNode} interface, including
 *  rendering and double-dispatch.  To implement a concrete composite node,
 *  just derive from CompositeBase and override the 
 *  <code>getVisibleSubgraphCount</code> and <code>getVisibleSubgraph</code>
 *  methods.
 */
public abstract class CompositeBase
    extends SceneGraphBase
    implements CompositeNode
{
    private ArrayList _nodes = new ArrayList();
    private transient ArrayList _last_drawn_nodes = new ArrayList();
    
    protected CompositeBase() {
    }
    
    /** Returns the number of subgraphs of this composite.
     */
    public int getSubgraphCount() {
        return _nodes.size();
    }
    
    /** Returns the <var>n</var>'th subgraph. 
     *  Subgraphs are indexed from zero.
     *
     *  @exception java.lang.IndexOutOfBoundsException
     *      <var>n</var> >= getVisibleSubgraphCount().
     */
    public SceneGraph getSubgraph( int n ) {
        return (SceneGraph)_nodes.get(n);
    }
    
    /** Returns getSubgraphCount().  By default, all subgraphs are visible.
     *  Override this in derived classes for which this is not the case.
     */
    public int getVisibleSubgraphCount() {
        return getSubgraphCount();
    }
    
    /** Returns getSubgraph(n).  By default, all subgraphs are visible.
     *  Override this in derived classes for which this is no the case.
     */
    public SceneGraph getVisibleSubgraph( int n ) {
        return getSubgraph(n);
    }
    
    /** Returns the number of subgraphs that were rendered by the last
     *  drawing operation.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     */
    public int getLastDrawnSubgraphCount() {
        return _last_drawn_nodes.size();
    }
    
    /** Returns the <var>n</var>'th subgraph that was rendered by the
     *  past drawing operation.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     *
     *  @exception java.lang.IndexOutOfBoundsException
     *      <var>n</var> >= getVisibleSubgraphCount().  
     */
    public SceneGraph getLastDrawnSubgraph( int n ) {
        return (SceneGraph)_last_drawn_nodes.get(n);
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
    
    /** Adds a sub-graph to the composite.
     *
     *  @exception uk.ac.ic.doc.scenebeans.TooManyChildrenException
     *      The maximum number of children have already been added to
     *      this composite.
     */
    public void addSubgraph( SceneGraph g ) {
        _nodes.add(g);
        setDirty(true);
    }
    
    /** Removes a sub-graph.
     *
     *  @param sg
     *      The subgraph to remove.
     */
    public void removeSubgraph( SceneGraph g ) {
        _nodes.remove(g);
        setDirty(true);
    }
    
    /** Removes a sub-graph by index. 
     *  Subgraphs are indexed from zero.
     *  
     *  @param n
     *      The index of the subgraph to remove.
     *
     *  @exception java.lang.IndexOutOfBoundsException
     *      <var>n</var> >= getVisibleSubgraphCount().  
     */
    public void removeSubgraph( int n ) {
        _nodes.remove(n);
        setDirty(true);
    }
    
    /** Implements the rendering of this node and its subgraphs.
     *  
     *  @param g
     *      The graphics context onto which to draw the scene graph.
     */
    public void draw( Graphics2D g ) {
        _last_drawn_nodes.clear();
        for( int i = getVisibleSubgraphCount()-1; i >= 0; i-- ) {
            SceneGraph sg = getVisibleSubgraph(i);
            sg.draw(g);
            _last_drawn_nodes.add( sg );
        }
        setDirty(false);
    }
}
