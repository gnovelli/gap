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
 * Created on 7 August 2006, 14.22 by Giovanni Novelli
 *
 * $Id: LinkFactory.java 275 2008-01-27 10:39:07Z gnovelli $
 *
 */

package net.sf.gap.factories;

import gridsim.ParameterException;
import gridsim.net.Link;
import gridsim.net.SimpleLink;

/**
 * 
 * @author Giovanni Novelli
 */
public class LinkFactory {
	private static int MTU = Link.DEFAULT_MTU;

	private static int counter;

	/** Creates a new instance of LinkFactory */
	public LinkFactory() {
	}

	static {
		counter = 0;
	}

	public static Link randomLink(double delay) throws Exception {
		double bps = 1000000000.0;
		Link link;
		link = new SimpleLink("link_" + LinkFactory.incCounter(), bps, delay,
				LinkFactory.getMTU());
		return link;
	}

	public static Link UserLink(double bps, double delay) throws Exception {
		Link link = new SimpleLink("agentlink_" + LinkFactory.incCounter(),
				bps, delay, LinkFactory.getMTU());
		return link;
	}

	private static Link CELink() throws Exception {
		double bps;
		bps = 1000000000;
		double delay = 2.0;
		Link link = new SimpleLink("celink_" + LinkFactory.incCounter(), bps,
				delay, LinkFactory.getMTU());
		return link;
	}

	private static Link SELink() throws Exception {
		double bps;
		bps = 10000000000.0;
		double delay = 2.0;
		Link link = new SimpleLink("selink_" + LinkFactory.incCounter(), bps,
				delay, LinkFactory.getMTU());
		return link;
	}

	public static Link GELink(boolean isSE) throws Exception {
		if (isSE) {
			return LinkFactory.SELink();
		} else {
			return LinkFactory.CELink();
		}
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

	public static SimpleLink getAgentLink(String name)
			throws ParameterException {
		return new SimpleLink("ag_link_" + name, 800000000000.0,
				0.000000000001, Link.DEFAULT_MTU);
	}

	public static int getCounter() {
		return counter;
	}

	public static void setCounter(int aCounter) {
		counter = aCounter;
	}

	public static int incCounter() {
		LinkFactory.setCounter(LinkFactory.getCounter() + 1);
		return LinkFactory.getCounter();
	}

	public static int getMTU() {
		return LinkFactory.MTU;
	}

	public static void setMTU(int MTU) {
		LinkFactory.MTU = MTU;
	}
}
