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
 * User.java
 *
 * Created on 15 March 2007, 21.32 by Giovanni Novelli
 *
 * $Id: User.java 563 2008-02-25 17:01:03Z gnovelli $
 *
 */
package net.sf.gap.mc.qagesa.users.impl;

import java.util.List;

import net.sf.gap.GAP;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.mc.QAGESA;
import net.sf.gap.mc.qagesa.agents.services.impl.MuMService;
import net.sf.gap.mc.qagesa.constants.QAGESAEntityTypes;
import net.sf.gap.mc.qagesa.constants.QAGESATags;
import net.sf.gap.mc.qagesa.messages.ChunkRequest;
import net.sf.gap.mc.qagesa.messages.ReFPlayReply;
import net.sf.gap.mc.qagesa.messages.ReFPlayRequest;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import net.sf.gap.mc.qagesa.users.QAGESAUser;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import eduni.simjava.distributions.Sim_negexp_obj;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
import gridsim.net.InfoPacket;
import gridsim.net.Link;

/**
 * @author Giovanni Novelli
 */
public class User extends QAGESAUser {

    public static final int MEASURE_RESPONSE = 1;
    public static final int MEASURE_STREAMING = 2;
    public static final int MEASURE_FIRST = 3;
    private static String distribution="";
    private static double relaxTime;

    public static String getDistribution() {
        return distribution;
    }

    public static void setDistribution(String aDistribution) {
        distribution = aDistribution;
    }
    private int selectedMeasure;
    private int uid;
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between REF_PLAY_REQ and REF_PLAY_REPLY
     */
    private Sim_stat statResponseTime;
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between SENDING_FIRST_CHUNK_REP and SENT_LAST_CHUNK_REP
     */
    private Sim_stat statStreaming;
    /**
     * 
     * Sim_stat object used to measure SERVICE_TIME between SENDING_FIRST_CHUNK_REP and 1st SEND_CHUNK_REQ
     */
    private Sim_stat statFirst;
    /**
     *
     * Movie tag that can be reiterated in subsequent play requests to measure the effect of chunks caching
     * on performances. This tag is considered when field repeated is set to true.
     *
     */
    private String repeatedMovieTag;
    /**
     *
     * Flag used to instruct an User entity instance to reiterate subsequent play requests with the same movie tag.
     * The shared movie tag is maintained in field repeatedMovieTag.
     *
     */
    private boolean repeated;
    /**
     *
     * Field used to instruct an User entit instance about how much max subsequent play requests it has to do.
     *
     */
    private int numRequests;
    /**
     *
     * Flag used to signal to ReF Service to not use its metric to select CE and SE but to do just a random selection of them.
     *
     */
    private boolean randomSelection;

    /**
     *
     * Creates a new instance of User
     *
     * @param name
     *            AgentMiddleware entity name
     * @param trace_flag
     *            GridSim trace flag
     * @throws Exception
     *             This happens when name is null or haven't initialized
     *             GridSim.
     */
    public User(String name, Link link, boolean trace_flag, boolean randomSelection, int numRequests, boolean repeated, String movieTag, int selectedMeasure) throws Exception {
        super(name, link, QAGESAEntityTypes.USER_USER, trace_flag);
        this.relaxTime = QAGESA.relaxTime;
        this.setRandomSelection(randomSelection);
        this.setNumRequests(numRequests);
        this.setRepeated(repeated);
        this.setRepeatedMovieTag(movieTag);
        this.setSelectedMeasure(selectedMeasure);
        switch (this.getSelectedMeasure()) {
            case User.MEASURE_RESPONSE:
                this.setupStatPlay();
                break;
            case User.MEASURE_STREAMING:
                this.setupStatStreaming();
                break;
            case User.MEASURE_FIRST:
                this.setupStatFirst();
                break;
        }
    }

    private void setupStatPlay() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.REF_PLAY_START_REP};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatResponseTime(stat);
    }

    private void setupStatStreaming() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.SENDING_FIRST_CHUNK_REP, QAGESATags.SENT_LAST_CHUNK_REP};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatStreaming(stat);
    }

    private void setupStatFirst() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.SENDING_FIRST_CHUNK_REP, QAGESATags.TRANSCODED_FIRST_CHUNK_REP};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        this.set_stat(stat);
        this.setStatFirst(stat);
    }

    @SuppressWarnings("unchecked")
    private void printStatFirst() {
        List[] list = this.getStatFirst().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        int nd = data.size();
        for (int i = 0; i < nd; i++) {
            double[] times = (double[]) data.toArray()[i];
            QAGESA.outUSER.println("CSV;USERS_FT;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() + ";" + times[0] + ";" + times[1] + ";" + (times[1] - times[0]));
        }
    }

    @SuppressWarnings("unchecked")
    private void printStatStreaming() {
        List[] list = this.getStatStreaming().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        int nd = data.size();
        for (int i = 0; i < nd; i++) {
            double[] times = (double[]) data.toArray()[i];
            QAGESA.outUSER.println("CSV;USERS_ST;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() + ";" + times[0] + ";" + times[1] + ";" + (times[1] - times[0]));
        }
    }

    @SuppressWarnings("unchecked")
    private void printStatResponseTime() {
        List[] list = this.getStatResponseTime().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        int nd = data.size();
        for (int i = 0; i < nd; i++) {
            double[] times = (double[]) data.toArray()[i];
            QAGESA.outUSER.println("CSV;USERS_RT;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() + ";" + times[0] + ";" + times[1] + ";" + (times[1] - times[0]));
        }
    }

    public void printStats() {
        switch (this.getSelectedMeasure()) {
            case User.MEASURE_RESPONSE:
                this.printStatResponseTime();
                break;
            case User.MEASURE_STREAMING:
                this.printStatStreaming();
                break;
            case User.MEASURE_FIRST:
                this.printStatFirst();
                break;
        }
    }

    protected ReFPlayReply playRequest(String movieTag) {
        int SIZE = 500;
        double evsend_time = 0;
        int userID = this.get_id();
        ReFPlayRequest request = new ReFPlayRequest(this.get_id(), this.get_id(), userID, movieTag, this.isRandomSelection());
        @SuppressWarnings("unused")
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.REF_PLAY_REQ, new IO_data(request, SIZE, QAGESAUser.getEntityId("ReF")));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> ReF REF_PLAY_REQUEST %4$s", evsend_time,
                reqrepID, this.get_name(), movieTag);
        this.write(msg);

        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(QAGESATags.REF_PLAY_START_REP);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        ReFPlayReply playReply = ReFPlayReply.get_data(ev);
        request = playReply.getRequest();
        double evrecv_time = GridSim.clock();
        if (playReply.isOk()) {
            msg = String.format(
                    "%1$f %2$d %3$s <-- ReF REF_PLAYSTART_REPLY (SUCCESS) %4$d %5$s",
                    evrecv_time, request.getReqrepID(), this.get_name(),
                    request.getReqrepID(),
                    request.getMovieTag());
        } else {
            msg = String.format(
                    "%1$f %2$d %3$s <-- ReF REF_PLAYSTART_REPLY (FAILURE) %4$d %5$s",
                    evrecv_time, request.getReqrepID(), this.get_name(),
                    request.getReqrepID(),
                    request.getMovieTag());
        }
        this.write(msg);
        if (playReply.isOk()) {
            Sim_event fcev = new Sim_event();
            predicate = new Predicate(QAGESATags.SENDING_FIRST_CHUNK_REP);
            super.sim_get_next(predicate, fcev); // only look for this type of ack
            ChunkRequest chunkRequest = ChunkRequest.get_data(fcev);
            reqrepID = chunkRequest.getReqrepID();
            evrecv_time = GridSim.clock();
            msg = String.format(
                    "%1$f %2$d %3$s <-- %4$s SENDING_FIRST_CHUNK %5$d %6$d",
                    evrecv_time, reqrepID, this.get_name(),
                    Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                    chunkRequest.getPlayReqrepID(),
                    chunkRequest.getSequenceNumber());
            this.write(msg);

            Sim_event tfcev = new Sim_event();
            predicate = new Predicate(QAGESATags.TRANSCODED_FIRST_CHUNK_REP);
            super.sim_get_next(predicate, tfcev); // only look for this type of ack
            ChunkRequest tchunkRequest = ChunkRequest.get_data(tfcev);
            int treqrepID = chunkRequest.getReqrepID();
            double tevrecv_time = GridSim.clock();
            String tmsg = String.format(
                    "%1$f %2$d %3$s <-- %4$s TRANSCODED_FIRST_CHUNK %5$d %6$d",
                    tevrecv_time, treqrepID, this.get_name(),
                    Sim_system.get_entity(tchunkRequest.getSrc_ID()).get_name(),
                    tchunkRequest.getPlayReqrepID(),
                    tchunkRequest.getSequenceNumber());
            this.write(tmsg);
            if (this.getSelectedMeasure() == User.MEASURE_FIRST) {
                sim_completed(fcev);
            }

            if (this.getSelectedMeasure() == User.MEASURE_RESPONSE) {
                sim_completed(ev);
            }

            Sim_event tev = new Sim_event();
            int[] tags = {QAGESATags.SENT_LAST_CHUNK_REP, QAGESATags.SEND_CHUNK_REQ};
            predicate = new Predicate(tags);
            super.sim_get_next(predicate, tev);
            this.processEvent(tev);
            for ( int i = 0; i < 6; i++) {
                Sim_event anev = new Sim_event();
                super.sim_get_next(predicate, anev);
                this.processEvent(anev);
            }
            if (this.getSelectedMeasure() == User.MEASURE_STREAMING) {
                sim_completed(fcev);
            }

            Sim_event perev = new Sim_event();
            predicate = new Predicate(QAGESATags.REF_PLAY_END_REP);
            super.sim_get_next(predicate, perev);
            this.processEvent(perev);

        } else {
            if (this.getSelectedMeasure() == User.MEASURE_RESPONSE) {
                sim_completed(ev);
            }
        }
        return playReply;
    }

    @Override
    public void processOtherEvent(Sim_event ev) {
        switch (ev.get_tag()) {
            case QAGESATags.SENT_LAST_CHUNK_REP:
                ChunkRequest chunkRequest = ChunkRequest.get_data(ev);
                @SuppressWarnings("unused") int playReqrepID = chunkRequest.getPlayReqrepID();
                 int reqrepID = chunkRequest.getReqrepID();
                 double evrecv_time = GridSim.clock();
                 String msg = String.format(
                        "%1$f %2$d %3$s <-- %4$s SENT_LAST_CHUNK %5$d",
                        evrecv_time, reqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID());
                this.write(msg);
                break;
            case QAGESATags.SEND_CHUNK_REQ:
                chunkRequest = ChunkRequest.get_data(ev);
                reqrepID = chunkRequest.getReqrepID();
                evrecv_time = GridSim.clock();
                msg = String.format(
                        "%1$f %2$d %3$s <-- %4$s SEND_CHUNK %5$d %6$d",
                        evrecv_time, reqrepID, this.get_name(),
                        Sim_system.get_entity(chunkRequest.getSrc_ID()).get_name(),
                        chunkRequest.getPlayReqrepID(),
                        chunkRequest.getSequenceNumber());
                this.write(msg);
                break;
            case QAGESATags.REF_PLAY_END_REP:
                 ReFPlayReply playReply = ReFPlayReply.get_data(ev);
                 ReFPlayRequest request = playReply.getRequest();
                evrecv_time = GridSim.clock();
                if (playReply.isOk()) {
                    msg = String.format(
                            "%1$f %2$d %3$s <-- ReF REF_PLAYEND_REPLY (SUCCESS) %4$d %5$s",
                            evrecv_time, request.getReqrepID(), this.get_name(),
                            request.getReqrepID(),
                            request.getMovieTag());
                } else {
                    msg = String.format(
                            "%1$f %2$d %3$s <-- ReF REF_PLAYEND_REPLY (FAILURE) %4$d %5$s",
                            evrecv_time, request.getReqrepID(), this.get_name(),
                            request.getReqrepID(),
                            request.getMovieTag());
                }
                this.write(msg);
                break;
            default:
                break;
        }
    }

    @Override
    public void initWork() {
        this.DoIt();
    }

    private void DoIt() {
        this.pingCEs();
        if (this.isRepeated() && this.getRepeatedMovieTag() == null) {
            String movieTag = MuMService.getMUMTranscodingSet().selectRandomTag();
            this.setRepeatedMovieTag(movieTag);
        }
        double time = User.clock();
        while (User.clock() < (GAP.getEndTime() - relaxTime)) {
            time = User.clock();
            /*
            int neededRequests = scalini(
            GAP.getStartTime(), 
            GAP.getEndTime(),
            5,
            20,
            time);
             */
            if (this.hastoask()) {
                QAGESAStat.incRequests(User.clock());
                boolean success=this.repeatedRandomRequest();
                QAGESAStat.decRequests(User.clock(),success);
            } else {
                super.gridSimHold(0.1);
            }
        }
    }

    private boolean hastoask() {
        boolean result;
        if (!User.distribution.equalsIgnoreCase("throughput")) {
        double time=User.clock();
        int neededRequests = fr(
                GAP.getStartTime(),
                GAP.getEndTime()-relaxTime,
                QAGESAStat.getNumUsers(),
                time);
        result = ((this.getUid() < neededRequests) || (this.getUid()==0));
        } else {
            result = QAGESAStat.getProcessedRequests()<QAGESA.requests;
        }
        return result;
    }
    
    private int fr(double a, double b, int maxusers, double currentTime) {
        int result = 0;
        if (User.distribution.equalsIgnoreCase("linear")) {
            result = linear(a, b, maxusers, currentTime);
        } else if (User.distribution.equalsIgnoreCase("scaled")) {
            result = scaled(a, b, maxusers, currentTime);
        } else if (User.distribution.equalsIgnoreCase("top")) {
            result = maxusers;
        } else {
            result = scaled(a, b, maxusers, currentTime);
        }
        return result;
    }

    private int linear(double a, double b, int maxusers, double currentTime) {
        int result = (int) Math.round(((currentTime - a) / (b - a)) * maxusers);
        return result;
    }

    private int scaled(double a, double b, int maxusers, double currentTime) {
        double center = (b-a)/2.0+a;
        int result;
        if (currentTime>center) {
            result=maxusers;
        } else {
            result = (int) Math.round(((currentTime - a) / (center - a)) * maxusers);
        }
        //System.out.println(a + " " + b + " " + center + " " + currentTime +": " + this.getUid() + " " + result + " with current " + QAGESAStat.getRequests());
        return result;
    }

    private void oldDoIt() {
        this.pingCEs();
        if (this.isRepeated() && this.getRepeatedMovieTag() == null) {
            String movieTag = MuMService.getMUMTranscodingSet().selectRandomTag();
            this.setRepeatedMovieTag(movieTag);
        }
        @SuppressWarnings("unused")
        double start_clock = User.clock();
        for ( int cycle = 1; cycle <= this.getNumRequests(); cycle++) {
            int ratio = QAGESAStat.getNumUsers() / this.getNumRequests();
            int maxUsers = cycle * ratio;
            double mean_interarrival = (QAGESAStat.getNumUsers() * 1.0) / maxUsers;
            Sim_negexp_obj negexp = new Sim_negexp_obj("interval", mean_interarrival);
            double diff_time = negexp.sample();
            double start_time = diff_time;
            super.gridSimHold(start_time);
            QAGESAStat.incRequests(User.clock());
            boolean success=this.repeatedRandomRequest();
            QAGESAStat.decRequests(User.clock(),success);
        }
        for ( int cycle = 1; cycle <= this.getNumRequests(); cycle++) {
            @SuppressWarnings("unused")
            int ratio = QAGESAStat.getNumUsers() / this.getNumRequests();
            Sim_negexp_obj negexp = new Sim_negexp_obj("interval", 1.0);
            double diff_time = negexp.sample();
            double start_time = diff_time;
            super.gridSimHold(start_time);
            QAGESAStat.incRequests(User.clock());
            boolean success=this.repeatedRandomRequest();
            QAGESAStat.decRequests(User.clock(),success);
        }
    }

    private boolean repeatedRandomRequest() {
        if (this.isRepeated()) {
            @SuppressWarnings("unused")
            ReFPlayReply playReply = this.playRequest(this.getRepeatedMovieTag());
            return playReply.isOk();
        } else {
            String movieTag = MuMService.getMUMTranscodingSet().selectRandomTag();
            @SuppressWarnings("unused")
            ReFPlayReply playReply = this.playRequest(movieTag);
            return playReply.isOk();
        }
    }

    public Sim_stat getStatResponseTime() {
        return statResponseTime;
    }

    public void setStatResponseTime(Sim_stat statResponseTime) {
        this.statResponseTime = statResponseTime;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public void setNumRequests(int numRequests) {
        this.numRequests = numRequests;
    }

    private String getRepeatedMovieTag() {
        return repeatedMovieTag;
    }

    private void setRepeatedMovieTag(String repeatedMovieTag) {
        this.repeatedMovieTag = repeatedMovieTag;
    }

    private boolean isRepeated() {
        return repeated;
    }

    private void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    public boolean isRandomSelection() {
        return randomSelection;
    }

    public void setRandomSelection(boolean randomSelection) {
        this.randomSelection = randomSelection;
    }

    public Sim_stat getStatStreaming() {
        return statStreaming;
    }

    public void setStatStreaming(Sim_stat statStreaming) {
        this.statStreaming = statStreaming;
    }

    public int getSelectedMeasure() {
        return selectedMeasure;
    }

    public void setSelectedMeasure(int selectedMeasure) {
        this.selectedMeasure = selectedMeasure;
    }

    private void pingCEs() {
        for (int i = 0; i < this.getVirtualOrganization().getNumCEs(); i++) {
            int ceID = this.getVirtualOrganization().getCEs().get(i).get_id();
            InfoPacket pkt = super.pingBlockingCall(ceID,
                    50);
            if (pkt != null) {
                this.getVirtualOrganization().getPlatform().getNetworkMonitor().getNetworkMap().addRTT(ceID, this.get_id(), pkt);
            }
        }
    }

    public Sim_stat getStatFirst() {
        return statFirst;
    }

    public void setStatFirst(Sim_stat statFirst) {
        this.statFirst = statFirst;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
