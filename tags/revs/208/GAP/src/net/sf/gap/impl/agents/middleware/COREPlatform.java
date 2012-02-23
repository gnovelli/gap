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
 * COREPlatform.java
 *
 * Created on 20 December 2007, 20:00 by Giovanni Novelli
 *
 * $Id: COREPlatform.java 204 2008-01-24 10:43:48Z gnovelli $
 *
 */

package net.sf.gap.impl.agents.middleware;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_system;

import net.sf.gap.agents.middleware.AgentPlatform;

import net.sf.gap.impl.MC;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class COREPlatform extends AgentPlatform {


	/** Creates a new instance of COREPlatform */
	public COREPlatform(String name, boolean trace) throws Exception {
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
        super.gridSimHold(MC.getPlatformStartTime());
        try {
            this.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process() {
        Sim_event ev = new Sim_event();
        while (MC.isRunning()) {
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
