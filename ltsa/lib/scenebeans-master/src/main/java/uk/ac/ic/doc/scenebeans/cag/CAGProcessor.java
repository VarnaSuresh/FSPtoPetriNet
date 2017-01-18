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






package uk.ac.ic.doc.scenebeans.cag;

import java.awt.Graphics2D;
import java.awt.geom.*;
import uk.ac.ic.doc.scenebeans.*;


public abstract class CAGProcessor
    implements SceneGraphProcessor
{
    private Graphics2D _graphics;
    private AffineTransform _transform;
    private Area _area = null;
    
    
    protected CAGProcessor( Graphics2D g ) {
        _graphics = g;
        _transform = new AffineTransform();
    }
    
    protected CAGProcessor( Graphics2D g, AffineTransform t ) {
        _graphics = g;
        _transform = new AffineTransform(t);
    }
    
    public Area getArea() {
        return (_area == null) ? new Area() : _area;
    }
    
    public void process( Primitive sg ) {
        Area primitive_area = new Area( sg.getShape(_graphics) );
        primitive_area.transform( _transform );
        if( _area == null ) {
            _area = primitive_area;
        } else {
            accumulateArea( _area, primitive_area );
        }
    }
    
    public void process( Transform sg ) {
        AffineTransform old_transform = new AffineTransform(_transform);
        _transform.concatenate( sg.getTransform() );
        sg.getTransformedGraph().accept(this);
        _transform = old_transform;
    }
    
    public void process( Input sg ) {
        sg.getSensitiveGraph().accept(this);
    }
    
    public void process( Style sg ) {
        sg.getStyledGraph().accept(this);
    }
    
    public void process( CompositeNode sg ) {
        for( int i = 0; i < sg.getVisibleSubgraphCount(); i++ ) {
            sg.getVisibleSubgraph(i).accept(this);
        }
    }
    
    
    protected abstract void accumulateArea( Area accumulator, Area a );
}
