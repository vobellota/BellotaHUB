/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.*;

/**
 *
 * @author ahenao
 */
public class Datos {

    /*Atributos*/

    private Connection con;

    /*Constructor, carga puente JDBC-ODBC*/
    public Datos() {

    }

    /**
     * Obtiene una conexión con el nombre del driver especificado
     *
     * @param usr
     * @param pswd
     * @param server
     * @param libreria
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public Connection db2Conection(String usr, String pswd, String server, String libreria) throws SQLException, ClassNotFoundException {
        String url = "jdbc:as400://";
        String driver = "com.ibm.as400.access.AS400JDBCDriver";
        Class.forName(driver);
        con = DriverManager.getConnection(url + server + "/" + libreria + ";prompt=false;", usr, pswd);
        System.out.println(new java.util.Date() + " **conectado AS400 " + url + server + "/" + libreria + ";prompt=false;");
        return con;
    }

    /* Cerrar la conexión.*/
    public boolean closeConecction() {
        try {
            con.close();
        } catch (SQLException sqle) {
            System.out.println("No se cerro la conexión");
            return false;
        }

        System.out.println("Conexión cerrada con éxito ");
        return true;
    }

    public Connection mysqlDbConnection(String mysqllogin, String mysqlpassword, String mysqlurlUs, String mysqlbdUs) {
        try {
            //obtenemos el driver de para mysql
            Class.forName("com.mysql.jdbc.Driver");
            //obtenemos la conexión
            Connection mysqlconnUs;
            mysqlconnUs = DriverManager.getConnection(mysqlurlUs + mysqlbdUs, mysqllogin, mysqlpassword);
            if (mysqlconnUs != null) {
                System.out.println("Conección a base de datos " + mysqlbdUs + " OK");
            }
            return mysqlconnUs;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
            return null;
        }
    }
}
