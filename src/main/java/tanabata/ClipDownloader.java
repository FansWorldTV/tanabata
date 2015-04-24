/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Content;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ClipDownloader extends TextLoggingProcess {

    public ClipDownloader(JTextArea txtLog) {
        super(txtLog);
    }

    public boolean downloadClips(List<Video> videos, Integer percentage, Integer length, String path)
    {
        for (Video video : videos) {
            try {
                Integer duration = video.getDuration();
                Integer startpoint = duration * percentage / 100;
                String startstr = this.getDurationString(startpoint);
                String lenstr = this.getDurationString(length);
                
                String[] commands = {path + "ffmpeg", "-i", video.getStream(), 
                "-ss", startstr, "-t", lenstr,
                "-codec:v", "libx264", "-profile:v", "high", "-preset", "veryfast", "-b:v", "2000k", "-maxrate", "2000k",
                "-bufsize", "4000k", "-vf", "scale=1280:720", "-threads", "0", "-codec:a", "mp3", "-b:a", "128k",
                "-r", "25", "-ar", "48k", "-y", video.getYoutubeId() + ".mp4"};
                ProcessBuilder pb = new ProcessBuilder(commands);
                this.log("Downloading: " + video.getYoutubeId() + " - " + startstr + " -> " + lenstr + " / " + duration);
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
                
                this.log("Downloaded " + video.getYoutubeId());
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(ClipDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return true;
    }
    
    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}
