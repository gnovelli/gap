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
 * ServicesList.java
 *
 * Created on 10 March 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 186 $
 * $Id: ServicesList.java 186 2008-01-23 11:30:55Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/qagesa/v2.5/GAP/src/net/sf/gap/agents/services/ServicesList.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.services;

import java.util.LinkedList;

/**
 * This class is responsible for maintaining a list of platform services
 * 
 * @author Giovanni Novelli
 */

public class ServicesList extends LinkedList<ServiceInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5780313736798117946L;

	@Override
	public String toString() {
		String str = "";
		str += "SERVICES[";
		for (int i = 0; i < this.size(); i++) {
			if ((i + 1) < this.size()) {
				str += "(" + this.get(i).getServiceName() + ", "
						+ this.get(i).getGridElement().get_name() + "), ";
			} else {
				str += "(" + this.get(i).getServiceName() + ", "
						+ this.get(i).getGridElement().get_name() + ")";
			}
		}
		str += "]";
		return str;
	}
}
