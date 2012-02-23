/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * HardDiskListType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id: HardDiskListType.java 196 2008-01-24 09:58:24Z gnovelli $
 *
 */

package net.sf.gap.xml.types;

import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class HardDiskListType {
    private LinkedList<HardDiskType> items;
    
    public HardDiskListType() {
        this.setItems(new LinkedList<HardDiskType>());
    }

    public HardDiskListType(LinkedList<HardDiskType> items) {
        this.setItems(items);
    }
    
    public boolean addHardDisk(HardDiskType hardDisk) {
        return this.getItems().add(hardDisk);
    }
    
    public LinkedList<HardDiskType> getItems() {
        return items;
    }

    public void setItems(LinkedList<HardDiskType> items) {
        this.items = items;
    }
}
