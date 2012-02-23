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
 * ResourceFactory.java
 *
 * Created on 7 August 2006, 13.37 by Giovanni Novelli
 *
 * $Id: ResourceFactory.java 275 2008-01-27 10:39:07Z gnovelli $
 *
 */

package net.sf.gap.factories;

import gridsim.net.Link;
import net.sf.gap.grid.components.GridElement;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class ResourceFactory {
	/** Creates a new instance of ResourceFactory */
	public ResourceFactory() {
	}

	public abstract GridElement create(boolean fixed, int geIndex, Link link,
			boolean isSE);
}
