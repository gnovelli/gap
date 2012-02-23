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
 * AgentsLocatorDirectory.java
 *
 * Created on 25 March 2007, 23:59 by Giovanni Novelli
 *
 * $Id: AgentsLocatorDirectory.java 186 2008-01-23 11:30:55Z gnovelli $
 *
 */

package net.sf.gap.mc.qagesa.agents.services.impl.al;

import java.util.concurrent.ConcurrentHashMap;

import net.sf.gap.messages.impl.AgentRequest;

/**
 *
 * @author Giovanni Novelli
 */
public class AgentsLocatorDirectory {
    private ConcurrentHashMap<Integer,AgentRequest> agentsMap; // used Agent Entity ID, ceID
    
    /** Creates a new instance of AgentsLocatorDirectory */
    public AgentsLocatorDirectory() {
        this.setAgentsMap(new ConcurrentHashMap<Integer,AgentRequest>());
    }
    
    public void addAgent(int agentID, AgentRequest request) {
        this.getAgentsMap().put(agentID, request);
    }
    
    public AgentRequest removeAgent(int agentID) {
        return this.getAgentsMap().remove(agentID);
    }

    public ConcurrentHashMap<Integer, AgentRequest> getAgentsMap() {
        return agentsMap;
    }

    public void setAgentsMap(ConcurrentHashMap<Integer, AgentRequest> agentsMap) {
        this.agentsMap = agentsMap;
    }
}
