/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.awt.Desktop;
import javax.swing.JTextArea;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import twitter4j.auth.RequestToken;

/**
 *
 * @author User
 */
public class TwitterShare {
    public static void share(final String text, final Twitter twitter, final JTextArea txtlog) {
        new Thread() {
            @Override
            public void run() {
                //Instantiate and initialize a new twitter status update
                StatusUpdate statusUpdate = new StatusUpdate(text);
                //attach any media, if you want to
                //statusUpdate.setMedia("", new URL("").openStream());

                //tweet or update status
                Status status;
                try {
                    status = twitter.updateStatus(statusUpdate);
                    
                    System.out.println("status.getId() = " + status.getId());
                    
                    txtlog.append("Tweet created (@" + status.getUser() + "): ID " + status.getId() + "\n");
                } catch (TwitterException ex) {
                    Logger.getLogger(TwitterShare.class.getName()).log(Level.SEVERE, null, ex);
                }

                
            }
        }.start();
    }
    
    public static void auth(final Twitter twitter) {
        new Thread() {
            @Override
            public void run() {
                
                try {
                    RequestToken requestToken = twitter.getOAuthRequestToken();

                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URL(requestToken.getAuthorizationURL() + "&oauth_callback=oob").toURI());
                            
                            String str = JOptionPane.showInputDialog(null, "Enter PIN : ",
                                    "", 1);
                            if (str != null) {
                                AccessToken at = twitter.getOAuthAccessToken(requestToken, str);
                                twitter.setOAuthAccessToken(at);
                                
                                JOptionPane.showMessageDialog(null, "Token acquired for user: " + at.getScreenName(),
                                        "", 1);
                            } else {

                            }
                            
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    
                } catch (TwitterException ex) {
                    Logger.getLogger(TwitterShare.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.start();
    }
}
