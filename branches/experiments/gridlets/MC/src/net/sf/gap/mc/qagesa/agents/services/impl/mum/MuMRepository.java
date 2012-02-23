/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * MuMRepository.java
 *
 * Created on 15 March 2007, 21.06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * $Id: MuMRepository.java 100 2008-01-07 11:23:02Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa.agents.services.impl.mum;

import java.util.HashMap;

/**
 * 
 * @author admin
 */
public class MuMRepository extends HashMap<String, GEList> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8523491610508912425L;

	public GEList put(String repTag, int se_id) {
		GEList list;
		if (this.containsKey(repTag)) {
			list = this.get(repTag);
		} else {
			list = new GEList();
		}
		list.add(se_id);
		return this.put(repTag, list);
	}

	public GEList getGEList(String name) {
		return this.get(name);
	}
}
