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
 * TranscodingAgent.java
 *
 * Created on 12 August 2006, 19.35 by Giovanni Novelli
 *
 * $Id: TranscodingAgent.java 632 2008-02-29 12:16:49Z gnovelli $
 *
 */
package net.sf.gap.mc.qagesa.agents;

import junit.framework.Assert;
import net.sf.gap.agents.GridAgent;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.mc.qagesa.constants.QAGESATags;
import net.sf.gap.mc.qagesa.grid.components.QAGESAGridElement;
import net.sf.gap.mc.qagesa.messages.ChunkReply;
import net.sf.gap.mc.qagesa.messages.ChunkRequest;
import net.sf.gap.mc.qagesa.messages.TranscodeReply;
import net.sf.gap.mc.qagesa.messages.TranscodeRequest;
import net.sf.gap.mc.qagesa.multimedia.Chunk;
import net.sf.gap.mc.qagesa.multimedia.ChunksSequence;
import net.sf.gap.util.EntitiesCounter;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.Gridlet;
import gridsim.IO_data;

/**
 * 
 * @author Giovanni Novelli
 */
public class TranscodingAgent extends GridAgent {

    /**
     * 
     * Sim_stat object used to measure the loop about:
     *  - get chunk
     *  - process chunk
     *  - send chunk
     * between receival of TRANSCODE_CHUNKS_REQ and the send of tag SENT_LAST_CHUNK_REP
     */
    private Sim_stat statGTS;
    private boolean enabledCaching;

    public TranscodingAgent(AbstractGridElement ge, String name, int agentSizeInBytes,
            boolean trace_flag, boolean enabledCaching) throws Exception {
        super(ge, name, agentSizeInBytes, trace_flag);
        this.setEnabledCaching(enabledCaching);
    //this.setupStatGTS();
    }

    @SuppressWarnings("unused")
    private void setupStatGTS() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.TRANSCODE_CHUNKS_REQ};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatGTS(stat);
    }

    @Override
    public void initialize() throws Exception {
        super.initialize();
    }

    @Override
    protected void dispose() {
    }

    public Chunk transcode(Chunk chunk) {
        if (!EntitiesCounter.contains("Gridlet")) {
            EntitiesCounter.create("Gridlet");
        }
        double length = chunk.getMIPS();
        long file_size = chunk.getInputSize();
        long output_size = chunk.getOutputSize();
        Gridlet gridlet = new Gridlet(EntitiesCounter.inc("Gridlet"), length,
                file_size, output_size);
        gridlet.setUserID(this.get_id());
        super.gridletSubmit(gridlet, this.getResourceID());
        // Receiving a Gridlet back
        gridlet = super.gridletReceive();
        String indent = "    ";
        System.out.print(indent + gridlet.getGridletID() + indent + indent);

        if (gridlet.getGridletStatus() == Gridlet.SUCCESS) {
            System.out.print("SUCCESS");
        }

        System.out.println(indent + indent + gridlet.getResourceID() +
                indent + gridlet.getExecStartTime() + indent + gridlet.getFinishTime());
        Chunk transcodedChunk = chunk.transcode();
        return transcodedChunk;
    }

    @Override
    public void processOtherEvent(Sim_event ev) {
        switch (ev.get_tag()) {
            case QAGESATags.GET_CHUNK_REP:
                ChunkReply gotChunkReply = ChunkReply.get_data(ev);
                ChunkRequest gotChunkRequest = gotChunkReply.getRequest();
                TranscodeRequest transcodeRequest = gotChunkRequest.getTranscodeRequest();
                int seID = transcodeRequest.getStorageElementID();
                String movieTag = gotChunkRequest.getMovieTag();
                // Get current chunks sequence on the storage element
                QAGESAGridElement se = (QAGESAGridElement) Sim_system.get_entity(seID);
                ChunksSequence sequence = se.getTranscodingSet().get(movieTag);
                ChunksSequence transcodedSequence = new ChunksSequence(sequence.getMovie(), sequence.getOperation(), sequence.getOperationParameters());
                boolean previouslyTranscoded = sequence.isTranscoded();
                int requestID = gotChunkRequest.getRequestID();
                int reqrepID = gotChunkRequest.getReqrepID();
                int sequenceNumber = gotChunkRequest.getSequenceNumber();
                Assert.assertEquals(requestID, gotChunkReply.getRequestID());
                Assert.assertEquals(QAGESATags.GET_CHUNK_REQ, gotChunkReply.getRequestTAG());
                Assert.assertEquals(QAGESATags.GET_CHUNK_REP, ev.get_tag());
                double evrecv_time = GridSim.clock();
                String msg = String.format("%1$f %2$d %3$s <-- %4$s GET_CHUNK_REPLY %5$s %6$d %7$s",
                        evrecv_time,
                        reqrepID,
                        this.get_name(),
                        this.getGridElement().get_name(),
                        movieTag,
                        sequenceNumber,
                        gotChunkReply.getChunk());
                this.write(msg);
                Chunk gotChunk = gotChunkReply.getChunk();
                Chunk transcodedChunk = null;
                if (!gotChunk.isTranscoded()) {
                    transcodedChunk = this.transcode(gotChunk);
                    if (this.isEnabledCaching()) {
                        transcodedSequence.add(transcodedChunk);
                    }
                } else {
                    transcodedChunk = gotChunk;
                }
                if (sequenceNumber == 1) {
                    ChunkRequest chunkRequest = new ChunkRequest(this.get_id(), this.get_id(),
                            transcodeRequest.getPlayReqrepID(),
                            transcodeRequest.getUserID(), movieTag, sequenceNumber, transcodeRequest.getStorageElementID(), transcodeRequest);
                    super.send(super.output, GridSimTags.SCHEDULE_NOW,
                            QAGESATags.TRANSCODED_FIRST_CHUNK_REP,
                            new IO_data(chunkRequest, 1, transcodeRequest.getUserID()));
                }
                this.sendChunk(transcodeRequest.getPlayReqrepID(), transcodeRequest.getUserID(), movieTag, sequenceNumber, transcodedChunk, transcodeRequest.getStorageElementID(), transcodeRequest);
                int nc = sequence.size();
                if (sequenceNumber <nc) {
                    int nextSequenceNumber = sequenceNumber+1;
                    int SIZE = 500;
                    double evsend_time = 0;
                    ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                            transcodeRequest.getPlayReqrepID(),
                            transcodeRequest.getUserID(), movieTag, nextSequenceNumber, transcodeRequest.getStorageElementID(), transcodeRequest);
                    requestID = request.getRequestID();
                    reqrepID = request.getReqrepID();
                    super.send(super.output, GridSimTags.SCHEDULE_NOW,
                            QAGESATags.GET_CHUNK_REQ, new IO_data(request, SIZE, request.getStorageElementID()));
                    evsend_time = GridSim.clock();
                    msg = String.format(
                            "%1$f %2$d %3$s --> %4$s GET_CHUNK_REQUEST %5$s %6$d",
                            evsend_time,
                            reqrepID,
                            this.get_name(),
                            this.getGridElement().get_name(),
                            movieTag,
                            nextSequenceNumber);
                    this.write(msg);
                } else {
                    this.sendLastChunk(transcodeRequest.getPlayReqrepID(), transcodeRequest.getUserID(), movieTag, seID, transcodeRequest);
                    if (!previouslyTranscoded && this.isEnabledCaching()) {
                        transcodeRequest.setSequence(transcodedSequence);
                        double evsend_time = 0;
                        reqrepID = transcodeRequest.getReqrepID();
                        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                                QAGESATags.CACHE_CHUNKS_REQ, new IO_data(transcodeRequest, transcodeRequest.getSequence().getInputSize(), transcodeRequest.getStorageElementID()));
                        evsend_time = GridSim.clock();
                        msg = String.format(
                                "%1$f %2$d %3$s --> %4$s CACHE_CHUNKS %5$s",
                                evsend_time,
                                reqrepID,
                                this.get_name(),
                                Sim_system.get_entity(transcodeRequest.getStorageElementID()).get_name(),
                                movieTag);
                        this.write(msg);
                    }
                    TranscodeReply transcodeReply = new TranscodeReply(ev.get_tag(), true, transcodeRequest, this.get_id());
                    super.send(super.output, GridSimTags.SCHEDULE_NOW,
                            QAGESATags.TRANSCODE_CHUNKS_REP, new IO_data(transcodeReply, 500, transcodeRequest.getSrc_ID()));
                }
                break;
            case QAGESATags.TRANSCODE_CHUNKS_REQ:
                transcodeRequest = TranscodeRequest.get_data(ev);
                int playReqrepID = transcodeRequest.getPlayReqrepID();
                int userID = transcodeRequest.getUserID();
                movieTag = transcodeRequest.getMovieTag();
                seID = transcodeRequest.getStorageElementID();
                // Get current chunks sequence on the storage element
                se = (QAGESAGridElement) Sim_system.get_entity(seID);
                sequence = se.getTranscodingSet().get(movieTag);
                transcodedSequence = new ChunksSequence(sequence.getMovie(), sequence.getOperation(), sequence.getOperationParameters());
                previouslyTranscoded = sequence.isTranscoded();

                Chunk chunk = sequence.get(0);
                sequenceNumber = chunk.getSequenceNumber();
                ChunkRequest chunkRequest = new ChunkRequest(this.get_id(), this.get_id(),
                        playReqrepID,
                        userID, movieTag, sequenceNumber, seID, transcodeRequest);
                super.send(super.output, GridSimTags.SCHEDULE_NOW,
                        QAGESATags.SENDING_FIRST_CHUNK_REP,
                        new IO_data(chunkRequest, 1, userID));
                int SIZE = 500;
                double evsend_time = 0;
                ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                        transcodeRequest.getPlayReqrepID(),
                        transcodeRequest.getUserID(), movieTag, sequenceNumber, transcodeRequest.getStorageElementID(), transcodeRequest);
                requestID = request.getRequestID();
                reqrepID = request.getReqrepID();
                super.send(super.output, GridSimTags.SCHEDULE_NOW,
                        QAGESATags.GET_CHUNK_REQ, new IO_data(request, SIZE, request.getStorageElementID()));
                evsend_time = GridSim.clock();
                msg = String.format(
                        "%1$f %2$d %3$s --> %4$s GET_CHUNK_REQUEST %5$s %6$d",
                        evsend_time,
                        reqrepID,
                        this.get_name(),
                        this.getGridElement().get_name(),
                        movieTag,
                       sequenceNumber);
                this.write(msg);
                break;

            default:
                break;
        }
    }

    protected void sendChunk(int playReqrepID, int userID, String movieTag,
            int sequenceNumber, Chunk chunk, int seID, TranscodeRequest transcodeRequest) {
        double evsend_time = 0;
        ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                playReqrepID,
                userID, movieTag, sequenceNumber, seID, transcodeRequest);
        request.setChunk(chunk);
        @SuppressWarnings("unused")
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        request.setChunk(chunk);
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.SEND_CHUNK_REQ, new IO_data(request, chunk.getOutputSize(), userID));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> %4$s SEND_CHUNK %5$s %6$d %7$s",
                evsend_time, reqrepID, this.get_name(), Sim_system.get_entity(
                userID).get_name(), movieTag, sequenceNumber, chunk.toString());
        this.write(msg);
    }

    protected void sendLastChunk(int playReqrepID, int userID, String movieTag,
            int seID, TranscodeRequest transcodeRequest) {
        double evsend_time = 0;
        ChunkRequest request = new ChunkRequest(this.get_id(), this.get_id(),
                playReqrepID,
                userID, movieTag, 0, seID, transcodeRequest);
        @SuppressWarnings("unused")
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        int SIZE = 500;
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.SENT_LAST_CHUNK_REP, new IO_data(request, SIZE, userID));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> %4$s SENT_LAST_CHUNK %5$d %6$s",
                evsend_time, reqrepID, this.get_name(), Sim_system.get_entity(
                userID).get_name(),
                playReqrepID,
                movieTag);
        this.write(msg);
    }

    public Sim_stat getStatGTS() {
        return statGTS;
    }

    public void setStatGTS(Sim_stat statGTS) {
        this.statGTS = statGTS;
    }

    public boolean isEnabledCaching() {
        return enabledCaching;
    }

    public void setEnabledCaching(boolean enabledCaching) {
        this.enabledCaching = enabledCaching;
    }
}
