/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.financiera;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entidades.financiera.PUC;
import sys.util.Datos;
import java.util.ResourceBundle;
import sys.util.PucDTODao;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
public class PUCDaoDB2 implements PucDTODao{
    
    public Sesion sesion;
    
    private final String SQL_INSERT     = "INSERT INTO COLLXUSRF.tblcta VALUES(?,?,?,?,?)";
    private final String SQL_UPDATE     = "UPDATE COLLXUSRF.tblcta SET CTDESC=?, CTCTYP=?, CTTERC=?, CTINVE=? WHERE CTCTA=?";
    private final String SQL_DELETE     = "DELETE COLLXUSRF.tblcta WHERE CTCTA=?";
    private final String SQL_SELECT     = "SELECT CTCTA, CTDESC, CTCTYP, CTTERC, CTINVE FROM COLLXUSRF.tblcta ORDER BY CTCTA DESC";
    private final String SQL_SELECT_ONE = "SELECT COUNT(*) FROM COLLXUSRF.tblcta WHERE CTCTA =?";
    
    
    public PUCDaoDB2(Sesion conn){
        this.sesion = conn;
    }
    
    @Override
    public int insert(PUC pucdto) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, pucdto.getCtcta());
            stmt.setString(index++, pucdto.getCtdesc());
            stmt.setString(index++, pucdto.getCtctyp());
            stmt.setString(index++, pucdto.getCtterc());
            stmt.setString(index++, pucdto.getCtinve());
            rows = stmt.executeUpdate();
            System.out.println("Registros Afectados: " + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
    @Override
    public int update(PUC pucdto) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setString(1, pucdto.getCtdesc());
            stmt.setString(2, pucdto.getCtctyp());
            stmt.setString(3, pucdto.getCtterc());
            stmt.setString(4, pucdto.getCtinve());
            // WHERE del SQL
            stmt.setString(5, pucdto.getCtcta());
            rows = stmt.executeUpdate();
            System.out.println("Registros Actualizados: " + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
    @Override
    public int delete(PUC pucdto) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_DELETE);
            // Clausula WHERE para eliminar los registros
            stmt.setString(1, pucdto.getCtcta());
            rows = stmt.executeUpdate();
            System.out.println("Registros Eliminados: " + rows);
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
    public int select_one(PUC pucdto) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        conn = sesion.getConexionBPCS();
        stmt = conn.prepareStatement(SQL_SELECT_ONE);
        // Orden de los campos de como estan en el SQL de arriba, el SQL_SELECT_ONE.
        // No se pasa el primer parametro por que esta quemado arriba
        // WHERE del SQL
        stmt.setString(1, pucdto.getCtcta());
        rs = stmt.executeQuery();
        
        rs.next();
        return rs.getInt(1);
    }
    
    @Override
    public List<PUC> select() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<PUC> pucdtos = new ArrayList<PUC>();
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs   = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables OP
                // **************************
                PUC pucdto = new PUC();
                pucdto.setCtcta(rs.getString(1));
                pucdto.setCtdesc(rs.getString(2));
                pucdto.setCtctyp(rs.getString(3));
                pucdto.setCtterc(rs.getString(4));
                pucdto.setCtinve(rs.getString(5));
                pucdtos.add(pucdto);
            }
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return pucdtos;
    }
}
