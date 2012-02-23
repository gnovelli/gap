/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * TEST_REPListener.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id: TEST_REPListener.java 219 2008-01-24 12:17:06Z gnovelli $
 *
 */  

package net.sf.gap.mc.experiments.agents.behaviours;

import eduni.simjava.Sim_event;

import net.sf.gap.agents.PluggableAgent;
import net.sf.gap.agents.behaviours.Sim_eventListener;

import net.sf.gap.mc.experiments.constants.ExperimentsTags;

/**
 *
 * @author Giovanni Novelli
 */
public class TEST_REPListener extends Sim_eventListener {
    public TEST_REPListener(PluggableAgent anAgent, int aTag) {
        super(anAgent, aTag);
    }
    
    public void processEvent(Sim_event ev) {
		switch (ev.get_tag()) {
                // Gridlets Management
		case ExperimentsTags.TEST_REP:
                    break;
                default:
                    break;
                }
    }
}
