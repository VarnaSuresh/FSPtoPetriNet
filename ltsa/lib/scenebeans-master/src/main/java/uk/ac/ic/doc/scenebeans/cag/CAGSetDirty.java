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


public class CAGSetDirty
    implements SceneGraphProcessor
{
    private boolean _is_dirty;
    
    public CAGSetDirty( boolean b ) {
        _is_dirty = b;
    }
    
    public static void setChildrenDirty( CAGComposite cag, boolean b ) {
        CAGSetDirty visitor = new CAGSetDirty(b);
        
        for( int i = 0; i < cag.getSubgraphCount(); i++ ) {
            cag.getSubgraph(i).accept(visitor);
        }
    }
    
    public void process( Primitive sg ) {
        sg.setDirty(_is_dirty);
    }
    
    public void process( Transform sg ) {
        sg.setDirty(_is_dirty);
        sg.getTransformedGraph().accept(this);
    }
    
    public void process( Input sg ) {
        sg.setDirty(_is_dirty);
        sg.getSensitiveGraph().accept(this);
    }
    
    public void process( Style sg ) {
        sg.setDirty(_is_dirty);
        sg.getStyledGraph().accept(this);
    }
    
    public void process( CompositeNode sg ) {
        sg.setDirty(_is_dirty);
        for( int i = 0; i < sg.getSubgraphCount(); i++ ) {
            sg.getSubgraph(i).accept(this);
        }
    }
}
