/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tanabata;

import javax.swing.JTextArea;

/**
 *
 * @author User
 */
class TextLoggingProcess {
    protected JTextArea txtLog;
    
    TextLoggingProcess(JTextArea txtLog) {
        this.txtLog = txtLog;
    }
    
    protected void log (String text) {
        this.txtLog.append(text + "\n");
    }
}
