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

/** A CompositeNode is composed of multiple scene graphs that are combined
 *  in some way.  Derived classes might only display a subset of their
 *  subgraphs or combine them into a completely new graph.
 */
public interface CompositeNode extends SceneGraph
{
    /** Returns the number of subgraphs of this composite.
     */
    int getSubgraphCount();
    
    /** Returns the <var>n</var>'th subgraph. 
     *  Subgraphs are indexed from zero.
     *
     *  @exception java.lang.IndexOutOfBoundsException
     *      <var>n</var> >= getVisibleSubgraphCount().
     */
    SceneGraph getSubgraph( int n );
    
    /** Returns the number of visible subgraphs of this composite.
     */
    int getVisibleSubgraphCount();
    
    /** Returns the <var>n</var>'th visible subgraph.
     *  Subgraphs are indexed from zero.
     *
     *  @exception java.lang.IndexOutOfBoundsException
     *      <var>n</var> >= getVisibleSubgraphCount().  
     */
    SceneGraph getVisibleSubgraph( int n );
    
    /** Returns the number of subgraphs that were rendered by the last
     *  drawing operation.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     */
    int getLastDrawnSubgraphCount();
    
    /** Returns the <var>n</var>'th subgraph that was rendered by the
     *  past drawing operation.
     *  This is used to optimise the rendering process.  User code should 
     *  avoid calling this.
     *
     *  @exception java.lang.IndexOutOfBoundsException
     *      <var>n</var> >= getVisibleSubgraphCount().  
     */
    SceneGraph getLastDrawnSubgraph( int n );
    
    /** Adds a sub-graph to the composite.
     *
     *  @param sg
     *      The scene graph to add to the composite.
     *
     *  @exception uk.ac.ic.doc.scenebeans.TooManyChildrenException
     *      The maximum number of children have already been added to
     *      this composite.
     */
    void addSubgraph( SceneGraph sg );
    
    /** Removes a sub-graph.
     *
     *  @param sg
     *      The subgraph to remove.
     */
    void removeSubgraph( SceneGraph sg );
    
    /** Removes a sub-graph by index. 
     *  Subgraphs are indexed from zero.
     *  
     *  @param n
     *      The index of the subgraph to remove.
     *
     *  @exception java.lang.IndexOutOfBoundsException
     *      <var>n</var> >= getVisibleSubgraphCount().  
     */
    void removeSubgraph( int n );
}
