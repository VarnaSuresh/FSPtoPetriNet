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

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



class BeanFactory
{
    private static class Package
    {
        private ClassLoader _loader;
        private String _package;
        
        public Package( ClassLoader loader, String pkg ) {
            _loader = loader;
            _package = pkg;
        }
        
        public Package( String pkg ) {
            this( ClassLoader.getSystemClassLoader(), pkg );
        }
        
        public Class loadClass( String class_name ) {
            String full_name = _package + "." + class_name;
            try {
                return _loader.loadClass( full_name );
            }
            catch( ClassNotFoundException ex ) {
                return null;
            }
        }
    }
    
    private static class Category
    {
        private String _name;
        private List _packages = new ArrayList();
        private String _prefix;
        private String _postfix;
        private boolean _capitalise;
        
        Category( String name, 
                  String prefix, String postfix, boolean capitalise ) 
        {
            _name = name;
            _prefix = prefix;
            _postfix = postfix;
            _capitalise = capitalise;
        }
        
        public void addPackage( ClassLoader loader, String pkg_name ) {
            _packages.add( new Package( loader, pkg_name ) );
        }
        
        public void addPackage( String pkg_name ) {
            _packages.add( new Package(pkg_name) );
        }
        
        Object newBean( String type )
            throws ClassNotFoundException, 
                   InstantiationException, IllegalAccessException
        {
            String class_name = 
                _prefix + Character.toUpperCase(type.charAt(0)) + 
                type.substring(1) + _postfix;
            
            Iterator i = _packages.iterator();
            while( i.hasNext() ) {
                Class c = ((Package)i.next()).loadClass( class_name );
                if( c != null ) return c.newInstance();
            }
            
            throw new ClassNotFoundException( "no class found for " + _name +
                                              " bean of type \""+type+"\"" );
        }
    }
    
    private Map _categories = new HashMap();
    
    /** Add a new bean category.
     */
    public void addCategory( String name, String prefix, String postfix,
                             boolean capitalise )
    {
        if( _categories.get(name) != null ) {
            throw new IllegalArgumentException( "category name \"" + name + 
                                                "\" already defined" );
        }
        
        _categories.put( name, 
                         new Category( name, prefix, postfix, capitalise ) );
    }
    
    /** Add a package to a bean category.
     */
    public void addPackage( String category, String pkg_name ) {
        getCategory(category).addPackage( pkg_name );
    }
    
    /** Add a package to a bean category.
     */
    public void addPackage( String category, 
                            ClassLoader loader, String pkg_name ) 
    {
        getCategory(category).addPackage( loader, pkg_name );
    }
    
    /** Allocate a new Bean of the given type in the given category.
     */
    public Object newBean( String category, String type )
        throws ClassNotFoundException,
               IllegalAccessException, InstantiationException
    {
        return getCategory(category).newBean(type);
    }
    
    
    private Category getCategory( String name ) {
        Category c = (Category)_categories.get(name);
        
        if( c != null ) {
            return c;
        } else {
            throw new IllegalArgumentException( "no category named \"" +
                                                name + "\"" );
        }
    }
}

