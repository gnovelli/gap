/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Parser.java
 *
 * Created on 22 January 2008, 09.00 by Giovanni Novelli
 *
 * $Id: Parser.java 196 2008-01-24 09:58:24Z gnovelli $
 *
 */

package net.sf.gap.xml.parsing;

import org.w3c.dom.*;

/**
 *
 * @author Giovanni Novelli
 */
public class Parser {

    public Parser(Document document) {
        this.setDocument(document);
    }

    private Document document;
    
    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
