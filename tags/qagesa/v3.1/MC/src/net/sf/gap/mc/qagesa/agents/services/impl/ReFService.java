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
 * ReFService.java
 *
 * Created on 15 March 2007, 18:19 by Giovanni Novelli
 *
 * $Id: ReFService.java 858 2008-03-13 15:32:58Z gnovelli $
 *
 */
package net.sf.gap.mc.qagesa.agents.services.impl;

import java.io.*;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;

import junit.framework.Assert;
import net.sf.gap.agents.middleware.AbstractAgentPlatform;
import net.sf.gap.agents.predicates.Predicate;
import net.sf.gap.agents.services.PlatformService;
import net.sf.gap.agents.services.impl.gis.GISEntry;
import net.sf.gap.agents.services.impl.gis.GISRepository;
import net.sf.gap.agents.services.impl.nm.NetworkMap;
import net.sf.gap.agents.services.impl.nm.RTTMap;
import net.sf.gap.constants.Tags;
import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.mc.QAGESA;
import net.sf.gap.mc.qagesa.agents.TranscodingAgent;
import net.sf.gap.mc.qagesa.agents.services.impl.al.AgentsLocatorDirectory;
import net.sf.gap.mc.qagesa.agents.services.impl.mum.GEList;
import net.sf.gap.mc.qagesa.agents.services.impl.ref.ReFCouple;
import net.sf.gap.mc.qagesa.agents.services.impl.ref.ReFProximityList;
import net.sf.gap.mc.qagesa.agents.services.impl.ref.ReFTriple;
import net.sf.gap.mc.qagesa.constants.QAGESAEntityTypes;
import net.sf.gap.mc.qagesa.constants.QAGESATags;
import net.sf.gap.mc.qagesa.grid.components.QAGESAGridElement;
import net.sf.gap.mc.qagesa.messages.MuMSearchReply;
import net.sf.gap.mc.qagesa.messages.MuMSearchRequest;
import net.sf.gap.mc.qagesa.messages.ReFPlayReply;
import net.sf.gap.mc.qagesa.messages.ReFPlayRequest;
import net.sf.gap.mc.qagesa.messages.TranscodeReply;
import net.sf.gap.mc.qagesa.messages.TranscodeRequest;
import net.sf.gap.mc.qagesa.stats.QAGESAStat;
import net.sf.gap.messages.impl.AgentReply;
import net.sf.gap.messages.impl.AgentRequest;
import net.sf.gap.messages.impl.GISReply;
import net.sf.gap.messages.impl.GISRequest;
import net.sf.gap.messages.impl.NetworkMapReply;
import net.sf.gap.messages.impl.NetworkMapRequest;
import eduni.simjava.*;
import gridsim.*;
import gridsim.net.InfoPacket;

/**
 * ReF service
 *
 *
 *
 * @author Giovanni Novelli
 * @see net.p2pgrid.gap.agents.services.PlatformService
 */
public class ReFService extends PlatformService {

    private double nmCacheLifetime;
    private NetworkMap networkMapCache;
    private double gisCacheLifetime;
    private GISRepository gisRepositoryCache;
    private AgentsLocatorDirectory alDirectory;
    /**
     * 
     * Sim_stat object used to collect measures between:
     *  - the receival of play request (tag REF_PLAY_REQ)
     *  - and the send of a play start reply (tag REF_PLAY_REP_START)
     */
    private Sim_stat statPlayStart;
    private Uniform_int rand;

    public void setGISCacheLifetime(double gisCacheLifetime) {
        this.gisCacheLifetime = gisCacheLifetime;
    }

    /**
     * @param ap
     * @param trace_flag
     * @throws Exception
     */
    public ReFService(AbstractAgentPlatform ap, boolean trace_flag,
            double celCacheLifetime, double nmCacheLifetime) throws Exception {
        this(ap, "ReF", trace_flag, celCacheLifetime, nmCacheLifetime);
    }

    public ReFService(AbstractAgentPlatform ap, String name, boolean trace_flag,
            double celCacheLifetime, double nmCacheLifetime) throws Exception {
        super(ap, name, trace_flag);
        this.setupStatPlayStart();
        this.setGISCacheLifetime(celCacheLifetime);
        this.setNMCacheLifetime(nmCacheLifetime);
        rand = new Uniform_int("prop_as");
    }    
    private void setupStatPlayStart() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.REF_PLAY_REQ};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
        stat.add_measure(Sim_stat.RESIDENCE_TIME);
        stat.add_measure(Sim_stat.ARRIVAL_RATE);
        stat.add_measure(Sim_stat.QUEUE_LENGTH);
        stat.add_measure(Sim_stat.WAITING_TIME);
        stat.add_measure(Sim_stat.UTILISATION);
        stat.add_measure(Sim_stat.THROUGHPUT);
        this.set_stat(stat);
        this.setStatPlayStart(stat);
    }

    @SuppressWarnings("unchecked")
    private void printStatPlayStart() {
        List[] list = this.getStatPlayStart().get_data();
        List datas = list[1];
        List data = (List) datas.toArray()[0];
        int nd = data.size();
        for (int i = 0; i < nd; i++) {
            double[] times = (double[]) data.toArray()[i];
            /*
            QAGESA.outReF_RT.println(
            "CSV;ReF_RT;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() 
            + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() 
            + ";" + (times[0]-QAGESA.getStartTime()) + ";" + (times[1]-QAGESA.getStartTime()) + ";" + (times[1] - times[0]));
             */
            int rep = QAGESAStat.getReplication();
            int nu = QAGESAStat.getNumUsers();
            int ca = 0;
            if (QAGESAStat.isCachingEnabled()) {
                ca = 1;
            }
            int wm = QAGESAStat.getWhichMeasure();
            double ta = (times[0] - QAGESA.getStartTime());
            double tb = (times[1] - QAGESA.getStartTime());
            double dt = (times[1] - times[0]);
            QAGESA.outReF_RT.printf(
                    "CSV\tReF_RT\t%2d\t%4d\t%d\t%d\t%s\t%6.4f\t%6.4f\t%6.4f\n",
                    rep,
                    nu,
                    ca,
                    wm,
                    this.get_name(),
                    ta,
                    tb,
                    dt);
        }
        QAGESA.outReF_RT.flush();
    }

    @Override
    public void initialize() throws Exception {
        super.initialize();
    }

    @Override
    protected void dispose() {
        this.printStatPlayStart();
    }

    private void updateGISCache() {
        GISReply gisReply = null;
        if (this.getGisRepositoryCache() == null) {
            gisReply = this.searchGIS();
            this.setGisRepositoryCache(gisReply.getGIS());
        } else if ((super.clock() - this.getGisRepositoryCache().getLastRequestTime()) > (this.getGISCacheLifetime())) {
            gisReply = this.searchGIS();
            this.setGisRepositoryCache(gisReply.getGIS());
        }
    }

    private void updateNMCache() {
        NetworkMapReply nmReply = null;
        if (this.getNetworkMapCache() == null) {
            nmReply = this.queryNM();
            this.setNetworkMapCache(nmReply.getNetworkMap());
        } else if ((super.clock() - this.getNetworkMapCache().getLastRequestTime()) > (this.getNMCacheLifetime())) {
            nmReply = this.queryNM();
            this.setNetworkMapCache(nmReply.getNetworkMap());
        }
    }

    private AgentReply activateAgents(ReFPlayRequest playRequest, int userID, String movieTag, int ceID, int seID) {
        AgentReply agentReply;
        QAGESAGridElement ce = (QAGESAGridElement) Sim_system.get_entity(ceID);
        if (!QAGESA.reuseagents) {
            agentReply = this.submitAgent(QAGESAEntityTypes.SERVER_PROXY,
                    ceID,
                    100000);
        } else if (ce.getLocalDirectory().getFreeAgents() > 0) {
            agentReply = this.submitAgent(QAGESAEntityTypes.SERVER_PROXY,
                    ceID,
                    100000);
        } else {
            AgentRequest agentRequest = new AgentRequest(this.get_id(), this.get_id(), null,
                    ceID, -1, QAGESAEntityTypes.SERVER_PROXY, 100000, ceID,
                    QAGESAEntityTypes.NOBODY);
            agentReply = new AgentReply(QAGESATags.AGENT_RUN_REQ, false, agentRequest);
        }
        int agentID = 0;
        if (!agentReply.isOk() && QAGESA.reuseagents) {
            Iterator<Integer> it = this.getAlDirectory().getAceMap().keySet().iterator();
            int npe = ce.getNumPE();
            int ai = rand.sample(npe);
            int na = -1;
            while (it.hasNext()) {
                Integer aid = it.next();
                Integer ceid = this.getAlDirectory().getAceMap().get(aid);
                agentID = aid;
                if (ceid == ceID) {
                    na++;
                }
                if ((ceid == ceID) && (na == ai)) {
                    agentReply.setOk(true);
                    agentReply.getRequest().setDst_agentID(agentID);
                    this.sendPlayStartReply(agentID, userID, playRequest, true);
                    System.out.println("AGENT_ID: " + aid + " for ai = " + ai);
                    break;
                }
            }
        } else if (agentReply.isOk()) {
            agentID = agentReply.getRequest().getDst_agentID();
            this.sendPlayStartReply(agentID, userID, playRequest, agentReply.isOk());
            agentID = agentReply.getRequest().getDst_agentID();
            if (this.getAlDirectory() == null) {
                this.setAlDirectory(new AgentsLocatorDirectory());
            }
            this.getAlDirectory().addAgent(agentID, agentReply.getRequest());
            this.getAlDirectory().addAgent(agentID, ceID);
        }
        if (agentReply.isOk()) {
            TranscodeRequest transcodeRequest = new TranscodeRequest(
                    this.get_id(),
                    this.get_id(),
                    playRequest, seID, 1.0);
            @SuppressWarnings("unused")
            int reqrepID = transcodeRequest.getReqrepID();
            int SIZE = 500;
            playRequest.setTranscodeRequest(transcodeRequest);
            super.send(super.output, GridSimTags.SCHEDULE_NOW,
                    QAGESATags.TRANSCODE_CHUNKS_REQ, new IO_data(transcodeRequest, SIZE, agentID));
            double evsend_time = GridSim.clock();
            String msg = String.format(
                    "%1$f %2$d %3$s --> %4$s TRANSCODE_CHUNKS_REQUEST %5$d %6$s",
                    evsend_time,
                    reqrepID,
                    this.get_name(),
                    Sim_system.get_entity(agentID).get_name(),
                    userID,
                    movieTag);
            this.write(msg);
        }
        return agentReply;
    }

    private ReFProximityList computeProximities(int userID) {
        ReFProximityList list = new ReFProximityList();
        Iterator<Integer> it = this.getGisRepositoryCache().keySet().iterator();
        double sumUserLatency = 0.0;
        int countedULatencies = 0;
        while (it.hasNext()) {
            int geID = it.next();
            GISEntry gisEntry = this.getGisRepositoryCache().get(geID);
            double cpuLoad = (gisEntry.getLoad());
            double ioLoad = gisEntry.getIoLoad();
            QAGESAGridElement ge = (QAGESAGridElement) Sim_system.get_entity(geID);
            double load;
            if (!ge.isSE()) {
                load = cpuLoad;
            } else {
                load = ioLoad;
            }
            RTTMap rttMap = this.getNetworkMapCache().get(geID);
            Iterator<Integer> itnm = rttMap.keySet().iterator();
            InfoPacket userPkt = rttMap.get(userID);
            double userLatency;
            countedULatencies++;
            if (userPkt != null) {
                userLatency = userPkt.getTotalResponseTime() / 2.0;
                sumUserLatency += userLatency;
            } else {
                userLatency = sumUserLatency / countedULatencies;
            }
            while (itnm.hasNext()) {
                int eid = itnm.next();
                if (Sim_system.get_entity(eid) instanceof QAGESAGridElement) {
                    ge = (QAGESAGridElement) Sim_system.get_entity(eid);
                    if (ge.isSE()) {
                        int seID = eid;
                        InfoPacket pkt = rttMap.get(seID);
                        double latency = pkt.getTotalResponseTime() / 2.0;
                        ReFTriple triple = 
                                new ReFTriple(
                                load, latency + userLatency, 
                                geID, seID, 
                                QAGESA.alfaLoad,
                                QAGESA.betaLatency
                                );
                        list.add(triple);
                    }
                }
            }
        }
        return list;
    }

    private boolean heuristicalSelection(Sim_event ev) {
        boolean success = false;
        ReFPlayRequest playRequest = ReFPlayRequest.get_data(ev);
        int playReqrepID = playRequest.getReqrepID();
        int userID = playRequest.getSrc_ID();
        String movieTag = playRequest.getMovieTag();

        ReFCouple choice = this.heuristicChoice(movieTag, userID);
        int ceID = choice.getComputingElementID();
        int seID = choice.getStorageElementID();
        AgentReply agentReply = this.activateAgents(playRequest, userID, movieTag, ceID, seID);

        if ((agentReply == null)) {
            this.sendPlayStartReply(-1, userID, playRequest, false);
            success = false;
        } else if (!agentReply.isOk()) {
            this.sendPlayStartReply(-1, userID, playRequest, false);
            success = false;
        } else {
            success = true;
        }
        super.sim_completed(ev);
        return success;
    }

    private double askInputLoad(int geid) {
        // Get Resource Dynamic information
        send(super.output, 0.0, Tags.INPUT_DYNAMICS,
                new IO_data(new Integer(super.get_id()), 4, this.get_id()));
        double load;
        try {
            // waiting for a response from system GIS
            Sim_type_p tag = new Sim_type_p(Tags.INPUT_DYNAMICS);

            // only look for this type of ack
            Sim_event ev = new Sim_event();
            super.sim_get_next(tag, ev);
            Accumulator accLoad = (Accumulator) ev.get_data();
            load = accLoad.getMean();
        } catch (Exception e) {
            load = 0.5;
        }
        return load;
    }

    private double askOutputLoad(int geid) {
        // Get Resource Dynamic information
        send(super.output, 0.0, Tags.OUTPUT_DYNAMICS,
                new IO_data(new Integer(super.get_id()), 4, this.get_id()));
        double load;
        try {
            // waiting for a response from system GIS
            Sim_type_p tag = new Sim_type_p(Tags.OUTPUT_DYNAMICS);

            // only look for this type of ack
            Sim_event ev = new Sim_event();
            super.sim_get_next(tag, ev);
            Accumulator accLoad = (Accumulator) ev.get_data();
            load = accLoad.getMean();
        } catch (Exception e) {
            load = 0.5;
        }
        return load;
    }

    private ReFCouple heuristicChoice(String movieTag, int userID) {
        ReFProximityList list;
        list = this.computeProximities(userID);
        Iterator<ReFTriple> it;
        it = list.iterator();
        ReFCouple choice = null;
        if (it.hasNext()) {
            ReFTriple triple = it.next();
            choice = triple.getCouple();
        //QAGESAGridElement se = (QAGESAGridElement) Sim_system.get_entity(choice.getStorageElementID());
        //boolean haveMovie = se.containsSequence(movieTag);
        //if (haveMovie) break;
        }
        /*
        int geID = choice.getComputingElementID();
        int seID = choice.getStorageElementID();
        }
        double seLoad = triple.getProximity();
        /*
        double inputLoad = this.askInputLoad(seID);
        double outputLoad = this.askOutputLoad(seID);
        double ioLoad = inputLoad + outputLoad;
        double seIOLoad = ioLoad;
         */
        /*
        double load = seLoad;
        double minLoad = seLoad;
        int minseID = seID;
        while (it.hasNext()) {
        ReFTriple aTriple = it.next();
        int aceID = aTriple.getCouple().getComputingElementID();
        if (aceID==geID) {
        int aseID = aTriple.getCouple().getStorageElementID();
        QAGESAGridElement se = (QAGESAGridElement) Sim_system.get_entity(aseID);
        boolean haveMovie = se.containsSequence(movieTag);
        if (haveMovie) {
        load = aTriple.getProximity();
        if (load<minLoad) {
        minLoad = load;
        minseID = aseID;
        }
        }
        }
        }
        if (minLoad>seLoad) {
        minseID = seID;
        minLoad = seLoad;
        }
        choice.setStorageElementID(minseID);
        }
         */
        return choice;
    }

    private boolean randomSelection(Sim_event ev) {
        boolean success = false;
        ReFPlayRequest playRequest = ReFPlayRequest.get_data(ev);
        int playReqrepID = playRequest.getReqrepID();
        int userID = playRequest.getSrc_ID();
        String movieTag = playRequest.getMovieTag();

        ReFCouple choice = this.randomChoice(movieTag, userID);
        int ceID = choice.getComputingElementID();
        int seID = choice.getStorageElementID();
        AgentReply agentReply = this.activateAgents(playRequest, userID, movieTag, ceID, seID);

        if ((agentReply == null)) {
            this.sendPlayStartReply(-1, userID, playRequest, false);
            success = false;
        } else if (!agentReply.isOk()) {
            this.sendPlayStartReply(-1, userID, playRequest, false);
            success = false;
        } else {
            success = true;
        }
        super.sim_completed(ev);
        return success;
    }

    private ReFCouple randomChoice(String movieTag, int userID) {
        GEList seList = this.requestGEList(movieTag).getGelist();
        Uniform_int r = new Uniform_int("ReFService_rand");
        ReFCouple choice;
        if (!QAGESA.ceGIS) {
            int ceidx = r.sample(this.getAgentPlatform().getVirtualOrganization().getNumCEs());
            int seidx = r.sample(seList.size());
            int ceID = this.getAgentPlatform().getVirtualOrganization().getCEs().get(ceidx).get_id();
            int seID = seList.get(seidx);
            choice = new ReFCouple(ceID, seID);
        } else {
            ReFCouple ceChoice = this.heuristicChoice(movieTag, userID);
            int ceID = ceChoice.getComputingElementID();
            int seidx = r.sample(seList.size());
            int seID = seList.get(seidx);
            choice = new ReFCouple(ceID, seID);
        }
        return choice;
    }

    private void processPlayRequest(Sim_event ev) {
        ReFPlayRequest playRequest = ReFPlayRequest.get_data(ev);
        boolean success = false;
        int count = 0;
        while (!success && count < QAGESA.retryCount) {
            count++;
            if (!playRequest.isRandomSelection()) {
                this.updateGISCache();
                this.updateNMCache();
                success = this.heuristicalSelection(ev);
            } else {
                if (QAGESA.ceGIS) {
                    this.updateGISCache();
                    this.updateNMCache();
                }
                success = this.randomSelection(ev);
            }
        }
    }

    private void sendPlayStartReply(int agentID, int userID, ReFPlayRequest playRequest, boolean flag) {
        int SIZE = 500;
        double evsend_time = 0;
        ReFPlayReply playReply = new ReFPlayReply(agentID, QAGESATags.REF_PLAY_REQ, flag, playRequest);
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.REF_PLAY_REP_START, new IO_data(playReply, SIZE, userID));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d ReF --> %3$s REF_PLAYSTART_REPLY %4$d %5$s",
                evsend_time, playRequest.getReqrepID(), Sim_system.get_entity(userID).get_name(),
                playRequest.getReqrepID(),
                playRequest.getMovieTag());
        this.write(msg);

    }

    private void sendPlayEndReply(ReFPlayReply playReply) {
        int SIZE = 500;
        @SuppressWarnings("unused")
        double evsend_time = 0;
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.REF_PLAY_REP_END, new IO_data(playReply, SIZE, playReply.getRequest().getUserID()));
        evsend_time = GridSim.clock();
    }

    @Override
    public void processOtherEvent(Sim_event ev) {
        switch (ev.get_tag()) {
            case QAGESATags.REF_PLAY_REQ:
                this.processPlayRequest(ev);
                break;

            case QAGESATags.TRANSCODE_CHUNKS_REP:
                TranscodeReply reply = TranscodeReply.get_data(ev);
                int agentID = reply.getAgentID();
                ReFPlayReply playReply = new ReFPlayReply(agentID, QAGESATags.REF_PLAY_REQ, reply.isOk(), reply.getRequest().getPlayRequest());
                TranscodingAgent agent = (TranscodingAgent) Sim_system.get_entity(agentID);
                QAGESAGridElement ce = (QAGESAGridElement) Sim_system.get_entity(agent.getResourceID());
                //if (!QAGESA.reuseagents || (ce.getLocalDirectory().getFreeAgents() > 0)) {
                if (!QAGESA.reuseagents) {
                    AgentRequest agentRequest = this.getAlDirectory().removeAgent(reply.getAgentID());
                    this.killAgent(agentRequest);
                }
                this.sendPlayEndReply(playReply);
                break;

            default:
                break;
        }
    }

    protected MuMSearchReply requestGEList(String movieTag) {
        int SIZE = 500;
        double evsend_time = 0;
        MuMSearchRequest request = new MuMSearchRequest(this.get_id(), this.get_id(), movieTag);
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.MUM_SEARCH_REQ, new IO_data(request, SIZE, super.getEntityId("MuM")));
        evsend_time = GridSim.clock();
        String msg = String.format(
                "%1$f %2$d %3$s --> MuM MUM_SEARCH_REQUEST %4$s", evsend_time,
                reqrepID, this.get_name(), movieTag);
        this.write(msg);

        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(QAGESATags.MUM_SEARCH_REP);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        MuMSearchReply reply = MuMSearchReply.get_data(ev);
        Assert.assertEquals(requestID, reply.getRequestID());
        Assert.assertEquals(QAGESATags.MUM_SEARCH_REQ, reply.getRequestTAG());
        Assert.assertEquals(QAGESATags.MUM_SEARCH_REP, ev.get_tag());
        double evrecv_time = GridSim.clock();
        if (reply.isOk()) {
            msg = String.format(
                    "%1$f %2$d %3$s <-- MuM MUM_SEARCH_REPLY %4$s %5$s",
                    evrecv_time, reqrepID, this.get_name(), movieTag, reply.getGelist());
        } else {
            msg = String.format(
                    "%1$f %2$d %3$s <-- MuM MUM_SEARCH_REPLY %4$s %5$s",
                    evrecv_time, reqrepID, this.get_name(), movieTag, "NONE");
        }
        this.write(msg);
        return reply;
    }

    protected GISReply searchGIS() {
        int SIZE = 500;
        double evsend_time = 0;
        GISRequest request = new GISRequest(this.get_id(), this.get_id());
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        int gis_id = super.getEntityId("GISService");
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.GIS_REQUEST, new IO_data(request, SIZE, gis_id));
        evsend_time = GridSim.clock();
        String msg = String.format("%1$f %2$d %3$s --> CEL CEL_SEARCH_REQUEST",
                evsend_time, reqrepID, this.get_name());
        this.write(msg);

        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(QAGESATags.GIS_REPLY);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        double evrecv_time = GridSim.clock();
        GISReply reply = GISReply.get_data(ev);
        Assert.assertEquals(requestID, reply.getRequestID());
        Assert.assertEquals(QAGESATags.GIS_REQUEST, reply.getRequestTAG());
        Assert.assertEquals(QAGESATags.GIS_REPLY, ev.get_tag());
        msg = String.format("%1$f %2$d %3$s <-- CEL CEL_SEARCH_REPLY",
                evrecv_time, reqrepID, this.get_name());
        this.write(msg);
        return reply;
    }

    protected NetworkMapReply queryNM() {
        int SIZE = 500;
        double evsend_time = 0;
        NetworkMapRequest request = new NetworkMapRequest(this.get_id(), this.get_id());
        int requestID = request.getRequestID();
        int reqrepID = request.getReqrepID();
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                Tags.NM_NETWORKMAP_REQ, new IO_data(request, SIZE, this.getAgentPlatform().get_id()));
        evsend_time = GridSim.clock();
        String msg = String.format("%1$f %2$d %3$s --> NM NM_NETWORKMAP_REQ",
                evsend_time, reqrepID, this.get_name());
        this.write(msg);

        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(Tags.NM_NETWORKMAP_REP);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        NetworkMapReply reply = NetworkMapReply.get_data(ev);
        Assert.assertEquals(requestID, reply.getRequestID());
        Assert.assertEquals(Tags.NM_NETWORKMAP_REQ, reply.getRequestTAG());
        Assert.assertEquals(Tags.NM_NETWORKMAP_REP, ev.get_tag());
        double evrecv_time = GridSim.clock();
        msg = String.format("%1$f %2$d %3$s <-- NM NM_NETWORKMAP_REP",
                evrecv_time, reqrepID, this.get_name());
        this.write(msg);
        return reply;
    }

    public Sim_stat getStatPlayStart() {
        return statPlayStart;
    }

    public void setStatPlayStart(Sim_stat statPlayStart) {
        this.statPlayStart = statPlayStart;
    }

    public GISRepository getGisRepositoryCache() {
        return gisRepositoryCache;
    }

    public void setGisRepositoryCache(GISRepository gisRepositoryCache) {
        this.gisRepositoryCache = gisRepositoryCache;
    }

    public double getGISCacheLifetime() {
        return gisCacheLifetime;
    }

    public double getNMCacheLifetime() {
        return nmCacheLifetime;
    }

    public void setNMCacheLifetime(double nmCacheLifetime) {
        this.nmCacheLifetime = nmCacheLifetime;
    }

    public NetworkMap getNetworkMapCache() {
        return networkMapCache;
    }

    public void setNetworkMapCache(NetworkMap networkMapCache) {
        this.networkMapCache = networkMapCache;
    }

    public AgentsLocatorDirectory getAlDirectory() {
        return alDirectory;
    }

    public void setAlDirectory(AgentsLocatorDirectory alDirectory) {
        this.alDirectory = alDirectory;
    }

    public Uniform_int getRand() {
        return rand;
    }

    public void setRand(Uniform_int rand) {
        this.rand = rand;
    }
}



