/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.financiera;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entidades.financiera.RESDIAN;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import sys.util.ResdianDTODao;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
public class RESDIANDaoDB2 implements ResdianDTODao {

    public Sesion sesion;

    private final String SQL_INSERT = "INSERT INTO ORDBELL.RESDIAN VALUES(?,?,?,?,?,?)";
    private final String SQL_UPDATE = "UPDATE ORDBELL.RESDIAN SET N_INICIAL=?, N_FINAL=?, FECH_INI=?, FEC_VENC=? WHERE RESOLUCION=? AND PREFIJO=?";
    private final String SQL_SELECT = "SELECT RESOLUCION, PREFIJO, N_INICIAL, N_FINAL, FECH_INI, FEC_VENC FROM ORDBELL.RESDIAN ORDER BY FEC_VENC DESC";
    private final String SQL_DELETE = "DELETE FROM ORDBELL.RESDIAN WHERE RESOLUCION=? AND PREFIJO=?";
    private final String SQL_ONE_DATES = "SELECT RESOLUCION, PREFIJO, N_INICIAL, N_FINAL, FECH_INI, FEC_VENC FROM ORDBELL.RESDIAN WHERE YEAR(NOW())*10000 +(MONTH(NOW()))*100 +DAY(NOW())\n" +
"BETWEEN FECH_INI and FEC_VENC";
    private final String SQL_ONE_DATES_NROS = "SELECT * FROM ORDBELL.RESDIAN \n" +
"WHERE \n" +
"(\n" +
"        YEAR(NOW())*10000 +(MONTH(NOW()))*100 +DAY(NOW())\n" +
"        BETWEEN FECH_INI and FEC_VENC\n" +
"        and ? \n" +
"        BETWEEN N_INICIAL and N_FINAL\n" +
")\n" +
"\n" +
"or\n" +
"\n" +
"(\n" +
"        YEAR(NOW())*10000 +(MONTH(NOW()))*100 +DAY(NOW())\n" +
"        BETWEEN FECH_INI and FEC_VENC\n" +
"        and ? \n" +
"        BETWEEN N_INICIAL and N_FINAL\n" +
")";

    public RESDIANDaoDB2(Sesion conn) {
        this.sesion = conn;
    }

    @Override
    public int insert(RESDIAN resdian) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, resdian.getResolucion());
            stmt.setString(index++, resdian.getPrefijo());
            stmt.setInt(index++, resdian.getN_inicial());
            stmt.setInt(index++, resdian.getN_final());
            stmt.setInt(index++, resdian.getFech_ini());
            stmt.setInt(index++, resdian.getFech_venc());
            rows = stmt.executeUpdate();
            System.out.println("Registros Afectados: " + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return rows;
    }

    @Override
    public int update(RESDIAN resdian) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
//        System.out.println(resdian.getFech_ini());
        
//        String fechaCadena = Integer.toString(resdian.getFech_ini());
//        System.out.println(fechaCadena);
//        
//        DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
//        Date date = format.parse(fechaCadena);
//        System.out.println(date);
        
        
        try {
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setInt(1, resdian.getN_inicial());
            stmt.setInt(2, resdian.getN_final());
            stmt.setInt(3, resdian.getFech_ini());
            stmt.setInt(4, resdian.getFech_venc());
            // WHERE del SQL
            stmt.setString(5, resdian.getResolucion());
            stmt.setString(6, resdian.getPrefijo());
            rows = stmt.executeUpdate();
            System.out.println("Registros Actualizados :" + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return rows;
    }

    @Override
    public int delete(RESDIAN resdian) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_DELETE);
            // Clausula WHERE para eliminar los registros
            stmt.setString(1, resdian.getResolucion());
            stmt.setString(2, resdian.getPrefijo());
            rows = stmt.executeUpdate();
            System.out.println("Registros Eliminados" + rows);
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return rows;
    }

    @Override
    public List<RESDIAN> select() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RESDIAN> resdians = new ArrayList<RESDIAN>();
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Asignacion de variables OP
                // **************************
                RESDIAN resdiandto = new RESDIAN();
                resdiandto.setResolucion(rs.getString(1));
                resdiandto.setPrefijo(rs.getString(2));
                resdiandto.setN_inicial(rs.getInt(3));
                resdiandto.setN_final(rs.getInt(4));
                resdiandto.setFech_ini(rs.getInt(5));
                resdiandto.setFech_venc(rs.getInt(6));
                resdians.add(resdiandto);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return resdians;
    }

    @Override
    public List<RESDIAN> select_range_dates(int fecha1, int fecha2) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RESDIAN> resdiansRange = new ArrayList<RESDIAN>();
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_ONE_DATES);
            // Condicional del WHERE
            // Toma la fecha solo el AS400
            // consumo del RS por medio de uin ciclo
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Asignacion de variables OP
                // **************************
                RESDIAN resdiandto = new RESDIAN();
                resdiandto.setResolucion(rs.getString(1));
                resdiandto.setPrefijo(rs.getString(2));
                resdiandto.setN_inicial(rs.getInt(3));
                resdiandto.setN_final(rs.getInt(4));
                resdiandto.setFech_ini(rs.getInt(5));
                resdiandto.setFech_venc(rs.getInt(6));
                resdiansRange.add(resdiandto);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return resdiansRange;
    }

    @Override
    public List<RESDIAN> select_range_dates_nros(int nroini, int nrofnl) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<RESDIAN> resdiansDatesNros = new ArrayList<RESDIAN>();
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_ONE_DATES_NROS);
            // Condicional del WHERE
            stmt.setInt(1, nroini);
            stmt.setInt(2, nrofnl);
            // consumo del RS por medio de uin ciclo
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Asignacion de variables OP
                // **************************
                RESDIAN resdiandto = new RESDIAN();
                resdiandto.setResolucion(rs.getString(1));
                resdiandto.setPrefijo(rs.getString(2));
                resdiandto.setN_inicial(rs.getInt(3));
                resdiandto.setN_final(rs.getInt(4));
                resdiandto.setFech_ini(rs.getInt(5));
                resdiandto.setFech_venc(rs.getInt(6));
                resdiansDatesNros.add(resdiandto);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return resdiansDatesNros;
    }
    
    
}
