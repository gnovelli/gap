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
 * AgentPlatform.java
 *
 * Created on 20 December 2007, 20:00 by Giovanni Novelli
 *
 * $Id: AgentPlatform.java 669 2008-03-03 12:03:57Z gnovelli $
 *
 */

package net.sf.gap.agents.middleware;

import net.sf.gap.GAP;
import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class AgentPlatform extends AbstractAgentPlatform {

	/** Creates a new instance of AgentPlatform */
	public AgentPlatform(String name, boolean trace) throws Exception {
		super(name, trace);
	}

	@Override
	public void initialize() throws Exception {
		super.initialize();
		this.initPlatform();
		this.initAgents();
	}

	public abstract void initializeServices() throws Exception;

	public abstract void initAgents() throws Exception;

	@Override
	public void initPlatform() throws Exception {
                super.initPlatform();

		this.getVirtualOrganization().initialize();

		this.initializeServices();
	}

	public abstract void preprocess();

	public abstract void postprocess();

	public void end() {
		// this.getNetworkMonitor().showNetworkMap();
		// //////////////////////////////////////////////////////
		// shut down I/O ports
		this.shutdownUserEntity();
		this.terminateIOEntities();

		// don't forget to close the file
		if (this.getReport_() != null) {
			this.getReport_().finalWrite();
		}
	}

	public void init() {
		// wait for a little while for about 3 seconds.
		// This to give a time for GridResource entities to register their
		// services to GIS (GridInformationService) entity.
		super.gridSimHold(GAP.getPlatformStartTime()-100.0);
		try {
			this.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void process() {
		Sim_event ev = new Sim_event();
		while (GAP.isRunning()) {
			super.sim_wait_for(Sim_system.SIM_ANY, 10.0, ev);

			this.processEvent(ev);
			while (super.sim_waiting() > 0) {
				this.processEvents();
			}
		}
	}

	protected void processEvents() {
		Sim_event ev = new Sim_event();

		super.sim_get_next(ev);
		this.processEvent(ev);
	}
}
