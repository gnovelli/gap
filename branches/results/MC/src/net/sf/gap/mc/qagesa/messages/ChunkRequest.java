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
 * ChunkRequest.java
 *
 * Created on 19 March 2007, 21.45 by Giovanni Novelli
 *
 * $Id: ChunkRequest.java 631 2008-02-29 11:45:46Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa.messages;

import net.sf.gap.mc.qagesa.multimedia.Chunk;
import net.sf.gap.messages.Message;
import net.sf.gap.messages.Request;
import eduni.simjava.Sim_event;

/**
 * 
 * @author Giovanni Novelli
 */
public class ChunkRequest extends Request {
        private TranscodeRequest transcodeRequest;
        // Used to associate a play request to a transcode request
        // Used in User to compute performance measures
        private int playReqrepID; 
        
	private int userID; // Used in request

	private String movieTag; // Used in request

	private int sequenceNumber; // Used in request
        
        private int storageElementID;           // Used in request

	private Chunk chunk; // Used in reply

	/**
	 * Creates a new instance of ChunkRequest
	 */
	public ChunkRequest(int src_ID, int src_resID, 
                int playReqrepID,
                int userID, String movieTag, int sequenceNumber, int storageElementID, TranscodeRequest transcodeRequest) {
		super(src_ID, src_resID);
                
                this.setPlayReqrepID(playReqrepID);
                
		this.setUserID(userID);
		this.setMovieTag(movieTag);
		this.setSequenceNumber(sequenceNumber);
                this.setStorageElementID(storageElementID);
                this.setTranscodeRequest(transcodeRequest);
	}

	public static ChunkRequest get_data(Sim_event ev) {
		Message<ChunkRequest> message = new Message<ChunkRequest>();
		return message.get_data(ev);
	}

	@Override
	public ChunkRequest clone() {
		return new ChunkRequest(this.getSrc_ID(), this.getSrc_resID(),
                        this.getPlayReqrepID(),
                        this.getUserID(), this.getMovieTag(), this.getSequenceNumber(), this.getStorageElementID(), this.getTranscodeRequest());
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	public String getMovieTag() {
		return movieTag;
	}

	public void setMovieTag(String movieTag) {
		this.movieTag = movieTag;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

    public int getStorageElementID() {
        return storageElementID;
    }

    public void setStorageElementID(int storageElementID) {
        this.storageElementID = storageElementID;
    }

    public int getPlayReqrepID() {
        return playReqrepID;
    }

    public void setPlayReqrepID(int playReqrepID) {
        this.playReqrepID = playReqrepID;
    }

    public TranscodeRequest getTranscodeRequest() {
        return transcodeRequest;
    }

    public void setTranscodeRequest(TranscodeRequest transcodeRequest) {
        this.transcodeRequest = transcodeRequest;
    }
}
