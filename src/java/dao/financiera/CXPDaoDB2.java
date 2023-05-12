/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.financiera;

import beansVista.financiera.CXPBean;
import java.sql.*;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import entidades.financiera.CXP;
import java.text.DecimalFormat;
import sys.util.Datos;
import java.util.ResourceBundle;
import sys.util.CxpDTODao;
import sys.util.Sesion;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
public class CXPDaoDB2 implements CxpDTODao{
    
    public Sesion sesion;
    
    private final String SQL_INSERT     = "INSERT INTO pp61bamusf.ncm VALUES(?,?,?,?,?,?,?,?,?,?,?)";  
    private final String SQL_UPDATE     = "UPDATE pp61bamusf.ncm SET CMEXC =?, CMEXP =?, CMEXF =?, CMWSID =?, CMWUSR =?, CMWDAT =?, CMWTIM =? WHERE CMID =? AND CMFRC =? AND CMTOC =? AND CMDAT =?";
    private final String SQL_DELETE     = "UPDATE pp61bamusf.ncm SET CMID='CZ' WHERE CMFRC=? AND CMTOC=? AND CMDAT=? AND CMEXC=? AND CMEXP=? AND CMEXF=? AND CMWSID=? AND CMWUSR=? AND CMWDAT=? AND CMWTIM=?";
    private final String SQL_SELECT     = "SELECT CMID, CMFRC, CMTOC, CMDAT, CMEXC, CMEXP, CMEXF, CMWSID, CMWUSR, CMWDAT, CMWTIM FROM pp61bamusf.ncm ORDER BY CMWDAT DESC";
    private final String SQL_SELECT_ONE = "SELECT COUNT(*) FROM pp61bamusf.ncm WHERE CMID=? AND CMFRC=? AND CMTOC=? AND CMDAT=?";
    private final String SQL_SELECT_ONE_UPDATE = "SELECT * FROM pp61bamusf.ncm WHERE CMID=? AND CMFRC=? AND CMTOC=? AND CMDAT=?";
    
    public CXPDaoDB2(Sesion conn){
        this.sesion = conn;
    }
    
    @Override
    public int insert(CXP cxp) throws SQLException{
       Connection conn = null;
       PreparedStatement stmt = null;
       int rows = 0;
       
       try{
           conn = sesion.getConexionBPCS();
           stmt = conn.prepareStatement(SQL_INSERT);
           int index = 1;
           stmt.setString(index++, cxp.getCmid());
           stmt.setString(index++, cxp.getCmfrc());
           stmt.setString(index++, cxp.getCmtoc());
           stmt.setInt(index++, cxp.getCmwdat());
           stmt.setDouble(index++, cxp.getCmexc());
           stmt.setDouble(index++, cxp.getCmexp());
           stmt.setDouble(index++, cxp.getCmexf());
           stmt.setString(index++, cxp.getCmwsid());
           stmt.setString(index++, cxp.getCmwusr());
           stmt.setInt(index++, cxp.getCmdat());
           stmt.setInt(index++, cxp.getCmwtim());
           rows = stmt.executeUpdate();
           System.out.println("Registros afectados: " + rows);
           
       } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
       return rows;
    }
    
    @Override
    public int update(CXP cxp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setDouble(1, cxp.getCmexc());
            stmt.setDouble(2, cxp.getCmexp());
            stmt.setDouble(3, cxp.getCmexf());
            stmt.setString(4, "BELLOTAHUB");
            stmt.setString(5, Sesion.getSesion().getFormUser());
            stmt.setInt(6, CXPBean.getFechaActual());
            stmt.setInt(7, CXPBean.getHoraActual());
            // WHERE del SQL
            stmt.setString(8, cxp.getCmid());
            stmt.setString(9, cxp.getCmfrc());
            stmt.setString(10, cxp.getCmtoc());
            stmt.setInt(11, cxp.getCmdat());
            rows = stmt.executeUpdate();
            System.out.println("Registros actualizados" + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return rows;
    }
    
    @Override
    public int delete(CXP cxp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_DELETE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_DELETE.
            // SET del SQL
            // No se pasa el primer parametro por que esta quemado arriba
            // WHERE del SQL
            stmt.setString(1, cxp.getCmfrc());
            stmt.setString(2, cxp.getCmtoc());
            stmt.setInt(3, cxp.getCmdat());
            stmt.setDouble(4, cxp.getCmexc());
            stmt.setDouble(5, cxp.getCmexp());
            stmt.setDouble(6, cxp.getCmexf());
            stmt.setString(7, cxp.getCmwsid());
            stmt.setString(8, cxp.getCmwusr());
            stmt.setInt(9, cxp.getCmwdat());
            stmt.setInt(10, cxp.getCmwtim());
            rows = stmt.executeUpdate();
            System.out.println("Registros Eliminados" + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return rows;
    }
    
    public int select_one(CXP cxp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = sesion.getConexionBPCS();
        stmt = conn.prepareStatement(SQL_SELECT_ONE);
        // Orden de los campos de como estan en el SQL de arriba, el SQL_SELECT_ONE.
        // No se pasa el primer parametro por que esta quemado arriba
        // WHERE del SQL
        stmt.setString(1, cxp.getCmid());
        stmt.setString(2, cxp.getCmfrc());
        stmt.setString(3, cxp.getCmtoc());
        stmt.setInt(4, cxp.getCmwdat());
        rs = stmt.executeQuery();

        rs.next();
        return rs.getInt(1);
    } 
    
    @Override
    public List<CXP> select() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CXP> cxpdtos = new ArrayList<CXP>();
//        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);
//        DecimalFormat df = (DecimalFormat)nf;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables muy OP
                // ******************************
                CXP cxpdto = new CXP();
                cxpdto.setCmid(rs.getString(1));
                cxpdto.setCmfrc(rs.getString(2));
                cxpdto.setCmtoc(rs.getString(3));
                cxpdto.setCmdat(rs.getInt(4));
                cxpdto.setCmexc(rs.getDouble(5));
                cxpdto.setCmexp(rs.getDouble(6));
                cxpdto.setCmexf(rs.getDouble(7));
                cxpdto.setCmwsid(rs.getString(8));
                cxpdto.setCmwusr(rs.getString(9));
                cxpdto.setCmwdat(rs.getInt(10));
                cxpdto.setCmwtim(rs.getInt(11));
//                cxpdto.setCmid(rs.getString(1));
//                cxpdto.setCmfrc(rs.getString(2));
//                cxpdto.setCmtoc(rs.getString(3));
//                cxpdto.setCmdat(rs.getInt(4));
//                cxpdto.setCmexc(nf.parse(rs.getDouble(5)));
//                cxpdto.setCmexp(rs.getDouble(6));
//                cxpdto.setCmexf(rs.getDouble(7));
//                cxpdto.setCmwsid(rs.getString(8));
//                cxpdto.setCmwusr(rs.getString(9));
//                cxpdto.setCmwdat(rs.getInt(10));
//                cxpdto.setCmwtim(rs.getInt(11));
                cxpdtos.add(cxpdto);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
         
        return cxpdtos;
    } 

    @Override
    public void select_one_to_update(CXP cxp) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = sesion.getConexionBPCS();
        stmt = conn.prepareStatement(SQL_SELECT_ONE_UPDATE);
        // Orden de los campos de como estan en el SQL de arriba, el SQL_SELECT_ONE.
        // No se pasa el primer parametro por que esta quemado arriba
        // WHERE del SQL
        stmt.setString(1, cxp.getCmid());
        // Los #s van invertidos por que se consulta la tasa inversa para actualizar
        stmt.setString(3, cxp.getCmfrc());
        stmt.setString(2, cxp.getCmtoc());
        stmt.setInt(4, cxp.getCmdat());
        rs = stmt.executeQuery();
        while(rs.next()){
            // Llenado de obj de tasas de cambio
            // *********************************
            CXP cxpdtonew = new CXP();
            cxpdtonew.setCmid(rs.getString(1));
            cxpdtonew.setCmfrc(rs.getString(2));
            cxpdtonew.setCmtoc(rs.getString(3));
            cxpdtonew.setCmdat(rs.getInt(4));
            cxpdtonew.setCmexc(rs.getDouble(5));
            cxpdtonew.setCmexp(rs.getDouble(6));
            cxpdtonew.setCmexf(rs.getDouble(7));
            cxpdtonew.setCmwsid(rs.getString(8));
            cxpdtonew.setCmwusr(rs.getString(9));
            cxpdtonew.setCmwdat(rs.getInt(10));
            cxpdtonew.setCmwtim(rs.getInt(11));
            // Invocación del metodo que actualiza
            // ***********************************
            update_one(cxpdtonew, cxp.getCmexc(), cxp.getCmexp(), cxp.getCmexf()); 
        }
    }

    @Override
    public int update_one(CXP cxp, double tasa1, double tasa2, double tasa3) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setDouble(1, (1/tasa1));
            stmt.setDouble(2, (1/tasa2));
            stmt.setDouble(3, (1/tasa3));
            stmt.setString(4, "BELLOTAHUB");
            stmt.setString(5, Sesion.getSesion().getFormUser());
            stmt.setInt(6, CXPBean.getFechaActual());
            stmt.setInt(7, CXPBean.getHoraActual());
            // WHERE del SQL
            stmt.setString(8, cxp.getCmid());
            stmt.setString(9, cxp.getCmfrc());
            stmt.setString(10, cxp.getCmtoc());
            stmt.setInt(11, cxp.getCmdat());
            rows = stmt.executeUpdate();
            System.out.println("Registros actualizados" + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return rows;
    }
    
}
