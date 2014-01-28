/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author User
 */
class ThumbnailTool extends MediaToolAdapter
  {
    /** {@inheritDoc} */
    
    boolean thumbGenerated = false;
    long offsetTime = 0;
    
    public ThumbnailTool()
    {
        super();
    }
    
    public ThumbnailTool(long offsetInMilliseconds)
    {
        super();
        offsetTime = offsetInMilliseconds;
    }
    
    public boolean isThumbnailGenerated() {
        return thumbGenerated;
    }

    @Override
    public void onVideoPicture(IVideoPictureEvent event)
    {
      long timeStamp = event.getPicture().getTimeStamp();
      
      if (!thumbGenerated && (offsetTime <= 0 || (timeStamp >= (offsetTime / 1000 * Global.DEFAULT_PTS_PER_SECOND)))) {
          // dump to file
          BufferedImage image = event.getImage();
          String outputFilename = "c:\\programas\\grabaciones\\thumb.jpg";
          try {
              ImageIO.write(image, "jpg", new File(outputFilename));
          } catch (IOException ex) {
              Logger.getLogger(ThumbnailTool.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          thumbGenerated = true;
      }
      
      // call parent which will pass the video onto next tool in chain

      super.onVideoPicture(event);
    }
  }
