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






package uk.ac.ic.doc.scenebeans.behaviour;

import java.awt.geom.Point2D;
import java.io.Serializable;
import uk.ac.ic.doc.scenebeans.activity.*;
import uk.ac.ic.doc.scenebeans.*;




/** The <a href="../../../../../../../beans/multitrack.html">MultiTrack</a> 
 *  behaviour bean.
 */
public class MultiTrack
    extends PointActivityBase
    implements Serializable
{
    private double[] _nodes;
    private String[] _events;
    private double _timeout = 0.0;
    private int _current = 0;
    private double _spacing=0.0; 
    private Point2D _avoid = new Point2D.Double(0.0,0.0);
    
    public MultiTrack() {
        _nodes = new double[0];
        _events = new String[0];
        _avoid = new Point2D.Double(100000.0,100000.0);
    }
    
    public void setAvoid(Point2D p){
        _avoid = p;
    }
    
    public Point2D getAvoid() {
        return _avoid;
    }
        
    public double getSpacing() {
        return _spacing;
    }
    
    public void setSpacing( double d ) {
        _spacing = d;
    }

    
    public synchronized int getPointCount() {
        return (_nodes.length+1) / 3;
    }
    
    public synchronized void setPointCount( int n ) {
        double[] new_nodes = new double[3*n - 1];
        System.arraycopy( _nodes, 0, new_nodes, 0,
                          Math.min( _nodes.length, new_nodes.length ) );
        _nodes = new_nodes;
        
        String[] new_events = new String[n-1];
        System.arraycopy( _events, 0, new_events, 0,
                          Math.min( _events.length, new_events.length ) );
        _events = new_events;
    }
    
    public synchronized Point2D getPoint( int n ) {
        n *= 3;
        return new Point2D.Double( _nodes[n], _nodes[n+1] );
    }
    
    public synchronized void setPoint( int n, Point2D p ) {
        n *= 3;
        _nodes[n] = p.getX();
        _nodes[n+1] = p.getY();
    }
    
    public synchronized double getX( int n ) {
        return _nodes[n*3];
    }
    
    public synchronized void setX( int n, double x ) {
        _nodes[n*3] = x;
    }
    
    public synchronized double getY( int n ) {
        return _nodes[n*3 + 1];
    }
    
    public synchronized void setY( int n, double y ) {
        _nodes[n*3 + 1] = y;
    }
    
    public synchronized double getDuration( int n ) {
        return _nodes[n*3 + 2];
    }
    
    public synchronized void setDuration( int n, double t ) {
        _nodes[n*3 + 2] = t;
    }
    
    public synchronized String getEvent( int n ) {
        return _events[n];
    }
    
    public synchronized void setEvent( int n, String event_name ) {
        _events[n] = event_name;
    }
    
    public synchronized Point2D getValue() {
        if( hasFinished() ) {
            return getPoint( getPointCount()-1 );
        } else {
            double from_x = getX(_current);
            double from_y = getY(_current);
            double to_x = getX(_current+1);
            double to_y = getY(_current+1);
            
            return new Point2D.Double( from_x + (ratio() * (to_x - from_x)),
                                       from_y + (ratio() * (to_y - from_y)) );
        }
    }
    
    public boolean isFinite() {
        return true;
    }
    
    public synchronized void reset() {
        _current = 0;
        _timeout = 0.0;
        postUpdate( getValue() );
    }
    
    public synchronized void performActivity( double t ) {
        if (!isCollision()) {
          _timeout += t;
          while( !hasFinished() && _timeout >= getDuration(_current) ) {
              _timeout -= getDuration(_current);
              if( _events[_current] != null ) {
                  postActivityComplete(_events[_current]);
              }
              _current++;
          }
          
          if( hasFinished() ) postActivityComplete();
          postUpdate( getValue() );
        }
    }
    
    private double ratio() {
        double d = getDuration(_current);
        return _timeout / d;
    }
    
    private boolean hasFinished() {
        return _current >= getPointCount() - 1;
    }
    
    // computes whether input collides with this behaviour
    
    private boolean isCollision() {
      return (_avoid.distanceSq(getValue())< (_spacing * _spacing));
    }
    
    
    public class AvoidAdapter implements PointBehaviourListener
    {
        public void behaviourUpdated( Point2D v ) {
            setAvoid(v);
        }
    }
    
    public final AvoidAdapter newAvoidAdapter() {
        return new AvoidAdapter();
    }

}

