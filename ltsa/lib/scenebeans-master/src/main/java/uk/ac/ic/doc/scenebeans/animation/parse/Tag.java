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


class Tag
{
    static final String ANIMATION = "animation";
    static final String BEHAVIOUR = "behaviour";
    static final String SEQ = "seq";
    static final String CO = "co";
    static final String FORALL = "forall";
    static final String EVENT = "event";
    static final String COMMAND = "command";
    static final String START = "start";
    static final String STOP = "stop";
    static final String RESET = "reset";
    static final String SET = "set";
    static final String INVOKE = "invoke";
    static final String ANNOUNCE = "announce";
    static final String DEFINE = "define";
    static final String DRAW = "draw";
    static final String TRANSFORM = "transform";
    static final String STYLE = "style";
    static final String COMPOSE = "compose";
    static final String INPUT = "input";
    static final String INST = "paste"; // Renamed since parser was written
    static final String INCLUDE = "include";
    static final String PRIMITIVE = "primitive";
    static final String IMAGE = "image";
    static final String TEXT = "text";
    static final String POLYGON = "polygon";
    static final String POINT = "point";
    static final String PARAM = "param";
    static final String ANIMATE = "animate";
    static final String NULL = "null";
}
