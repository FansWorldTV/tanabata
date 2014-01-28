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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author User
 */
public class VideoStreamFiller extends TextLoggingProcess {

    public VideoStreamFiller(JTextArea txtLog) {
        super(txtLog);
    }

    public List<Video> fillVideos(List<Video> videos, String path)
    {
        List<Video> videosfinal = new ArrayList<>();
        for (Video video : videos) {
            List<String> commands = new ArrayList<>();
            commands.add("youtube-dl");
            commands.add("-g");
            commands.add("http://www.youtube.com/watch?v=" + video.getYoutubeId());
            
            try {
                ProcessBuilder pb = new ProcessBuilder(commands);
                pb.directory(new File(path));
                Process p = pb.start();

                BufferedReader stdInput = new BufferedReader(new 
                    InputStreamReader(p.getInputStream()));

                BufferedReader stdError = new BufferedReader(new 
                    InputStreamReader(p.getErrorStream()));

                // read the output from the command
                this.log("getting stream: " + video.getYoutubeId());
                String s;
                while ((s = stdInput.readLine()) != null) {
                    this.log(s);
                    if (s != null && !s.isEmpty()) video.setStream(s);
                }

                // read any errors from the attempted command
                while ((s = stdError.readLine()) != null) {
                    this.log("ERROR GETTING STREAM: " + video.getYoutubeId() + " " + s);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ParamChooser.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(video.getStream() != null && !video.getStream().isEmpty())
                videosfinal.add(video);
        }

        return videosfinal;
    }
}
