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
 * Predicate.java
 *
 * Created on 21 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: Predicate.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/829/GAP/src/net/sf/gap/agents/predicates/Predicate.java $
 *
 *****************************************************************************************
 */

package net.sf.gap.agents.predicates;

import eduni.simjava.Sim_event;
import eduni.simjava.Sim_predicate;

/**
 * 
 * @author Giovanni Novelli
 */
public class Predicate extends Sim_predicate {
	private int[] tags;

	/** Creates a new instance of Predicate */
	public Predicate(int tag) {
		int[] atag = { tag };
		this.setTags(atag);
	}

	/** Creates a new instance of Predicate */
	public Predicate(int[] tags) {
		this.setTags(tags);
	}

	@Override
	public boolean match(Sim_event ev) {
		int nt = this.getTags().length;
		boolean matched = false;
		for (int i = 0; i < nt; i++) {
			matched = matched || (this.getTags()[i] == ev.get_tag());
		}
		return matched;
	}

	public int[] getTags() {
		return tags;
	}

	public void setTags(int[] tags) {
		this.tags = tags;
	}
}
