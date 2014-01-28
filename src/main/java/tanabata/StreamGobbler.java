/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author User
 */
public class StreamGobbler extends Thread {
    private InputStream is;
    private StringBuilder output;
    private volatile boolean completed; // mark volatile to guarantee a thread safety

    public StreamGobbler(InputStream is, boolean readStream) {
        this.is = is;
        this.output = (readStream ? new StringBuilder(256) : null);
    }

    public void run() {
        completed = false;
        try {
            String NL = System.getProperty("line.separator", "\r\n");

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null) {
                if (output != null)
                    output.append(line + NL); 
            }
        } catch (IOException ex) {
            // ex.printStackTrace();
        }
        completed = true;
    }

    /**
     * Get inputstream buffer or null if stream
     * was not consumed.
     * @return
     */
    public String getOutput() {
        return (output != null ? output.toString() : null);
    }

    /**
     * Is input stream completed.
     * @return
     */
    public boolean isCompleted() {
        return completed;
    }

}
