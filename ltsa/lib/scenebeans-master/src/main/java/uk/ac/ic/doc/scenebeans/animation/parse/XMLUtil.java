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

import org.w3c.dom.*;


class XMLUtil
{
    static String getRequiredAttribute( Element elt, String name ) 
        throws AnimationParseException
    {
        String value = elt.getAttribute(name);
        if( value.equals("") ) {
            throw new AnimationParseException( "required attribute \"" +
                                               name + "\" not found" );
        } else {
            return value;
        }
    }
    
    static String getOptionalAttribute( Element elt, String name ) {
        String value = elt.getAttribute(name);
        if( value.equals("") ) {
            return null;
        } else {
            return value;
        }
    }
    
    /*  An example of the "Bouncer" pattern.
     *  <p>
     *  See: http://www.c2.com/cgi-bin/wiki?BouncerPattern
     */
    static void checkElementType( Element elt, String type_str )
        throws AnimationParseException
    {
        if( !( elt.getTagName().equals(type_str) ) ) {
            throw new AnimationParseException( type_str+" element expected" );
        }
    }
}
