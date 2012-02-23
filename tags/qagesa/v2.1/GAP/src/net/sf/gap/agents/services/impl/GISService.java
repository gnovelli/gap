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
 * GISService.java
 *
 * Created on 12 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 326 $
 * $Id: GISService.java 326 2008-02-05 14:48:42Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/qagesa/v2.1/GAP/src/net/sf/gap/agents/services/impl/GISService.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services.impl;

import net.sf.gap.agents.middleware.AbstractAgentPlatform;
import net.sf.gap.agents.services.PlatformService;
import net.sf.gap.agents.services.impl.gis.GISEntry;
import net.sf.gap.agents.services.impl.gis.GISRepository;
import net.sf.gap.constants.Tags;
import net.sf.gap.grid.components.AbstractGridElement;
import net.sf.gap.messages.impl.GISReply;
import net.sf.gap.messages.impl.GISRequest;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;
import gridsim.Accumulator;
import gridsim.GridSimTags;
import gridsim.IO_data;

/**
 * This class is responsible for a sample test subclass of PlatformService
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.agents.services.PlatformService
 */
public class GISService extends PlatformService {

	private GISRepository gisRepository;

	/**
	 * @param ap
	 * @param name
	 * @param trace_flag
	 * @throws Exception
	 */
	public GISService(AbstractAgentPlatform ap, boolean trace_flag)
			throws Exception {
		super(ap, "GISService", trace_flag);
	}

	public GISService(AbstractAgentPlatform ap, String name, boolean trace_flag)
			throws Exception {
		super(ap, name, trace_flag);
	}

	@Override
	public void initialize() throws Exception {
		super.initialize();
		this.setGisRepository(new GISRepository(this.getAgentPlatform()));

                if (this.getAgentPlatform().getVirtualOrganization().getScenario()==null) {
                    for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization()
                                    .getNumCEs(); i++) {
                            AbstractGridElement ce = (AbstractGridElement) Sim_system
                                            .get_entity("CE_" + i);
                            this.getGisRepository().addGE(ce);
                    }
                    for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization()
                                    .getNumSEs(); i++) {
                            AbstractGridElement se = (AbstractGridElement) Sim_system
                                            .get_entity("SE_" + i);
                            this.getGisRepository().addGE(se);
                    }
                } else {
                    for (int i = 0; i < this.getAgentPlatform().getVirtualOrganization()
                                    .getNumCEs()
                                    +this.getAgentPlatform().getVirtualOrganization()
                                    .getNumSEs(); i++) {
                        String gename = 
                                this.getAgentPlatform().getVirtualOrganization().getScenario().getGrid().getGridElements().get(i).getName();
                            AbstractGridElement ge = (AbstractGridElement) Sim_system
                                            .get_entity(gename);
                            this.getGisRepository().addGE(ge);
                    }
                }
	}

	@Override
	protected void dispose() {
	}

	@Override
	public void processOtherEvent(Sim_event ev) {
		// TODO Auto-generated method stub
		switch (ev.get_tag()) {
		case Tags.GIS_REQUEST:
			this.processGIS();
			this.sendGIS_REPLY(ev);
			break;

		default:
			break;
		}
	}

	protected void sendGIS_REPLY(Sim_event ev) {
		int SIZE = 500;
		GISRequest request = GISRequest.get_data(ev);
		GISReply reply = new GISReply(ev.get_tag(), true, request, this
				.getGisRepository());
		super.send(super.output, GridSimTags.SCHEDULE_NOW, Tags.GIS_REPLY,
				new IO_data(reply, SIZE, request.getSrc_ID()));
	}

	public GISRepository getGisRepository() {
		return gisRepository;
	}

	public void setGisRepository(GISRepository gisRepository) {
		this.gisRepository = gisRepository;
	}

	public void processGIS() {
		int numCEs = this.getAgentPlatform().getVirtualOrganization()
				.getNumCEs();
		for (int i = 0; i < numCEs; i++) {
			AbstractGridElement ge = this.getAgentPlatform().getVirtualOrganization().getCEs().get(i);
			int geid = ge.get_id();
			int numPEs = ge.getNumPE();
			int numFreePEs = super.getNumFreePE(ge.get_id());
			int numFreeAgents = ge.getLocalDirectory().getFreeAgents();
			int totalMIPS = ge.getTotalMIPS();
			boolean SE = ge.isSE();
			double MB_size = ge.getTotalStorageCapacity();
			this.addEntry(geid, numPEs, numFreePEs, numFreeAgents, totalMIPS,
					SE, MB_size, ge.getTotalLoad());
		}
		this.getGisRepository().setLastRequestTime(super.clock());
	}

	public GISEntry addEntry(int geid, int numPEs, int numFreePEs,
			int numFreeAgents, int totalMIPS, boolean SE, double MB_size,
			Accumulator load) {
		GISEntry entry = new GISEntry(numPEs, numFreePEs, numFreeAgents,
				totalMIPS, SE, MB_size, load);
		if (this.getGisRepository() == null) {
			this.setGisRepository(new GISRepository(this.getAgentPlatform()));

			for (int i = 0; i < this.getAgentPlatform()
					.getVirtualOrganization().getNumCEs(); i++) {
				AbstractGridElement ce = this.getAgentPlatform().getVirtualOrganization().getCEs().get(i);
				this.getGisRepository().addGE(ce);
			}
			for (int i = 0; i < this.getAgentPlatform()
					.getVirtualOrganization().getNumSEs(); i++) {
				AbstractGridElement se = this.getAgentPlatform().getVirtualOrganization().getSEs().get(i);
				this.getGisRepository().addGE(se);
			}
		}
		return this.getGisRepository().put(geid, entry);
	}

	public GISEntry removeEntry(int geid) {
		return this.getGisRepository().remove(geid);
	}

}
