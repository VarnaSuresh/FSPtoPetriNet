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


/** The <em>Visitor</em> [GoF] interface used to determine the type of a
 *  {@link uk.ac.ic.doc.scenebeans.SceneGraph} node.  
 *  A reference to this this interface is passed to the
 *  {@link uk.ac.ic.doc.scenebeans.SceneGraph#accept} method of the node
 *  being processed, and the node calls back to the method of the interface
 *  that matches its type.
 *  <p>
 *  Unlike the traditional implementation of the <em>Visitor</em> pattern,
 *  the SceneGraphProcessor interface does not expose the concrete type
 *  of a node.  Because nodes are dynamically loaded components, this would
 *  be impossible.  Therefore it exposes the "category" of a node: the
 *  interface extending {@link uk.ac.ic.doc.scenebeans.SceneGraph} that
 *  is implemented by the node.
 *  <p>
 *  This interface is usually implemented by objects that traverse the 
 *  scene graph to perform some geometric calculation.
 *
 *  @see uk.ac.ic.doc.scenebeans.SceneGraph
 *  @see uk.ac.ic.doc.scenebeans.Primitive
 *  @see uk.ac.ic.doc.scenebeans.CompositeNode
 *  @see uk.ac.ic.doc.scenebeans.Transform
 *  @see uk.ac.ic.doc.scenebeans.Style
 *  @see uk.ac.ic.doc.scenebeans.Input
 */
public interface SceneGraphProcessor
{
    void process( Primitive primitive );
    void process( CompositeNode composite );
    void process( Transform transform );
    void process( Style style );
    void process( Input input );
}
