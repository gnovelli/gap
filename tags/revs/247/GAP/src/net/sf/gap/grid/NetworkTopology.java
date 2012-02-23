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
 * NetworkTopology.java
 *
 * Created on 25 August 2006, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 186 $
 * $Id: NetworkTopology.java 186 2008-01-23 11:30:55Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/247/GAP/src/net/sf/gap/grid/NetworkTopology.java $
 *
 *****************************************************************************************
 */


package net.sf.gap.grid;

import gridsim.net.RIPRouter;

import java.util.Vector;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class NetworkTopology extends Vector<RIPRouter> {
        private int numRouters;

	/** Creates a new instance of NetworkTopology */
	public NetworkTopology(int numRouters) {
            this.setNumRouters(numRouters);
	}

        public abstract void initialize();
        
	public abstract void create(boolean trace) throws Exception;

	public void setRouters(Vector<RIPRouter> routers) {
		this.addAll(routers);
	}

    public int getNumRouters() {
        return numRouters;
    }

    public void setNumRouters(int numRouters) {
        this.numRouters = numRouters;
    }
}
