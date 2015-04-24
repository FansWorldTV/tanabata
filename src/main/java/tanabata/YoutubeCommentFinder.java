/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
class YoutubeCommentFinder {
    void find(final String searchterm, final Integer videolimit, final Integer commentlimit, final JTextArea log) {
        
        new Thread() {
            @Override
            public void run() {
                try {
                    Integer vl = videolimit;
                    if (videolimit > 50) vl = 50;
                    Content c = Request.Get("http://gdata.youtube.com/feeds/api/videos/?vq=" +
                            URLEncoder.encode(searchterm, "UTF-8") +
                            "&alt=json&max-results=" +
                            vl)
                            .execute().returnContent();
                    
                    String jsonstr = c.toString();
                    
                    JSONObject jo = new JSONObject(jsonstr);
                    
                    JSONArray ja = jo.getJSONObject("feed").getJSONArray("entry");
                    Integer cl = commentlimit;
                    
                    for(int i = 0 ; i < ja.length(); i++){
                        String dataurl = ja.getJSONObject(i).getJSONObject("id").getString("$t");
                        String youtubeid = dataurl.substring(dataurl.lastIndexOf("/") + 1);
                        Integer commentamount = 0;
                        
                        Content cc = Request.Get("http://gdata.youtube.com/feeds/api/videos/" + youtubeid + "/comments" +
                            "?alt=json&max-results=50")
                            .execute().returnContent();
                    
                        String jsonstrc = cc.toString();

                        JSONObject joc = new JSONObject(jsonstrc);

                        JSONArray jac = joc.getJSONObject("feed").getJSONArray("entry");
                        
                        for (int ic = 0; (ic < jac.length() && commentamount < cl); ic++) {
                            String commenttext = jac.getJSONObject(ic).getJSONObject("content").getString("$t");
                            commenttext = commenttext.replace("\n", " ").replace("\r", "");
                            
                            if (commenttext.length() <= 140 && commenttext.length() > 20 && !(commenttext.toLowerCase().matches(".*(video|song|listen|watch|\\d:\\d|i am \\d).*"))) {
                                log.append(commenttext + "\n|||XXX|||\n");
                                commentamount++;
                            }
                        }
                        
                    }   } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(YoutubeCommentFinder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(YoutubeCommentFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }
}
