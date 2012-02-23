/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * EntityType.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id: EntityType.java 196 2008-01-24 09:58:24Z gnovelli $
 *
 */

package net.sf.gap.xml.types;

/**
 *
 * @author Giovanni Novelli
 */
public abstract class EntityType {
    private String name;

    public EntityType(String name) {
        this.setName(name);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
