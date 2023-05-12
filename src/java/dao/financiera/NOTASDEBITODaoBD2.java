/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.financiera;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entidades.financiera.NOTASDEBITO;
import beansVista.financiera.NOTASDEBITOBean;
import java.util.Map;
import sys.util.Datos;
import java.util.ResourceBundle;
import sys.util.NotasDebidoDTODao;
import sys.util.Sesion;
/**
 *
 * @author ebedoya
 */
public class NOTASDEBITODaoBD2 implements NotasDebidoDTODao{
    
    public Sesion sesion;
    
    private final String SQL_UPDATE= "update RAR set RDDTE = ? where RCUST = ? and ARDTYP = '2' and RRID = 'RD'";
    private final String SQL_SELECT= "select RCUST, ARDPFX, RINVC, RAMT, RIDTE, RDDTE from RAR where ARDTYP = '2' and RRID = 'RD'";
    
    public NOTASDEBITODaoBD2(Sesion conn){
        this.sesion = conn;
    }

    @Override
    public int update(NOTASDEBITO notasdebito, int fechaVencEntero) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setInt(1, fechaVencEntero);
            // WHERE del SQL
            stmt.setString(2, notasdebito.getRcust());
            rows = stmt.executeUpdate();
            System.out.println("Registros Actualizados :" + rows);
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return rows;
    }

    @Override
    public List<NOTASDEBITO> select() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<NOTASDEBITO> notasdebitodtos = new ArrayList<NOTASDEBITO>();
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables muy OP
                // ******************************
                NOTASDEBITO notasdebito = new NOTASDEBITO();
                notasdebito.setRcust(rs.getString(1));
                notasdebito.setArdpfx(rs.getString(2));
                notasdebito.setRinvc(rs.getInt(3));
                notasdebito.setRamt(rs.getDouble(4));
                notasdebito.setRidte(rs.getInt(5));
                notasdebito.setRddte(rs.getInt(6));
                notasdebitodtos.add(notasdebito);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return notasdebitodtos;
    }
}
