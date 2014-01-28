/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author User
 */
public class YoutubeFinder extends TextLoggingProcess {

    public YoutubeFinder(JTextArea txtLog) {
        super(txtLog);
    }

    public List<Video> findIds(String searchterm, Integer cliplimit, Integer maxduration)
    {
        List<Video> youtubes = new ArrayList<>();
        try {
            this.log("Searching Youtube for: " + searchterm);
            
            Content c = Request.Get("http://gdata.youtube.com/feeds/api/videos/?vq=" + 
                    URLEncoder.encode(searchterm, "UTF-8") + 
                    "&fields=" + URLEncoder.encode("entry[media:group/yt:duration/@seconds < " + (maxduration + 1) + "]", "UTF-8") +
                    "&alt=json&max-results=" + 
                    50)
                .execute().returnContent();
            
            String jsonstr = c.toString();
            
            JSONObject jo = new JSONObject(jsonstr);
            
            JSONArray ja = jo.getJSONObject("feed").getJSONArray("entry");
            
            
            for(int i = 0 ; ((i < ja.length()) && (youtubes.size() < cliplimit)); i++){
                String dataurl = ja.getJSONObject(i).getJSONObject("id").getString("$t");
                String youtubeid = dataurl.substring(dataurl.lastIndexOf("/") + 1);
                Integer duration = ja.getJSONObject(i).getJSONObject("media$group").getJSONObject("yt$duration").getInt("seconds");
                if (duration <= maxduration) {
                    Video v = new Video();
                    v.setYoutubeId(youtubeid);
                    v.setDuration(duration);
                    youtubes.add(v);
                    this.log("Found clip: " + youtubeid);
                }
            }
            
            
            return youtubes;
        } catch (IOException ex) {
            Logger.getLogger(YoutubeFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return youtubes;
    }
}
