/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ExperimentsEntityTypes.java
 *
 * Created on 8 January 2008, 11.51 by Giovanni Novelli
 *
 * $Id: ExperimentsEntityTypes.java 186 2008-01-23 11:30:55Z gnovelli $
 *
 */

package net.sf.gap.mc.experiments.constants;

import net.sf.gap.constants.EntityTypes;

/**
 * 
 * @author Giovanni Novelli
 */
public class ExperimentsEntityTypes extends EntityTypes {
    public static final int EEBASE = 200 + ENTITYBASE;
	public static final int AGENT_AGENT = 11 + EEBASE;
	public static final int USER_USER = 21 + EEBASE;

	/**
	 * Creates a new instance of ExperimentsEntityTypes
	 */
	public ExperimentsEntityTypes() {
	}

	@Override
	public String otherTypes(int entityType) {
		String str = null;
		switch (entityType) {
		case ExperimentsEntityTypes.AGENT_AGENT:
			str = "AGENT_AGENT";
			break;
		case ExperimentsEntityTypes.USER_USER:
			str = "USER_USER";
			break;
		default:
			str = "UNKNOWN_ENTITY_TYPE";
			break;
		}
		return str;
	}
}
