/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        QAGESA Simulator
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * ReFProximityList.java
 *
 * Created on 29 March 2007, 13:05 by Giovanni Novelli
 *
 * $Id: ReFProximityList.java 267 2008-01-25 19:05:19Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa.agents.services.impl.ref;

import java.util.Iterator;
import java.util.TreeSet;

/**
 *
 * @author Giovanni Novelli
 */
@SuppressWarnings("serial")
public class ReFProximityList extends TreeSet<ReFTriple>{
    
    /**
     * Creates a new instance of ReFProximityList
     */
    public ReFProximityList() {
    }
    
    public static void main(String[] args) {
        ReFProximityList list = new ReFProximityList();
        for (int ceID = 1; ceID<=10; ceID++) {
            for (int seID = 1; seID<=10; seID++) {
                int numFreeAgents = ceID+seID;
                double latency = (ceID*1.0)/(seID*1.0);
                ReFTriple triple = new ReFTriple(numFreeAgents, latency, ceID, seID);
                list.add(triple);
            }
        }
        // Iterating over the elements in the set
        Iterator<ReFTriple> it = list.iterator();
        while (it.hasNext()) {
            // Get element
            ReFTriple triple = it.next();
            System.out.println(triple);
        }        
    }
}
