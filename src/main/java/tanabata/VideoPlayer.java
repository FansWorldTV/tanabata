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
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
class VideoPlayer {
    
    public void play(final String stream)
    {
        new Thread() {
            @Override
            public void run() {
                //Play movie on button click
                
                //String rtmp = "rtmp://live4.stweb.tv/fans/live1 live=1 stop=5000";
                String rtmp = stream;
                IMediaReader reader = ToolFactory.makeReader(rtmp);
                
                reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
                
                IMediaTool addTimeStamp = new TimeStampTool(); //TimeStampToolFactory.makeTimeStampTool();
                //IMediaWriter writer = ToolFactory.makeWriter("c:\\programas\\grabaciones\\output.mp4", reader);
                //IMediaViewer viewer = ToolFactory.makeViewer(true);
                
                PositionCheck checkPos = new PositionCheck();
                reader.addListener(addTimeStamp);
                
                //addTimeStamp.addListener(checkPos);
                reader.addListener(checkPos);
                
                boolean updatedS = false;
                boolean updatedE = false;
                
                
                Long startTime = 10 * Global.DEFAULT_PTS_PER_SECOND;
                Long endTime = 20 * Global.DEFAULT_PTS_PER_SECOND;
                
                IMediaWriter writer = ToolFactory.makeWriter("c:\\programas\\grabaciones\\output.mp4", reader);
                
                float thumbPercentage = 0.5f;
                long duration = 0;
                
                boolean attachedThumbnail = false;
                
                while(reader.readPacket() == null)
                {
                    if (duration <= 0) duration = reader.getContainer().getDuration();
                    
                    if (duration > 0 && !attachedThumbnail && checkPos.getTimeStamp() >= thumbPercentage * duration) {
                        attachedThumbnail = true;
                        ThumbnailTool createThumb = new ThumbnailTool();
                        checkPos.addListener(createThumb);
                    }
                    
                    if(!updatedS && (checkPos.getTimeStamp() >= startTime))
                    {
                     updatedS = true;
                     updatedE = false;
                     writer = ToolFactory.makeWriter("c:\\programas\\grabaciones\\output.mp4", reader);
                     checkPos.addListener(writer);
                    }

                    if(!updatedE && (checkPos.getTimeStamp() >= endTime ))
                    {
                     updatedE = true;
                     checkPos.removeListener(writer);
                     writer.close();
                     System.out.print("\nCLOSE\n");
                    } 
                }
                
                //This should appear after video ends
                System.out.println("End of video");
                
                String[] commands = {"ffmpeg", "-i", "output.mp4",
                "c:\\programas\\grabaciones\\outputxx.mp4"};
                ProcessBuilder pb = new ProcessBuilder(commands);
                pb.directory(new File("c:\\programas\\grabaciones"));
                Process proc;
                try {
                    proc = pb.start();

                    StreamGobbler errorGobbler, outputGobbler;

                    boolean readOutput = false, readError = false;
                    errorGobbler  = new StreamGobbler(proc.getErrorStream(), readError);
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
            }
        }.start();
        
    }
    
}

