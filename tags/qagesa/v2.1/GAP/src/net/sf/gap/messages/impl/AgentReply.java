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
 * AgentReply.java
 *
 * Created on 16 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 261 $
 * $Id: AgentReply.java 261 2008-01-25 10:40:59Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/qagesa/v2.1/GAP/src/net/sf/gap/messages/impl/AgentReply.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages.impl;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class AgentReply extends Reply {

	public AgentReply(int requestTAG, boolean ok, AgentRequest agentRequest) {
		super(requestTAG, ok, agentRequest);
	}

	@Override
	public AgentReply clone() {
		return new AgentReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone());
	}

	public static AgentReply get_data(Sim_event ev) {
		Message<AgentReply> message = new Message<AgentReply>();
		return message.get_data(ev);
	}

	@Override
	public AgentRequest getRequest() {
		return (AgentRequest) this.request;
	}

	public void setRequest(AgentRequest agentRequest) {
		this.request = agentRequest;
	}
}
