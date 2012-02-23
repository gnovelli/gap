/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Experiments.java
 *
 * Created on 19 August 2006, 17.14 by Giovanni Novelli
 *
 * $Id: EXPERIMENTS.java 212 2008-01-24 12:07:14Z gnovelli $
 *
 */

package net.sf.gap.mc.experiments;

import net.sf.gap.AbstractGAP;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.GAP;
import net.sf.gap.mc.experiments.constants.ExperimentsEntityTypes;
import net.sf.gap.mc.experiments.constants.ExperimentsTags;

/**
 *
 * @author Giovanni Novelli
 */
public class EXPERIMENTS extends GAP {
	/** 
         * Initializes simulation's time parameters
         * @param platformStart Platform's start time
         * @param start         Simulation's start time
         * @param end           Simulation's end time
         */
	public static void initialize(double platformStart, double start, double end) {
		AbstractGAP.setPlatformStartTime(platformStart);
		AbstractGAP.setStartTime(start);
		AbstractGAP.setEndTime(end);
		EntityTypes.setInstance(new ExperimentsEntityTypes());
		Tags.setInstance(new ExperimentsTags());
	}
}
