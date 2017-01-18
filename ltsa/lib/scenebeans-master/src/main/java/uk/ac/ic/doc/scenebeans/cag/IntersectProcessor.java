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
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;


public class IntersectProcessor extends CAGProcessor
{
    public IntersectProcessor( Graphics2D g ) {
        super(g);
    }
    
    public IntersectProcessor( Graphics2D g, AffineTransform t ) {
        super( g, t );
    }
    
    protected void accumulateArea( Area accumulator, Area a ) {
        accumulator.intersect(a);
    }
}
