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
 * AbstractVirtualOrganization.java
 *
 * Created on 19 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 186 $
 * $Id: AbstractVirtualOrganization.java 186 2008-01-23 11:30:55Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/208/GAP/src/net/sf/gap/grid/AbstractVirtualOrganization.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.grid;

import gridsim.GridSim;
import gridsim.datagrid.index.DataGIS;
import gridsim.datagrid.index.TopRegionalRC;
import gridsim.net.Link;
import gridsim.net.RIPRouter;
import gridsim.net.SimpleLink;

import java.util.HashMap;
import java.util.Vector;

import net.sf.gap.agents.middleware.AgentMiddleware;
import net.sf.gap.agents.middleware.AgentPlatform;
import net.sf.gap.grid.components.GridElement;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class AbstractVirtualOrganization {
	private AgentPlatform platform;

	private Vector<GridElement> ces;

	private Vector<GridElement> ses;

	private int numCEs;

	private int numSEs;

	protected HashMap<Integer, RIPRouter> mapCEs;

	protected HashMap<Integer, RIPRouter> mapSEs;

	private DataGIS dataGIS;

	private TopRegionalRC topRegionalRC;

	private NetworkTopology topology;

	private Vector<AgentMiddleware> ams;

	/**
	 * Creates a new instance of AbstractVirtualOrganization
	 */
	public AbstractVirtualOrganization() {
	}

        public abstract void initialize();
        
        protected abstract void createEntities() throws Exception;
        
	public AgentPlatform getPlatform() {
		return this.platform;
	}

	public void setPlatform(AgentPlatform agentPlatform) {
		this.platform = agentPlatform;
	}

	public Vector<GridElement> getCEs() {
		return this.ces;
	}

	public void setCEs(Vector<GridElement> ces) {
		this.ces = ces;
	}

	public Vector<GridElement> getSEs() {
		return this.ses;
	}

	public void setSEs(Vector<GridElement> SEs) {
		this.ses = SEs;
	}

	public int getNumCEs() {
		return this.numCEs;
	}

	public void setNumCEs(int numCEs) {
		this.numCEs = numCEs;
	}

	public int getNumSEs() {
		return this.numSEs;
	}

	public void setNumSEs(int numSEs) {
		this.numSEs = numSEs;
	}

	protected TopRegionalRC createTopRegionalRC() throws Exception {
		TopRegionalRC trrc = null;
		Link l = new SimpleLink("trrc_link", 1000000000.0, 1.0, 1500);
		trrc = new TopRegionalRC(l);
		return trrc;
	}

	protected DataGIS createDataGIS() throws Exception {
		DataGIS gis = null;
		// set the GIS into DataGIS that handles specifically for data grid
		// scenarios
		gis = new DataGIS("DataGIS", 1000000000.0);
		GridSim.setGIS(gis);
		return gis;
	}

	public NetworkTopology getTopology() {
		return this.topology;
	}

	public void setTopology(NetworkTopology topology) {
		this.topology = topology;
	}

	public TopRegionalRC getTopRegionalRC() {
		return this.topRegionalRC;
	}

	public void setTopRegionalRC(TopRegionalRC topRegionalRC) {
		this.topRegionalRC = topRegionalRC;
	}

	public DataGIS getDataGIS() {
		return this.dataGIS;
	}

	public void setDataGIS(DataGIS dataGIS) {
		this.dataGIS = dataGIS;
	}

	public HashMap<Integer, RIPRouter> getMapCEs() {
		return this.mapCEs;
	}

	public void setMapCEs(HashMap<Integer, RIPRouter> mapCEs) {
		this.mapCEs = mapCEs;
	}

	public HashMap<Integer, RIPRouter> getMapSEs() {
		return this.mapSEs;
	}

	public void setMapSEs(HashMap<Integer, RIPRouter> mapSEs) {
		this.mapSEs = mapSEs;
	}

	public Vector<AgentMiddleware> getAMs() {
		return this.ams;
	}

	public void setAMs(Vector<AgentMiddleware> AMs) {
		this.ams = AMs;
	}

    public abstract void createAndAttachAgentPlatform() throws Exception;

    public abstract void createAndAttachAgents() throws Exception;

    public abstract void createAndAttachCEs() throws Exception;

    public abstract void createAndAttachSEs() throws Exception;

    public abstract void createAndAttachUsers() throws Exception;
}
