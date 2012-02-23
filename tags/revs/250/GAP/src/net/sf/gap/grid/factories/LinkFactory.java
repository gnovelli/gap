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
 * LinkFactory.java
 *
 * Created on 19 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 186 $
 * $Id: LinkFactory.java 186 2008-01-23 11:30:55Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/250/GAP/src/net/sf/gap/grid/factories/LinkFactory.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.grid.factories;

import gridsim.ParameterException;
import gridsim.net.Link;
import gridsim.net.SimpleLink;

/**
 * 
 * @author Giovanni Novelli
 */
public class LinkFactory {

	/** Creates a new instance of LinkFactory */
	public LinkFactory() {
	}

	public static SimpleLink getAgentLink(String name)
			throws ParameterException {
		return new SimpleLink("ag_link_" + name, 800000000000.0,
				0.000000000001, Link.DEFAULT_MTU);
	}

	public static SimpleLink getMiddlewareLink(String name)
			throws ParameterException {
		return new SimpleLink("mw_link_" + name, 800000000000.0,
				0.000000000001, Link.DEFAULT_MTU);
	}

	public static SimpleLink getGELink(String name) throws ParameterException {
		return new SimpleLink("ge_link_" + name, 800000000000.0,
				0.000000000001, Link.DEFAULT_MTU);
	}
}
