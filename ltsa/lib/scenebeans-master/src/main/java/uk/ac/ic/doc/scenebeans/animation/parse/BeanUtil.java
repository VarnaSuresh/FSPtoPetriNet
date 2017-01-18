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

import java.awt.Font;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.lang.reflect.*;
import java.beans.*;
import java.util.*;
import uk.ac.ic.doc.natutil.*;


/** Convenience functions for accessing features of Java Beans.
 */
class BeanUtil
{
    static BeanInfo getBeanInfo( Class c )
        throws AnimationParseException
    {
        try {
            return Introspector.getBeanInfo(c);
        }
        catch( IntrospectionException ex ) {
            throw new AnimationParseException(
                "could not find information about " + c.getName() + 
                " bean: " + ex.getMessage() );
        }
    }
    
    static BeanInfo getBeanInfo( Object o )
        throws AnimationParseException
    {
        return getBeanInfo( o.getClass() );
    }
    
    static Object getProperty( Object bean, String name )
        throws AnimationParseException
    {
        BeanInfo info = getBeanInfo(bean);
        return getProperty( bean, info, name );
    }
    
    static Object getProperty( Object bean, BeanInfo info, String name )
        throws AnimationParseException 
    {
        PropertyDescriptor pd = getPropertyDescriptor( info, name );
        
        try {
            Method get = pd.getReadMethod();
            return get.invoke( bean, new Object[0] );
        }
        catch( RuntimeException ex ) {
            throw ex;
        }
        catch( Exception ex ) {
            throw new AnimationParseException( "cannot get " + name + 
                                               " property of bean: " +
                                               ex.getMessage() );
        }
    }
    
    static void setProperty( Object bean, BeanInfo info,
                             String name, String value_str,
                             ValueParser parser )
        throws AnimationParseException
    {
        PropertyDescriptor pd = getPropertyDescriptor( info, name );
        Object value = parser.newObject( pd.getPropertyType(), value_str );
        
        try {
            Method set = pd.getWriteMethod();
            if( set != null ) {
                set.invoke( bean, new Object[]{value} );
            } else {
                throw new AnimationParseException( 
                    "attempted to set read-only property " + name );
            }
        }
        catch( RuntimeException ex ) {
            throw ex;
        }
        catch( Exception ex ) {
            throw new AnimationParseException( "cannot set " + name + 
                                               " property of bean: " +
                                               ex.getMessage() );
        }
    }
    
    static void setIndexedProperty( Object bean, BeanInfo info,
                                    String name, int index, 
                                    String value_str, ValueParser parser )
        throws AnimationParseException
    {
        PropertyDescriptor pd = getPropertyDescriptor( info, name );
        if( !(pd instanceof IndexedPropertyDescriptor) ) {
            throw new AnimationParseException( "the " + name + 
                                            " property is not indexed" );
        }
        IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor)pd;
        Object value = parser.newObject( ipd.getIndexedPropertyType(),
                                         value_str );
        
        try {
            Method set = ipd.getIndexedWriteMethod();
            if( set != null ) {
                set.invoke( bean, new Object[]{ new Integer(index), value } );
            } else {
                throw new AnimationParseException( 
                    "attempted to set read-only property " + name );
            }
        }
        catch( RuntimeException ex ) {
            throw ex;
        }
        catch( Exception ex ) {
            throw new AnimationParseException( "cannot set " + name + 
                                               " property of bean: " +
                                               ex.getMessage() );
        }
    }
    
    static PropertyDescriptor getPropertyDescriptor( BeanInfo info, 
                                                     String name ) 
        throws AnimationParseException
    {
        PropertyDescriptor[] props = info.getPropertyDescriptors();
        for( int i = 0; i < props.length; i++ ) {
            if( props[i].getName().equals( name ) ) {
                return props[i];
            }
        }
        
        throw new AnimationParseException( "beans of type " + 
                                        info.getBeanDescriptor().getName() +
                                        " do not have a property named " + 
                                        name );
    }
    
    static void bindEventListener( Object listener, Object event_source ) 
        throws AnimationParseException
    {
        EventSetDescriptor ev = findCompatibleEvent( listener, event_source );
        Method add = ev.getAddListenerMethod();
        
        try {
            add.invoke( event_source, new Object[]{listener} );
        }
        catch( Exception ex ) {
            throw new AnimationParseException( 
                "failed to register event listener: " + ex.getMessage() );
        }
    }
    
    static EventSetDescriptor findCompatibleEvent( Object listener, 
                                                   Object event_source ) 
        throws AnimationParseException
    {
        BeanInfo source_info;
        try {
            source_info = Introspector.getBeanInfo(event_source.getClass());
        }
        catch( IntrospectionException ex ) {
            throw new AnimationParseException( 
                "cannot find info about event source: " + ex.getMessage() );
        }
        
        EventSetDescriptor[] events = source_info.getEventSetDescriptors();
        for( int i = 0; i < events.length; i++ ) {
            Class ev_listener_type = events[i].getListenerType();
            
            if( ev_listener_type.isInstance(listener) ) {
                return events[i];
            }
        }
        
        throw new AnimationParseException( 
            "listener not compatible with event source" );
    }
}
