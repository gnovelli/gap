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
 * EntitiesCounter.java
 *
 * Created on 7 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: EntitiesCounter.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/march08tuning_v0.9/GAP/src/net/sf/gap/util/EntitiesCounter.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Giovanni Novelli
 */

public class EntitiesCounter {

	private static ConcurrentHashMap<String, Integer> map;

	/**
	 * Creates a new instance of EntitiesCounter
	 */
	public EntitiesCounter() {
		setMap(new ConcurrentHashMap<String, Integer>());
	}

	public static boolean contains(String key) {
		return getMap().containsKey(key);
	}

	public static Integer create(String key) {
		if (!contains(key)) {
			return getMap().put(key, 1);
		} else {
			return getMap().get(key);
		}
	}

	public static Integer inc(String key) {
		if (!contains(key)) {
			return create(key);
		} else {
			return getMap().put(key, getMap().get(key) + 1);
		}
	}

	public static Integer get(String key) {
		return getMap().get(key);
	}

	public static ConcurrentHashMap<String, Integer> getMap() {
		return map;
	}

	public static void setMap(ConcurrentHashMap<String, Integer> aMap) {
		map = aMap;
	}
}
