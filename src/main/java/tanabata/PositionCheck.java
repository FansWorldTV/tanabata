/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.IWriteHeaderEvent;
import com.xuggle.mediatool.event.IWritePacketEvent;
import com.xuggle.mediatool.event.IWriteTrailerEvent;

/**
 *
 * @author User
 */
class PositionCheck extends MediaToolAdapter
{
  public Long timeStamp = (long) 0;
  public boolean convert = true;
  
  public Long getTimeStamp()
  {
      return timeStamp;
  }
  
  @Override
    public void onVideoPicture(IVideoPictureEvent event)
  {
   timeStamp = event.getTimeStamp();
   
    if(convert)
   super.onVideoPicture(event);
  }
  
  @Override
   public void onAudioSamples(IAudioSamplesEvent event)
  {
   if(convert)
    super.onAudioSamples(event);
  }
  
  @Override
   public void onWritePacket(IWritePacketEvent event)
  {
   if(convert)
    super.onWritePacket(event);
  }
  
  @Override
   public void onWriteTrailer(IWriteTrailerEvent event)
  {
   if(convert)
    super.onWriteTrailer(event);
  }
  
  @Override
   public void onReadPacket(IReadPacketEvent event)
  {
   if(convert)
    super.onReadPacket(event);
  }
  
  @Override
   public void onWriteHeader(IWriteHeaderEvent event)
  {
   if(convert)
    super.onWriteHeader(event);
  }
}
