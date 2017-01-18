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


/** An implementation of the SceneGraph interface that manages the dirty flag.
 */
public abstract class SceneGraphBase 
    implements SceneGraph
{
    private transient boolean _is_dirty = true;
    
    
    /** Returns whether this node is "dirty", that is, whether it's visible
     *  state been modified since it was last rendered. This is used 
     *  internally to optimise the rendering process and should not be 
     *  called by user code unless you intend to interact with the renderer in
     *  some bizarre manner.
     *
     *  @return 
     *      <code>true</code> if the node has been modified since it was
     *      last drawn, <code>false</code> otherwise.
     */
    public boolean isDirty() {
        return _is_dirty;
    }
    
    
    /** Record whether the node is "dirty".  This is used internally to
     *  optimise the rendering process and should not be called by
     *  user code unless you intend to interact with the renderer in
     *  some bizarre manner.
     *
     *  @param b
     *      A flag indicating whether the node is dirty (<code>true</code>) or
     *      clean (<code>false</code>).
     */
    public void setDirty( boolean b ) {
        _is_dirty = b;
    }
}
