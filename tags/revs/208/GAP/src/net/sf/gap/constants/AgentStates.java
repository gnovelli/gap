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
 * AgentStates.java
 *
 * Created on 12 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 186 $
 * $Id: AgentStates.java 186 2008-01-23 11:30:55Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/208/GAP/src/net/sf/gap/constants/AgentStates.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.constants;

/**
 * This class is like an enum about GAP agents' states.
 * <p>
 * Grid Agents are supposed to be in one of the following states:
 * <p>
 * <ul>
 * <li> RUNNING This state is when an agent is active on a Grid Resource and is
 * consuming resources (Memory and CPU) From this state an agent can migrate to
 * both PAUSED and ZOMBIE
 * 
 * <li> PAUSED This state is when an agent is active on a Grid Resource but is
 * not consuming CPU resources From this state an agent can migrate to RUNNING
 * 
 * <li> ZOMBIE This state is when an agent is not active on a Grid Resource and
 * is not consuming any resource From this state an agent can migrate to RUNNING
 * 
 * </ul>
 * 
 * @author Giovanni Novelli
 */
public class AgentStates {
        public static final int STATESBASE = 100;
	public static final int RUNNING = 1 + STATESBASE;

	public static final int PAUSED = 2 + STATESBASE;

	public static final int ZOMBIE = 3 + STATESBASE;

	/**
	 * Creates a new instance of AgentStates
	 */
	public AgentStates() {
	}

	public static String toString(int state) {
		switch (state) {
		case AgentStates.RUNNING:
			return "RUNNING";
		case AgentStates.PAUSED:
			return "PAUSED";
		case AgentStates.ZOMBIE:
			return "ZOMBIE";
		default:
			return "UNKNOWN_STATE";
		}
	}
}
