/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.comercial;

import entidades.comercial.preciosEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
public class preciosDaoDB2 implements PreciosDTODaoDB2 {

    public Sesion sesion;

    public preciosDaoDB2() {
        this.sesion = new Sesion();
    }

    //CONSULTA DE TODA LA LISTA DE PRECIOS
    java.util.Date fecha = new Date();
    String anio = Integer.toString(fecha.getYear() + 1900);
    public final String SQL_SELECT_ALL = "SELECT PMETH,SUBSTR(PRKEY,0,36),SUBSTR(PRKEY,36,8),PFCT1,PSDTE,PSEDT,PCURR FROM ESP WHERE SPID='SP' AND YEAR(PSDTE)='" + anio + "' ORDER BY PSEDT DESC";
    public final String SQL_COPY_LIST = "INSERT INTO ESP SELECT SPID, PMETH, REPLACE(PRKEY,'origen','destino') AS PRKEY, PQTY1, PQTY2, PQTY3, PQTY4, PQTY5, PQTY6, "
            + "PQTY7, PQTY8, PQTY9, PFCT1, PFCT2, PFCT3, PFCT4, PFCT5, PFCT6, PFCT7, PFCT8, PFCT9, PSDTE, PSEDT, "
            + "PDESC, PCOMM, PCONT, PTYPE, PCURR, PCOMP, SPENDT, SPENTM, SPENUS, 'fecha' AS SPMNDT, 'fechaHora' AS SPMNTM, 'usuario' AS SPMNUS, SPECRO "
            + "FROM ESP WHERE PRKEY LIKE ? AND ? BETWEEN PSDTE AND PSEDT";

    //MÉTODO QUE RETORNA TODA LA LISTA DE PRECIOS ACTIVOS
    public List<preciosEntity> select_all() throws SQLException {
        Connection conn = null; 
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<preciosEntity> listaPrecios = new ArrayList<preciosEntity>();

        try {
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_SELECT_ALL);
 
            try {
                rs = stmt.executeQuery();
                while (rs.next()) {
                    preciosEntity recorrido = new preciosEntity();

                    recorrido.setPMETH(rs.getString(1));
                    recorrido.setPRKEY1(rs.getString(2));
                    recorrido.setPRKEY2(rs.getString(3));
                    recorrido.setPFCT1(rs.getString(4));
                    recorrido.setPSDTE(rs.getString(5));
                    recorrido.setPSEDT(rs.getString(6));
                    recorrido.setPCURR(rs.getString(7));

                    listaPrecios.add(recorrido);
                }
            } catch (SQLException sex) {
                Logger.getLogger(preciosDaoDB2.class.getName()).log(Level.SEVERE, null, sex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(preciosDaoDB2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaPrecios;
    }

    public String copiarListaPrecios(String origen, String destino, Integer fechaInicio) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rs = 0;

        String patternDate = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patternDate);
        String fechaIngreso = simpleDateFormat.format(new Date());

        String patternTime = "HHmmss";
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat(patternTime);
        String fechaHoraIngreso = simpleDateFormatTime.format(new Date());

        String usr = "'"+Sesion.getSesion().getFormUser()+"'";
        
        String nuevaLista = "";//variable que tiene el sql de inserción a la base de datos segun la consulta, reemplazando las cadenas necesarias

        try {
            conn = sesion.getConexionBPCS();
            nuevaLista = SQL_COPY_LIST.replace("'origen'","'"+origen+"'").replace("'destino'","'"+destino+"'").replace("'usuario'", usr).replace("'fecha'",fechaIngreso).replace("'fechaHora'",fechaHoraIngreso);
            stmt = conn.prepareStatement(nuevaLista);
            try {
                stmt.setString(1, "%" + origen + "%");
                stmt.setInt(2, fechaInicio);

                rs = stmt.executeUpdate();
                if (rs > 0) {
                    return "Copiados "+rs+" registros exitosamente";
                } else {
                    return "no copy";
                }
            } catch (SQLException sex) {
                Logger.getLogger(preciosDaoDB2.class.getName()).log(Level.SEVERE, null, sex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(preciosDaoDB2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "No se inserto la lista";
    }

}
