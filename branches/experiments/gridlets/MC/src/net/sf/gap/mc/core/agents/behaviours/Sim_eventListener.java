/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Sim_eventListener.java
 *
 * Created on 8 January 2008, 09.28 by Giovanni Novelli
 *
 * $Id: Sim_eventListener.java 111 2008-01-08 10:49:06Z gnovelli $
 *
 */  
package net.sf.gap.mc.core.agents.behaviours;

import net.sf.gap.mc.core.agents.*;
import eduni.simjava.Sim_event;

/**
 *
 * @author Giovanni Novelli
 */
public abstract class Sim_eventListener implements ISim_eventListener {
  private int tag;
  private PluggableAgent agent;
  
  public Sim_eventListener(PluggableAgent anAgent, int aTag) {
    this.setAgent(anAgent);
    this.setTag(aTag);
  }
  
  public void eventOccurred(Sim_event ev) {
    this.processEvent(ev);
  }
  
  protected abstract void processEvent(Sim_event ev);

    public PluggableAgent getAgent() {
        return agent;
    }

    public void setAgent(PluggableAgent agent) {
        this.agent = agent;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
