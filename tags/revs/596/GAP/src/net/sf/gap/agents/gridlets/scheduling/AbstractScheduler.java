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
 * AbstractScheduler.java
 *
 * Created on 13 December 2007, 12.00 by Giovanni Novelli
 *
 ****************************************************************************************
 *
 * $Revision: 275 $
 * $Id: AbstractScheduler.java 275 2008-01-27 10:39:07Z gnovelli $
 * $HeadURL: https://gap.svn.sourceforge.net/svnroot/gap/tags/revs/596/GAP/src/net/sf/gap/agents/gridlets/scheduling/AbstractScheduler.java $
 *
 *****************************************************************************************
 */
package net.sf.gap.agents.gridlets.scheduling;

import gridsim.Gridlet;
import net.sf.gap.agents.GridAgent;
import net.sf.gap.agents.gridlets.GAPGridletList;
import net.sf.gap.agents.gridlets.GridletsBag;

/**
 * 
 * @author Giovanni Novelli
 */
public abstract class AbstractScheduler implements IJobScheduler {
	private double MIPS;
	private long inputSize;
	private long outputSize;

	/**
	 * GridAgent using this scheduler
	 */
	private GridAgent agent;
	/**
	 * Upper bound for queued gridlets
	 */
	private int upperBound;
	/**
	 * GridletsBag tracking
	 */
	private GridletsBag gridlets;

	public AbstractScheduler(GridAgent anAgent) {
		this.setUpperBound(1);
		this.setAgent(anAgent);
		this.setGridletsBag(new GridletsBag());
	}

	public AbstractScheduler(GridAgent anAgent, int anUpperBound) {
		this.setUpperBound(anUpperBound);
		this.setAgent(anAgent);
		this.setGridletsBag(new GridletsBag());
	}

	protected int getUpperBound() {
		return upperBound;
	}

	protected void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * This method is responsible to check if there are gridlets submitted by
	 * agent on Grid Element
	 * 
	 * @return <tt>true</tt> if there are agent's gridlets on Grid Element,
	 *         <tt>false</tt> otherwise
	 * @pre $none
	 * @post $none
	 */
	public boolean hasGridlets() {
		return !this.isEmpty() || (!this.getCurrentList().isEmpty());
	}

	/**
	 * This method is responsible for gridlets submission to Grid Element
	 * 
	 * @return <tt>true</tt> if gridlet has been successfully submitted,
	 *         <tt>false</tt> otherwise
	 * @pre $none
	 * @post $none
	 */
	@SuppressWarnings("unchecked")
	public boolean gridletSubmit() {
		// @TODO Add another method for 1+ concurrent gridlets by an agent
		// if there is NOT any gridlet on the Grid Element
		if (this.getCurrentList().isEmpty() || true) {
			// if there are gridlets on local agent's queue
			if (!this.isEmpty()) {
				// Peeks the gridlet on the head of agent's scheduling queue
				Gridlet gridlet = this.peek();
				// Submits the gridlet to agent's rid Element
				boolean submitted = this.getAgent().gridletSubmit(gridlet);
				// If the gridlet has been successfully submitted
				if (submitted) {
					// Removes the gridlet from the head of agent's
					// scheduling queue and adds the gridlet to a list
					// used for tracking submitted gridlets
					this.getGridletsBag().addSubmitted(this.deque());
				}
				return submitted;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Cancels a Gridlet on the Grid Element of this agent without any delay. An
	 * acknowledgement to denote the success of this method is by default off or
	 * false
	 * 
	 * @param gl
	 *            the gridlet to be canceled
	 * @return <tt>true</tt> if gridlet has been successfully canceled,
	 *         <tt>false</tt> otherwise
	 */
	public Gridlet gridletCancel(Gridlet gl) {
		// If the gridlet has been previously submitted and has yet NOT returned
		if (this.getGridletsBag().getGridletSubmitted().contains(gl)) {
			Gridlet canceledGridlet = this.getAgent().gridletCancel(gl);
			if (canceledGridlet.getGridletStatus() == Gridlet.CANCELED) {
				this.getGridletsBag().addCanceled(canceledGridlet);
				return canceledGridlet;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Pauses a Gridlet on the Grid Element of this agent without any delay. An
	 * acknowledgement to denote the success of this method is by default off or
	 * false
	 * 
	 * @param gl
	 *            the gridlet to be paused
	 * @return <tt>true</tt> if gridlet has been successfully paused,
	 *         <tt>false</tt> otherwise
	 */
	public boolean gridletPause(Gridlet gl) {
		// If the gridlet has been previously submitted and has yet NOT returned
		if (this.getGridletsBag().getGridletSubmitted().contains(gl)) {
			boolean paused = this.getAgent().gridletPause(gl);
			if (paused) {
				this.getGridletsBag().addPaused(gl);
				return paused;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Resumes a Gridlet on the Grid Element of this agent without any delay. An
	 * acknowledgement to denote the success of this method is by default off or
	 * false
	 * 
	 * @param gl
	 *            the gridlet to be resumed
	 * @return <tt>true</tt> if gridlet has been successfully resumed,
	 *         <tt>false</tt> otherwise
	 */
	public boolean gridletResume(Gridlet gl) {
		// If the gridlet has been previously submitted and has yet NOT returned
		if (this.getGridletsBag().getGridletSubmitted().contains(gl)) {
			boolean resumed = this.getAgent().gridletResume(gl);
			if (resumed) {
				this.getGridletsBag().removePaused(gl);
				return resumed;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// @TODO Add MOVE gridlet

	/**
	 * This method is responsible for syncronously receiving a gridlet back from
	 * Grid Element
	 * 
	 * @return the gridlet received by Grid Element or null
	 */
	public Gridlet gridletReceive() {
		// If there is at least a gridlet on agent's Grid Element
		if (!this.getCurrentList().isEmpty()) {
			// gets the gridlet back from agent's Grid Element
			Gridlet receivedGridlet = this.getAgent().gridletReceive();
			return this.gridletReceive(receivedGridlet);
		} else {
			return null;
		}
	}

	/**
	 * This method is responsible for managing a gridlet received in some way
	 * (sync/asyng) back from Grid Element
	 * 
	 * @return the gridlet received by Grid Element or null
	 */
	public Gridlet gridletReceive(Gridlet receivedGridlet) {
		// if the received gridlet is NOT null
		if (receivedGridlet != null) {
			// Extract receveid gridlet's ID
			int gridletID = receivedGridlet.getGridletID();
			// Gets original gridlet from its ID
			Gridlet gridlet = this.getGridletsBag().getGridlet(gridletID);
			// If the gridlet extracted from gridlets tracking field
			// is NOT null
			if (gridlet != null) {
				// Checks that the extracted gridlet is really on
				// current gridlets list
				if (this.getCurrentList().contains(gridlet)) {
					// Removes the gridlet from such list
					this.getCurrentList().remove(gridlet);
					// Tracks the received gridlet in the correct list
					// based on its return status
					switch (receivedGridlet.getGridletStatus()) {
					case Gridlet.SUCCESS:
						this.getGridletsBag().addSuccesses(receivedGridlet);
						break;
					case Gridlet.FAILED:
					case Gridlet.FAILED_RESOURCE_UNAVAILABLE:
						this.getGridletsBag().addFailures(receivedGridlet);
						break;
					default:
						break;
					}
				}
			}
			return receivedGridlet;
		} else {
			return null;
		}
	}

	public abstract boolean enque(Gridlet gridlet);

	public abstract Gridlet deque();

	public abstract Gridlet peek();

	public abstract boolean isEmpty();

	public abstract boolean isFull();

	public abstract int size();

	public GridAgent getAgent() {
		return agent;
	}

	public void setAgent(GridAgent agent) {
		this.agent = agent;
	}

	public GAPGridletList getCurrentList() {
		return this.getGridletsBag().getGridletSubmitted();
	}

	public GridletsBag getGridletsBag() {
		return gridlets;
	}

	public void setGridletsBag(GridletsBag gridlets) {
		this.gridlets = gridlets;
	}

	public double getMIPS() {
		return MIPS;
	}

	public void setMIPS(double MIPS) {
		this.MIPS = MIPS;
	}

	public void decMIPS(double MIPS) {
		this.MIPS -= MIPS;
	}

	public void incMIPS(double MIPS) {
		this.MIPS += MIPS;
	}

	public long getInputSize() {
		return inputSize;
	}

	public void setInputSize(long inputSize) {
		this.inputSize = inputSize;
	}

	public void decInputSize(long inputSize) {
		this.inputSize -= inputSize;
	}

	public void incInputSize(long inputSize) {
		this.inputSize += inputSize;
	}

	public long getOutputSize() {
		return outputSize;
	}

	public void setOutputSize(long outputSize) {
		this.outputSize = outputSize;
	}

	public void decOutputSize(long outputSize) {
		this.outputSize -= outputSize;
	}

	public void incOutputSize(long outputSize) {
		this.outputSize += outputSize;
	}
}
