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
 * $Id: ReFService.java 612 2008-02-28 10:06:47Z gnovelli $
 *
 */
package net.sf.gap.mc.qagesa.agents.services.impl;

import java.io.*;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import net.sf.gap.GAP;
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
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;
import gridsim.GridSim;
import gridsim.GridSimTags;
import gridsim.IO_data;
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
     *  - and the send of a play start reply (tag REF_PLAY_START_REP)
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
        super(ap, "ReF", trace_flag);
        this.setupStatPlayStart();
        this.setGISCacheLifetime(celCacheLifetime);
        this.setNMCacheLifetime(nmCacheLifetime);
    }

    private void setupStatPlayStart() {
        Sim_stat stat = new Sim_stat();
        int[] tags = {QAGESATags.REF_PLAY_REQ};
        stat.measure_for(tags);
        stat.add_measure(Sim_stat.SERVICE_TIME);
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
            QAGESA.outReF_RT.println("CSV;ReF_RT;" + QAGESAStat.getReplication() + ";" + QAGESAStat.getNumUsers() + ";" + QAGESAStat.isCachingEnabled() + ";" + QAGESAStat.getWhichMeasure() + ";" + this.get_name() + ";" + times[0] + ";" + times[1] + ";" + (times[1] - times[0]));
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

    private AgentReply activateAgents(Sim_event ev, ReFPlayRequest playRequest, int playReqrepID, int userID, String movieTag, int ceID, int seID) {
        AgentReply agentReply = this.submitAgent(QAGESAEntityTypes.SERVER_PROXY,
                ceID,
                100000);
        int agentID = -1;
        if (!agentReply.isOk()) {
            Iterator<Integer> it = this.getAlDirectory().getAceMap().keySet().iterator();
            while (it.hasNext()) {
                Integer aid = it.next();
                Integer ceid = this.getAlDirectory().getAceMap().get(aid);
                if (ceid == ceID) {
                    agentID = aid;
                    agentReply.setOk(true);
                    agentReply.getRequest().setDst_agentID(agentID);
                    this.sendPlayStartReply(userID, playRequest, true);
                    break;
                }
            }
        } else {
            this.sendPlayStartReply(userID, playRequest, agentReply.isOk());
            agentID = agentReply.getRequest().getDst_agentID();
            if (this.getAlDirectory() == null) {
                this.setAlDirectory(new AgentsLocatorDirectory());
            }
            this.getAlDirectory().addAgent(agentID, agentReply.getRequest());
            this.getAlDirectory().addAgent(agentID, ceID);
        }
        TranscodeRequest transcodeRequest = new TranscodeRequest(
                this.get_id(),
                this.get_id(),
                playRequest, seID);
        @SuppressWarnings("unused")
        int requestID = transcodeRequest.getRequestID();
        int reqrepID = transcodeRequest.getReqrepID();
        int SIZE = 500;
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
        return agentReply;
    }

    private ReFProximityList computeProximities(int userID) {
        ReFProximityList list = new ReFProximityList();
        Iterator<Integer> it = this.getGisRepositoryCache().keySet().iterator();
        double sumUserLatency = 0.0;
        int countedULatencies = 0;
        while (it.hasNext()) {
            int ceID = it.next();
            GISEntry gisEntry = this.getGisRepositoryCache().get(ceID);
            int numFreeAgents = gisEntry.getNumFreeAgents();
            //if (numFreeAgents>0) {
            int totalMIPS = gisEntry.getTotalMIPS();
            //double load = (gisEntry.getLoad().getMean())*totalMIPS;
            double load = 1.0 - ((gisEntry.getNumFreePEs() * 1.0) / (gisEntry.getNumPEs() * 1.0));
            double weigth = load * totalMIPS;
            RTTMap rttMap = this.getNetworkMapCache().get(ceID);
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
                    QAGESAGridElement ge = (QAGESAGridElement) Sim_system.get_entity(eid);
                    if (ge.isSE()) {
                        int seID = eid;
                        InfoPacket pkt = rttMap.get(seID);
                        double latency = pkt.getTotalResponseTime() / 2.0;
                        ReFTriple triple = new ReFTriple(weigth, latency + userLatency, ceID, seID);
                        list.add(triple);
                    }
                }
            }
        }
        return list;
    }

    private void processPlayRequest(Sim_event ev) {

        ReFPlayRequest playRequest = ReFPlayRequest.get_data(ev);
        int playReqrepID = playRequest.getReqrepID();
        int userID = playRequest.getSrc_ID();
        String movieTag = playRequest.getMovieTag();
        GEList seList = this.requestGEList(movieTag).getGelist();
        int ceID = -1;
        int seID = -1;
        AgentReply agentReply = null;
        boolean doing;
        int maxRetryCount;
        int retryCount;
        if (!playRequest.isRandomSelection()) {
            this.updateGISCache();
            this.updateNMCache();
            ReFProximityList list;
            list = this.computeProximities(userID);
            Iterator<ReFTriple> it;
            it = list.iterator();
            doing = false;
            maxRetryCount = 3;
            retryCount = 0;
            while (it.hasNext() && (retryCount < maxRetryCount) && !doing) {
                //while (it.hasNext() &&  !doing) {
                ReFTriple triple = it.next();
                ReFCouple couple = triple.getCouple();
                ceID = couple.getComputingElementID();
                seID = couple.getStorageElementID();
                agentReply = this.activateAgents(ev, playRequest, playReqrepID, userID, movieTag, ceID, seID);
                doing = agentReply.isOk();
                retryCount++;
            //}
            }
        }
        //if ((playRequest.isRandomSelection()) || (agentReply == null)) {
        doing = false;
        maxRetryCount = 3;
        retryCount = 0;
        while ((playRequest.isRandomSelection()) && (retryCount < maxRetryCount) && !doing) {
            Uniform_int r = new Uniform_int("ReFService");
            int ceidx = r.sample(this.getAgentPlatform().getVirtualOrganization().getNumCEs());
            int seidx = r.sample(seList.size());
            ceID = this.getAgentPlatform().getVirtualOrganization().getCEs().get(ceidx).get_id();
            seID = seList.get(seidx);
            agentReply = this.activateAgents(ev, playRequest, playReqrepID, userID, movieTag, ceID, seID);
            doing = agentReply.isOk();
            retryCount++;
        }
        if ((agentReply == null)) {
            this.sendPlayStartReply(userID, playRequest, false);
        } else if (!agentReply.isOk()) {
            this.sendPlayStartReply(userID, playRequest, false);
        }
        super.sim_completed(ev);
    }

    private void sendPlayStartReply(int userID, ReFPlayRequest playRequest, boolean flag) {
        int SIZE = 500;
        double evsend_time = 0;
        ReFPlayReply playReply = new ReFPlayReply(QAGESATags.REF_PLAY_REQ, flag, playRequest);
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.REF_PLAY_START_REP, new IO_data(playReply, SIZE, userID));
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
                QAGESATags.REF_PLAY_END_REP, new IO_data(playReply, SIZE, playReply.getRequest().getUserID()));
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
                @SuppressWarnings("unused") int agentID = reply.getAgentID();
                 ReFPlayReply playReply = new ReFPlayReply(QAGESATags.REF_PLAY_REQ, reply.isOk(), reply.getRequest().getPlayRequest());
                /*
                AgentRequest agentRequest = this.getAlDirectory().removeAgent(reply.getAgentID());
                this.killAgent(agentRequest);
                 */
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
        super.send(super.output, GridSimTags.SCHEDULE_NOW,
                QAGESATags.GIS_REQUEST, new IO_data(request, SIZE, super.getEntityId("GISService")));
        evsend_time = GridSim.clock();
        String msg = String.format("%1$f %2$d %3$s --> CEL CEL_SEARCH_REQUEST",
                evsend_time, reqrepID, this.get_name());
        this.write(msg);

        Sim_event ev = new Sim_event();
        Predicate predicate = new Predicate(QAGESATags.GIS_REPLY);
        super.sim_get_next(predicate, ev); // only look for this type of ack
        GISReply reply = GISReply.get_data(ev);
        Assert.assertEquals(requestID, reply.getRequestID());
        Assert.assertEquals(QAGESATags.GIS_REQUEST, reply.getRequestTAG());
        Assert.assertEquals(QAGESATags.GIS_REPLY, ev.get_tag());
        double evrecv_time = GridSim.clock();
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



