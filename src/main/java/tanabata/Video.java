/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

/**
 *
 * @author User
 */
public class Video {
    protected String youtubeId;
    protected String stream;
    protected Integer duration;

    /**
     * @return the youtubeId
     */
    public String getYoutubeId() {
        return youtubeId;
    }

    /**
     * @param youtubeId the youtubeId to set
     */
    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    /**
     * @return the stream
     */
    public String getStream() {
        return stream;
    }

    /**
     * @param stream the stream to set
     */
    public void setStream(String stream) {
        this.stream = stream;
    }

    /**
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    
}
