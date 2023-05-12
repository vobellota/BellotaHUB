/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.distribucion;

import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.ProductosCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import entidades.distribucion.Estibas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import sys.util.Datos;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import sys.util.Sesion;
import sys.util.Funciones;

/**
 * Esta clase obtiene la información de la base de datos postgresql del objeto
 * ProductosCompatibles del esquema distribución
 *
 * @author Edison Bedoya
 * @version 1.0 2019-09-13
 */
public class ProductosCompatiblesDao {
    
      /**
     * @see sys.util.Sesion Objeto de sesión
     * abierto
     */
    public Sesion sesion;
    
    private final String SQL_INSERT = "INSERT INTO distribucion.productos_compatibles (codigo_producto, descripcion, largo, alto, ancho, peso_caja_master, estado_producto, id_grupo_compatible, id_estiba_asignada) VALUES(?,?,?,?,?,?,CAST(? AS enum_4),?,?)";
    private final String SQL_UPDATE = "UPDATE distribucion.productos_compatibles\n" +
"SET codigo_producto=?, \n" +
"descripcion=?, largo=?, alto=?, \n" +
"ancho=?, peso_caja_master=?,\n" +
"estado_producto = CAST(? AS enum_4), id_grupo_compatible=?, id_estiba_asignada=?\n" +
"WHERE id_productos_compatibles=?";
    private final String SQL_SELECT_ALL = "SELECT * FROM distribucion.productos_compatibles";
    private final String SQL_SELECT_ALL_UNITIES = "SELECT * FROM distribucion.unidades_empaque WHERE codigo_producto = ?";
    private final String SQL_COUNT_ALARM = "select * from unidades_empaque where id_grupo_compatible is null and id_estiba_asignada is null";
    private transient Connection conexionPOSTGRES;
    
     /**
     * Constructor, Inicializa el objeto sesión 
     *
     */
    public ProductosCompatiblesDao(){
        //this.sesion = conn;
    }
    
     public ProductosCompatiblesDao(Sesion conn){
        this.sesion = conn;
    }
    /**
     * Este método inserta un nuevo producto en la base de datos postgresql a partir
     * de una data extraída de DB2-AS400.
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int insert(ProductosCompatibles prodtcomp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT);
            int index = 1;
            stmt.setString(index++, prodtcomp.getCodigoProducto());
            stmt.setString(index++, prodtcomp.getDescripcion());
            stmt.setDouble(index++, prodtcomp.getLargo());
            stmt.setDouble(index++, prodtcomp.getAlto());
            stmt.setDouble(index++, prodtcomp.getAncho());
            stmt.setDouble(index++, prodtcomp.getPesoCajaMaster());
            stmt.setString(index++, prodtcomp.getEstadoProducto());
            rows = stmt.executeUpdate();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        
        return rows;
    }
    
    /**
     * Este método actualiza la información básica del producto, como su nombre,
     * medidas, entre otros.
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int update(ProductosCompatibles prodtcomp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setString(1, prodtcomp.getCodigoProducto());
            stmt.setString(2, prodtcomp.getDescripcion());
            stmt.setDouble(3, prodtcomp.getLargo());
            stmt.setDouble(4, prodtcomp.getAlto());
            stmt.setDouble(5, prodtcomp.getAncho());
            stmt.setDouble(6, prodtcomp.getPesoCajaMaster());
            stmt.setString(7, prodtcomp.getEstadoProducto());
            // WHERE del SQL
            stmt.setInt(8,prodtcomp.getIdProductosCompatibles());
            rows = stmt.executeUpdate();
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return rows;
    }
//    
//    public int delete(ProductosCompatibles prodtcomp) throws SQLException{
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        int rows = 0;
//        
//        try{
//            conn = sesion.getConexionPOSTGRES();
//            stmt = conn.prepareStatement(SQL_DELETE);
//            // Clausula WHERE para el eliminar los registros
//            stmt.setInt(8,prodtcomp.getIdProductosCompatibles());
//            rows = stmt.executeUpdate();
//        } catch(SQLException sqle){
//            sqle.printStackTrace();
//        }
//        
//        return rows;
//    }
//    
    
     /**
     * Este método obtiene la lista de productos almacenados en la base de datos postgresql.
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @return Lista de productos consultadas
     */
    public List<ProductosCompatibles> select() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ProductosCompatibles> prodcomps = new ArrayList<ProductosCompatibles>();
        
        try{
            conn = getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT_ALL);
            rs = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables
                // **************************
                ProductosCompatibles productoscompatibles = new ProductosCompatibles();
                
                // Objeto normal de Productos compatibles
                // **************************************
                productoscompatibles.setIdProductosCompatibles(rs.getInt("id_productos_compatibles"));
                productoscompatibles.setCodigoProducto(rs.getString("codigo_producto"));
                productoscompatibles.setDescripcion(rs.getString("descripcion"));
                productoscompatibles.setLargo(rs.getDouble("largo"));
                productoscompatibles.setAlto(rs.getDouble("alto"));
                productoscompatibles.setAncho(rs.getDouble("ancho"));
                productoscompatibles.setPesoCajaMaster(rs.getDouble("peso_caja_master"));
                productoscompatibles.setEstadoProducto(rs.getString("estado_producto"));
                
                // Agregacion de todo el objeto manipulado "productoscompatibles" a lo que se retorna "prodcomps"
                prodcomps.add(productoscompatibles);
            }
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return prodcomps;
    }
    
     /**
     * Este método obtiene la lista de unidades de empaque de un producto en específico.
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @return Lista de unidades de empaque consultadas
     */
    public List<UnidadesEmpaque> select_one(ProductosCompatibles prodtcomp) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UnidadesEmpaque> unities_emapq = new ArrayList<UnidadesEmpaque>();
        try{
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT_ALL_UNITIES);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_SELECT_ONE.
            // No se pasa el primer parametro por que esta quemado arriba
            // WHERE del SQL
            stmt.setString(1, prodtcomp.getCodigoProducto());
            rs = stmt.executeQuery();
            while(rs.next()){
                // Asignacion de variables
                // ***********************
                UnidadesEmpaque unidadesempaque = new UnidadesEmpaque();
                ProductosCompatibles idProducto = new ProductosCompatibles();
                
                // Objeto para la foranea id_grupos_compatibles
                // ********************************************
                idProducto.setCodigoProducto(rs.getString("codigo_producto"));
                
                // Asignacion de "idProducto" que es el obj de unidades de empaque
                unidadesempaque.setCodigoProducto(idProducto);
                
                unidadesempaque.setCantidad(rs.getInt("cantidad"));
                unidadesempaque.setDescripcion(rs.getString("descripcion"));
                unidadesempaque.setCantidadDescrita(rs.getString("cantidad_descrita"));
                
                // Agregacion de todo el objeto manipulado "unidadesempaque" a lo que se retorna "unities_emapq"
                unities_emapq.add(unidadesempaque);
            }
            
        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        return unities_emapq;
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
