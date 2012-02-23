/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GridParser.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id: GridParser.java 339 2008-02-05 19:15:41Z gnovelli $
 *
 */

package net.sf.gap.xml.parsing.impl;

import net.sf.gap.xml.parsing.Parser;
import net.sf.gap.xml.types.GridElementType;
import net.sf.gap.xml.types.GridType;
import net.sf.gap.xml.types.HardDiskListType;
import net.sf.gap.xml.types.HardDiskType;
import net.sf.gap.xml.types.MachineType;
import net.sf.gap.xml.types.StorageType;
import net.sf.gap.xml.types.TapeListType;
import net.sf.gap.xml.types.TapeType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Giovanni Novelli
 */
public class GridParser extends Parser {
	public GridParser(Document document) {
		super(document);
	}

	public GridType getGrid() {
		GridType grid = new GridType();
		NodeList gridNodeList = this.getDocument().getElementsByTagName("grid");
		if (gridNodeList.getLength() > 0) {
			Element gridNodeListElement = (Element) gridNodeList.item(0);
			NodeList gridElements = gridNodeListElement
					.getElementsByTagName("gridElement");
			if (gridElements.getLength() > 0) {
				grid = new GridType();
				for (int ig = 0; ig < gridElements.getLength(); ig++) {
					Element geItem = (Element) gridElements.item(ig);
					GridElementType gridElementInstance = this
							.getGridElement(geItem);
					grid.addGridElement(gridElementInstance);
				}
			}
		}
		return grid;
	}

	private StorageType getStorage(Element storageElement) {
		Element rmElement = (Element) storageElement.getElementsByTagName("RM")
				.item(0);
		String rmName = rmElement.getAttribute("name");
		StorageType storage = new StorageType(rmName);
		NodeList hdItems = storageElement.getElementsByTagName("hdItem");
		HardDiskListType hdList = new HardDiskListType();
		for (int i = 0; i < hdItems.getLength(); i++) {
			Element hdItem = (Element) hdItems.item(i);
			String hdName = hdItem.getAttribute("name");
			Element capacityElement = (Element) hdItem.getElementsByTagName(
					"Capacity").item(0);
			double aCapacity = Double.parseDouble(capacityElement
					.getTextContent());
			Element latencyElement = (Element) hdItem.getElementsByTagName(
					"Latency").item(0);
			double aLatency = Double.parseDouble(latencyElement
					.getTextContent());
			Element askElement = (Element) hdItem.getElementsByTagName(
					"AvgSeekTime").item(0);
			double aAvgSeekTime = Double.parseDouble(askElement
					.getTextContent());
			Element mtrElement = (Element) hdItem.getElementsByTagName(
					"MaxTransferRate").item(0);
			double aMaxTransferRate = Double.parseDouble(mtrElement
					.getTextContent());
			HardDiskType hd = new HardDiskType(hdName, aCapacity, aLatency,
					aAvgSeekTime, aMaxTransferRate);
			hdList.addHardDisk(hd);
		}
		storage.setHardDiskList(hdList);
		NodeList tapeItems = storageElement.getElementsByTagName("tapeItem");
		if (tapeItems.getLength() > 0) {
			TapeListType tapeList = new TapeListType();
			for (int i = 0; i < hdItems.getLength(); i++) {
				Element tapeItem = (Element) tapeItems.item(i);
				String tapeName = tapeItem.getAttribute("name");
				Element capacityElement = (Element) tapeItem
						.getElementsByTagName("Capacity").item(0);
				double aCapacity = Double.parseDouble(capacityElement
						.getTextContent());
				Element atElement = (Element) tapeItem.getElementsByTagName(
						"AccessTime").item(0);
				double aAccessTime = Double.parseDouble(atElement
						.getTextContent());
				Element rtElement = (Element) tapeItem.getElementsByTagName(
						"RewindTime").item(0);
				double aRewindTime = Double.parseDouble(rtElement
						.getTextContent());
				Element mtrElement = (Element) tapeItem.getElementsByTagName(
						"MaxTransferRate").item(0);
				double aMaxTransferRate = Double.parseDouble(mtrElement
						.getTextContent());
				TapeType tape = new TapeType(tapeName, aCapacity, aAccessTime,
						aRewindTime, aMaxTransferRate);
				tapeList.addTape(tape);
			}
			storage.setTapeList(tapeList);
		}
		return storage;
	}

	private GridElementType getGridElement(Element geItem) {
		GridElementType gridElementInstance = new GridElementType();
		gridElementInstance.setName(geItem.getAttribute("name"));
                gridElementInstance.setRB(false);
		NodeList isRBListItems = geItem.getElementsByTagName("isRB");
		for (int i = 0; i < isRBListItems.getLength(); i++) {
        		Element isRBItem = (Element) isRBListItems.item(i);
                        gridElementInstance.setRB(true);
                        //gridElementInstance.setRB(Boolean.parseBoolean(isRBItem.getTextContent()));
                }
		NodeList machineListItems = geItem.getElementsByTagName("Machine");
		for (int i = 0; i < machineListItems.getLength(); i++) {
			Element machineItem = (Element) machineListItems.item(i);
			MachineType machine = new MachineType();
			NodeList rmListItems = machineItem.getElementsByTagName("repeatMachine");
                        int repeatMachine = 1;
			for (int j = 0; j < rmListItems.getLength(); j++) {
 			  Element rmItem = (Element) rmListItems.item(j);
                          repeatMachine = Integer.parseInt(rmItem.getTextContent());
                        }
			NodeList peListItems = machineItem.getElementsByTagName("PE");
			for (int j = 0; j < peListItems.getLength(); j++) {
				Element peItem = (Element) peListItems.item(j);
				Element mipsItem = (Element) peItem
						.getElementsByTagName("MIPS").item(0);
				int MIPS = Integer.parseInt(mipsItem.getTextContent());
        			NodeList countListItems = machineItem.getElementsByTagName("count");
                                int count = 1;
        			for (int jj = 0; jj < countListItems.getLength(); jj++) {
                                    Element countItem = (Element) countListItems.item(jj);
                                    count = Integer.parseInt(countItem.getTextContent());
                                    for (int k=0;k<count;k++) {
                                            machine.addPE(MIPS);
                                    }
                                }
                                
			}
                        for (int k=0;k<repeatMachine;k++) {
        			gridElementInstance.addMachine(machine);
                        }
		}
		NodeList storageNodeList = geItem.getElementsByTagName("storage");
		if (storageNodeList.getLength() > 0) {
			Element storageElement = (Element) storageNodeList.item(0);
			StorageType storage = this.getStorage(storageElement);
			gridElementInstance.setStorage(storage);
		}
		Element linkItem = (Element) geItem.getElementsByTagName("link")
				.item(0);
		String linkName = linkItem.getTextContent();
		gridElementInstance.setLink(linkName);
		return gridElementInstance;
	}
}
