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






package uk.ac.ic.doc.scenebeans.animation.parse;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.StringTokenizer;
import uk.ac.ic.doc.natutil.StringParser;


class ValueParser
    extends StringParser
{
    ValueParser( final URL document_base_url ) {
        TypeSpecificParser double_parser = new TypeSpecificParser() {
            public Object parse( String str ) 
                throws IllegalArgumentException 
            {
                return new Double(ExprUtil.evaluate(str));
            }
        };
        
        addParser( Double.class, double_parser );
        addParser( Double.TYPE, double_parser );
        
        TypeSpecificParser float_parser = new TypeSpecificParser() {
            public Object parse( String str ) 
                throws IllegalArgumentException 
            {
                return new Float((float)ExprUtil.evaluate(str));
            }
        };
        
        addParser( Float.class, float_parser );
        addParser( Float.TYPE, float_parser );
        
        
        TypeSpecificParser int_parser = new TypeSpecificParser() {
            public Object parse( String str )
                throws IllegalArgumentException
            {
                return new Integer((int)Math.floor(ExprUtil.evaluate(str)));
            }
        };
        
        addParser( Integer.class, int_parser );
        addParser( Integer.TYPE, int_parser );
        
        addParser( Point2D.class, new TypeSpecificParser() {
            public Object parse( String point_str )
                throws IllegalArgumentException
            {
                check( point_str.charAt(0) == '(' &&
                       point_str.charAt(point_str.length()-1) == ')' );
                
                StringTokenizer tok = new StringTokenizer(
                    point_str.substring( 1, point_str.length()-1 ),
                    "," );
                String x_str = tok.hasMoreTokens() ? tok.nextToken() : null;
                String y_str = tok.hasMoreTokens() ? tok.nextToken() : null;
                
                check( x_str != null || y_str != null || !tok.hasMoreTokens() );
                
                double x = ExprUtil.evaluate(x_str);
                double y = ExprUtil.evaluate(y_str);
                return new Point2D.Double(x,y);
            }
            
            void check( boolean b ) {
                if(!b) throw new IllegalArgumentException("invalid point value");
            }
        } );
        
        addParser( Font.class, new TypeSpecificParser() {
            public Object parse( String font_str )
                throws IllegalArgumentException
            {
                return Font.decode(font_str);
            }
        } );
        
        addParser( Color.class, new TypeSpecificParser() {
            public Object parse( String color_str )
                throws IllegalArgumentException
            {
                check( color_str.length() >= 6 && color_str.length() <= 8 && 
                       (color_str.length() % 2) == 0 );
                
                int r = Integer.parseInt( color_str.substring(0,2), 16 );
                int g = Integer.parseInt( color_str.substring(2,4), 16 );
                int b = Integer.parseInt( color_str.substring(4,6), 16 );
                int a = ( color_str.length() == 8 ) ?
                    Integer.parseInt( color_str.substring(6,8), 16 ) : 0xFF;
                
                return new Color( r, g, b, a );
            }
            
            void check( boolean b ){
                if(!b) throw new IllegalArgumentException("invalid color value");
            }
        } );
        
        addParser( URL.class, new TypeSpecificParser() {
            public Object parse( String url_str )
                throws IllegalArgumentException
            {
                try {
                    return new URL( document_base_url, url_str );
                }
                catch( MalformedURLException ex ) {
                    throw new IllegalArgumentException("invalid URL value");
                }
            }
        } );
    }
}
