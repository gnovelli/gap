/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * PEType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id: PEType.java 274 2008-01-27 09:50:33Z gnovelli $
 *
 */

package net.sf.gap.xml.types;

/**
 * 
 * @author Giovanni Novelli
 */
public class PEType {
	private int MIPS;

	public PEType() {
		this.setMIPS(1000);
	}

	public PEType(int MIPS) {
		this.setMIPS(MIPS);
	}

	public int getMIPS() {
		return MIPS;
	}

	public void setMIPS(int MIPS) {
		this.MIPS = MIPS;
	}
}
