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
 * TranscodingSet.java
 *
 * Created on 14 March 2007, 09.07 by Giovanni Novelli
 *
 * $Id: TranscodingSet.java 755 2008-03-06 14:15:28Z gnovelli $
 *
 */
package net.sf.gap.mc.qagesa.multimedia;

import java.util.HashMap;
import java.util.Iterator;

import net.sf.gap.distributions.Uniform_int;

import com.csvreader.CsvReader;

/**
 * This class is responsible to maintain information about all possible
 * transcoding sequences that can be simulated about a movie
 * <p>
 * It's organized as a set of ChunksSequence It's also responsible to read and
 * store the data set for the whole simulation
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.qagesa.multimedia.ChunksSequence
 */
public class TranscodingSet extends HashMap<String, ChunksSequence> {

    private MoviesSet moviesSet; // Movies Set
    /**
     * 
     */
    private static final long serialVersionUID = -944938460684249661L;
    private Uniform_int random;

    public TranscodingSet() {
        super();
        this.setMoviesSet(new MoviesSet());
    }

    public TranscodingSet(String videos_csv) {
        super();
        this.setMoviesSet(new MoviesSet(videos_csv));
        this.random = new Uniform_int("random_ts");
    }

    public TranscodingSet(String videos_csv, String chunks_csv) {
        this(videos_csv);
        this.csvRead(chunks_csv);
    }

    public static void main(String[] args) {
        TranscodingSet transcodingSet = new TranscodingSet(
                "measures/videos.csv", "measures/chunks.csv");
        MoviesSet moviesSet = transcodingSet.getMoviesSet();
        System.out.println("There are " + moviesSet.size() + " movies:");
        System.out.println(moviesSet);
        System.out.println("There are " + transcodingSet.size() + " possible transcoding sequences:");
        System.out.println(transcodingSet);
        int ns = transcodingSet.size();
        System.out.println(ns + " random chunks sequences:");
        Iterator<String> it = transcodingSet.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            i++;
            ChunksSequence acs = transcodingSet.get(it.next());
            System.out.println("{i=" + i + ", " + " md5=" + acs.getMessageDigest() + " " + acs);
        }
    }

    public String toString() {
        String msg = "";
        msg += "TRANSCODINGSET{";
        int sequences = this.size();
        for (int j = 0; j < sequences; j++) {
            ChunksSequence chunksSequence = this.get(j);
            if ((j + 1) < sequences) {
                msg += chunksSequence + ", ";
            } else {
                msg += chunksSequence + "}";
            }
        }
        return msg;
    }

    public void csvRead(String csvname) {
        try {
            CsvReader reader = new CsvReader("measures/chunks.csv", ';');

            reader.readHeaders();

            ChunksSequence chunksSequence = null;
            while (reader.readRecord()) {
                String strName = reader.get("Name");
                String strOperation = reader.get("Operation");
                String strOperationParameters = reader.get("OperationParameters");
                String strSequenceNumber = reader.get("SequenceNumber");
                String strInputSize = reader.get("InputSize");
                String strOutputSize = reader.get("OutputSize");
                String strMsProcessing = reader.get("MsProcessing");
                String strMsDuration = reader.get("MsDuration");

                int iSequenceNumber = Integer.parseInt(strSequenceNumber);
                int iInputSize = Integer.parseInt(strInputSize);
                int iOutputSize = Integer.parseInt(strOutputSize);
                int iMsProcessing = Integer.parseInt(strMsProcessing);
                int iMsDuration = Integer.parseInt(strMsDuration);

                Movie movie = this.getMoviesSet().get(strName);
                if (iSequenceNumber == 1) {
                    chunksSequence = new ChunksSequence(movie, strOperation,
                            strOperationParameters);
                }

                int div = 2;
                for (int i = 0; i < div; i++) {
                    int sn = iSequenceNumber * div + i - (div - 1);
                    Chunk chunk = new Chunk(sn, Math.round(iInputSize * (1.0f / div)),
                            Math.round(iOutputSize * (1.0f / div)), Math.round(iMsProcessing * (1.0f / div)), Math.round(iMsDuration * (1.0f / div)), 1.0);
                    chunksSequence.add(chunk);
                }
                if (iSequenceNumber == 6) {
                    ChunksSequence sequence = this.expandChunksSequence(chunksSequence);
                    this.addSequence(sequence.getMessageDigest(),
                            sequence);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private ChunksSequence expandChunksSequence(ChunksSequence currentSequence) {
        ChunksSequence sequence =
                new ChunksSequence(
                currentSequence.getMovie(),
                currentSequence.getOperation(),
                currentSequence.getOperationParameters());
        int nc = currentSequence.size();
        for (int k = 0; k < 5; k++) {
            for (int n = 0; n < nc; n++) {
                int nsn = k*nc + n + 1;
                Chunk chunk = currentSequence.get(n).clone();
                chunk.setSequenceNumber(nsn);
                sequence.add(chunk);
            }
        }
        return sequence;
    }

    public boolean containsSequence(String movieTag) {
        boolean result = false;
        Iterator it = this.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.equalsIgnoreCase(movieTag)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public ChunksSequence addSequence(String movieTag, ChunksSequence sequence) {
        this.getMoviesSet().put(movieTag, sequence.getMovie());
        return this.put(movieTag, sequence);
    }

    public String selectRandomTag() {
        int index = this.random.sample(this.size());
        String md5 = (String) (this.keySet().toArray()[index]);
        return md5;
    }

    public ChunksSequence selectRandomChunksSequence(String movieName) {
        ChunksSequence aChunksSequence;
        do {
            int index = this.random.sample(this.size());
            String md5 = (String) (this.keySet().toArray()[index]);
            aChunksSequence = this.get(md5);
        } while (aChunksSequence.getMovie().getName().compareTo(movieName) != 0);
        return aChunksSequence;
    }

    public MoviesSet getMoviesSet() {
        return moviesSet;
    }

    public void setMoviesSet(MoviesSet moviesSet) {
        this.moviesSet = moviesSet;
    }
}
