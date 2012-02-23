/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * GridType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id: GridType.java 196 2008-01-24 09:58:24Z gnovelli $
 *
 */

package net.sf.gap.xml.types;

import java.util.LinkedList;

/**
 *
 * @author Giovanni Novelli
 */
public class GridType {
    private LinkedList<GridElementType> gridElements;
    
    public GridType() {
        this.setGridElements(new LinkedList<GridElementType>());
    }

    public boolean addGridElement(GridElementType gridElement) {
        return this.getGridElements().add(gridElement);
    }
    
    public LinkedList<GridElementType> getGridElements() {
        return gridElements;
    }

    public void setGridElements(LinkedList<GridElementType> gridElements) {
        this.gridElements = gridElements;
    }
    
}
