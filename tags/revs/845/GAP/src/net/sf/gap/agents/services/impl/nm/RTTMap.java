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
 * RTTMap.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 186 $
 * $Id: RTTMap.java 186 2008-01-23 11:30:55Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/845/GAP/src/net/sf/gap/agents/services/impl/nm/RTTMap.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl.nm;

import gridsim.net.InfoPacket;

import java.util.HashMap;

/**
 * 
 * @author admin
 */
public class RTTMap extends HashMap<Integer, InfoPacket> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2437318028250188286L;

	/** Creates a new instance of RTTMap */
	public RTTMap() {
	}

}
