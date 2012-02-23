/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * GEList.java
 *
 * Created on 15 March 2007, 21.08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * $Id: GEList.java 100 2008-01-07 11:23:02Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa.agents.services.impl.mum;

import eduni.simjava.Sim_system;

import java.util.Vector;

/**
 * 
 * @author admin
 */
public class GEList extends Vector<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4065631737093602170L;

	@Override
	public String toString() {
		String str = "";
		str += "GEs(";
		for (int i = 0; i < this.size(); i++) {
			if ((i + 1) < this.size()) {
				str += Sim_system.get_entity(this.get(i)).get_name() + ", ";
			} else {
				str += Sim_system.get_entity(this.get(i)).get_name();
			}
		}
		str += ")";
		return str;
	}
}
