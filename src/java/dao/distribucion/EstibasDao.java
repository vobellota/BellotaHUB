/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.distribucion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entidades.distribucion.Estibas;
import java.math.BigDecimal;
import sys.util.Datos;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import sys.util.Sesion;
/**
 *
 * @author ebedoya
 */
public class EstibasDao {
    
    public Sesion sesion;
    
    private final String SQL_INSERT     = "INSERT INTO distribucion.estibas (codigo_estiba, descripcion_estiba, permite_agrupar_referencias, peso_maximo_soportado, ancho, largo, altura_maxima_estiba, factor_estiba, estado_estiba) VALUES(?,?,CAST(? AS enum_5),?,?,?,?,?,CAST(? AS enum_6))";
    private final String SQL_UPDATE     = "UPDATE distribucion.estibas SET codigo_estiba=?, descripcion_estiba=?, permite_agrupar_referencias=CAST(? AS enum_5), peso_maximo_soportado=?, ancho=?, largo=?, altura_maxima_estiba=?, factor_estiba=?, estado_estiba=CAST(? AS enum_6) WHERE id_estiba=?";
    private final String SQL_DELETE     = "UPDATE distribucion.estibas SET estado_estiba='Inactivo' WHERE id_estiba=?";
    private final String SQL_SELECT     = "SELECT * FROM distribucion.estibas";
    private final String SQL_SELECT_ONE = "SELECT * FROM distribucion.estibas WHERE id_estiba=?";
    private final String SQL_COUNT_ALARM = "select * from unidades_empaque where id_grupo_compatible is null and id_estiba_asignada is null";
    private transient Connection conexionPOSTGRES;
    
    public EstibasDao(Sesion conn){
        this.sesion = conn; 
    }
    
    public int insert(Estibas estibas) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, estibas.getCodigoEstiba());
            stmt.setString(index++, estibas.getDescripcionEstiba());
            stmt.setString(index++, estibas.getPermiteAgruparReferencias());
            stmt.setDouble(index++, estibas.getPesoMaximoSoportado());
            stmt.setDouble(index++, estibas.getAncho());
            stmt.setDouble(index++, estibas.getLargo());
            stmt.setDouble(index++, estibas.getAlturaMaximaEstiba());
            stmt.setDouble(index++, estibas.getFactorEstiba());
            stmt.setString(index++, estibas.getEstadoEstiba());
            rows = stmt.executeUpdate();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
    public int update(Estibas estibas) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setString(1, estibas.getCodigoEstiba());
            stmt.setString(2, estibas.getDescripcionEstiba());
            stmt.setString(3, estibas.getPermiteAgruparReferencias());
            stmt.setDouble(4, estibas.getPesoMaximoSoportado());
            stmt.setDouble(5, estibas.getAncho());
            stmt.setDouble(6, estibas.getLargo());
            stmt.setDouble(7, estibas.getAlturaMaximaEstiba());
            stmt.setDouble(8, estibas.getFactorEstiba());
            stmt.setString(9, estibas.getEstadoEstiba());
            // WHERE del SQL
            stmt.setInt(10, estibas.getIdEstiba());
            rows = stmt.executeUpdate();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
    public int delete(Estibas estibas) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_DELETE);
            // Clausula WHERE para el eliminar los registros
            stmt.setInt(1, estibas.getIdEstiba());
            rows = stmt.executeUpdate();
            System.out.println("Registros Inactivados: " + rows);
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
    public List<Estibas> select() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Estibas> estibasList = new ArrayList<Estibas>();
        
        try{
            conn = getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs   = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables OP
                // **************************
                Estibas estibas = new Estibas();
                estibas.setIdEstiba(rs.getInt("id_estiba"));
                estibas.setCodigoEstiba(rs.getString("codigo_estiba"));
                estibas.setDescripcionEstiba(rs.getString("descripcion_estiba"));
                estibas.setPermiteAgruparReferencias(rs.getString("permite_agrupar_referencias"));
                estibas.setPesoMaximoSoportado(rs.getDouble("peso_maximo_soportado"));
                estibas.setAncho(rs.getDouble("ancho"));
                estibas.setLargo(rs.getDouble("largo"));
                estibas.setAlturaMaximaEstiba(rs.getDouble("altura_maxima_estiba"));
                estibas.setFactorEstiba(rs.getDouble("factor_estiba"));
                estibas.setEstadoEstiba(rs.getString("estado_estiba"));
                estibasList.add(estibas);
            }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return estibasList;
    }
    
    public Estibas selectOne(int idEstiba) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        conn = sesion.getConexionPOSTGRES();
        stmt = conn.prepareStatement(SQL_SELECT_ONE);
        // Clausula WHERE para el eliminar los registros
        stmt.setInt(1, idEstiba);
        rs = stmt.executeQuery();
        // Asignacion de variables OP
        // **************************
        if(rs.next()){
            Estibas estibas = new Estibas();
            estibas.setIdEstiba(rs.getInt("id_estiba"));
            estibas.setCodigoEstiba(rs.getString("codigo_estiba"));
            estibas.setDescripcionEstiba(rs.getString("descripcion_estiba"));
            estibas.setPermiteAgruparReferencias(rs.getString("permite_agrupar_referencias"));
            estibas.setPesoMaximoSoportado(rs.getDouble("peso_maximo_soportado"));
            estibas.setAncho(rs.getDouble("ancho"));
            estibas.setLargo(rs.getDouble("largo"));
            estibas.setAlturaMaximaEstiba(rs.getDouble("altura_maxima_estiba"));
            estibas.setFactorEstiba(rs.getDouble("factor_estiba"));
            estibas.setEstadoEstiba(rs.getString("estado_estiba"));
            
            return estibas; 
        } else {
            return null;
        }
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
}
