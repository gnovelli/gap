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
 * ISim_eventListener.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id: ISim_eventListener.java 275 2008-01-27 10:39:07Z gnovelli $
 *
 */

package net.sf.gap.agents.behaviours;

import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public interface ISim_eventListener {
	public void eventOccurred(Sim_event ev);
}
