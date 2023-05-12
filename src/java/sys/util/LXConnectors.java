/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import com.infor.lx.xmg.bean.LxIntegratorConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ggaviria
 */
public class LXConnectors {

    public static String host = "213.62.216.84";
    //#puerto para pruebas 9904
    //public static int port = 9904;
    public static int port = 9902;
    public static String home = "C:\\LxConnector_ago2018";
    //public static String home = "C:\\LxConnector_ago2018Test";
    //public static String home = "/LXCONNECTOR_ASCPEAME-COTLX835EC";
    //public static String home = "/LXCONNECTOR_ASCPEAME-COLLX835EC";        
    public static String instance = "COLLX835EC";

    public static LxIntegratorConnection getConnection() {
        try {
            LxIntegratorConnection connection = new LxIntegratorConnection(home);
            connection.logon(port, host, instance);
            return connection;
        } catch (Exception e) {
            Logger.getLogger(LxIntegratorConnection.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }
}
