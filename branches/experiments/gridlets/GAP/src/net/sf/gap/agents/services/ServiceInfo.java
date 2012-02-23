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
 * ServiceInfo.java
 *
 * Created on 10 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 100 $
 * $Id: ServiceInfo.java 100 2008-01-07 11:23:02Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/branches/experiments/gridlets/GAP/src/net/sf/gap/agents/services/ServiceInfo.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services;

import net.sf.gap.grid.components.GridElement;

/**
 * This class is responsible for maintaining info about a platform service
 * 
 * @author Giovanni Novelli
 */

public class ServiceInfo {
	private String serviceName;

	private GridElement gridElement;

	public ServiceInfo(String serviceName, GridElement gridElement) {
		this.setServiceName(serviceName);
		this.setGridElement(gridElement);
	}

	public boolean isPlatformService() {
		return (this.getGridElement().getAgentPlatform().getGridElement()
				.get_id() == this.getGridElement().get_id());
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public GridElement getGridElement() {
		return gridElement;
	}

	public void setGridElement(GridElement gridElement) {
		this.gridElement = gridElement;
	}
}
