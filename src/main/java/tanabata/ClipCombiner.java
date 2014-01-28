/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import java.util.Date;

/**
 *
 * @author User
 */
public class ClipCombiner extends TextLoggingProcess {

    public ClipCombiner(JTextArea txtLog) {
        super(txtLog);
    }

    public boolean combineClips(List<Video> videos, String path)
    {
        try {
            PrintWriter writer;
            writer = new PrintWriter(path + File.separator + "mylist.txt", "UTF-8");
            
            for (Video video : videos) {
                File f = new File(path + File.separator + video.getYoutubeId() + ".mp4");
                if (f.exists() && f.length() > 100 * 1024)
                    writer.println("file '" + video.getYoutubeId() + ".mp4'");
            }
            writer.close();
            
            this.log("Starting combination");
            Date date = new Date();
            
            String[] commands = {"ffmpeg", "-f", "concat", "-i", "mylist.txt",
            "-codec:v", "libx264", "-profile:v", "high", "-preset", "veryfast", "-b:v", "2000k", "-maxrate", "2000k",
            "-bufsize", "4000k", "-vf", "scale=1280:720", "-threads", "0", "-codec:a", "mp3", "-b:a", "128k",
            "-r", "25", "-ar", "48k", "combined-" + date.getTime() + ".mp4"};
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.directory(new File(path));
            Process proc = pb.start();
                        
            StreamGobbler errorGobbler, outputGobbler;
                
            boolean readOutput = false, readError = false;
            errorGobbler  = new StreamGobbler(proc.getErrorStream(), readError);
            outputGobbler = new StreamGobbler(proc.getInputStream(), readOutput);
            errorGobbler.start();
            outputGobbler.start();

            int exitCode = 0;
            proc.waitFor();
            exitCode = proc.exitValue();
            
            this.log("Combination complete!");
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(ClipCombiner.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ClipCombiner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
