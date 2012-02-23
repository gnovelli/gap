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
 * AgentHistory.java
 *
 * Created on 20 August 2006, 16.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 100 $
 * $Id: AgentHistory.java 100 2008-01-07 11:23:02Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/branches/experiments/gridlets/GAP/src/net/sf/gap/agents/history/AgentHistory.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.history;

import java.util.LinkedList;

/**
 * AgentHistory is a LinkedList that contains a list of entries about agents'
 * lifecycle
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.gap.agents.history.AgentHistoryEntry
 */
public class AgentHistory extends LinkedList<AgentHistoryEntry> {
	private static final long serialVersionUID = -3239225485248569470L;
}
