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

/** An Input scene-graph node marks a subgraph of the scene as being sensitive
 *  to some form of input.  Derived classes define the kind of input and
 *  bean properties define the events that are signalled.
 */
public interface Input extends SceneGraph
{
    /** Returns the subgraph that is sensitive to the type of input
     *  defined by this bean.
     *
     *  @return
     *      The sensitive subgraph.
     */
    SceneGraph getSensitiveGraph();
    
    /** Sets the subgraph that is sensitive to the type of input
     *  defined by this bean.
     *
     *  @param sg
     *      The sensitive subgraph.
     */
    void setSensitiveGraph( SceneGraph sg );
}
