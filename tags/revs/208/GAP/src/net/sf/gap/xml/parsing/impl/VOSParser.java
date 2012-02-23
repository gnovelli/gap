/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * VOSParser.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id: VOSParser.java 196 2008-01-24 09:58:24Z gnovelli $
 *
 */

package net.sf.gap.xml.parsing.impl;

import net.sf.gap.xml.parsing.*;
import org.w3c.dom.*;

import net.sf.gap.xml.types.*;

import net.sf.gap.xml.parsing.Parser;

/**
 *
 * @author Giovanni Novelli
 */
public class VOSParser extends Parser {
    public VOSParser(Document document) {
        super(document);
    }
    
    public VOSType getVOS() {
        VOSType vos = null;
        return vos;
    }
}
