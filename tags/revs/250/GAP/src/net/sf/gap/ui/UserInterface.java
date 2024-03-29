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
 * UserInterface.java
 *
 * Created on 4 March 2007, 13.51 by Giovanni Novelli
 *
 * $Id: UserInterface.java 240 2008-01-24 13:04:18Z gnovelli $
 *
 */

package net.sf.gap.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 
 * @author Giovanni Novelli
 */
public class UserInterface extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8687484659669194891L;

	/** Creates new form UserInterface */
	public UserInterface(String title) {
		super(title);
		initComponents();
		initConsole();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        console.setColumns(20);
        console.setFont(new java.awt.Font("Monospaced", 0, 10));
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea console;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

	private ConsoleOutputStream outputStream = null;

	private PrintStream printStream = null;

	private OutputStream copy = null;

	private void initConsole() {
		this.installConsole();
	}

	public synchronized void updateConsole() {
		jScrollPane1.getVerticalScrollBar().setValue(
				jScrollPane1.getVerticalScrollBar().getMaximum());
	}

	public void setCopyOutputStream(OutputStream c) {
		setCopy(c);
	}

	public OutputStream getOutputStream() {
		if (outputStream == null)
			outputStream = new ConsoleOutputStream(this, System.out);
		return outputStream;
	}

	public PrintStream getPrintStream() {
		if (printStream == null)
			printStream = new PrintStream(getOutputStream(), true);
		return printStream;
	}

	public void installConsole() {
		System.setOut(getPrintStream());
		System.setErr(getPrintStream());
	}

	// WARNING - doesn't correctly translate bytes to chars.
	private class ConsoleOutputStream extends OutputStream {
		OutputStream orig;

		UserInterface ui;

		public ConsoleOutputStream(UserInterface ui, OutputStream o) {
			orig = o;
			this.ui = ui;
		}

		@Override
		public void write(int b) throws IOException {
			orig.write(b);
			if (getCopy() != null)
				getCopy().write(b);
			byte[] buf = new byte[1];
			buf[0] = (byte) b;
			console.append(new String(buf));
			ui.updateConsole();
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			orig.write(b, off, len);
			if (getCopy() != null)
				getCopy().write(b, off, len);
			console.append(new String(b, off, len));
			ui.updateConsole();
		}
	}

	public void setOutputStream(ConsoleOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void setPrintStream(PrintStream printStream) {
		this.printStream = printStream;
	}

	public OutputStream getCopy() {
		return copy;
	}

	public void setCopy(OutputStream copy) {
		this.copy = copy;
	}
}
