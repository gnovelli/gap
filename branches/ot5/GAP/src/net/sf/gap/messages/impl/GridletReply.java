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
 * GridletReply.java
 *
 * Created on 20 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: GridletReply.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/branches/ot5/GAP/src/net/sf/gap/messages/impl/GridletReply.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages.impl;

import eduni.simjava.Sim_event;
import gridsim.Gridlet;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;

/**
 * 
 * @author Giovanni Novelli
 */
public class GridletReply extends Reply {

	/**
	 * Creates a new instance of GridletReply
	 */
	public GridletReply(int requestTAG, boolean ok, GridletRequest request,
			Gridlet receivedGridlet) {
		super(requestTAG, ok, request);
		this.setReceivedGridlet(receivedGridlet);
	}

	@Override
	public GridletRequest getRequest() {
		return (GridletRequest) this.request;
	}

	public void setRequest(GridletRequest request) {
		this.request = request;
	}

	public Gridlet getReceivedGridlet() {
		return this.getRequest().getReceivedGridlet();
	}

	public void setReceivedGridlet(Gridlet receivedGridlet) {
		this.getRequest().setReceivedGridlet(receivedGridlet);
	}

	public static GridletReply get_data(Sim_event ev) {
		Message<GridletReply> message = new Message<GridletReply>();
		return message.get_data(ev);
	}
}
