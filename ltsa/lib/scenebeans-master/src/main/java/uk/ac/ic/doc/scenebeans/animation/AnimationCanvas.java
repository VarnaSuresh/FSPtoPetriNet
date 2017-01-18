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






package uk.ac.ic.doc.scenebeans.animation;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import uk.ac.ic.doc.scenebeans.*;
import uk.ac.ic.doc.scenebeans.event.*;
import uk.ac.ic.doc.scenebeans.bounds.*;

/** An AWT Component that can display and animate 
 *  {@link uk.ac.ic.doc.scenebeans.animation.Animation}s.
 */
public class AnimationCanvas extends Canvas
{
    private Animation _animation = null; // Animation being displayed
    private WindowTransform _root = new WindowTransform();
    private RenderingHints _hints = null;
    private Image _backbuffer;
    private Thread _runner;
    private long _frame_delay = 25;
    private double _time_warp = 1.0;
    private boolean _pause = false, _paused = false;
    private boolean _is_timing_adaptive = false;
    private boolean _is_update_pending = false;
    
    /** Constructs an AnimationCanvas.
     */
    public AnimationCanvas() {
        enableEvents( AWTEvent.COMPONENT_EVENT_MASK );
        
        _runner = new Thread() {
            public void run() { animateAnimation(); }
        };
        _runner.start();
    }
    
    /** Returns the Animation displayed by the canvas.  This allows an
     *  application to send commands to or register for events from the
     *  Animation.
     *  <p><strong>Important:</strong>
     *  The AnimationCanvas runs animation behaviours in a worker thread,
     *  concurrently to the AWT dispatcher and any threads spawned by the
     *  application.  The worker thread and AWT thread serialise access
     *  to the Animation by synchronizing on the canvas displaying the
     *  Animation.  Therefore, to avoid errors when invoking commands on
     *  the animation or modifying the scene graph, code must synchronize
     *  on the canvas object <em>before</em> calling <code>getAnimation</code>.
     *  For example:
     *  <pre>
     *  AnimationCanvas canvas = ...;
     *  ...
     *  synchronized(canvas) {
     *      canvas.getAnimation().invokeCommand("start");
     *  }
     *  </pre>
     *
     *  @return The Animation displayed by the canvas.
     */
    public Animation getAnimation() {
        return _animation;
    }
    
    /** Sets the animation displayed on the canvas.
     *
     *  @param animation
     *      The animation to display.
     */
    public synchronized void setAnimation( Animation animation ) {
        _animation = animation;
        _root.setTransformedGraph( animation );
        
        invalidate();
        repaint();
        notifyAll();
    }
    
    /** Returns the root of the scene graph.  This may not be the
     *  Animation because the canvas inserts 
     *  {@link uk.ac.ic.doc.scenebeans.Transform}
     *  objects into the graph to center and scale the animation.
     */
    public SceneGraph getSceneGraph() {
        return _root;
    }
    
    /** Returns the number of milliseconds that the animation thread sleeps
     *  between each frame.  The default is 25 milliseconds, giving a maximum
     *  frame rate of about 40 fps.
     *
     *  @return
     *      The duration the animation thread sleeps between each frame in
     *      milliseconds.
     */
    public synchronized long getFrameDelay() {
        return _frame_delay;
    }
    
    /** Sets the number of milliseconds that the animation thread sleeps
     *  between each frame.
     *
     *  @param msecs
     *      The duration the animation thread sleeps between each frame in
     *      milliseconds.
     */
    public synchronized void setFrameDelay( long msecs ) {
        _frame_delay = msecs;
    }
    
    /** Returns the time warp ratio.  The duration of each frame is multiplied
     *  by the time warp before being passed to the 
     *  {@link uk.ac.ic.doc.scenebeans.animation#performActivity} method
     *  of the animation.  Therefore, setting the time warp property to a value
     *  less than 1 will slow down the animation -- the animation will see
     *  each frame as being shorter than real-time -- and setting it to a value
     *  greater than 1 will speed up the animation -- the animation will see
     *  each frame as being longer than real-time.  The default time warp is
     *  1, meaning that the animation runs at wall-clock time.
     *  
     *  @return
     *      The time-warp ratio.
     */
    public synchronized double getTimeWarp() {
        return _time_warp;
    }
    
    /** Sets the time warp ratio.  The duration of each frame is multiplied
     *  by the time warp before being passed to the 
     *  {@link uk.ac.ic.doc.scenebeans.animation#performActivity} method
     *  of the animation.  Therefore, setting the time warp property to a value
     *  less than 1 will slow down the animation (the animation will see
     *  each frame as being shorter than real-time) and setting it to a value
     *  greater than 1 will speed up the animation (the animation will see
     *  each frame as being longer than real-time).  The default time warp is
     *  1, meaning that the animation runs at wall-clock time.
     *  <p>
     *  <strong>Note:</strong> setting the time warp property to a value other
     *  than 1 will make the animation run at a different speed than other
     *  threads or devices that may be involved in the animation.  For example,
     *  animated images displayed by {@link uk.ac.ic.doc.scenebeans.Sprite}
     *  beans are animated by threads within the AWT, and sound is played at
     *  real-time by the audio hardware.  Therefore, changing the time warp
     *  will make the animation run out of sync with sprites and audio.
     *  
     *  @param tw
     *      The time-warp ratio.
     */
    public synchronized void setTimeWarp( double tw ) {
        _time_warp = tw;
    }
    
    /** Returns whether the timing algorithm is adapting to timing variations
     *  caused by thread scheduling, or is not.  By default it is not, because
     *  the adaptive timing algorithm does not work very well.  It may be 
     *  improved in the future, but for now, ignore this property.
     *
     *  @return
     *    <code>true</code> if timing is adaptive, <code>false</code> otherwise.
     */
    public boolean isTimingAdaptive() {
        return _is_timing_adaptive;
    }
    
    /** Sets whether the timing algorithm is adapting to timing variations
     *  caused by thread scheduling, or is not.  By default it is not, because
     *  the adaptive timing algorithm does not work very well.  It may be 
     *  improved in the future, but for now, ignore this property.
     *
     *  @param b
     *    <code>true</code> if timing is adaptive, <code>false</code> otherwise.
     */
    public void setTimingAdaptive( boolean b ) {
        _is_timing_adaptive = b;
    }
    
    /** Returns the RenderingHints used to control the rendering of shapes in
     *  the scene graph.  Changing this property allows an application to trade
     *  off visual quality against speed.
     *
     *  @return
     *      The RenderingHints used when rendering the scene graph.
     */
    public RenderingHints getRenderingHints() {
        return _hints;
    }
    
    /** Sets the RenderingHints used to control the rendering of shapes in
     *  the scene graph.  Changing this property allows an application to trade
     *  off visual quality against speed.
     *
     *  @param hints
     *      The RenderingHints used when rendering the scene graph.
     */
    public void setRenderingHints( RenderingHints hints ) {
        _hints = hints;
        repaint();
    }
    
    /** Returns whether the animation thread is paused.
     *
     *  @return
     *      <code>true</code> if the thread is paused, <code>false</code> if
     *      the thread is running.
     */
    public boolean isPaused() {
        return _paused;
    }
    
    /** Requests that the animation thread pauses or resumes, but does not 
     *  wait for it to meet the request.
     *
     *  @param pause
     *      <code>true</code> to pause the animation thread, <code>false</code>
     *      to resume it.
     *  @see #waitPaused
     */
    public synchronized void setPaused( boolean pause ) {
        _pause = pause;
        _paused = false;
        if( !pause ) notifyAll();
    }
    
    /** Pauses the animation thread and waits for the thread to enter the paused
     *  state.
     *
     *  @throw java.lang.InterruptedException
     *      The calling thread was interrupted while waiting for the animation
     *      thread to pause.
     */
    public synchronized void waitPaused() throws InterruptedException {
        setPaused(true);
        while( !isPaused() ) wait();
    }
    
    /** Returns whether the origin of the animation's coordinate space is centered
     *  within the canvas window or not.
     *
     *  @return
     *      <code>true</code> if the origin is centered in the window, 
     *      <code>false</code> if it is in the top left-hand corner of the window.
     */
    public boolean isAnimationCentered() {
        return _root.isCentered();
    }
    
    /** Sets whether the origin of the animation's coordinate space is centered
     *  within the canvas window or not.
     *  
     *  @param b
     *      <code>true</code> if the origin is centered in the window, 
     *      <code>false</code> if it is in the top left-hand corner of the window.
     */
    public synchronized void setAnimationCentered( boolean b ) {
        _root.setCentered(b);
        if( _paused ) {
            paintBackbuffer();
            repaint();
        }
    }
    
    /** Returns whether the animation is stretched or shrunk to fill the window,
     *  or displayed at its natural size and, potentially, clipped by the edges
     *  of the window.  The natural size of the animation is given by its
     *  {@link uk.ac.ic.doc.scenebeans.animation.Animation#getWidth} and
     *  {@link uk.ac.ic.doc.scenebeans.animation.Animation#getHeight} methods.
     *
     *  @return
     *      <code>true</code> if the animation is stretched, <code>false</code>
     *      if it isn't.
     *  @see #isAnimationAspectFixed
     *  @see #setAnimationAspectFixed
     */
    public boolean isAnimationStretched() {
        return _root.isStretched();
    }
    
    /** Sets whether the animation is stretched or shrunk to fill the window,
     *  or displayed at its natural size and, potentially, clipped by the edges
     *  of the window.  The natural size of the animation is given by its
     *  {@link uk.ac.ic.doc.scenebeans.animation.Animation#getWidth} and
     *  {@link uk.ac.ic.doc.scenebeans.animation.Animation#getHeight} methods.
     *
     *  @param b
     *      <code>true</code> if the animation is stretched, <code>false</code>
     *      if it isn't.
     *  @see #isAnimationAspectFixed
     *  @see #setAnimationAspectFixed
     */
    public synchronized void setAnimationStretched( boolean b ) {
        _root.setStretched(b);
        if( _paused ) {
            paintBackbuffer();
            repaint();
        }
    }
    
    /** Returns whether the aspect ratio of the animation is maintained when
     *  it is stretched.
     *
     *  @return
     *      <code>true</code> if the aspect ratio is fixed, <code>false</code>
     *      if the animation is allowed to scale by different amounts in the
     *      x and y direections when stretched to fit the canvas window.
     *  @see #isAnimationStretched
     *  @see #setAnimationStretched
     */
    public boolean isAnimationAspectFixed() {
        return _root.isAspectFixed();
    }
    
    /** Sets whether the aspect ratio of the animation is maintained when
     *  it is stretched.
     *
     *  @param b
     *      <code>true</code> if the aspect ratio is fixed, <code>false</code>
     *      if the animation is allowed to scale by different amounts in the
     *      x and y direections when stretched to fit the canvas window.
     *  @see #isAnimationStretched
     *  @see #setAnimationStretched
     */
    public synchronized void setAnimationAspectFixed( boolean b ) {
        _root.setAspectFixed(b);
        if( _paused ) {
            paintBackbuffer();
            repaint();
        }
    }
    
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
    
    public Dimension getPreferredSize() {
        if( _animation == null ) {
            return new Dimension( 256, 256 );
        } else {
            return new Dimension( (int)Math.ceil( _animation.getWidth() ),
                                  (int)Math.ceil( _animation.getHeight() ) );
        }
    }
    
    public boolean isDoubleBuffered() {
        return true;
    }
    
    /** Stops the animation thread, but does not wait for it to stop.
     */
    public synchronized void stop() {
        if( _runner != null ) {
            Thread runner = _runner;
            _runner = null;
            runner.interrupt();
        }
    }
    
    protected void finalize() throws Throwable {
        _runner.interrupt();
        super.finalize();
    }
    
    void animateAnimation() {
        long start = System.currentTimeMillis();
        
        try {
            for(;;) {
                synchronized(this) {
                    while( _animation == null ) wait();
                    
                    while( _pause ) {
                        _paused = true;
                        notifyAll();
                        wait();
                        start = System.currentTimeMillis();
                    }
                    
                    long now = System.currentTimeMillis();
                    double t;
                    
                    if( isTimingAdaptive() ) {
                        t = _time_warp * ((double)(now-start)) / 1000.0;
                    } else {
                        t = _time_warp * (double)_frame_delay / 1000.0;
                    }
                    
                    _animation.performActivity(t);
                    paintAnimationFrame();
                    
                    start = now;
                }
                
                Thread.sleep( _frame_delay );
            }
        }
        catch( InterruptedException ex ) {
            return;
        }
    }
    
    
    private void discardBackbuffer() {
        if( _backbuffer != null ) {
            _backbuffer.flush();
            _backbuffer = null;
        }
    }
    
    /** Paints the scene graph onto the backbuffer and returns the smallest
     *  rectangle that contains the painted changes.
     */
    private Rectangle2D paintBackbuffer() {
        Graphics2D g = null;
        
        try {
            Rectangle2D clip = null;
            
            if( _backbuffer == null ) {
                _backbuffer = createImage( getWidth(), getHeight() );
                g = (Graphics2D)_backbuffer.getGraphics();
            } else {
                g = (Graphics2D)_backbuffer.getGraphics();
                clip = DirtyBounds.getBounds( _root, g );
                if( clip != null ) {
                    g.clip(clip);
                } else {
                    return null; // Avoid drawing if there is nothing to do
                }
            }
            
            if( _hints != null ) g.setRenderingHints(_hints);
            
            g.clearRect( 0, 0, getWidth(), getHeight() );
            _root.draw(g);
            
            return clip;
        }
        finally {
            if( g != null ) g.dispose();
        }
    }
    
    /*  Flip the back-buffer to the front buffer. The clip rectangle of the
     *  front buffer's graphics context ensures only the minimum blit is
     *  performed.
     */
    private void paintFrontbuffer( Graphics2D g ) {
        g.drawImage( _backbuffer, 0, 0, null );
    }
    
    /** Overridden <em>not</em> to clear the front buffer.
     */
    public synchronized void update( Graphics g ) {
        paint(g);
    }
    
    public synchronized void paint( Graphics g ) {
        if( _backbuffer == null ) paintBackbuffer();
        paintFrontbuffer( (Graphics2D)g );
    }
    
    /*  Called by the animation loop.
     */
    private synchronized void paintAnimationFrame() {
        if( !isShowing() ) return;
        
        _is_update_pending = false;
        
        Rectangle2D clip = paintBackbuffer();
        if( clip != null ) {
            Graphics2D g = (Graphics2D)getGraphics();
            try {
                g.setClip(clip);
                paintFrontbuffer(g);
            }
            finally {
                g.dispose();
            }
        }
    }
    
    protected synchronized void processComponentEvent( ComponentEvent ev ) {
        if( ev.getID() == ComponentEvent.COMPONENT_RESIZED || 
            ev.getID() == ComponentEvent.COMPONENT_SHOWN ) 
        {
            _root.setWindowSize( (double)ev.getComponent().getWidth(),
                                 (double)ev.getComponent().getHeight() );
        }
        
        if( ev.getID() == ComponentEvent.COMPONENT_RESIZED ) {
            discardBackbuffer();
        }
        
        super.processComponentEvent(ev);
    }
}
