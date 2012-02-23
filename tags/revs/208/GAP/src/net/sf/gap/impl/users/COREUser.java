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
 * COREUser.java
 *
 * Created on 13 August 2006, 20.24 by Giovanni Novelli
 *
 * $Id: COREUser.java 204 2008-01-24 10:43:48Z gnovelli $
 *
 */

package net.sf.gap.impl.users;

import eduni.simjava.Sim_event;
import gridsim.net.Link;
import net.sf.gap.users.AbstractUser;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class COREUser extends AbstractUser {

	/** Creates a new instance of QAGESAUser */
	public COREUser(String name, Link link, int entityType, boolean trace_flag)
			throws Exception {
		super(name, link, entityType, trace_flag);
	}

	@Override
	public abstract void processOtherEvent(Sim_event ev);
}
