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

import java.awt.Component;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.util.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import com.sun.xml.parser.*;
import com.sun.xml.tree.XmlDocument;
import com.sun.xml.tree.XmlDocumentBuilder;

import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.event.*;
import uk.ac.ic.doc.scenebeans.activity.*;
import uk.ac.ic.doc.scenebeans.behaviour.*;
import uk.ac.ic.doc.scenebeans.animation.*;
import uk.ac.ic.doc.natutil.StringParser;
import uk.ac.ic.doc.natutil.MacroExpander;
import uk.ac.ic.doc.natutil.MacroException;



/** An XMLAnimationParser is responsible for translating an XML document 
 *  into an {@link uk.ac.ic.doc.scenebeans.animation.Animation}.  It can
 *  load the XML document from a file or URL.
 *
 *  @see uk.ac.ic.doc.scenebeans.animation.Animation
 */
public class XMLAnimationParser
{
    private BeanFactory _factory = new BeanFactory();
    private Map _symbol_table = new HashMap(); // Indexed by symbol ID
    private List _behaviour_links = new ArrayList();
    private List _event_links = new ArrayList();
    private MacroExpander _macro_table = new MacroExpander();
    ValueParser _value_parser;
    private URL _doc_url;
    private Component _component;
    private Animation _anim = null; // The animation currently being parsed
    
    private static final String PROPERTY_ACTIVITY_NAME = "activityName";
    private static final String PI_TARGET = "scenebeans";
    private static final String PI_CODEBASE = "codebase";
    private static final String PI_CATEGORY = "category";
    private static final String PI_PACKAGE = "package";
    
    /*  Bean categories and packages
     */
    private static final String CATEGORY_SCENE = "scene";
    private static final String PKG_SCENE = "uk.ac.ic.doc.scenebeans";
    private static final String CATEGORY_BEHAVIOUR = "behaviour";
    private static final String PKG_BEHAVIOUR = 
        "uk.ac.ic.doc.scenebeans.behaviour";
    
    private static interface ForallParser {
        void parse( Element child ) throws AnimationParseException;
    }
    
    /** Constructs an AnimationParser that parses an XML file located
     *  at <var>doc_url</var> and is to be displayed on the Component
     *  <var>view</var>.
     *
     *  @param doc_url
     *      The URL of the XML document to be parsed.
     *  @param view
     *      The Component on which the Animation is to be displayed.
     */
    public XMLAnimationParser( URL doc_url, Component view ) {
        _doc_url = doc_url;
        _value_parser = new ValueParser(doc_url);
        _component = view;
        
        _factory.addCategory( CATEGORY_SCENE, "", "", true );
        _factory.addPackage( CATEGORY_SCENE, PKG_SCENE );
        _factory.addCategory( CATEGORY_BEHAVIOUR, "", "", true );
        _factory.addPackage( CATEGORY_BEHAVIOUR, PKG_BEHAVIOUR );
    }
    
    /** Constructs an AnimationParser that parses an XML file stored in
     *  a file and is to be displayed on the Component <var>view</var>.
     *  
     *  @param file
     *      The file containing the XML document to be parsed.
     *  @param view
     *      The Component on which the Animation is to be displayed.
     */
    public XMLAnimationParser( File file, Component view ) 
        throws MalformedURLException
    {
        this( file.toURL(), view );
    }
    
    /** Returns the URL of the XML document being parsed.
     */
    public URL getDocumentURL() {
        return _doc_url;
    }
    
    /** Returns the Component on which the parsed Animation is to be displayed.
     */
    public Component getViewComponent() {
        return _component;
    }
    
    /** Registers a package in the system class loader to be searched for classes
     *  of scene-graph node.
     *  <p>
     *  Names of scene bean types are translated into class names by capitalising
     *  their first letter and then searching for a class with that name
     *  in the packages registered by the <code>addScenePackage</code> functions.
     *  Packages are searched in order of registration, with the package
     *  <code>uk.ac.ic.doc.scenebeans</code> being searched first.
     *  Packages of beans can also be registered within an XML document using
     *  the <code>&lt;?scenebeans...?&gt;</code> processing instruction.
     *  
     *  @param pkg
     *      The name of the package containing the SceneBean classes.
     */
    public void addScenePackage( String pkg ) {
        _factory.addPackage( CATEGORY_SCENE, pkg );
    }
    
    /** Registers a package in the given class loader to be searched for classes
     *  of scene-graph node.
     *  <p>
     *  Names of scene bean types are translated into class names by capitalising
     *  their first letter and then searching for a class with that name
     *  in the packages registered by the <code>addScenePackage</code> functions.
     *  Packages are searched in order of registration, with the package
     *  <code>uk.ac.ic.doc.scenebeans</code> being searched first.
     *  Packages of beans can also be registered within an XML document using
     *  the <code>&lt;?scenebeans...?&gt;</code> processing instruction.
     *  
     *  @param l
     *      The ClassLoader used to load the package.
     *  @param pkg
     *      The name of the package containing the SceneBean classes.
     */
    public void addScenePackage( ClassLoader l, String pkg ) {
        _factory.addPackage( CATEGORY_SCENE, l, pkg );
    }
    
    /** Registers a package in the given class loader to be searched for classes
     *  of behaviour bean.
     *  <p>
     *  Names of behaviour algorithms are translated into class names by 
     *  capitalising their first letter and then searching for a class with that
     *  name in the packages registered by the <code>addScenePackage</code> 
     *  functions.
     *  Packages are searched in order of registration, with the package
     *  <code>uk.ac.ic.doc.scenebeans</code> being searched first.
     *  Packages of beans can also be registered within an XML document using
     *  the <code>&lt;?scenebeans...?&gt;</code> processing instruction.
     *  
     *  @param pkg
     *      The name of the package containing the SceneBean classes.
     */
    public void addBehaviourPackage( String pkg ) {
        _factory.addPackage( CATEGORY_BEHAVIOUR, pkg );
    }
    
    /** Registers a package in the system class loader to be searched for classes
     *  of behaviour bean.
     *  <p>
     *  Names of behaviour algorithms are translated into class names by 
     *  capitalising their first letter and then searching for a class with that
     *  name in the packages registered by the <code>addScenePackage</code> 
     *  functions.
     *  Packages are searched in order of registration, with the package
     *  <code>uk.ac.ic.doc.scenebeans</code> being searched first.
     *  Packages of beans can also be registered within an XML document using
     *  the <code>&lt;?scenebeans...?&gt;</code> processing instruction.
     *  
     *  @param l
     *      The ClassLoader used to load the package.
     *  @param pkg
     *      The name of the package containing the SceneBean classes.
     */
    public void addBehaviourPackage( ClassLoader l, String pkg ) {
        _factory.addPackage( CATEGORY_BEHAVIOUR, pkg );
    }
    
    
    /** Parses the data identified by the URL passed to the parser's
     *  constructor into a DOM document model and then translates that
     *  document model into a Animation.
     *
     *  @exception java.lang.IOException
     *      An I/O error occurred while reading the XML document.
     * @exception uk.ac.ic.doc.scenebeans.animation.parse.AnimationParseException
     *      The XML file contained invalid information.  It may, for example,
     *      be malformed or contain elements or attributes not understood by
     *      the parser.
     */
    public Animation parseAnimation() 
        throws IOException, AnimationParseException 
    {
        try {
            InputSource in = Resolver.createInputSource( _doc_url, false );
            com.sun.xml.parser.Parser sax_parser = 
                new com.sun.xml.parser.Parser();
            XmlDocumentBuilder doc_builder = new XmlDocumentBuilder();
            
            sax_parser.setDocumentHandler( doc_builder );
            sax_parser.parse(in);
            
            XmlDocument doc = doc_builder.getDocument();
            doc.getDocumentElement().normalize();
            
            return translateDocument(doc);
        }
        catch( SAXParseException ex ) {
            throw new AnimationParseException(
                ex.getSystemId() + " line  " + ex.getLineNumber() +
                ": " + ex.getMessage() );
        }
        catch( SAXException ex ) {
            throw new AnimationParseException( "failed to parse XML: " + 
                                               ex.getMessage() );
        }
    }
    
    
    Animation translateDocument( Document doc )
        throws AnimationParseException
    {
        translateProcessingInstructions( doc );
        
        Element e = doc.getDocumentElement();
        if( !e.getTagName().equals(Tag.ANIMATION) ) {
            throw new AnimationParseException("invalid document type");
        }
        
        _anim = new Animation();
        
        setAnimationDimensions( e, _anim );        
        NodeList nodes = e.getChildNodes();
        for( int i = 0; i < nodes.getLength(); i++ ) {
            Node node = nodes.item(i);
            if( node instanceof Element ) {
                translateElement( (Element)node );
            }
        }
        
        /*  Return the Animation to the caller, marking the currently parsed
         *  sprite as null to indicate the end of parsing.
         */
        Animation anim = _anim;
        _anim = null;
        return anim;
    }
    
    void translateProcessingInstructions( Document d ) 
        throws AnimationParseException
    {
        NodeList nodes = d.getChildNodes();
        for( int i = 0; i < nodes.getLength(); i++ ) {
            Node node = nodes.item(i);
            if( node instanceof ProcessingInstruction ) {
                translateProcessingInstruction( (ProcessingInstruction)node );
            }
        }
    }
    
    void translateProcessingInstruction( ProcessingInstruction pi )
        throws AnimationParseException
    {
        if( !pi.getTarget().equals(PI_TARGET) ) return;
        
        String codebase = null, category = null, pkg_name = null;
        
        try {
            PushbackReader r = 
                new PushbackReader(new StringReader(pi.getData()));
            
            while( trim(r) ) {
                String tag = parseTag(r);
                String value = parseValue(r);
                
                if( tag.equals(PI_CODEBASE) ) {
                    codebase = value;
                } else if( tag.equals(PI_CATEGORY) ) {
                    category = value;
                } else if( tag.equals(PI_PACKAGE) ) {
                    pkg_name = value;
                } else {
                    throw new AnimationParseException(
                        "unknown element \"" + tag + 
                        "\" in processing instruction" );
                }
            }
        }
        catch( IOException ex ) {
            throw new AnimationParseException( 
              "failed to parse processing instruction: " + ex.getMessage() );
        }
        
        if( category == null ) {
            throw new AnimationParseException( 
              "category not specified in processing instruction" );
        } else if( pkg_name == null ) {
            throw new AnimationParseException( 
              "package not specified in processing instruction" );
        }
        
        if( codebase == null ) {
            _factory.addPackage( category, pkg_name );
        } else {
            try {
                ClassLoader loader = new URLClassLoader( new URL[] {
                    new URL( _doc_url, codebase )
                } );
                
                _factory.addPackage( category, loader, pkg_name );
            }
            catch( MalformedURLException ex ) {
                throw new AnimationParseException( 
                    "malformed URL in codebase of processing instruction: " +
                    ex.getMessage() );
            }
        }
    }
    
    private String parseTag( PushbackReader r ) 
        throws AnimationParseException, IOException
    {
        StringBuffer buf = new StringBuffer();
        int ch;
        
        for(;;) {
            ch = r.read();
            if( ch == -1 ) {
                throw new AnimationParseException(
                    "malformed processing instruction" );
            } else if( ch == '=' ) {
                return buf.toString();
            } else {
                buf.append( (char)ch );
            }
        }
    }
    
    private String parseValue( PushbackReader r ) 
        throws AnimationParseException, IOException
    {
        expect( r, '\"' );
        
        StringBuffer buf = new StringBuffer();
        int ch;
        for(;;) {
            ch = r.read();
            if( ch == -1 ) {
                throw new AnimationParseException(
                    "malformed processing instruction" );
            } else if( ch == '\"' ) {
                return buf.toString();
            } else {
                buf.append( (char)ch );
            }
        }
    }
    
    private boolean trim( PushbackReader r ) 
        throws IOException 
    {
        int ch;
        
        do {
            ch = r.read();
            if( ch == -1 ) return false;
        } while( Character.isWhitespace( (char)ch ) );
        
        r.unread( ch );
        return true;
    }
    
    private void expect( PushbackReader r, char expected_ch ) 
        throws AnimationParseException, IOException
    {
        int ch = r.read();
        if( ch != expected_ch ) {
            throw new AnimationParseException(
                "malformed processing exception" );
        }
    }
    
    private void setAnimationDimensions( Element e, Animation s )
        throws AnimationParseException
    {
        try {
            String width_str = getOptionalAttribute( e, Attr.WIDTH );
            String height_str = getOptionalAttribute( e, Attr.HEIGHT );
            
            _anim.setWidth( ExprUtil.evaluate(width_str) );
            _anim.setHeight( ExprUtil.evaluate(height_str) );
        }
        catch( NumberFormatException ex ) {
            throw new AnimationParseException( "invalid dimension: " + 
                                            ex.getMessage() );
        }
    }
    
    void translateElement( Element elt )
        throws AnimationParseException
    {
        String type = elt.getTagName();
        
        if( type.equals(Tag.BEHAVIOUR) ) {
            translateBehaviour(elt);
            
        } else if( type.equals(Tag.SEQ) ) {
            translateSeq(elt);
            
        } else if( type.equals(Tag.CO) ) {
            translateCo(elt);
            
        } else if( type.equals(Tag.COMMAND) ) {
            translateCommand(elt);
            
        } else if( type.equals(Tag.EVENT) ) {
            translateEvent(elt);
            
        } else if( type.equals(Tag.DEFINE) ) {
            translateDefine(elt);
            
        } else if( type.equals(Tag.DRAW) ) {
            translateDraw(elt);
            
        } else if( type.equals(Tag.FORALL) ) {
            parseForall( elt, new ForallParser() {
                public void parse( Element e ) throws AnimationParseException {
                    translateElement(e);
                }
            } );
            
        } else {
            throw new AnimationParseException( "invalid element \"" + 
                                               type + "\"" );
        }
    }
    
    
    
    /*-----------------------------------------------------------------------
     *  Forall: macro definition and iteration
     */
    
    void parseForall( Element elt, ForallParser child_parser )
        throws AnimationParseException
    {
        String var = getRequiredAttribute( elt, Attr.VAR );
        String values = getRequiredAttribute( elt, Attr.VALUES );
        String sep = getOptionalAttribute( elt, Attr.SEP );
        
        if( sep == null ) {
            sep = " \n\t";
        }
        
        StringTokenizer tok = new StringTokenizer( values, sep );
        while( tok.hasMoreTokens() ) {
            addMacro( var, tok.nextToken() );
            
            NodeList children = elt.getChildNodes();
            for( int i = 0; i < children.getLength(); i++ ) {
                Node child_node = children.item(i);
                if( child_node instanceof Element ) {
                    Element child_elt = (Element)child_node;
                    if( child_elt.getTagName().equals(Tag.FORALL) ) {
                        parseForall( child_elt, child_parser );
                    } else {
                        child_parser.parse( child_elt );
                    }
                }
            }
            
            removeMacro(var);
        }
    }
    
    
    /*-----------------------------------------------------------------------
     *  Behaviour
     */
    
    void translateBehaviour( Element elt )
        throws AnimationParseException
    {
        Object behaviour = createBehaviour(elt);
        
        if( behaviour instanceof Activity ) {
            optionalStartActivity( elt, (Activity)behaviour );
        }
    }
    
    void translateSeq( Element elt )
        throws AnimationParseException
    {
        SequentialActivity seq = createSequentialActivity(elt);
        optionalStartActivity( elt, seq );
    }
    
    void translateCo( Element elt ) 
        throws AnimationParseException
    {
        ConcurrentActivity co = createConcurrentActivity(elt);
        optionalStartActivity( elt, co );
    }
    
    SequentialActivity createSequentialActivity( Element elt )
        throws AnimationParseException
    {
        SequentialActivity seq = new SequentialActivity();
        
        String event_name = getOptionalAttribute( elt, Attr.EVENT );
        if( event_name != null ) seq.setActivityName(event_name);
        
        createSubActivities( seq, elt );
        putOptionalSymbol( elt, seq );
        return seq;
    }
    
    ConcurrentActivity createConcurrentActivity( Element elt )
        throws AnimationParseException
    {
        ConcurrentActivity co = new ConcurrentActivity();
        
        String event_name = getOptionalAttribute( elt, Attr.EVENT );
        if( event_name != null ) co.setActivityName(event_name);
        
        createSubActivities( co, elt );
        putOptionalSymbol( elt, co );
        return co;
    }
    
    
    void createSubActivities( CompositeActivity ca, Element elt )
        throws AnimationParseException
    {
        NodeList children = elt.getChildNodes();
        for( int i = 0; i < children.getLength(); i++ ) {
            Node child_node = children.item(i);
            if( child_node instanceof Element ) {
                createSubActivity( ca, (Element)child_node );
            }
        }
    }
    
    void createSubActivity( final CompositeActivity ca, Element elt )
      throws AnimationParseException
    {
        String elt_type = elt.getTagName();
        
        if( elt_type.equals(Tag.FORALL) ) {
            parseForall( elt, new ForallParser() {
                public void parse( Element e ) throws AnimationParseException {
                    createSubActivity( ca, e );
                }
            } );
            
        } else if( elt_type.equals(Tag.BEHAVIOUR) ) {
            Object behaviour = createBehaviour(elt);
            
            if( behaviour instanceof Activity &&
                ((Activity)behaviour).isFinite() )
            {
                ca.addActivity( (Activity)behaviour );
            } else {
                throw new AnimationParseException(
                    elt.getTagName() +
                    " elements can only contain finite behaviours" );
            }
            
        } else if( elt_type.equals(Tag.CO) ) {
            ca.addActivity( createConcurrentActivity(elt) );
            
        } else if( elt_type.equals(Tag.SEQ) ) {
            ca.addActivity( createSequentialActivity(elt) );
            
        } else {
            throw new AnimationParseException( "invalid element " + elt_type );
        }
    }
    
    Object createBehaviour( Element elt )
        throws AnimationParseException
    {
        try {
            String algorithm = getRequiredAttribute( elt, Attr.ALGORITHM );
            String event = getOptionalAttribute( elt, Attr.EVENT );
            
            Object behaviour = 
                _factory.newBean( CATEGORY_BEHAVIOUR, algorithm );
            BeanInfo behaviour_info = BeanUtil.getBeanInfo(behaviour);
            
            if( event != null ) {
                if( behaviour instanceof Activity ) {
                    BeanUtil.setProperty( behaviour, behaviour_info,
                                          PROPERTY_ACTIVITY_NAME, event,
                                          _value_parser );
                    
                } else {
                    throw new AnimationParseException(
                        "only activities report completion events" );
                }
            }
            
            initialiseParameters( behaviour, behaviour_info, elt );
            putOptionalSymbol( elt, behaviour );
            return behaviour;
        }
        catch( RuntimeException ex ) {
            throw ex;
        }
        catch( Exception ex ) {
            throw new AnimationParseException( "could not create behaviour: " +
                                               ex.getMessage() );
        }
    }
    
    void optionalStartActivity( Element elt, Activity a ) 
        throws AnimationParseException
    {
        String state = getOptionalAttribute( elt, Attr.STATE );
        
        if( (state != null) && state.equals(Value.STARTED) ) {
            _anim.addActivity(a);
        }
    }
    
    /*-----------------------------------------------------------------------
     *  Commands and events
     */
    
    void translateCommand( Element elt )
        throws AnimationParseException
    {
        String name = getRequiredAttribute( elt, Attr.NAME );
        
        if( _anim.getCommand(name) != null ) {
            throw new AnimationParseException( "a command named \"" + name +
                                            "\" has already been defined" );
        }
        
        Command cmd = createCompositeCommand(elt);
        _anim.addCommand( name, cmd );
    }
    
    void translateEvent( Element elt )
        throws AnimationParseException
    {
        String object_id = getRequiredAttribute( elt, Attr.OBJECT );
        String event_name = getRequiredAttribute( elt, Attr.EVENT );
        
        Object bean = getSymbol(object_id);
        Command cmd = createCompositeCommand(elt);
        
        EventInvoker invoker = new EventInvoker( event_name, cmd );
        BeanUtil.bindEventListener( invoker, bean );
        _event_links.add( new EventLink( bean, object_id, invoker ) );
    }
    
    
    Command createCompositeCommand( Element elt ) 
        throws AnimationParseException
    {
        NodeList sub_cmds = elt.getChildNodes();
        final CompositeCommand cmd = new CompositeCommand();
        
        if( sub_cmds.getLength() == 0 ) {
            throw new AnimationParseException( "empty command body" );
        }
        
        for( int i = 0; i < sub_cmds.getLength(); i++ ) {
            Node child_node = sub_cmds.item(i);
            if( child_node instanceof Element ) {
                Element child_elt = (Element)child_node;
                if( child_elt.getTagName().equals(Tag.FORALL) ) {
                    parseForall( child_elt, new ForallParser() {
                        public void parse( Element e ) 
                            throws AnimationParseException
                        {
                            cmd.addCommand( createSubCommand(e) );
                        }
                    } );
                } else {
                    cmd.addCommand( createSubCommand( child_elt ) );
                }
            }
        }
        
        if( cmd.getCommandCount() == 1 ) {
            return cmd.getCommand(0);
        } else {
            return cmd;
        }
    }
    
    Command createSubCommand( Element elt )
        throws AnimationParseException
    {
        String tag = elt.getTagName();
        
        if( tag.equals(Tag.START) ) {
            return createStartCommand( elt );
            
        } else if( tag.equals(Tag.STOP) ) {
            return createStopCommand( elt );
            
        } else if( tag.equals(Tag.RESET) ) {
            return createResetCommand( elt );
            
        } else if( tag.equals(Tag.SET) ) {
            return createSetCommand( elt );
            
        } else if( tag.equals(Tag.INVOKE) ) {
            return createInvokeCommand( elt );
            
        } else if( tag.equals(Tag.ANNOUNCE) ) {
            return createAnnounceCommand( elt );
            
        } else {
            throw new AnimationParseException( "unexpected element type \"" +
                                            tag + "\"" );
        }
    }
    
    Command createStartCommand( Element elt )
        throws AnimationParseException
    {
        String name = getRequiredAttribute( elt, Attr.BEHAVIOUR );
        Object bean = getSymbol(name);
        
        if( bean instanceof Activity ) {
            Activity activity = (Activity)bean;
            ActivityRunner runner = activity.getActivityRunner();
            if( runner == null ) runner = _anim;
            
            return new StartActivityCommand( activity, runner );
            
        } else {
            throw new AnimationParseException(
               "symbol \"" + name + "\" does not refer to an activity" );
        }
    }
    
    Command createStopCommand( Element elt )
        throws AnimationParseException
    {
        String name = getRequiredAttribute( elt, Attr.BEHAVIOUR );
        Object bean = getSymbol(name);
        
        if( bean instanceof Activity ) {
            return new StopActivityCommand( (Activity)bean );
            
        } else {
            throw new AnimationParseException( 
               "symbol \"" + name + "\" does not refer to an activity" );
        }
    }
    
    Command createResetCommand( Element elt )
        throws AnimationParseException
    {
        String name = getRequiredAttribute( elt, Attr.BEHAVIOUR );
        Object bean = getSymbol(name);
        
        if( bean instanceof Activity ) {
            return new ResetActivityCommand( (Activity)bean );
            
        } else {
            throw new AnimationParseException( 
               "symbol \"" + name + "\" does not refer to an activity" );
        }
    }
    
    Command createSetCommand( Element elt )
        throws AnimationParseException
    {
        String symbol = getRequiredAttribute( elt, Attr.OBJECT );
        String param_str = getRequiredAttribute( elt, Attr.PARAM );
        String value_str = getRequiredAttribute( elt, Attr.VALUE );
        
        Object bean = getSymbol(symbol);
        BeanInfo info = BeanUtil.getBeanInfo(bean);
        PropertyDescriptor pd = BeanUtil.getPropertyDescriptor( info, 
                                                                param_str );
        
        Object value = _value_parser.newObject(pd.getPropertyType(), value_str);
        Method set_method = pd.getWriteMethod();
        
        return new SetParameterCommand( bean, set_method, value );
    }
    
    Command createInvokeCommand( Element elt )
        throws AnimationParseException
    {
        String cmd_name = getRequiredAttribute( elt, Attr.COMMAND );
        String obj_name = getOptionalAttribute( elt, Attr.OBJECT );
        
        Animation anim;
        if( obj_name == null ) {
            anim = _anim;
        } else {
            Object sym = getSymbol(obj_name);
            if( sym instanceof Animation ) {
                anim = (Animation)sym;
            } else {
                throw new AnimationParseException(
                    "symbol \"" + obj_name + 
                    "\" does not refer to an animation" );
            }
        }
        
        Command cmd = anim.getCommand(cmd_name);
        if( cmd != null ) {
            return cmd;
        } else {
            throw new AnimationParseException( 
                "command \"" + cmd_name + "\" not supported by animation" );
        }
    }
    
    Command createAnnounceCommand( Element elt )
        throws AnimationParseException
    {
        String event_name = getRequiredAttribute( elt, Attr.EVENT );
        _anim.addEventName(event_name);
        return new AnnounceCommand( _anim, event_name );
    }
    
    
    
    /*-----------------------------------------------------------------------
     *  Graphical elements
     */
    
    void translateDefine( Element elt )
        throws AnimationParseException
    {
        SceneGraph sg = createDrawNode(elt);
        putOptionalSymbol( elt, sg );
    }
    
    void translateDraw( Element elt ) 
        throws AnimationParseException
    {
        SceneGraph sg = createDrawNode(elt);
        putOptionalSymbol( elt, sg );
        _anim.addSubgraph(sg);
    }
    
    SceneGraph createDrawNode( Element elt )
        throws AnimationParseException
    {
        return minimise( createChildren(elt) );
    }
    
    SceneGraph createSceneGraph( Element elt )
        throws AnimationParseException
    {
        String elt_type = elt.getTagName();
        SceneGraph sg = null;
        
        if( elt_type.equals(Tag.DRAW) ) {
            sg = createDrawNode(elt);
        } else if( elt_type.equals(Tag.TRANSFORM) ) {
            sg = createTransformNode(elt);
        } else if( elt_type.equals(Tag.STYLE) ) {
            sg = createStyleNode(elt);
        } else if( elt_type.equals(Tag.INPUT) ) {
            sg = createInputNode(elt);
        } else if( elt_type.equals(Tag.COMPOSE) ) {
            sg = createComposeNode(elt);
        } else if( elt_type.equals(Tag.INST) ) {
            sg = createInstNode(elt);
        } else if( elt_type.equals(Tag.INCLUDE) ) {
            sg = createIncludeNode(elt);
        } else if( elt_type.equals(Tag.PRIMITIVE) ) {
            sg = createPrimitiveNode(elt);
        } else {
            throw new AnimationParseException( "unknown scene-graph type \"" +
                                               elt_type + "\"" );
        }
        
        //putOptionalSymbol( elt, sg );
        
        return sg;
    }
    
    Layered createChildren( Element elt ) 
        throws AnimationParseException
    {
        Layered layers = new Layered();
        createChildren( layers, elt );
        return layers;
    }
    
    CompositeNode createChildren( final CompositeNode composite, Element elt )
        throws AnimationParseException
    {
        NodeList nodes = elt.getChildNodes();
        
        for( int i = 0; i < nodes.getLength(); i++ ) {
            Node node = nodes.item(i);
            if( node instanceof Element ) {
                Element child = (Element)nodes.item(i);
                String child_type = child.getTagName();
                
                if( child_type.equals(Tag.FORALL) ) {
                    parseForall( child, new ForallParser() {
                        public void parse( Element e ) 
                            throws AnimationParseException 
                        {
                            composite.addSubgraph( createSceneGraph(e) );
                        }
                    } );
                    
                } else if( !(child_type.equals(Tag.PARAM) ||
                             child_type.equals(Tag.ANIMATE)) )
                {
                    composite.addSubgraph( createSceneGraph(child) );
                }
            }
        }
        
        if( composite.getSubgraphCount() == 0 ) {
            throw new AnimationParseException( "no layers in composite" );
        }
        
        return composite;
    }
    
    SceneGraph minimise( CompositeNode composite )
        throws AnimationParseException
    {
        if( composite.getSubgraphCount() == 0 ) {
            throw new AnimationParseException( "no layers in composite" );
            
        } else if( composite.getSubgraphCount() == 1 ) {
            return composite.getSubgraph(0);
            
        } else {
            return composite;
        }
    }
    
    SceneGraph createTransformNode( Element elt )
        throws AnimationParseException
    {
        String type = getRequiredAttribute( elt, Attr.TYPE );
        Transform bean;
        try {
            bean = (Transform)newSceneBean(type);
        }
        catch( ClassCastException ex ) {
            throw new AnimationParseException(type+" is not a transform node");
        }
        
        putOptionalSymbol( elt, bean );
        
        SceneGraph sg = minimise( createChildren(elt) );
        bean.setTransformedGraph(sg);
        initialiseParameters( bean, BeanUtil.getBeanInfo(bean), elt );
        return bean;
    }
    
    SceneGraph createStyleNode( Element elt )
        throws AnimationParseException
    {
        String type = getRequiredAttribute( elt, Attr.TYPE );
        Style bean;
        try {
            bean = (Style)newSceneBean(type);
        }
        catch( ClassCastException ex ) {
            throw new AnimationParseException(type+" is not a style node" );
        }
        
        putOptionalSymbol( elt, bean );
        
        SceneGraph sg = minimise( createChildren(elt) );
        bean.setStyledGraph(sg);
        initialiseParameters( bean, BeanUtil.getBeanInfo(bean), elt );
        return bean;
    }
    
    SceneGraph createInputNode( Element elt )
        throws AnimationParseException
    {
        String type = getRequiredAttribute( elt, Attr.TYPE );
        Input bean;
        try {
            bean = (Input)newSceneBean(type);
        }
        catch( ClassCastException ex ) {
            throw new AnimationParseException(type+" is not an input node");
        }
        
        putOptionalSymbol( elt, bean );
        
        SceneGraph sg = minimise( createChildren(elt) );
        bean.setSensitiveGraph(sg);
        initialiseParameters( bean, BeanUtil.getBeanInfo(bean), elt );
        return bean;
    }
    
    SceneGraph createComposeNode( Element elt )
        throws AnimationParseException
    {
        String type = getRequiredAttribute( elt, Attr.TYPE );
        CompositeNode comp;
        try {
            comp = (CompositeNode)newSceneBean(type);
        }
        catch( ClassCastException ex ) {
            throw new AnimationParseException(type+" is not a composite node");
        }
        
        putOptionalSymbol( elt, comp );
        
        createChildren( comp, elt );
        initialiseParameters( comp, BeanUtil.getBeanInfo(comp), elt );
        return comp;
    }
    
    SceneGraph createInstNode( Element elt )
        throws AnimationParseException
    {
        String src = getRequiredAttribute( elt, Attr.OBJECT );
        Object link = getSymbol(src);
        if( link instanceof SceneGraph ) {
            return (SceneGraph)link;
        } else {
            throw new AnimationParseException( "link target \"" + src + 
                                            "\" does not refer to a " +
                                            "scene-graph node" );
        }
    }
    
    SceneGraph createIncludeNode( Element elt )
        throws AnimationParseException
    {
        String src_str = getRequiredAttribute( elt, Attr.SRC );
        
        try {
            URL inc_url = new URL( _doc_url, src_str );
            
            XMLAnimationParser inc_parser = 
                new XMLAnimationParser( inc_url, getViewComponent() );
            
            /*  Add all macros to the parser
             */
            NodeList params = elt.getChildNodes();
            for( int i = 0; i < params.getLength(); i++ ) {
                Node node = params.item(i);
                if( node instanceof Element ) {
                    Element e = (Element)params.item(i);
                    
                    if( !e.getTagName().equals(Tag.PARAM) ) {
                        throw new AnimationParseException(
                            "only " + Tag.PARAM + " tags are allowed in a " +
                            elt.getTagName() + " node" );
                    }
                    
                    String name = getRequiredAttribute( e, Attr.NAME );
                    String value = getRequiredAttribute( e, Attr.VALUE );
                    
                    inc_parser.addMacro( name, value );
                }
            }
            
            /*  Parse the Animation
             */
            Animation inc_anim = inc_parser.parseAnimation();
            
            /*  Embed the included Animation 
             */
            _anim.addActivity(inc_anim);
            
            putOptionalSymbol( elt, inc_anim );
            
            return inc_anim;
        }
        catch( MalformedURLException ex ) {
            throw new AnimationParseException( "invalid URL " + src_str +
                                            ": " + ex.getMessage() );
        }
        catch( IOException ex ) {
            throw new AnimationParseException( "failed to include animation " 
                                            + src_str + ": " + 
                                            ex.getMessage() );
        }
    }
    
    
    SceneGraph createPrimitiveNode( Element elt )
        throws AnimationParseException
    {
        String type = getRequiredAttribute( elt, Attr.TYPE );
        String drawn = getOptionalAttribute( elt, Attr.DRAWN );
        
        Object bean = newSceneBean(type);
        if( !(bean instanceof SceneGraph) ) {
            throw new AnimationParseException( "type \"" + type + 
                                            "\" is not a SceneGraph class" );
        }
        
        BeanInfo info = BeanUtil.getBeanInfo(bean);
        
        initialiseParameters( bean, info, elt );
        
        putOptionalSymbol( elt, bean );
        
        return (SceneGraph)bean;
    }
    
    Object newSceneBean( String type ) 
        throws AnimationParseException
    {
        try {
            return _factory.newBean( CATEGORY_SCENE, type );
        }
        catch( Exception ex ) {
            throw new AnimationParseException( 
                "failed to create scene bean: " + ex.getMessage() );
        }
    }
    
    
    /*-----------------------------------------------------------------------
     *  Methods for processing the "param" and "animate" elements that are
     *  contained in many different elements.
     */
    
    void initialiseParameters( Object bean, Element bean_elt )
        throws AnimationParseException
    {
        initialiseParameters( bean, BeanUtil.getBeanInfo(bean), bean_elt );
    }
    
    void initialiseParameters( Object bean, BeanInfo info, Element bean_elt )
        throws AnimationParseException
    {
        NodeList params = bean_elt.getChildNodes();
        for( int i = 0; i < params.getLength(); i++ ) {
            Node node = params.item(i);
            if( node instanceof Element ) {
                initialiseParameter( bean, info, (Element)node );
            }
        }
    }
    
    void initialiseParameter( final Object bean, final BeanInfo info,
                                     final Element param_elt )
        throws AnimationParseException
    {
        if( param_elt.getTagName().equals(Tag.PARAM) ) {
            setParameter( bean, info, param_elt );
        } else if( param_elt.getTagName().equals(Tag.ANIMATE) ) {
            animateParameter( bean, param_elt );
        } else if( param_elt.getTagName().equals(Tag.FORALL) ) {
            parseForall( param_elt, new ForallParser() {
                public void parse( Element e ) throws AnimationParseException {
                    initialiseParameter( bean, info, e );
                }
            } );
        }
    }
    
    /** Sets a single parameter of the object 'bean' from the attributes
     *  of the "param" element 'param_elt'.
     */
    void setParameter( Object bean, BeanInfo info, Element param_elt )
        throws AnimationParseException
    {
        String param_name = getRequiredAttribute( param_elt, Attr.NAME);
        String index_str = getOptionalAttribute( param_elt, Attr.INDEX );
        String value_str = getRequiredAttribute( param_elt, Attr.VALUE);
        
        if( index_str == null ) {
            BeanUtil.setProperty( bean, info, param_name, value_str, 
                                  _value_parser );
        } else {
            int index;
            try {
                index = (int)Math.floor( ExprUtil.evaluate(index_str) );
            }
            catch( IllegalArgumentException ex ) {
                throw new AnimationParseException( "invalid property index: " +
                                                   ex.getMessage() );
            }
            
            BeanUtil.setIndexedProperty( bean, info, param_name, index, 
                                         value_str, _value_parser );
        }
    }
    
    /** Binds a behaviour to a parameter so that the parameter's value is
     *  updated whenever the behaviour is simulated.  A single behaviour
     *  can update multiple parameters and a single parameter can have
     *  multiple behaviours, although the effect is undefined if more
     *  than one of those behaviours is being simulated at the same time.
     */
    void animateParameter( Object bean, Element anim_elt )
        throws AnimationParseException
    {
        String param_name = getRequiredAttribute( anim_elt, Attr.PARAM);
        String index_str = getOptionalAttribute( anim_elt, Attr.INDEX );
        String behaviour_id = getRequiredAttribute( anim_elt, Attr.BEHAVIOUR);
        String facet_id = getOptionalAttribute( anim_elt, Attr.FACET );
        Object behaviour;
        Object facet;
        Object behaviour_listener;
        
        if( index_str == null ) {
            behaviour_listener = newBehaviourAdapter( bean, param_name );
        } else {
            int index;
            try {
                index = Integer.parseInt(index_str);
            }
            catch( NumberFormatException ex ) {
                throw new AnimationParseException( "invalid property index: " +
                                                   ex.getMessage() );
            }
            
            behaviour_listener =
                newIndexedBehaviourAdapter( bean, param_name, index );
        }
        
        behaviour = getSymbol(behaviour_id);
        if( facet_id != null ) {
            facet_id += "Facet";
            facet = BeanUtil.getProperty( behaviour, facet_id );
        } else {
            facet = behaviour;
        }
        
        BeanUtil.bindEventListener( behaviour_listener, facet );
        
        _behaviour_links.add( new BehaviourLink( behaviour, behaviour_id,
                                                 facet, facet_id,
                                                 bean, behaviour_listener,
                                                 param_name ) );
    }
    
    Object newBehaviourAdapter( Object bean, String param_name )
        throws AnimationParseException
    {
        Class bean_class = bean.getClass();
        String method_name = adapterMethodName(param_name);
        
        try {
            Method method = bean_class.getMethod( method_name, new Class[0] );
            return method.invoke( bean, new Object[0] );
        }
        catch( Exception ex ) {
            throw new AnimationParseException( 
                "could not create adapter for parameter \"" + 
                param_name + "\": " + ex.getMessage() );
        }
    }
    
    Object newIndexedBehaviourAdapter( Object bean, 
                                       String param_name, 
                                       int idx )
        throws AnimationParseException
    {
        Class bean_class = bean.getClass();
        String method_name = 
            "new" + Character.toUpperCase(param_name.charAt(0)) +
            param_name.substring(1) + "Adapter";
        
        try {
            Method method = bean_class.getMethod( method_name,
                                                  new Class[]{Integer.TYPE} );
            return method.invoke( bean, new Object[]{new Integer(idx)} );
        }
        catch( Exception ex ) {
            throw new AnimationParseException( 
                "could not create adapter for parameter \"" + 
                param_name + "\": " + ex.getMessage() );
        }
    }
    
    String adapterMethodName( String param_name )
        throws AnimationParseException
    {
        return "new" + Character.toUpperCase(param_name.charAt(0)) +
            param_name.substring(1) + "Adapter";
    }
    
    
    /*-----------------------------------------------------------------------
     *  General utility methods to access parsing state
     */
    
    void putOptionalSymbol( Element elt, Object bean )
        throws AnimationParseException
    {
        String symbol = getOptionalAttribute( elt, Attr.ID );
        if( symbol != null ) putSymbol( symbol, bean );
    }
    
    
    /** Put a named bean into the symbol table.  This allows an application
     *  to make its user-interface elements and other objects available as
     *  behaviours to XML documents.
     *
     *  @param symbol
     *      The name of the symbol.
     *  @param bean
     *      The value of the symbol.
     * @exception uk.ac.ic.doc.scenebeans.animation.parse.AnimationParseException
     *      The name of the symbol is already defined in the symbol table.
     */
    public void putSymbol( String symbol, Object bean ) 
        throws AnimationParseException
    {
        if( _symbol_table.containsKey(symbol) ) {
            throw new AnimationParseException(
                "duplicate definition of symbol \"" + symbol + "\"" );
        }
        
        _symbol_table.put( symbol, bean );
    }
    
    /** Looks up an object (scene-graph node or behaviour) in the symbol
     *  table, indexed by it's XML name (given by the the <code>id</code> tag
     *  attribute.
     *
     *  @param symbol
     *      The name of the symbol.
     *  @return
     *      The value of the symbol.
     * @exception uk.ac.ic.doc.scenebeans.animation.parse.AnimationParseException
     *      The symbol is not defined in the symbol table.
     */
    public Object getSymbol( String symbol )
        throws AnimationParseException
    {
        if( _symbol_table.containsKey(symbol) ) {
            return _symbol_table.get(symbol);
        } else {
            throw new AnimationParseException(
                "symbol \"" + symbol + "\" has not been defined" );
        }
    }
    
    /** Returns an immutable view of the symbol table.
     *
     *  @return
     *      An immutable map, indexed by string symbol name.
     */
    public Map getSymbols() {
        return Collections.unmodifiableMap(_symbol_table);
    }
    
    /** Returns an immutable view of thelinks between behaviours and 
     *  animated beans.
     *
     *  @return
     *      A Collection of 
     *      {@link uk.ac.ic.doc.scenebeans.animation.parse.BehaviourLink}
     *      objects.
     */
    public Collection getBehaviourLinks() {
        return Collections.unmodifiableList(_behaviour_links);
    }
    
    
    /** Returns an immutable view of the links between event sources and
     *  commands invoked in response to events from those sources.
     *  
     *  @return
     *      A Collection of 
     *      {@link uk.ac.ic.doc.scenebeans.animation.parse.EventLink} objects.
     */
    public Collection getEventLinks() {
        return Collections.unmodifiableList(_event_links);
    }
    
    
    String getRequiredAttribute( Element e, String attr ) 
        throws AnimationParseException
    {
        try {
            String s =  XMLUtil.getRequiredAttribute( e, attr );
            return _macro_table.expandMacros(s);
        }
        catch( MacroException ex ) {
            throw new AnimationParseException(ex.getMessage());
        }
    }
    
    String getOptionalAttribute( Element e, String attr )
        throws AnimationParseException
    {
        try {
            String s = XMLUtil.getOptionalAttribute( e, attr );
            if( s != null ) s = _macro_table.expandMacros(s);
            return s;
        }
        catch( MacroException ex ) {
            throw new AnimationParseException(ex.getMessage());
        }
    }
    
    /** Adds a macro to the parser.  XML attribute values are macro-expanded
     *  before the parser uses them to translate the document into an Animation.
     *  Macros allow Animation documents to be parameterised and paramaters 
     *  to be passed to the parser from user input or the command line.
     *  <p>
     *  Macro expansion is <em>textual</em>:  it does not take the syntactic
     *  structure (such as expression syntax) of the expanded string into 
     *  account.  Be careful when expanding macros in strings with a syntactic
     *  structure; you may, for example, need to enclose macros in brackes 
     *  inside expressions.
     *  
     *  @param name
     *      The name of the macro.
     *  @param value
     *      The value of the macro.
     */
    public void addMacro( String name, String value ) 
        throws AnimationParseException 
    {
        try {
            _macro_table.addMacro( name, value );
        }
        catch( MacroException ex ) {
            throw new AnimationParseException( ex.getMessage() );
        }
    }
    
    
    /** Removes a macro from the parser.
     *
     *  @param name
     *      The name of the macro to remove.
     */
    public void removeMacro( String name ) {
        _macro_table.removeMacro(name);
    }
}
