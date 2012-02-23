/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ISim_eventListener.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id: ISim_eventListener.java 111 2008-01-08 10:49:06Z gnovelli $
 *
 */

package net.sf.gap.mc.core.agents.behaviours;

import eduni.simjava.Sim_event;

/**
 *
 * @author Giovanni Novelli
 */
public interface ISim_eventListener {
    public void eventOccurred(Sim_event ev);
}
