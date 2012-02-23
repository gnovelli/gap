/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ResourceFactory.java
 *
 * Created on 7 August 2006, 13.37 by Giovanni Novelli
 *
 * $Id: ResourceFactory.java 114 2008-01-08 11:59:36Z gnovelli $
 *
 */

package net.sf.gap.mc.core.factories;

import gridsim.net.Link;

import net.sf.gap.mc.core.grid.components.COREGridElement;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class ResourceFactory {
	/** Creates a new instance of ResourceFactory */
	public ResourceFactory() {
	}

	public abstract COREGridElement create(boolean fixed, int geIndex, Link link, boolean isSE);
}
