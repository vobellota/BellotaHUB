/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase es responsable por almacenar información de logs para ser
 * guardados en la base de datos
 *
 * @author Camilo Rojas
 * @version 1.0 2019-09-13
 */
public class ExceptionDataOperationDao {

    /**
     * @see com.bellota.apprest.persistence.util.Session Objeto de sesión
     * abierto
     */
    Sesion sesion;

    /**
     * Query de inserción en tabla de logs de la base de datos postgresql
     */
    String SQL_INSERT_BD = "INSERT INTO distribucion.logs(\n"
            + "            id_obj_pg, id_obj_as, id_obj_as_2, package_name, class_name, \n"
            + "            method_name, variable_name, type_operation, \n"
            + "            message, status, cod_error)\n"
            + "    VALUES ( ?, ?, ?, ?, ?, \n"
            + "            ?, ?, ?, \n"
            + "            ?, ?, ?);";

    /**
     * Constructor. Inicializa el objeto de sesión
     */
    public ExceptionDataOperationDao() {
        sesion = Sesion.getSesion();
    }

   /**
     * Este metodo inserta un registro en la tabla de logs de la base de datos postgresql
     */
    public void saveInBd(ExceptionDataOperation obj) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT_BD);
            stmt.setInt(1, obj.getIdObjPg());
            stmt.setString(2, obj.getIdObjAS());
            stmt.setString(3, obj.getIdObjAS2());
            stmt.setString(4, obj.getPackageName());
            stmt.setString(5, obj.getClassName());
            stmt.setString(6, obj.getMethodName());
            stmt.setString(7, obj.getVariableName());
            stmt.setString(8, obj.getTypeOperation());
            stmt.setString(9, obj.getMessage());
            stmt.setString(10, obj.getStatus());
            stmt.setString(11, obj.getCodError());
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }
}
