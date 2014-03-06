/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import java.awt.Desktop;
import java.net.URL;
import java.net.URLEncoder;
import javax.swing.JTextArea;

/**
 *
 * @author User
 */
class FacebookShare {
    public static void share(final String text, final JTextArea txtlog) {
        new Thread() {
            @Override
            public void run() {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        URL url = new URL("https://www.facebook.com/sharer/sharer.php?u=" + URLEncoder.encode(text, "UTF-8"));
                        desktop.browse(url.toURI());
                        txtlog.append("Opening Facebook Sharer in browser..." + "\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
