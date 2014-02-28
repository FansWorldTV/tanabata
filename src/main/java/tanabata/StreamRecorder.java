/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanabata;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;

import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

/**
 *
 * @author User
 */
class StreamRecorder extends Thread {

    public boolean read = true;
    private String stream;
    private String path;
    private Integer clipNumber;
    private DefaultListModel list;
    private JTextArea txtLog;
    private Process ffproc;

    public StreamRecorder(final String stream, final String path, final Integer clipNumber, final DefaultListModel list, final JTextArea txtLog) {
        this.stream = stream;
        this.path = path;
        this.clipNumber = clipNumber;
        this.list = list;
        this.txtLog = txtLog;
    }

    @Override
    public void run() {
        //Play movie on button click

        //String rtmp = "rtmp://live4.stweb.tv/fans/live1 live=1 stop=5000";
        String rtmp = stream;
        String clipName = "record" + clipNumber;
        String clipPath = path + clipName + ".flv";
        
        String[] commands = {"ffmpeg", "-y", "-i", rtmp,
            "-ar", "44100", "-b:v", "4000k", "-qmax", "10",
            clipPath};
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(path));
        Process proc;
        try {
            proc = pb.start();
            this.txtLog.append("Started recording " + clipName + "..." + "\n");
            StreamGobbler errorGobbler, outputGobbler;

            boolean readOutput = false, readError = false;
            errorGobbler = new StreamGobbler(proc.getErrorStream(), readError);
            outputGobbler = new StreamGobbler(proc.getInputStream(), readOutput);
            errorGobbler.start();
            outputGobbler.start();
            this.ffproc = proc;
        } catch (IOException ex) {
            Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*
         IMediaReader reader = ToolFactory.makeReader(rtmp);
         reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
                
         IMediaTool addTimeStamp = new TimeStampTool();
         reader.addListener(addTimeStamp);
         IMediaWriter writer = ToolFactory.makeWriter(clipPath, reader);
         reader.addListener(writer);
                
         this.txtLog.append("Started recording " + clipName + "..." + "\n");
                
         addTimeStamp.addListener(ToolFactory.makeViewer());
                
         while(reader.readPacket() == null && this.read) {}
         writer.close();
         reader.close();
         */
                //This should appear after video ends
    }

    public void stoprec() {
        System.out.println("End of video");
        this.ffproc.destroy();
        this.txtLog.append("Stopped recording " + this.clipNumber + "." + "\n");
        list.addElement("record" + this.clipNumber);
    }
}
