/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import entidades.distribucion.UnidadesEmpaque;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import sys.util.Sesion;

/**
 *
 * @author ahenao
 */
public class Funciones {
    
    public Sesion sesion;
    
    private transient Connection conexionPOSTGRES;
    private transient Connection conexionBPCS;
    private final String SQL_COUNT_ALARM = "SELECT COUNT(1) FROM distribucion.unidades_empaque WHERE id_grupo_compatible is null and id_estiba_asignada is null";
    private final String SQL_ITEMS_WITHOUT_PARAMETERS = "SELECT distribucion.unidades_empaque.descripcion FROM distribucion.unidades_empaque WHERE id_grupo_compatible is null and id_estiba_asignada is null";

    public Funciones() {
        //sesion = Sesion.getSesion();
    }
    
    public static String leerURL(String ruta) throws Exception {
        URL url = new URL(ruta);
        BufferedReader estilos = new BufferedReader(new InputStreamReader(url.openStream()));
        String lin = "";
        String contenido = "";

        while ((lin = estilos.readLine()) != null) {
            contenido += lin + "\n";
        }
        return contenido;
    }

    public static Date convertirDateBPCS(String fechaBPCS) {
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(fechaBPCS.substring(0, 4)), Integer.parseInt(fechaBPCS.substring(4, 6)) - 1, Integer.parseInt(fechaBPCS.substring(6)));
        return c.getTime();
    }
    
    public static int copiarTabla(PreparedStatement stmBPCS, PreparedStatement psMySQL) throws SQLException{
        ResultSet rsBPCS = stmBPCS.executeQuery();
        while (rsBPCS.next()) {
            for (int i = 1; i <= rsBPCS.getMetaData().getColumnCount(); i++) {
                String campo = rsBPCS.getString(i).trim();
                psMySQL.setString(i, campo);
            }
            psMySQL.addBatch();
        }
        int[] trans = psMySQL.executeBatch();
        int totalTrans = 0;
        for (int x : trans) {
            totalTrans += x;
        }
        return totalTrans;
    }
    
     /***
     * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
     * @param message texto a encriptar     * 
     * @return mensaje encriptado
     */
    public static String getStringMessageDigest(String message){
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest "+ex.getMessage());
        }
        return toHexadecimal(digest);
    } 
    
    /***
     * Convierte un arreglo de bytes a String usando valores hexadecimales
     * @param digest arreglo de bytes a convertir
     * @return String creado a partir de <code>digest</code>
     */
    private static String toHexadecimal(byte[] digest){
        String hash = "";
        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }
    
    // Alarma de parametrización
    
    public int parametrizables () throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = getConexionPOSTGRES();
        stmt = conn.prepareStatement(SQL_COUNT_ALARM);
        rs = stmt.executeQuery();

        rs.next();
        return rs.getInt(1);
    }
    
    public List<UnidadesEmpaque> unitiesWithoutParams() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UnidadesEmpaque> unitiesWiParams = new ArrayList<UnidadesEmpaque>();
        
        try{
            conn = getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_ITEMS_WITHOUT_PARAMETERS);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                UnidadesEmpaque unidadesTemp = new UnidadesEmpaque();
                unidadesTemp.setDescripcion(rs.getString("descripcion"));
                unitiesWiParams.add(unidadesTemp);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return unitiesWiParams;
    }
    
    public static int getHour(){
        LocalDateTime locaDate = LocalDateTime.now();
        int horaActual = locaDate.getHour()+locaDate.getMinute()+locaDate.getSecond();
        return horaActual;
    }
    
    public Connection getConexionPOSTGRES() {
        if (conexionPOSTGRES == null) {
            try {
                ResourceBundle rb = ResourceBundle.getBundle("sys.util.conexiones");
                String user = rb.getString("usuarioPOSTGRES");
                String password = rb.getString("clavePOSTGRES");
                String server = rb.getString("servidorPOSTGRES");
                String db = rb.getString("bdPOSTGRES");
                String urlDatabase = server + db;
                conexionPOSTGRES = DriverManager.getConnection(urlDatabase, user, password);
            } catch (SQLException sqle) {
                Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, sqle);
                sqle.printStackTrace();
            }
        }

        return conexionPOSTGRES;
    }
    
    public Connection getConexionBPCS() {
        return getConexionBPCS_sinpool();
    }

    public Connection getConexionBPCS_pool() {
        try {
            InitialContext context = new InitialContext();
            //DataSource ds = (DataSource) context.lookup("jdbc/MP61BPCSF"); //Websphere
            DataSource ds = (DataSource) context.lookup("java:app/jdbc/COLLX835F"); //Probar en websphere
            conexionBPCS = ds.getConnection();

        } catch (NamingException | SQLException ex) {
            Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return conexionBPCS;
    }

    public Connection getConexionBPCS_sinpool() {
        if (conexionBPCS == null) {
            ResourceBundle rb = ResourceBundle.getBundle("sys.util.conexiones");
            String user = rb.getString("usuarioBPCS");
            String clave = rb.getString("claveBPCS");
            String server = rb.getString("servidor");
            String libreria = rb.getString("libreria");
            Datos datos = new Datos();
            try {
                conexionBPCS = datos.db2Conection(user, clave, server, libreria);
            } catch (SQLException ex) {
                Logger.getLogger(Sesion.class
                        .getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        return conexionBPCS;
    }

    public void setConexionBPCS(Connection conexionBPCS) {
        this.conexionBPCS = conexionBPCS;
    }
}


