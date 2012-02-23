/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Sim_eventListenerList.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id: Sim_eventListenerList.java 204 2008-01-24 10:43:48Z gnovelli $
 *
 */

package net.sf.gap.impl.agents.behaviours;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Giovanni Novelli
 */
public class Sim_eventListenerList extends ConcurrentHashMap<Integer,Sim_eventListener>{
   public Sim_eventListener addListener(int tag, Sim_eventListener listener) {
       return this.put(tag, listener);
   } 
   
   public Sim_eventListener removeListener(int tag) {
       return this.remove(tag);
   }
}
