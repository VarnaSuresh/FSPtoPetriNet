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
import java.awt.geom.Area;
import java.awt.Shape;
import java.util.List;
import java.util.ArrayList;
import uk.ac.ic.doc.scenebeans.cag.CAGProcessor;
import uk.ac.ic.doc.scenebeans.cag.CAGDirty;
import uk.ac.ic.doc.scenebeans.cag.CAGSetDirty;


/** A {@link uk.ac.ic.doc.scenebeans.CompositeNode} that composes its subgraphs by
 *  Constructive Area Geometry.  Derived classes use various CAG operators.
 *
 *  This class is a bit of a wierd hack.  It supports the CompositeNode
 *  interface, so that the XML parser and other graph builders can
 *  modify the graphs to be composed, but acts like a Primitive to any
 * operation implemented as a {@link uk.ac.ic.doc.scenebeans.SceneGraphProcessor}.
 */
public abstract class CAGComposite 
    extends PrimitiveBase
    implements CompositeNode
{
    private Area _area;
    private List _args = new ArrayList();
    
    
    public void draw( Graphics2D g ) {
        super.draw(g);
        CAGSetDirty.setChildrenDirty( this, false );
    }
    
    /*   Primitive interface
     */
    
    public Shape getShape( Graphics2D g ) {
        if( _area == null || isDirty() ) _area = calculateArea(g);
        return _area;
    }
    
    public boolean isDirty() {
        return super.isDirty() || CAGDirty.areChildrenDirty(this);
    }
    
    /*  CompositeNode interface
     */
    
    public int getSubgraphCount() {
        return _args.size();
    }
    
    public SceneGraph getSubgraph( int n ) {
        return (SceneGraph)_args.get(n);
    }
    
    public int getVisibleSubgraphCount() {
        return 0;
    }
    
    public SceneGraph getVisibleSubgraph( int n ) {
        throw new IndexOutOfBoundsException( "subgraph index out of range" );
    }
    
    public int getLastDrawnSubgraphCount() {
        return 0;
    }
    
    public SceneGraph getLastDrawnSubgraph( int n ) {
        throw new IndexOutOfBoundsException( "last-drawn subgraph index " +
                                             "out of range" );
    }
    
    public void addSubgraph( SceneGraph g ) {
        _args.add(g);
        setDirty(true);
    }
    
    public void removeSubgraph( SceneGraph g ) {
        _args.remove(g);
        setDirty(true);
    }
    
    public void removeSubgraph( int n ) {
        _args.remove(n);
        setDirty(true);
    }
    
    private Area calculateArea( Graphics2D g ) {
        CAGProcessor p = newCAGProcessor(g);
        
        for( int i = 0; i < getSubgraphCount(); i++ ) {
            getSubgraph(i).accept(p);
        }
        
        return p.getArea();
    }
    
    protected abstract CAGProcessor newCAGProcessor( Graphics2D g );
}
