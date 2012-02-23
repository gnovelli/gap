/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Simulation.java
 *
 * Created on 8 January 2008, 13.00 by Giovanni Novelli
 *
 * $Id: Simulation.java 130 2008-01-15 10:53:14Z gnovelli $
 *
 */

package net.sf.gap.mc.experiments.simulation.impl;

import eduni.simjava.Sim_stat;
import eduni.simjava.Sim_system;

import gridsim.GridSim;

import java.util.Calendar;

import net.sf.gap.simulation.AbstractSimulation;

import net.sf.gap.mc.experiments.EXPERIMENTS;
import net.sf.gap.mc.experiments.grid.ExperimentsVirtualOrganization;
import net.sf.gap.mc.experiments.users.User;

/**
 * 
 * @author Giovanni Novelli
 */
public class Simulation extends AbstractSimulation {
        private int experimentID;
        private int numUsers;
        private int numRequests;
        
	private ExperimentsVirtualOrganization virtualOrganization;
        
	/**
	 * Creates a new instance of Simulation
	 */
	public Simulation(int experimentID, int numUsers, int numRequests, int replications, double confidence, double accuracy) {
            super(replications, confidence, accuracy);
            this.setExperimentID(experimentID);
            this.setNumUsers(numUsers);
            this.setNumRequests(numRequests);
	}

	private void initialize() throws Exception {
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = true; // mean trace GridSim events/activities
		boolean default_gis = false;

		System.out.println("Initializing GridSim package");
		GridSim.init(1, calendar, trace_flag, default_gis);
		EXPERIMENTS.initialize(100.0, 200.0, 1000.0);
		this.setVirtualOrganization(new ExperimentsVirtualOrganization(false, 4,
				1000, 1, 16, 16, 4, 100, 100, 2, 2, true, 1.0,
                                this.getNumUsers(), this.getExperimentID()));
	}

	public void start() {
		try {
			this.initialize();

			// ////////////////////////////////////////
			// Final step: Starts the simulation
                        super.setupOutputAnalysis();

                        User user0 = (User) Sim_system.get_entity("USER_0");
                        int measure0 = user0.getMeasure().getMeasure();
                                Sim_system.set_termination_condition(Sim_system.INTERVAL_ACCURACY,
                                                           Sim_system.IND_REPLICATIONS,
                                                           this.getConfidence(),
                                                           this.getAccuracy(),
                                                           "USER_0",
                                                           measure0);			
                                
                        EXPERIMENTS.startSimulation();

			System.out.println("\nFinished Simulation ...");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unwanted errors happen");
		}
	}

	public ExperimentsVirtualOrganization getVirtualOrganization() {
		return virtualOrganization;
	}

	public void setVirtualOrganization(ExperimentsVirtualOrganization aVirtualOrganization) {
		virtualOrganization = aVirtualOrganization;
	}

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public void setNumRequests(int numRequests) {
        this.numRequests = numRequests;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }
}
