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
 * NetworkMapRequest.java
 *
 * Created on 23 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: NetworkMapRequest.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/branches/march08tuning/GAP/src/net/sf/gap/messages/impl/NetworkMapRequest.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages.impl;

import net.sf.gap.agents.services.impl.nm.NetworkMap;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class NetworkMapRequest extends Request {

	private NetworkMap networkMap; // Used in reply

	/**
	 * Creates a new instance of NetworkMapRequest
	 */
	public NetworkMapRequest(int src_ID, int src_resID) {
		super(src_ID, src_resID);
	}

	public static NetworkMapRequest get_data(Sim_event ev) {
		Message<NetworkMapRequest> message = new Message<NetworkMapRequest>();
		return message.get_data(ev);
	}

	@Override
	public NetworkMapRequest clone() {
		return new NetworkMapRequest(this.getSrc_ID(), this.getSrc_resID());
	}

	public NetworkMap getNetworkMap() {
		return this.networkMap;
	}

	public void setNetworkMap(NetworkMap networkMap) {
		this.networkMap = networkMap;
	}
}
