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
 * GISRequest.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: GISRequest.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/qagesa/v2.6/GAP/src/net/sf/gap/messages/impl/GISRequest.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.messages.impl;

import net.sf.gap.agents.services.impl.gis.GISRepository;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class GISRequest extends Request {

	private GISRepository gis; // Used in reply

	/**
	 * Creates a new instance of GISRequest
	 */
	public GISRequest(int src_ID, int src_resID) {
		super(src_ID, src_resID);
	}

	public static GISRequest get_data(Sim_event ev) {
		Message<GISRequest> message = new Message<GISRequest>();
		return message.get_data(ev);
	}

	@Override
	public GISRequest clone() {
		return new GISRequest(this.getSrc_ID(), this.getSrc_resID());
	}

	public GISRepository getGIS() {
		return this.gis;
	}

	public void setGIS(GISRepository gis) {
		this.gis = gis;
	}
}
