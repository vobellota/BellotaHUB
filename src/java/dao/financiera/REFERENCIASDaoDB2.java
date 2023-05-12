/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.financiera;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entidades.financiera.REFERENCIAS;
import sys.util.ReferenciasDTODao;
import sys.util.Sesion;
/**
 *
 * @author ebedoya
 */
public class REFERENCIASDaoDB2 implements ReferenciasDTODao{
    
    public Sesion sesion;
    private String anioConsult;
    
    public String getAnioConsult() {
        return anioConsult;
    }

    public void setAnioConsult(String anioConsult) {
        this.anioConsult = anioConsult;
    }
    
    private final String SQL_FILTER= "select LHLDGR as ledger,\n" +
"LHBOOK as libro,\n" +
"LHYEAR as ano,\n" +
"LHPERD as periodo,\n" +
"lhjnen as comprobante,\n" +
"lhjnln as linea,\n" +
"crsg01 as compania,\n" +
"crsg02 as cuenta,\n" +
"crsg03 as cto_costo,\n" +
"crsg04 as area,\n" +
"crsg05 as tercero,\n" +
"LHDREF as documento,\n" +
"LHJRF1 as ref1,\n" +
"LHJRF2 as ref2,\n" +
"LHDDAT as fecha FROM GLH\n" +
"inner join gcr on crian=lhian\n" +
"where LHBOOK=? and LHPERD=? and lhyear=? and lhjnen=? and lhdate >=?";
    
//    private final String SQL_FILTER_UPDATE="UPDATE GLH SET LHDREF=?, LHJRF1=?, LHJRF2=?, LHDDAT=? WHERE LHLDGR=? and LHBOOK=? and LHYEAR=? and LHPERD=? and LHJNEN=? and LHJNLN=? and lhjnln='1'";
    private final String SQL_FILTER_UPDATE="UPDATE GLH SET LHDREF=?, LHJRF1=?, LHJRF2=?, LHDDAT=? WHERE LHLDGR=? and LHBOOK=? and LHYEAR=? and LHPERD=? and LHJNEN=? and LHJNLN=?";
    
    public REFERENCIASDaoDB2(Sesion conn){
        this.sesion = conn;
    }
    
    @Override
    public List<REFERENCIAS> filter(String libro, String periodo, int anio, String nroComprobante) throws SQLException {
        // Convertidor de la cadena de consulta
        String concactCadena;
        concactCadena = String.valueOf(anio) + "0101";
        setAnioConsult(concactCadena);
        // Procedimiento normal
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<REFERENCIAS> referenciasdtos = new ArrayList<REFERENCIAS>();
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_FILTER);
            // Orden de los campos de como estan en el SQL de arriba
            // WHERE del SQL
            stmt.setString(1, libro);
            stmt.setString(2, periodo);
            stmt.setInt(3, anio);
            stmt.setString(4, nroComprobante);
            stmt.setString(5, anioConsult);
            rs = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables muy OP
                // ******************************
                REFERENCIAS refer = new REFERENCIAS();
                refer.setLedger(rs.getString(1));
                refer.setLibro(rs.getString(2));
                refer.setAno(rs.getInt(3));
                refer.setPeriodo(rs.getInt(4));
                refer.setComprobante(rs.getString(5));
                refer.setLinea(rs.getInt(6));
                refer.setCompania(rs.getInt(7));
                refer.setCuenta(rs.getInt(8));
                refer.setCto_costo(rs.getString(9));
                refer.setArea(rs.getString(10));
                refer.setTercero(rs.getInt(11));
                refer.setDocumento(rs.getString(12));
                refer.setRef1(rs.getString(13));
                refer.setRef2(rs.getString(14));
                refer.setFecha(rs.getInt(15));
                refer.setLlaveUnica(rs.getString(1)+rs.getString(2)+rs.getInt(3)+rs.getInt(4)+rs.getString(5)+rs.getInt(6));
                referenciasdtos.add(refer);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return referenciasdtos;
    }

    @Override
    public int update(REFERENCIAS referencias, int fecha, String documentoUpdate, String ref1Update, String ref2Update) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_FILTER_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setString(1, documentoUpdate);
            stmt.setString(2, ref1Update);
            stmt.setString(3, ref2Update);
            stmt.setInt(4, fecha);
            // WHERE del SQL
            stmt.setString(5, referencias.getLedger());
            stmt.setString(6, referencias.getLibro());
            stmt.setInt(7, referencias.getAno());
            stmt.setInt(8, referencias.getPeriodo());
            stmt.setString(9, referencias.getComprobante());
            stmt.setInt(10, referencias.getLinea());
            rows = stmt.executeUpdate();
            System.out.println("Registros Actualizados: " + rows);
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
}
