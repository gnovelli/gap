/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * TESTAgent.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id: TESTAgent.java 111 2008-01-08 10:49:06Z gnovelli $
 *
 */

package net.sf.gap.mc.experiments.agents;

import net.sf.gap.grid.components.GridElement;

import net.sf.gap.mc.core.agents.PluggableAgent;

import net.sf.gap.mc.experiments.constants.ExperimentsTags;
import net.sf.gap.mc.experiments.agents.behaviours.TEST_REPListener;
import net.sf.gap.mc.experiments.agents.behaviours.TEST_REQListener;

/**
 *
 * @author Giovanni Novelli
 */
public class TESTAgent extends PluggableAgent {
	/**
	 * Creates a new instance of TESTAgent class
	 * 
	 * @param name agent name
	 * @param agentSizeInBytes agent size in bytes
	 * @param trace_flag trace flag for GridSim
	 * @throws Exception
	 */
	public TESTAgent(
                GridElement ge, 
                String name, 
                int agentSizeInBytes,
		boolean trace_flag) 
                throws Exception 
        {
		super(ge, name, agentSizeInBytes, trace_flag);
	}
        
        public void addListeners() {
            TEST_REQListener instanceTEST_REQListener = 
                    new TEST_REQListener(
                    this,
                    ExperimentsTags.TEST_REQ);
            TEST_REPListener instanceTEST_REPListener = 
                    new TEST_REPListener(
                    this,
                    ExperimentsTags.TEST_REP);
            this.addListener(instanceTEST_REQListener);
            this.addListener(instanceTEST_REPListener);
        }
        
        public void dispose() {}
}
