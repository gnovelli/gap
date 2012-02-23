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
 * NetworkMapReply.java
 *
 * Created on 23 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: NetworkMapReply.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/branches/ot5/GAP/src/net/sf/gap/messages/impl/NetworkMapReply.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages.impl;

import net.sf.gap.agents.services.impl.nm.NetworkMap;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class NetworkMapReply extends Reply {

	/**
	 * Creates a new instance of NetworkMapReply
	 */
	public NetworkMapReply(int requestTAG, boolean ok,
			NetworkMapRequest request, NetworkMap networkMap) {
		super(requestTAG, ok, request);
		this.setNetworkMap(networkMap);
	}

	public NetworkMap getNetworkMap() {
		return this.getRequest().getNetworkMap();
	}

	public void setNetworkMap(NetworkMap networkMap) {
		this.getRequest().setNetworkMap(networkMap);
	}

	@Override
	public NetworkMapReply clone() {
		return new NetworkMapReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone(), this.getNetworkMap());
	}

	public static NetworkMapReply get_data(Sim_event ev) {
		Message<NetworkMapReply> message = new Message<NetworkMapReply>();
		return message.get_data(ev);
	}

	@Override
	public NetworkMapRequest getRequest() {
		return (NetworkMapRequest) this.request;
	}

	public void setRequest(NetworkMapRequest request) {
		this.request = request;
	}
}
