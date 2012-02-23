/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * TEST_REQListener.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id: TEST_REQListener.java 111 2008-01-08 10:49:06Z gnovelli $
 *
 */  

package net.sf.gap.mc.experiments.agents.behaviours;

import eduni.simjava.Sim_event;

import net.sf.gap.mc.core.agents.PluggableAgent;
import net.sf.gap.mc.core.agents.behaviours.Sim_eventListener;

import net.sf.gap.mc.experiments.constants.ExperimentsTags;

/**
 *
 * @author Giovanni Novelli
 */
public class TEST_REQListener extends Sim_eventListener {
    public TEST_REQListener(PluggableAgent anAgent, int aTag) {
        super(anAgent, aTag);
    }
    
    public void processEvent(Sim_event ev) {
		switch (ev.get_tag()) {
                // Gridlets Management
		case ExperimentsTags.TEST_REQ:
                    break;
                default:
                    break;
                }
    }
}
