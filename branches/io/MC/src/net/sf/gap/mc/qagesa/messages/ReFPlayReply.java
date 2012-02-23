/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        QAGESA Simulator
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ReFPlayReply.java
 *
 * Created on 24 March 2007, 17.54 by Giovanni Novelli
 *
 * $Id: ReFPlayReply.java 673 2008-03-03 15:05:30Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa.messages;

import net.sf.gap.messages.Message;
import net.sf.gap.messages.Reply;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class ReFPlayReply extends Reply {
        private int agentID;

	/**
	 * Creates a new instance of ReFPlayRequest
	 */
	public ReFPlayReply(int agentID, int requestTAG, boolean ok, ReFPlayRequest request) {
		super(requestTAG, ok, request);
                this.setAgentID(agentID);
	}

	@Override
	public ReFPlayReply clone() {
		return new ReFPlayReply(this.getAgentID(), this.getRequestTAG(), this.isOk(), this
				.getRequest().clone());
	}

	public static ReFPlayReply get_data(Sim_event ev) {
		Message<ReFPlayReply> message = new Message<ReFPlayReply>();
		return message.get_data(ev);
	}

	@Override
	public ReFPlayRequest getRequest() {
		return (ReFPlayRequest) this.request;
	}

	public void setRequest(ReFPlayRequest request) {
		this.request = request;
	}

    public Sim_event getReplyEv() {
        return this.getRequest().getReplyEv();
    }

    public void setReplyEv(Sim_event replyEv) {
        this.getRequest().setReplyEv(replyEv);
    }

    public Sim_event getFcEv() {
        return this.getRequest().getFcEv();
    }

    public void setFcEv(Sim_event fcEv) {
        this.getRequest().setFcEv(fcEv);
    }

    public int getAgentID() {
        return agentID;
    }

    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }
}
