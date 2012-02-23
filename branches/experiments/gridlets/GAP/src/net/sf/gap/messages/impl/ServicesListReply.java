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
 * ServicesListReply.java
 *
 * Created on 16 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 100 $
 * $Id: ServicesListReply.java 100 2008-01-07 11:23:02Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/branches/experiments/gridlets/GAP/src/net/sf/gap/messages/impl/ServicesListReply.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.messages.impl;

import net.sf.gap.agents.services.ServicesList;
import eduni.simjava.Sim_event;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;

/**
 * 
 * @author Giovanni Novelli
 */
public class ServicesListReply extends Reply {

	/**
	 * Creates a new instance of ServicesListReply
	 */
	public ServicesListReply(int requestTAG, boolean ok,
			ServicesListRequest request, ServicesList servicesList) {
		super(requestTAG, ok, request);
		this.setServicesList(servicesList);
	}

	public ServicesList getServicesList() {
		return this.getRequest().getServicesList();
	}

	public void setServicesList(ServicesList servicesList) {
		this.getRequest().setServicesList(servicesList);
	}

	@Override
	public ServicesListReply clone() {
		return new ServicesListReply(this.getRequestTAG(), this.isOk(), this
				.getRequest().clone(), this.getServicesList());
	}

	public static ServicesListReply get_data(Sim_event ev) {
		Message<ServicesListReply> message = new Message<ServicesListReply>();
		return message.get_data(ev);
	}

	@Override
	public ServicesListRequest getRequest() {
		return (ServicesListRequest) this.request;
	}

	public void setRequest(ServicesListRequest request) {
		this.request = request;
	}
}
