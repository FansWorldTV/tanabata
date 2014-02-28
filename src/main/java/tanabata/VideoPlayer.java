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
import javax.swing.DefaultListModel;

/**
 *
 * @author User
 */
class VideoPlayer {
    
    public void play(final String filepath)
    {
        new Thread() {
            @Override
            public void run() {
                //Play movie on button click
                
                IMediaReader reader = ToolFactory.makeReader(filepath);
                reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
                IMediaTool addTimeStamp = new TimeStampTool();
                
                reader.addListener(addTimeStamp);
                addTimeStamp.addListener(ToolFactory.makeViewer());
                
                while(reader.readPacket() == null) {}
                
            }
        }.start();
        
    }
    
}

