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
 * Reply.java
 *
 * Created on 29 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: Reply.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/branches/t/GAP/src/net/sf/gap/messages/Reply.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages;

import net.sf.gap.util.EntitiesCounter;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class Reply implements Cloneable {
	private int replyID; // Unique reply ID

	protected Request request;

	/**
	 * Creates a new instance of Reply
	 */
	public Reply(int requestTAG, boolean ok, Request request) {
		this.setRequestTAG(requestTAG);
		this.setOk(ok);
		this.setRequest(request);

		EntitiesCounter.create("REP");
		Integer irepID = EntitiesCounter.inc("REP");
		int repID = irepID;
		this.setReplyID(repID);
	}

	public static Reply get_data(Sim_event ev) {
		Message<Reply> message = new Message<Reply>();
		return message.get_data(ev);
	}

	protected boolean ok;

	protected int requestTAG;

	public int getEntityType() {
		return this.getRequest().getDst_entityType();
	}

	public int getRequestTAG() {
		return this.requestTAG;
	}

	public boolean isOk() {
		return this.ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public void setRequestTAG(int requestTAG) {
		this.requestTAG = requestTAG;
	}

	public Request getRequest() {
		return this.request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public int getRequestID() {
		return this.getRequest().getRequestID();
	}

	public int getReplyID() {
		return replyID;
	}

	public void setReplyID(int replyID) {
		this.replyID = replyID;
	}

	public int getReqrepID() {
		return this.getRequest().getReqrepID();
	}
}
