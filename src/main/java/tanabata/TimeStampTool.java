/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author User
 */
class TimeStampTool extends MediaToolAdapter
  {
    /** {@inheritDoc} */

    @Override
    public void onVideoPicture(IVideoPictureEvent event)
    {
      // get the graphics for the image

      Graphics2D g = event.getImage().createGraphics();

      // establish the timestamp and how much space it will take

      Font font = new Font(Font.MONOSPACED, Font.PLAIN, 32);
      g.setFont(font);
      
      String timeStampStr = event.getPicture().getFormattedTimeStamp();
      Rectangle2D bounds = g.getFont().getStringBounds(timeStampStr,
        g.getFontRenderContext());
      
      // compute the amount to inset the time stamp and translate the
      // image to that position

      double inset = bounds.getHeight() / 2;
      g.translate(inset, event.getImage().getHeight() - inset);
      
      // draw a white background and black timestamp text

      g.setColor(Color.WHITE);
      g.fill(bounds);
      g.setColor(Color.BLACK);
      g.drawString(timeStampStr, 0, 0);
      
      // call parent which will pass the video onto next tool in chain

      super.onVideoPicture(event);
    }
  }
