/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author User
 */
class ClipCutter extends TextLoggingProcess {

    public ClipCutter(JTextArea txtLog) {
        super(txtLog);
    }
    
    void cut(final String path, final String clipName, final String from, final String to) {
        final JTextArea log = this.txtLog;
        new Thread() {
            @Override
            public void run() {
                String sourcePath = path + clipName + ".flv";
                String destPath = path + "clip_" + clipName + "_" + from.replace(":", "_") + "-" + to.replace(":", "_") + ".flv";
                
                // ffmpeg -i "r1.mp4" -ss 00:00:10 -to 00:00:10 -codec:v libx264 -profile:v high
                // -preset veryfast -b:v 2000k -maxrate 2000k -bufsize 4000k
                // -vf scale=-1:720 -threads 1 -codec:a mp3 -b:a 128k finales\salida.mp4
                
                log.append("Clipping " + clipName + " from " + from + " to " + to + "..." + "\n");
                
                String[] commands = {"ffmpeg", "-i", sourcePath,
                    "-ss", from, "-to", to, "-c", "copy",
                    //"-codec:v", "libx264", "-profile:v", "high", "-preset", "veryfast", "-b:v", "2000k",
                    //"-maxrate", "2000k", "-bufsize", "4000k", "-vf", "scale=-1:720", "-codec:a", "mp3",
                    //"-b:a", "128k",
                    destPath};
                ProcessBuilder pb = new ProcessBuilder(commands);
                pb.directory(new File(path));
                Process proc;
                try {
                    proc = pb.start();

                    StreamGobbler errorGobbler, outputGobbler;

                    boolean readOutput = false, readError = false;
                    errorGobbler = new StreamGobbler(proc.getErrorStream(), readError);
                    outputGobbler = new StreamGobbler(proc.getInputStream(), readOutput);
                    errorGobbler.start();
                    outputGobbler.start();

                    int exitCode = 0;
                    proc.waitFor();
                    exitCode = proc.exitValue();
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Transcoded");
                log.append("Success! Clip name: " + destPath + "\n");
            }
        }.start();
    }
}
