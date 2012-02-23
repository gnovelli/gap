/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * QAGESA.java
 *
 * Created on 19 August 2006, 17.14 by Giovanni Novelli
 *
 * $Id: QAGESA.java 212 2008-01-24 12:07:14Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa;

import net.sf.gap.AbstractGAP;
import net.sf.gap.constants.EntityTypes;
import net.sf.gap.constants.Tags;
import net.sf.gap.GAP;
import net.sf.gap.mc.qagesa.constants.QAGESAEntityTypes;
import net.sf.gap.mc.qagesa.constants.QAGESATags;

/**
 *
 * @author Giovanni Novelli
 */
public class QAGESA extends GAP {
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
		EntityTypes.setInstance(new QAGESAEntityTypes());
		Tags.setInstance(new QAGESATags());
	}
}
