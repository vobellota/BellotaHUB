/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.distribucion;

import entidades.distribucion.Estibas;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import entidades.distribucion.ProductosCompatibles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import sys.util.Datos;
import java.util.ResourceBundle;
import sys.util.Sesion;

/**
 * Esta clase obtiene la información de la base de datos postgresql del objeto
 * UnidadEmpaque del esquema distribución
 *
 * @author Edison Bedoya
 * @version 1.0 2019-09-13
 */

public class UnidadesEmpaqueDao {

    /**
     * @see  sys.util.Sesion Objeto de sesión
     * abierto
     */
    public Sesion sesion;

    private final String SQL_SELECT_LEFT_JOIN = "select estibas.id_estiba, \n"
            + "estibas.codigo_estiba, \n"
            + "estibas.descripcion_estiba, \n"
            + "grupos_compatibles.id_grupo_compatible, \n"
            + "grupos_compatibles.codigo_grupo, \n"
            + "grupos_compatibles.descripcion AS descripcion_grupos_compatibles, \n"
            + "unidades_empaque.*\n"
            + "from distribucion.unidades_empaque\n"
            + "left join distribucion.grupos_compatibles\n"
            + "on distribucion.grupos_compatibles.id_grupo_compatible = distribucion.unidades_empaque.id_grupo_compatible\n"
            + "left join distribucion.estibas\n"
            + "on distribucion.estibas.id_estiba = distribucion.unidades_empaque.id_estiba_asignada where distribucion.unidades_empaque.estado_unidad='LI'";

    private final String SQL_UPDATE = "UPDATE distribucion.unidades_empaque\n"
            + "SET id_grupo_compatible=?, id_estiba_asignada=?\n"
            + "WHERE id_unidad_empaque=?";

    private final String SQL_COUNT_ALARM = "select * from unidades_empaque where id_grupo_compatible is null and id_estiba_asignada is null";

    /**
     * Constructor
     * @param conn, objeto session
     *
     */
    public UnidadesEmpaqueDao(Sesion conn) {
        this.sesion = conn;
    }

    /**
     * Este método obtiene la información de las unidades de empaque ya sea que
     * estén relacionadas o no con los grupos compatibles.
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @return Lista de unidades de empaque consultadas
     */
    public List<UnidadesEmpaque> select() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UnidadesEmpaque> unidadeslist = new ArrayList<UnidadesEmpaque>();

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT_LEFT_JOIN);
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Asignacion de variables
                // **************************
                UnidadesEmpaque unidadesempaque = new UnidadesEmpaque();
                GruposCompatibles idGrupo = new GruposCompatibles();
                Estibas idEstiba = new Estibas();
                ProductosCompatibles idProducto = new ProductosCompatibles();

                // Objeto normal de Unidades de Empaque
                // ************************************
                unidadesempaque.setIdUnidadEmpaque(rs.getInt("id_unidad_empaque"));
                unidadesempaque.setCantidad(rs.getInt("cantidad"));
                unidadesempaque.setDescripcion(rs.getString("descripcion"));
                unidadesempaque.setCantidadDescrita(rs.getString("cantidad_descrita"));

//                unidadesempaque.getCodigoProducto().setCodigoProducto(rs.getString("codigo_producto"));
                // Objeto para la foranea id_grupos_compatibles
                // ********************************************
                idGrupo.setIdGrupoCompatible(rs.getInt("id_grupo_compatible"));
                idGrupo.setCodigoGrupo(rs.getString("codigo_producto"));
                idGrupo.setDescripcion(rs.getString("descripcion_grupos_compatibles"));

                // Objeto para la foranea id_estiba_asignada
                // *****************************************
                idEstiba.setIdEstiba(rs.getInt("id_estiba_asignada"));
                idEstiba.setCodigoEstiba(rs.getString("codigo_estiba"));
                idEstiba.setDescripcionEstiba(rs.getString("descripcion_estiba"));

                // Objeto para la foranea id_productos_compatibles
                // ***********************************************
                idProducto.setCodigoProducto(rs.getString("codigo_producto"));

                // Asignacion de "idCodigoProducto" que es el obj de productos
                unidadesempaque.setCodigoProducto(idProducto);

                // Asignacion de "idGrupo" que es el obj de grupos compatibles
                unidadesempaque.setIdGrupoCompatible(idGrupo);

                // Asignacion de "idEstibaAsignada" que es el obj de estibas
                unidadesempaque.setIdEstibaAsignada(idEstiba);

                // Agregacion de todo el objeto manipulado "unidadesempaque" a lo que se retorna "unidadeslist"
                unidadeslist.add(unidadesempaque);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return unidadeslist;
    }

    /**
     * Este método actualiza el grupo compatible de una unidad de empaque en específico.
     * @author Edison Bedoya
     * @param unidadEmpa, Objeto tipo Unidad de Empaque
     * @version 1.0 2019-09-13
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int update(UnidadesEmpaque unidadEmpa) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_UPDATE);
            // Orden de los campos de como estan en el SQL de arriba, el SQL_UPDATE.
            // SET del SQL
            stmt.setInt(1, unidadEmpa.getIdGrupoCompatible().getIdGrupoCompatible());
            stmt.setInt(2, unidadEmpa.getIdEstibaAsignada().getIdEstiba());
            // WHERE del SQL
            stmt.setInt(3, unidadEmpa.getIdUnidadEmpaque());
            rows = stmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return rows;
    }

}
