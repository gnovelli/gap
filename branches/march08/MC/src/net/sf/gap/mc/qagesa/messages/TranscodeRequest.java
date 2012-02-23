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
 * TranscodeRequest.java
 *
 * Created on 23 March 2007, 15.06 by Giovanni Novelli
 *
 * $Id: TranscodeRequest.java 752 2008-03-06 12:57:44Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa.messages;

import net.sf.gap.mc.qagesa.multimedia.ChunksSequence;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;
import gridsim.Accumulator;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class TranscodeRequest extends Request {
        private ReFPlayRequest playRequest;
    
	// Entity ID of SE from which TranscodingAgent will get chunks to transcode
	private int storageElementID; // Used in request

        private ChunksSequence sequence; // Used in request
        
        private int agentID;            // Used in reply
        
        private double quality;
        
        private Accumulator qualityMean;
        

	/**
	 * Creates a new instance of TranscodeRequest
	 */
	public TranscodeRequest(int src_ID, int src_resID, 
                        ReFPlayRequest playRequest, int storageElementID, double quality) {
		super(src_ID, src_resID);
                this.setPlayRequest(playRequest);
		this.setStorageElementID(storageElementID);
                this.setQuality(quality);
                this.setQualityMean(new Accumulator());
	}

	public static TranscodeRequest get_data(Sim_event ev) {
		Message<TranscodeRequest> message = new Message<TranscodeRequest>();
		return message.get_data(ev);
	}

	@Override
	public TranscodeRequest clone() {
		return new TranscodeRequest(this.getSrc_ID(), this.getSrc_resID(), 
                        this.getPlayRequest(), this.getStorageElementID(), this.getQuality());
	}

	public ChunksSequence getSequence() {
		return sequence;
	}

	public void setSequence(ChunksSequence sequence) {
		this.sequence = sequence;
	}

	public String getMovieTag() {
		return this.getPlayRequest().getMovieTag();
	}

	public int getStorageElementID() {
		return storageElementID;
	}

	public void setStorageElementID(int storageElementID) {
		this.storageElementID = storageElementID;
	}

	public int getUserID() {
		return this.getPlayRequest().getUserID();
	}

    public int getPlayReqrepID() {
        return this.getPlayRequest().getReqrepID();
    }

    public ReFPlayRequest getPlayRequest() {
        return playRequest;
    }

    public void setPlayRequest(ReFPlayRequest playRequest) {
        this.playRequest = playRequest;
    }

    public int getAgentID() {
        return agentID;
    }

    public void setAgentID(int agentID) {
        this.agentID = agentID;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }
    
    public double getMinQuality() {
        return this.getPlayRequest().getMinQuality();
    }

    public Accumulator getQualityMean() {
        return qualityMean;
    }

    public void setQualityMean(Accumulator qualityMean) {
        this.qualityMean = qualityMean;
    }
    
    public double updateQualityMean() {
        this.getQualityMean().add(this.getQuality());
        return this.getQualityMean().getMean();
    }
}