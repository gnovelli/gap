/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * MC.java
 *
 * Created on 19 August 2006, 17.14 by Giovanni Novelli
 *
 * $Id: MC.java 100 2008-01-07 11:23:02Z gnovelli $
 *
 */

package net.sf.gap.mc.core;

import net.sf.gap.GAP;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class MC extends GAP {

	/** Creates a new instance of MC */
	public MC() {
		super(true);
	}

	/** 
         * Creates a new instance of MC indicating preference about graphing
         */
	public MC(boolean aGraphing) {
		super(aGraphing);
	}

	/** 
         * Starts simulation
         */
	public static void startSimulation() {
		System.out.println("Starting simulation");
		GAP.startSimulation();
	}

	/** 
         * Stops simulation
         */
	public static void stopSimulation() {
		System.out.println("Stopping simulation");
		GAP.stopSimulation();
	}
}
