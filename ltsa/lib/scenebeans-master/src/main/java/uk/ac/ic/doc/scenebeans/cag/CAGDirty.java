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


/** A SceneGraphProcessor that determines if any nodes of a scene graph are
 *  dirty.
 */
public class CAGDirty
    implements SceneGraphProcessor
{
    private boolean _is_dirty = false;
    
    public static boolean areChildrenDirty( CAGComposite sg ) {
        CAGDirty visitor = new CAGDirty();
        
        for( int i = 0; i < sg.getSubgraphCount(); i++ ) {
            sg.getSubgraph(i).accept( visitor );
            if( visitor.isDirty() ) return true;
        }
        
        return false;
    }
    
    public boolean isDirty() {
        return _is_dirty;
    }
    
    public void process( Primitive sg ) {
        _is_dirty = sg.isDirty();
    }
    
    public void process( Transform sg ) {
        if( sg.isDirty() ) {
            _is_dirty = true;
        } else {
            sg.getTransformedGraph().accept(this);
        }
    }
    
    public void process( Input sg ) {
        if( sg.isDirty() ) {
            _is_dirty = true;
        } else {
            sg.getSensitiveGraph().accept(this);
        }
    }
    
    public void process( Style sg ) {
        if( sg.isDirty() ) {
            _is_dirty = true;
        } else {
            sg.getStyledGraph().accept(this);
        }
    }
    
    public void process( CompositeNode sg ) {
        if( sg.isDirty() ) {
            _is_dirty = true;
        } else {
            for( int i = 0; i < sg.getVisibleSubgraphCount(); i++ ) {
                sg.getVisibleSubgraph(i).accept(this);
                if( _is_dirty ) return;
            }
        }
    }
}
