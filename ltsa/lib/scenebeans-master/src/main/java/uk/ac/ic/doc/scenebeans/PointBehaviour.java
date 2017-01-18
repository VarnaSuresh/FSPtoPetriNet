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

import java.awt.geom.Point2D;


/** A behaviour that encapsulates a changing {@link java.awt.geom.Point2D} value.
 */
public interface PointBehaviour
{
    /** Add a listener to this behaviour.  The listener will be notified
     *  whenever the value of the behaviour changes.
     *
     *  @param l
     *      The listener being added to the behaviour.
     */
    void addPointBehaviourListener( PointBehaviourListener l );

    /** Remove a listener to this behaviour.
     *
     *  @param l
     *      The listener being removed from the behaviour.
     */
    void removePointBehaviourListener( PointBehaviourListener l );
}
