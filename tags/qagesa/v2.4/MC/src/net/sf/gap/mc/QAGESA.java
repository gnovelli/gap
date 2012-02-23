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
 * Simulation.java
 *
 * Created on 7 August 2006, 18.07 by Giovanni Novelli
 *
 * $Id: QAGESA.java 738 2008-03-05 21:02:44Z gnovelli $
 *
 */
package net.sf.gap.mc;

import java.util.Locale;
import java.util.Properties;
import java.io.*;

import net.sf.gap.mc.qagesa.grid.QAGESAVirtualOrganization;
import net.sf.gap.mc.qagesa.simulation.impl.Simulation;
import net.sf.gap.mc.qagesa.simulation.impl.XMLSimulation;
import net.sf.gap.mc.qagesa.users.impl.User;
import net.sf.gap.ui.UserInterface;


// All writes to this print stream are copied to two print streams
/**
 * This class is responsible of main entry method of QAGESA's simulation
 * 
 * @author Giovanni Novelli
 */
public class QAGESA {

    public static double getStartTime() {
        return startTime;
    }

    public static void setStartTime(double aStartTime) {
        startTime = aStartTime;
    }
    public static double relaxTime = 200.0;
    public static int requests;
    public static double linearDelta=1.0-0.61803398874989484820458683436564;
    public static double thetaU=0.5;
    public static double thetaR=0.25;
    public static boolean caching;
    public static boolean repeated;
    public static boolean reuseagents;
    private static double startTime;
    
    public static String getOutputPath() {
        return outputPath;
    }

    public static void setOutputPath(String aOutputPath) {
        outputPath = aOutputPath;
    }
    private static String outputPath;
    private static String usercsvName;

    public static void main(String[] args) {
    // Get default locale
    Locale locale = new Locale("en", "US");
    Locale.setDefault(locale);
        String confname = "QAGESA.conf";
        if (args.length == 1) {
            confname = args[0];
        }
        QAGESA.execute(confname);
    }

    private static String usedConf;
    public static void execute(String confname) {
        usedConf=confname;
        Properties conf = new Properties();
        try {
            conf.load(new FileInputStream(confname));
        } catch (final IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        boolean swing = false;
        String prop;
        prop = conf.getProperty("link");
        if (prop != null) {
            try {
                conf.load(new FileInputStream(prop));
                usedConf = prop;
            } catch (final IOException e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
        setOutputPath(conf.getProperty("output"));
        QAGESA.prepareOutput();
        prop = conf.getProperty("ui");
        if (prop.compareTo("false") == 0) {
            swing = false;
        }
        if (prop.compareTo("true") == 0) {
            swing = true;
        }
        prop = conf.getProperty("measure");
        Integer whichMeasure = 3;
        usercsvName = "USERS_" + prop + ".csv";
        QAGESA.openOutput();
        if (prop.compareTo("MS") == 0) {
            whichMeasure = QAGESAVirtualOrganization.MS;
        }
        if (prop.compareTo("MF") == 0) {
            whichMeasure = QAGESAVirtualOrganization.MF;
        }
        if (prop.compareTo("MR") == 0) {
            whichMeasure = QAGESAVirtualOrganization.MR;
        }
        if (prop.compareTo("RMS") == 0) {
            whichMeasure = QAGESAVirtualOrganization.RMS;
        }
        if (prop.compareTo("RMF") == 0) {
            whichMeasure = QAGESAVirtualOrganization.RMF;
        }
        if (prop.compareTo("RMR") == 0) {
            whichMeasure = QAGESAVirtualOrganization.RMR;
        }
        prop = conf.getProperty("users");
        Integer numUsers = Integer.parseInt(prop);
        
        prop = conf.getProperty("requests");
        Integer numRequests;
        numRequests = Integer.parseInt(prop);
        QAGESA.requests=numRequests;
        prop = conf.getProperty("distribution");
        String distribution = prop;
        if (prop != null) {
            User.setDistribution(distribution);
            if (prop.compareToIgnoreCase("zipf")==0) {
                prop = conf.getProperty("thetaU");
                QAGESA.thetaU = Double.parseDouble(prop);
                prop = conf.getProperty("thetaR");
                QAGESA.thetaR = Double.parseDouble(prop);
            } 
            if (prop.compareToIgnoreCase("scaled")==0) {
                prop = conf.getProperty("linearDelta");
                QAGESA.linearDelta = Double.parseDouble(prop);
            } 
        }

        prop = conf.getProperty("replications");
        Integer numReplications = Integer.parseInt(prop);
        prop = conf.getProperty("confidence");
        Double confidence = Double.parseDouble(prop);
        prop = conf.getProperty("accuracy");
        Double accuracy = Double.parseDouble(prop);
        //prop = conf.getProperty("pstart");
        //Double pstart = Double.parseDouble(prop);
        Double pstart = 500.0;
        prop = conf.getProperty("start");
        Double start = Double.parseDouble(prop);
        QAGESA.setStartTime(start);
        prop = conf.getProperty("end");
        Double end = Double.parseDouble(prop);
        prop = conf.getProperty("relax");
        relaxTime = Double.parseDouble(prop);
        prop = conf.getProperty("caching");
        caching = Boolean.parseBoolean(prop);
        prop = conf.getProperty("repeated");
        repeated = Boolean.parseBoolean(prop);
        prop = conf.getProperty("reuseagents");
        reuseagents = Boolean.parseBoolean(prop);
        try {
            if (swing) {
                java.awt.EventQueue.invokeAndWait(new Runnable() {

                    public void run() {
                        new UserInterface("QAGESA Simulation").setVisible(true);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unwanted errors happen");
        }

        String xml = conf.getProperty("xml");
        String xsd = conf.getProperty("xsd");
        if (xml != null) {
            QAGESA.simulate(xml, xsd, pstart, start, end, numUsers, numRequests, false, whichMeasure,
                    numReplications, confidence, accuracy, swing);
        } else {
            prop = conf.getProperty("ces");
            Integer numCE = Integer.parseInt(prop);
            prop = conf.getProperty("ses");
            Integer numSE = Integer.parseInt(prop);
            prop = conf.getProperty("machines");
            Integer numMachine = Integer.parseInt(prop);
            prop = conf.getProperty("pes");
            Integer numPE = Integer.parseInt(prop);
            prop = conf.getProperty("mips");
            Integer MIPS = Integer.parseInt(prop);
            QAGESA.simulate(pstart, start, end, numCE, numMachine, numPE, MIPS, numSE, numUsers, numRequests, caching, whichMeasure,
                    numReplications, confidence, accuracy, swing);
        }
        QAGESA.closeOutput();
    }

    private static void simulate(double pstart, double start, double end, 
            int numCE, int numMachine, int numPE, int MIPS, int numSE,
            int numUsers, int numRequests,
            boolean caching, int whichMeasure, int replications,
            double confidence, double accuracy, boolean swing) {

        Simulation simulation;
        simulation = new Simulation(pstart, start, end, numCE, numMachine, numPE, MIPS, numSE, numUsers, numRequests, caching,
                whichMeasure, replications, confidence, accuracy);
        simulation.start();
    }

    private static void simulate(String xml, String xsd, double pstart, double start, double end, int numUsers, int numRequests,
            boolean caching, int whichMeasure, int replications,
            double confidence, double accuracy, boolean swing) {

        XMLSimulation simulation;
        simulation = new XMLSimulation(xml, xsd, pstart, start, end, numUsers, numRequests, caching,
                whichMeasure, replications, confidence, accuracy);
        simulation.start();
    }
    // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    private static void copy(String srcPath, String dstPath) throws IOException {
        File src = new File(srcPath);
        File dst = new File(dstPath);
        if (src.exists()) {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            src.delete();
        }
    }
    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    private static void copywd(String srcPath, String dstPath) throws IOException {
        File src = new File(srcPath);
        File dst = new File(dstPath);
        if (src.exists()) {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
    public static PrintStream outQoS;
    public static PrintStream outReF_RT;
    public static PrintStream outReF_CR;
    public static PrintStream outReF_PR;
    public static PrintStream outUSER;

    private static void prepareOutput() {
        // Create a directory; all non-existent ancestor directories are
        // automatically created
        File outputDir = new File(getOutputPath());
        QAGESA.deleteDir(outputDir);
        boolean success = outputDir.mkdirs();
        if (!success) {
            success = QAGESA.deleteDir(outputDir);
            success = outputDir.mkdirs();
        }
        if (!success) {
            System.exit(2);
        }
    }

    private static void openOutput() {
        try {
                // Tee standard output
                PrintStream out = new PrintStream(new FileOutputStream("sim_out.txt"));
                PrintStream tee = new TeeStream(System.out, out);

                System.setOut(tee);

                // Tee standard error
                PrintStream err = new PrintStream(new FileOutputStream("sim_err.txt"));
                tee = new TeeStream(System.err, err);

                System.setErr(tee);
            } catch (FileNotFoundException e) {
            }
        try {
            File outFile;
            outFile = new File(QAGESA.getOutputPath() + "/ReF_RT.csv");
            outReF_RT = new PrintStream(new FileOutputStream(outFile, true));
            outFile = new File(QAGESA.getOutputPath() + "/QoS.csv");
            outQoS = new PrintStream(new FileOutputStream(outFile, true));
            outFile = new File(QAGESA.getOutputPath() + "/ReF_CR.csv");
            outReF_CR = new PrintStream(new FileOutputStream(outFile, true));
            outFile = new File(QAGESA.getOutputPath() + "/ReF_PR.csv");
            outReF_PR = new PrintStream(new FileOutputStream(outFile, true));
            outFile = new File(QAGESA.getOutputPath() + "/" + usercsvName);
            outUSER = new PrintStream(new FileOutputStream(outFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeOutput() {
        try {
            outQoS.close();
            outReF_RT.close();
            outReF_CR.close();
            outReF_PR.close();
            outUSER.close();
            File usedConfFile = new File(usedConf);
            QAGESA.copywd(usedConf, getOutputPath() + "/" + usedConfFile.getName());
            QAGESA.copy("sim_graphs.sjg", getOutputPath() + "/sim_graphs.sjg");
            QAGESA.copy("sim_trace", getOutputPath() + "/sim_trace.txt");
            QAGESA.copy("sim_report", getOutputPath() + "/sim_report.txt");
            //QAGESA.copy("sim_out.txt", getOutputPath() + "/sim_out.txt");
            QAGESA.copy("sim_err.txt", getOutputPath() + "/sim_err.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class TeeStream extends PrintStream {
    PrintStream out;
    public TeeStream(PrintStream out1, PrintStream out2) {
        super(out1);
        this.out = out2;
    }
    public void write(byte buf[], int off, int len) {
        try {
            super.write(buf, off, len);
            out.write(buf, off, len);
        } catch (Exception e) {
        }
    }
    public void flush() {
        super.flush();
        out.flush();
    }
}
