/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.distribucion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import java.math.BigDecimal;
import sys.util.Datos;
import java.util.ResourceBundle;
import sys.util.Sesion;
import sys.util.Funciones;
/**
 * Esta clase obtiene la información de la base de datos postgresql del objeto
 * GruposCompatibles del esquema distribución
 *
 * @author Edison Bedoya
 * @version 1.0 2019-09-13
 */
public class GruposCompatiblesDao {
    
     /**
     * @see  sys.util.Sesion Objeto de sesión
     * abierto
     */
    public Sesion sesion;
    
    private final String SQL_INSERT     = "INSERT INTO distribucion.grupos_compatibles (codigo_grupo, descripcion, permite_agrupar_referencias, metodo_calculo, maximo_cajas_agrupar, peso_maximo_lio, ancho, alto, largo, factor_peso_volumen, estado_grupos_compatibles) VALUES(?,?,CAST(? AS enum_1),CAST(? AS enum_2),?,?,?,?,?,?,CAST(? AS enum_3))";
    private final String SQL_UPDATE     = "UPDATE distribucion.grupos_compatibles SET codigo_grupo=?, descripcion=?, permite_agrupar_referencias=CAST(? AS enum_1), metodo_calculo=CAST(? AS enum_2), maximo_cajas_agrupar=?, peso_maximo_lio=?, ancho=?, alto=?, largo=?, factor_peso_volumen=?, estado_grupos_compatibles=CAST(? AS enum_3) WHERE id_grupo_compatible=?";
    private final String SQL_DELETE     = "UPDATE distribucion.grupos_compatibles SET estado_grupos_compatibles='Inactivo' WHERE id_grupo_compatible=?";
    private final String SQL_SELECT     = "SELECT * FROM distribucion.grupos_compatibles";
    private final String SQL_SELECT_ONE = "SELECT * FROM distribucion.grupos_compatibles WHERE id_grupo_compatible = ?";
        
    /**
     * Constructor, Inicializa el objeto sesión 
     *
     */
    public GruposCompatiblesDao(Sesion conn){
        this.sesion = conn;
    }
    
     /**
     * Este método inserta un nuevo grupo compatible en la base de datos.
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @param grupcomp
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     * @throws java.sql.SQLException
     */
    public int insert(GruposCompatibles grupcomp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, grupcomp.getCodigoGrupo());
            stmt.setString(index++, grupcomp.getDescripcion());
            stmt.setString(index++, grupcomp.getPermiteAgruparReferencias());
            stmt.setString(index++, grupcomp.getMetodoCalculo());
            stmt.setInt(index++, grupcomp.getMaximoCajasAgrupar());
            stmt.setDouble(index++, grupcomp.getPesoMaximoLio());
            stmt.setDouble(index++, grupcomp.getAncho());
            stmt.setDouble(index++, grupcomp.getAlto());
            stmt.setDouble(index++, grupcomp.getLargo());
            stmt.setDouble(index++, grupcomp.getFactorPesoVolumen());
            stmt.setString(index++, grupcomp.getEstadoGruposCompatibles());
            rows = stmt.executeUpdate();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
       /**
     * Este método actualiza la información básica del grupo compatible, 
     * como su descripción, cantidad de paquetes, entre otros.
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int update(GruposCompatibles grupcomp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setString(1, grupcomp.getCodigoGrupo());
            stmt.setString(2, grupcomp.getDescripcion());
            stmt.setString(3, grupcomp.getPermiteAgruparReferencias());
            stmt.setString(4, grupcomp.getMetodoCalculo());
            stmt.setInt(5, grupcomp.getMaximoCajasAgrupar());
            stmt.setDouble(6, (grupcomp.getPesoMaximoLio()));
            stmt.setDouble(7, grupcomp.getAncho());
            stmt.setDouble(8, grupcomp.getAlto());
            stmt.setDouble(9, grupcomp.getLargo());
            stmt.setDouble(10, grupcomp.getFactorPesoVolumen());
            stmt.setString(11, grupcomp.getEstadoGruposCompatibles());
            // WHERE del SQL
            stmt.setInt(12, grupcomp.getIdGrupoCompatible());
            rows = stmt.executeUpdate();
            System.out.println("Registros editados: " + rows);
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
       /**
     * Este método elimina un grupo compatible.
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int delete(GruposCompatibles grupcomp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
           conn = sesion.getConexionPOSTGRES();
           stmt = conn.prepareStatement(SQL_DELETE);
           // Clausula WHERE para el eliminar los registros
           stmt.setInt(1, grupcomp.getIdGrupoCompatible());
           rows = stmt.executeUpdate();
           System.out.println("Registros Inactivados: " + rows);
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return rows;
    }
    
     /**
     * Este método obtiene la lista de grupos compatibles de la base de datos postgresql.
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @return Lista de grupos compatibles consultados
     */
    public List<GruposCompatibles> select() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<GruposCompatibles> grupcomps = new ArrayList<GruposCompatibles>();
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables OP
                // **************************
                GruposCompatibles gruposcompatibles = new GruposCompatibles();
                gruposcompatibles.setIdGrupoCompatible(rs.getInt(1));
                gruposcompatibles.setCodigoGrupo(rs.getString(2));
                gruposcompatibles.setDescripcion(rs.getString(3));
                gruposcompatibles.setPermiteAgruparReferencias(rs.getString(4));
                gruposcompatibles.setMetodoCalculo(rs.getString(5));
                gruposcompatibles.setMaximoCajasAgrupar(rs.getInt(6));
                gruposcompatibles.setPesoMaximoLio(rs.getDouble(7));
                gruposcompatibles.setAncho(rs.getDouble(8));
                gruposcompatibles.setAlto(rs.getDouble(9));
                gruposcompatibles.setLargo(rs.getDouble(10));
                gruposcompatibles.setFactorPesoVolumen(rs.getInt(11));
                gruposcompatibles.setEstadoGruposCompatibles(rs.getString(12));
                grupcomps.add(gruposcompatibles);
           }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return grupcomps;
    }
    
    /**
     * Este método obtiene un grupo compatible de la base de datos postgresql.
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param idGrupoCompatible
     * @return Un objeto de tipo grupos compatibles
     * @throws java.sql.SQLException
     */
    public GruposCompatibles selectOne(int idGrupoCompatible) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        conn = sesion.getConexionPOSTGRES();
        stmt = conn.prepareStatement(SQL_SELECT_ONE);
        // Clausula WHERE para el eliminar los registros
        stmt.setInt(1, idGrupoCompatible);
        rs = stmt.executeQuery();
        // Asignacion de variables OP
        // **************************
        if(rs.next()){
            GruposCompatibles gruposcompatibles = new GruposCompatibles();
            gruposcompatibles.setIdGrupoCompatible(rs.getInt(1));
            gruposcompatibles.setCodigoGrupo(rs.getString(2));
            gruposcompatibles.setDescripcion(rs.getString(3));
            gruposcompatibles.setPermiteAgruparReferencias(rs.getString(4));
            gruposcompatibles.setMetodoCalculo(rs.getString(5));
            gruposcompatibles.setMaximoCajasAgrupar(rs.getInt(6));
            gruposcompatibles.setPesoMaximoLio(rs.getDouble(7));
            gruposcompatibles.setAncho(rs.getDouble(8));
            gruposcompatibles.setAlto(rs.getDouble(9));
            gruposcompatibles.setLargo(rs.getDouble(10));
            gruposcompatibles.setFactorPesoVolumen(rs.getInt(11));
            gruposcompatibles.setEstadoGruposCompatibles(rs.getString(12));

            return gruposcompatibles;
        } else {
            return null;
        }
        
    }
}
