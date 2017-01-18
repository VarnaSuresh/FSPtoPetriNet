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

import java.util.*;


/** A Command that invokes multiple commands when it is, itself, invoked.
 */
public class CompositeCommand implements Command
{
    private List _actions;
    
    /** Constructs a CompositeCommand.
     */
    public CompositeCommand() {
        _actions = new ArrayList();
    }
    
    /** Constructs a CompositeCommand, specifying the list used to hold 
     *  its Command elements.
     *
     *  @param actions
     *      The list used to hold the commands invoked by this 
     *      CompositeCommand.
     */
    public CompositeCommand( List actions ) {
        _actions = actions;
    }
    
    /** Adds a command to the CompositeCommand.  The command will be invoked
     *  after those previously added.
     *
     *  @param action
     *      The command to add.
     */
    public void addCommand( Command action ) {
        _actions.add(action);
    }
    
    /** Removes the first occurrence of a command in the composite.
     *
     *  @param action
     *      The command to remove.
     */
    public void removeCommand( Command action ) {
        _actions.remove(action);
    }
    
    /** Removes a command by index.
     *
     *  @param action
     *      The index of the command to remove.  
     *      Commands are indexed from zero.
     */
    public void removeCommand( int n ) {
        _actions.remove(n);
    }
    
    /** Returns the number of commands in this composite.
     *
     *  @return
     *      The number of commands in this composite.
     */
    public int getCommandCount() {
        return _actions.size();
    }
    
    /** Returns a List containing all the commands in this CompositeCommand.
     *
     *  @return
     *      An immutable list containing all the commands in this 
     *      CompositeCommand.  A zero-length list is returned if no 
     *      commands have been added.
     */
    public List getCommands() {
        return Collections.unmodifiableList( _actions );
    }
    
    /** Get a command by index.  Commands are indexed from zero.
     *
     *  @param n
     *      The index of the command.
     */
    public Command getCommand( int n ) {
        return (Command)_actions.get(n);
    }
    
    public void invoke() throws CommandException {
        for( Iterator i = _actions.iterator(); i.hasNext(); ) {
            ((Command)i.next()).invoke();
        }
    }
}

