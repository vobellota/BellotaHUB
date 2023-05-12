/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.distribucion;

import beansVista.distribucion.ListasEmpaqueFacBean;
import beansVista.distribucion.models.Empaques;
import beansVista.distribucion.models.Facturas;
import beansVista.distribucion.models.Lios;
import beansVista.distribucion.models.ListasEmpaqueFac;
import beansVista.distribucion.models.ListasEmpaqueFacCabecera;
import beansVista.distribucion.models.UnidadesEmpaque2;
import entidades.distribucion.Estibas;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.ProductosCompatibles;
import entidades.distribucion.UnidadesEmpaque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import sys.util.Datos;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.*;
import sys.util.ExceptionDataOperation;
import sys.util.ExceptionDataOperationDao;
import sys.util.Sesion;

/**
 * Esta clase obtiene la información de la base de datos postgresql del objeto
 * ListaEmpaque del esquema distribución
 *
 * @author Edison Bedoya
 * @version 1.0 2019-09-13
 */
@LocalBean
public class ListasEmpaqueFacDao {

    /**
     * @see sys.util.Sesion Objeto de sesión abierto
     */
    public Sesion sesion;
    ExceptionDataOperationDao exceptionPOSTGRES;

    private final String SQL_SELECT_RANGE = "SELECT T2.prefijo, T2.nrofac, T2.nombreclien, T2.destino, T2.catidadlineas, T2.peso_unitario, T2.volumen FROM\n" +
                        "(SELECT S.CHPREF AS prefijo, S.HORD AS nrofac, R.CNME AS nombreclien, P.PCLCTN AS destino, S.HLINS AS catidadlineas,\n" +
                        "SUM (I.IWGHT* L.LQORD / I.IVULP) AS peso_unitario, SUM(COALESCE((I.IMHIGH * I.IMWIDE * I.IMLONG * L.LQORD ) / I.IVULP , 0)) AS volumen\n" +
                        "FROM ECH S\n" +
                        "INNER JOIN RCM R ON R.CCUST = S.HCUST\n" +
                        "INNER JOIN ECL L ON l.LORD = S.HORD\n" +
                        "INNER JOIN IIM I ON I.IPROD = L.LPROD\n" +
                        "INNER JOIN LPC P ON R.CZIP = P.PCPSCD\n" +
                        "INNER JOIN COLLXUSRF.WMS_LISTAEMPAQUE LE ON LE.ORDENDESALIDA = S.HORD \n" +
                        "WHERE LE.FECHAORDEN BETWEEN ? AND ? \n" +
                        "GROUP BY S.CHPREF, S.HORD, R.CNME, LE.FECHAORDEN, S.HCUST, P.PCLCTN, S.HLINS)T2\n" +
                        "ORDER BY T2.prefijo,T2.nrofac ASC";

    /*private String SQL_SELECT_ITEMS_ORDER = "SELECT S.IHDPFX, S.SIINVN, I.IDESC, I.IPROD, L.ILQTY "
            + "FROM SIH S\n"
            + "INNER JOIN SIL L\n"
            + "ON L.ILCUST=S.SICUST AND L.ILDPFX=S.IHDPFX AND L.ILDOCN=S.IHDOCN AND L.ILDYR=S.IHDYR AND L.ILDTYP=S.IHDTYP\n"
            + "INNER JOIN IIM I\n"
            + "ON I.IPROD = L.ILPROD\n"
            + "WHERE S.IHDTYP IN (1,2)";*/
    private String SQL_SELECT_ITEMS_ORDER = "SELECT T2.CHPREF, T2.FECHAORDEN, T2.IDESC, T2.IPROD, T2.LQORD, T2.PESO, T2.VOLUMEN  FROM\n" +
                    "(SELECT S.CHPREF, VARCHAR_FORMAT(LE.FECHAORDEN, 'YYYYMMDD') AS FECHAORDEN, I.IDESC, I.IPROD, L.LQORD,\n" +
                    "(I.IWGHT) / (CASE WHEN I.IVULP IS NULL OR  I.IVULP = 0 THEN 1 ELSE  I.IVULP END) AS PESO, COALESCE((I.IMHIGH * I.IMWIDE * I.IMLONG ) / (CASE WHEN I.IVULP IS NULL OR  I.IVULP = 0 THEN 1 ELSE  I.IVULP END) , 0) AS VOLUMEN\n" +
                    "FROM ECH S\n" +
                    "INNER JOIN ECL L ON l.LORD = S.HORD\n" +
                    "INNER JOIN IIM I ON I.IPROD = L.LPROD\n" +
                    "INNER JOIN COLLXUSRF.WMS_LISTAEMPAQUE LE ON LE.ORDENDESALIDA = S.HORD \n" +
                    "LEFT JOIN COLLXUSRF.LEITEMSCOMPOSED C ON C.CODITEMBILLED = I.IPROD\n" +
                    "WHERE C.CODITEMBILLED IS NULL\n" +
                    "AND S.HDTYP IN (1,2)\n" +
                    "AND S.HORD =? AND S.CHPREF =?\n" +
                    "UNION ALL\n" +
                    "SELECT T1.CHPREF, T1.FECHAORDEN, TRIM(T1.PRODOR) || ' - ' || TRIM(I.IDESC) AS IDESC, I.IPROD, T1.LQORD,\n" +
                    "(I.IWGHT) / (CASE WHEN I.IVULP IS NULL OR  I.IVULP = 0 THEN 1 ELSE  I.IVULP END) AS PESO, COALESCE((I.IMHIGH * I.IMWIDE * I.IMLONG) / (CASE WHEN I.IVULP IS NULL OR  I.IVULP = 0 THEN 1 ELSE  I.IVULP END) , 0) AS VOLUMEN\n" +
                    "FROM\n" +
                    "(SELECT  C.CODITEMPACKAGED AS IPROD, S.CHPREF, VARCHAR_FORMAT(LE.FECHAORDEN, 'YYYYMMDD') AS FECHAORDEN, L.LQORD, (SELECT T0.IDESC FROM IIM T0 WHERE T0.IPROD = C.CODITEMBILLED) AS PRODOR\n" +
                    "FROM ECH S\n" +
                    "INNER JOIN ECL L ON l.LORD = S.HORD\n" +
                    "INNER JOIN IIM I ON I.IPROD = L.LPROD\n" +
                    "INNER JOIN COLLXUSRF.WMS_LISTAEMPAQUE LE ON LE.ORDENDESALIDA = S.HORD\n" +
                    "INNER JOIN COLLXUSRF.LEITEMSCOMPOSED C ON C.CODITEMBILLED = I.IPROD\n" +
                    "WHERE\n" +
                    "S.HDTYP IN (1,2)\n" +
                    "AND S.HORD =? AND S.CHPREF =?\n" +
                    ") T1\n" +
                    "INNER JOIN IIM I ON I.IPROD = T1.IPROD\n" +
                    ") T2\n" +
                    "ORDER BY T2.CHPREF, T2.FECHAORDEN, T2.IDESC ASC";

    private String SQL_SELECT_ITEMS_ORDER_EXCLUDES = "SELECT T2.CHPREF, T2.FECHAORDEN, T2.IDESC, T2.IPROD, T2.LQORD, T2.PESO, T2.VOLUMEN  FROM\n" +
                    "(SELECT S.CHPREF, VARCHAR_FORMAT(LE.FECHAORDEN, 'YYYYMMDD') AS FECHAORDEN, I.IDESC, I.IPROD, L.LQORD,\n" +
                    "(I.IWGHT) / (CASE WHEN I.IVULP IS NULL OR I.IVULP = 0 THEN 1 ELSE I.IVULP END) AS PESO, COALESCE((I.IMHIGH * I.IMWIDE * I.IMLONG) / (CASE WHEN I.IVULP IS NULL OR  I.IVULP = 0 THEN 1 ELSE I.IVULP END), 0) AS VOLUMEN\n" +
                    "FROM ECH S\n" +
                    "INNER JOIN ECL L ON l.LORD = S.HORD\n" +
                    "INNER JOIN IIM I ON I.IPROD = L.LPROD\n" +
                    "INNER JOIN COLLXUSRF.WMS_LISTAEMPAQUE LE ON LE.ORDENDESALIDA = S.HORD \n" +
                    "LEFT JOIN COLLXUSRF.LEITEMSCOMPOSED C ON C.CODITEMBILLED = I.IPROD\n" +
                    "WHERE C.CODITEMBILLED IS NULL\n" +
                    "AND S.HDTYP IN (1,2)\n" +
                    "AND S.HORD =? AND S.CHPREF =?\n" +
                    "UNION ALL\n" +
                    "SELECT T1.CHPREF, T1.FECHAORDEN, TRIM(T1.PRODOR) || ' - ' || TRIM(I.IDESC) AS IDESC, I.IPROD, T1.LQORD,\n" +
                    "(I.IWGHT ) / (CASE WHEN I.IVULP IS NULL OR  I.IVULP = 0 THEN 1 ELSE I.IVULP END) AS PESO, COALESCE((I.IMHIGH * I.IMWIDE * I.IMLONG) / (CASE WHEN I.IVULP IS NULL OR  I.IVULP = 0 THEN 1 ELSE I.IVULP END), 0) AS VOLUMEN\n" +
                    "FROM\n" +
                    "(SELECT  C.CODITEMPACKAGED AS IPROD, S.CHPREF, VARCHAR_FORMAT(LE.FECHAORDEN, 'YYYYMMDD') AS FECHAORDEN, L.LQORD, (SELECT T0.IDESC FROM IIM T0 WHERE T0.IPROD = C.CODITEMBILLED) AS PRODOR\n" +
                    "FROM ECH S\n" +
                    "INNER JOIN ECL L ON l.LORD = S.HORD\n" +
                    "INNER JOIN IIM I ON I.IPROD = L.LPROD\n" +
                    "INNER JOIN COLLXUSRF.WMS_LISTAEMPAQUE LE ON LE.ORDENDESALIDA = S.HORD \n" +
                    "INNER JOIN COLLXUSRF.LEITEMSCOMPOSED C ON C.CODITEMBILLED = I.IPROD\n" +
                    "WHERE\n" +
                    "S.HDTYP IN (1,2) AND S.HORD =? AND S.CHPREF =?\n" +
                    ") T1\n" +
                    "INNER JOIN IIM I ON I.IPROD = T1.IPROD\n" +
                    ") T2 WHERE TRIM(T2.IPROD) IN (|VARARRAYID|)\n" +
                    "ORDER BY T2.CHPREF, T2.FECHAORDEN, T2.IDESC ASC";

    private final String SQL_SELECT_COMPATIBLE_GROUPS
            = "SELECT * FROM distribucion.grupos_compatibles g WHERE g.id_grupo_compatible = ?";

    private final String SQL_SELECT_ITEMS_COMPATIBLE_GROUPS
            = "SELECT p.* FROM distribucion.grupos_compatibles g "
            + "INNER JOIN distribucion.productos_compatibles p "
            + "ON g.id_grupo_compatible = p.id_grupo_compatible "
            + "WHERE p.id_grupo_compatible = ?";

    private String SQL_SELECT_BILL_HEADER = "SELECT T0.HCUST ,T0.HNAME ,T0.FECHAORDEN, T0.HPOST ,T0.HAD\n" +
                ",SUM(T0.IWGHT * T0.LQORD / T0.IVULP) AS PESO\n" +
                ",SUM(COALESCE((T0.IMHIGH * T0.IMWIDE * T0.IMLONG * T0.LQORD ) / T0.IVULP , 0)) AS VOLUMEN,\n" +
                "TRIM(T0.PCLCTN) || ' - ' ||  T0.CCDESC AS CCDESC,  T0.CHFAX AS SIPHONE, T0.CHDATN AS SIMAIL \n" +
                "FROM (              \n" +
                "SELECT S.HCUST ,TRIM(S.HNAME) AS HNAME ,VARCHAR_FORMAT(LE.FECHAORDEN, 'YYYYMMDD') AS FECHAORDEN,CONCAT(CONCAT(TRIM(S.HPOST), ' - '),TRIM(R.CSTE)) AS HPOST\n" +
                ",CONCAT(CONCAT(CONCAT(CONCAT(TRIM(S.HAD1),' '), TRIM(S.HAD2)), ' '), TRIM(S.HAD3)) AS HAD, TRIM(Z.CCDESC) AS CCDESC\n" +
                ",I.IWGHT ,L.LQORD ,I.IVULP ,I.IMHIGH ,I.IMWIDE ,I.IMLONG,M.PCLCTN, S.CHFAX, S.CHDATN\n" +
                "FROM ECH S\n" +
                "INNER JOIN  RCM R ON (S.HCUST=R.CCUST)\n" +
                "INNER JOIN ECL L ON L.LORD = S.HORD\n" +
                "INNER JOIN IIM I ON I.IPROD = L.LPROD\n" +
                "INNER JOIN COLLXUSRF.WMS_LISTAEMPAQUE LE ON LE.ORDENDESALIDA = S.HORD \n" +
                "LEFT JOIN ZCC Z ON (Z.CCCODE = R.CSTE AND Z.CCTABL = 'STATE')\n" +
                "INNER JOIN LPC M ON M.PCPSCD = S.HPOST\n" +
                "WHERE S.HORD =? AND S.CHPREF =?) T0\n" +
                "GROUP BY T0.HCUST ,T0.HNAME ,T0.FECHAORDEN, T0.HPOST, T0.HAD, T0.CCDESC, T0.CHFAX, T0.CHDATN, T0.PCLCTN";

    private String SQL_SELECT_UNITIES_PRODUCTS = "SELECT DISTINCT(u.codigo_producto) AS codigo_producto, u.cantidad,"
            + " u.descripcion, u.cantidad_descrita, u.id_grupo_compatible, u.id_estiba_asignada FROM  distribucion.unidades_empaque u INNER JOIN "
            + "  distribucion.productos_compatibles p ON (u.codigo_producto = p.codigo_producto ) INNER JOIN "
            + "  distribucion.grupos_compatibles g ON (u.id_grupo_compatible = g.id_grupo_compatible ) "
            + " WHERE u.estado_unidad ='LI'";

    private final String SQL_INSERT_LIST_PACKAGE_HEADER = "INSERT INTO distribucion.lista_empaque_cabecera \n"
            + "(idfacturaas, prefijo, nrofactura, descripcioncliente, idcliente, fecha, origen, destino, \n"
            + "direccion, totalpaquetes, pesototal, volumentotal, comentario, estado, telefono, correo) \n"
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING idlistaempaque";

    private final String SQL_INSERT_LIST_PACKAGE_DETAILS = "INSERT INTO distribucion.lista_empaque_detalle \n"
            + "(idlistaempaque, idlio, embalaje, idempaque, idgrupo, idproducto, productodescripcion, \n"
            + "grupodescripcion, codigoproducto, idunidadempaque, unidadempaquedescripcion,peso,volumen) \n"
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING idlinealistaempaque";

    private final String SQL_COUNT_ALARM = "select * from unidades_empaque where id_grupo_compatible is null and id_estiba_asignada is null";

    private final String SQL_INSERT_LIST_PACKAGE_EXCLUDES = "INSERT INTO distribucion.lista_empaque_detalle_excluidos \n"
            + "(idlistaempaque, idlio, embalaje, idempaque, idgrupo, idproducto, productodescripcion, \n"
            + "grupodescripcion, codigoproducto, idunidadempaque, unidadempaquedescripcion, estado,peso,volumen) \n"
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING idlinealistaempaque";

    private final String SQL_INSERT_LIST_PACKAGE_GROUP = "INSERT INTO distribucion.lista_empaque_detalle_agrupada \n"
            + "(idlistaempaque, idlio, embalaje, idempaque, idgrupo, idproducto, productodescripcion, \n"
            + "grupodescripcion, codigoproducto, idunidadempaque, unidadempaquedescripcion, estado,peso,volumen) \n"
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING idlinealistaempaque";

    private final String SQL_SELECT_BILL_STATE = "SELECT idlistaempaque,estado FROM distribucion.lista_empaque_cabecera WHERE nrofactura = ? AND prefijo = ? ORDER BY idlistaempaque DESC LIMIT 1";

    private final String SQL_SELECT_BILL_LINES_STATE = "SELECT d.* FROM distribucion.lista_empaque_detalle_excluidos d\n"
            + "INNER JOIN distribucion.lista_empaque_cabecera c ON d.idlistaempaque = c.idlistaempaque  \n"
            + "WHERE d.idlistaempaque = ? AND c.estado = 1 AND d.estado = 1 ORDER BY idempaque";

    private final String SQL_SELECT_BILL_HEADER_CREATED = "SELECT * FROM distribucion.lista_empaque_cabecera WHERE nrofactura = ? AND prefijo = ? ";

    private final String SQL_SELECT_BILL_LINES_CREATED = "SELECT * FROM distribucion.lista_empaque_detalle WHERE idlistaempaque = ? ORDER BY idempaque";

    private final String SQL_UPDATE_BILL_STATE_CHANGE = "UPDATE distribucion.lista_empaque_cabecera SET estado = ?, totalpaquetes=? WHERE nrofactura = ? AND prefijo = ?";

    private final String SQL_UPDATE_BILL_COMMENT = "UPDATE distribucion.lista_empaque_cabecera SET comentario = ? WHERE idlistaEmpaque = ?";

    private final String SQL_DELETE_BILL_LINES = "DELETE FROM distribucion.lista_empaque_detalle WHERE idlistaEmpaque = ?";

    private final String SQL_SELECT_BILL_LINES_GROUP_CREATED = "SELECT * FROM distribucion.lista_empaque_detalle_agrupada WHERE idlistaEmpaque = ? ORDER BY idempaque";

    /**
     * Constructor, Inicializa el objeto sesión y el objeto
     * ExceptionDataOperationDao de logs
     *
     */
    public ListasEmpaqueFacDao() {
        sesion = Sesion.getSesion();
        exceptionPOSTGRES = new ExceptionDataOperationDao();
    }

    /**
     * Este método obtiene la información de las facturas en un rango
     * de fechas y/o de identificadores dados
     *
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @param prefijoFactura, prefijo de facturación
     * @param enteronumeroInicial, número inicial del rango de facturas
     * @param enteronumeroFinal, número final del rango de facturas
     * @param enterofechaInicio, fecha inicial del rango de facturas
     * @param enterofechaFin, fecha final del rango de facturas
     * @return Lista de listas de empaque consultadas
     */
    public List<ListasEmpaqueFac> real_range(String prefijoFactura, String enteronumeroInicial, String enteronumeroFinal, String enterofechaInicio, String enterofechaFin) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ListasEmpaqueFac> listasemapquefac = new ArrayList<ListasEmpaqueFac>();

        try {
            conn = sesion.getConexionBPCS();
            // Variables del condicional del WHERE 
            // ***********************************

            String condicion = "SELECT T2.prefijo, T2.nrofac, T2.nombreclien, T2.destino, T2.catidadlineas, T2.peso_unitario, T2.volumen FROM\n" +
                                "(SELECT S.CHPREF AS prefijo, S.HORD AS nrofac, R.CNME AS nombreclien, P.PCLCTN AS destino, S.HLINS AS catidadlineas,\n" +
                                "SUM (I.IWGHT* L.LQORD / I.IVULP) AS peso_unitario, SUM(COALESCE((I.IMHIGH * I.IMWIDE * I.IMLONG * L.LQORD ) / I.IVULP , 0)) AS volumen\n" +
                                "FROM ECH S\n" +
                                "INNER JOIN RCM R ON R.CCUST = S.HCUST\n" +
                                "INNER JOIN ECL L ON l.LORD = S.HORD\n" +
                                "INNER JOIN IIM I ON I.IPROD = L.LPROD\n" +
                                "INNER JOIN LPC P ON R.CZIP = P.PCPSCD\n" +
                                "INNER JOIN COLLXUSRF.WMS_LISTAEMPAQUE LE ON LE.ORDENDESALIDA = S.HORD \n" +
                                "WHERE S.CHPREF = '" + prefijoFactura + "' AND S.CHUTYP NOT IN ('V','7')";

            if (enteronumeroInicial != null && enteronumeroFinal != null) {
                condicion = condicion + " AND S.HORD BETWEEN '" + enteronumeroInicial + "' AND '" + enteronumeroFinal + "'";
            }
            if (enterofechaInicio != null && enterofechaFin != null) {
                condicion = condicion + " AND VARCHAR_FORMAT(LE.FECHAORDEN, 'YYYYMMDD') BETWEEN '" + enterofechaInicio + "' AND '" + enterofechaFin + "'";
            }

            condicion = condicion + " GROUP BY S.CHPREF, S.HORD, R.CNME, LE.FECHAORDEN, S.HCUST, P.PCLCTN, S.HLINS)T2\n" +
                        "ORDER BY T2.prefijo,T2.nrofac ASC";

            stmt = conn.prepareStatement(condicion);

            rs = stmt.executeQuery();

            while (rs.next()) {
                ListasEmpaqueFac listaempafa = new ListasEmpaqueFac();
                // estas son las salidas
                listaempafa.setFactura(rs.getString(1) + rs.getString(2));
                listaempafa.setNroFactura(rs.getString(2));
                listaempafa.setPrefijo(rs.getString(1));
                listaempafa.setCliente(rs.getString(3));
                listaempafa.setDestino(rs.getString(4));
                listaempafa.setNroLineas(rs.getInt(5));
                listaempafa.setPeso(rs.getDouble(6));
                listaempafa.setVolumen(rs.getDouble(7));
                listasemapquefac.add(listaempafa);
            }

        } catch (SQLException ddilefrer001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "rearl_range", "ddilefrer001", "SELECT", ddilefrer001.getMessage(), null, String.valueOf(ddilefrer001.getSQLState())));
        }

        //System.out.println(listasemapquefac);
        return listasemapquefac;
    }

    /**
     * Este método obtiene la información de las ítems de una factura teniendo
     * en cuenta que los ítems pueden ser de dos tipos: simples o compuestos;
     * los ítems simples son consultados directamente en la tabla IIM de BPCS y
     * los ítems compuestos son las piezas de un ítem como carretillas, los
     * ítems compuestos se consultan de la tabla IIM de BPCS cruzándola con la
     * tabla de usuario COLLXUSRF.LEITEMSCOMPOSED la cual almacena el
     * identificador del ítem compuesto y el identificador de sus piezas.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param empaqueFacAux, objeto ListasEmpaqueFac con identificador de
     * factura a consultar
     * @return Lista de tipo Empaques la cual trae la información detallada de
     * cada producto de una factura
     */
    public List<Empaques> selectionItemsBills(ListasEmpaqueFac empaqueFacAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int iax = 0;
        List<Empaques> listasEmpaqueAux = new ArrayList<Empaques>();
        //String SQL_SELECT_ITEMS_ORDER_AUX = this.SQL_SELECT_ITEMS_ORDER + " AND S.SIINVN ='" + empaqueFacAux.getNroFactura() + "' AND S.IHDPFX ='" + empaqueFacAux.getPrefijo() + "' ORDER BY S.IHDPFX, S.SIINVN, I.IPROD DESC";
        /*    String SQL_SELECT_ITEMS_ORDER_AUX = this.SQL_SELECT_ITEMS_ORDER;
        SQL_SELECT_ITEMS_ORDER_AUX = SQL_SELECT_ITEMS_ORDER_AUX.replace("|VARIDDOC|", empaqueFacAux.getNroFactura());
        SQL_SELECT_ITEMS_ORDER_AUX = SQL_SELECT_ITEMS_ORDER_AUX.replace("|VARIDPRE|", empaqueFacAux.getPrefijo());
         */
        try {
            conn = sesion.getConexionBPCS();
            if (empaqueFacAux.getListaLios().size() > 0) {
                String SQL_SELECT_ITEMS_ORDER_AUX = this.SQL_SELECT_ITEMS_ORDER_EXCLUDES.replace("|VARARRAYID|", this.countIdInArray(empaqueFacAux.getListaLios()));
                stmt = conn.prepareStatement(SQL_SELECT_ITEMS_ORDER_AUX);
                int itId = 5;
                for (int i = 0; i < empaqueFacAux.getListaLios().size(); i++) {
                    stmt.setString(itId, empaqueFacAux.getListaLios().get(i));
                    itId++;
                }
                itId = 5;
            } else {
                stmt = conn.prepareStatement(this.SQL_SELECT_ITEMS_ORDER);
            }

            // Clausula WHERE para el eliminar los registros
            stmt.setString(1, empaqueFacAux.getNroFactura());
            stmt.setString(2, empaqueFacAux.getPrefijo());
            stmt.setString(3, empaqueFacAux.getNroFactura());
            stmt.setString(4, empaqueFacAux.getPrefijo());

            rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    Empaques empaque = new Empaques();
                    ProductosCompatibles productosCompatibles = new ProductosCompatibles();
                    empaque.setProductoCompatible(productosCompatibles);
                    List<UnidadesEmpaque> unidadesEmpaque = new ArrayList<>();
                    empaque.getProductoCompatible().setUnidadesEmpaqueCollection(unidadesEmpaque);

                    // estas son las salidas
                    //empaque.getProductoCompatible().setIdProductosCompatibles(Integer.parseInt(rs.getString(6)));
                    empaque.getProductoCompatible().setDescripcion(rs.getString(3));
                    empaque.getProductoCompatible().setCodigoProducto(rs.getString(4));
                    empaque.setIdFactura(rs.getString(1) + rs.getString(2));
                    empaque.setCantidadVendida(Double.parseDouble(rs.getString(5)));
                    empaque.setPeso(Double.parseDouble(rs.getString(6)));
                    empaque.setVolumen(Double.parseDouble(rs.getString(7)));
                    // empaque.getProductoCompatible().setUnidadesEmpaqueCollection(this.selectCompatibleGroup(productosCompatibles));
                    empaque.getProductoCompatible().setUnidadesEmpaqueCollection(this.selectionUnityProducts(productosCompatibles));
                    /*empaque.setCajaMaster(72);
                empaque.setCajaInner(24);
                empaque.setUnidad(1);*/
                    empaque.setPrefijo(rs.getString(1));
                    empaque.setNroFactura(rs.getString(2));
                    listasEmpaqueAux.add(empaque);
                    iax++;
                } catch (Exception ddilefsib001) {
                    exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectionItemsBills", "ddilefsib001", "SELECT", ddilefsib001.getMessage(), null, null));
                    iax++;
                }
            }
        } catch (SQLException ddilefsib002) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectionItemsBills", "ddilefsib002", "SELECT", ddilefsib002.getMessage(), null, String.valueOf(ddilefsib002.getSQLState())));
            iax++;
            return null;
        }
        return listasEmpaqueAux;
    }

    /**
     * Este método obtiene el la información detallada del grupo compatible de
     * una unidad de empaque en específico
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param unidadEmpaque, objeto unidadempaque
     * @return Devuelve un objeto de tipo GrupoCompatibles a partir de una unidad 
     * de empaque 
     */
    public GruposCompatibles selectCompatibleGroup(UnidadesEmpaque unidadEmpaque) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        GruposCompatibles grupoCompatible = new GruposCompatibles();

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT_COMPATIBLE_GROUPS);
            stmt.setInt(1, unidadEmpaque.getIdGrupoCompatible().getIdGrupoCompatible());
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Asignacion de variables OP
                // **************************
                grupoCompatible.setIdGrupoCompatible(rs.getInt(1));
                grupoCompatible.setCodigoGrupo(rs.getString(2));
                grupoCompatible.setDescripcion(rs.getString(3));
                grupoCompatible.setPermiteAgruparReferencias(rs.getString(4));
                grupoCompatible.setMetodoCalculo(rs.getString(5));
                grupoCompatible.setMaximoCajasAgrupar(rs.getInt(6));
                grupoCompatible.setPesoMaximoLio(rs.getDouble(7));
                grupoCompatible.setAncho(rs.getDouble(8));
                grupoCompatible.setAlto(rs.getDouble(9));
                grupoCompatible.setLargo(rs.getDouble(10));
                grupoCompatible.setFactorPesoVolumen(rs.getInt(11));
                grupoCompatible.setEstadoGruposCompatibles(rs.getString(12));
            } else {
                return null;
            }
        } catch (SQLException ddilefscg001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectCompatibleGroup", "ddilefscg001", "SELECT", ddilefscg001.getMessage(), null, String.valueOf(ddilefscg001.getSQLState())));
            return null;
        }
        return grupoCompatible;
    }

    /**
     * Este método obtiene el la lista de productos que se encuentren en el mismo
     * grupo compatible en específico
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param gruposCompatibles, objeto gruposCompatibles
     * @return Devuelve una lista de tipo ProductosCompatibles a partir de un 
     * grupoCompatible
     */
    public List<ProductosCompatibles> selectionItemsGroup(GruposCompatibles gruposCompatibles) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ProductosCompatibles> listaProductosCompatiblesAux = new ArrayList<ProductosCompatibles>();
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT_ITEMS_COMPATIBLE_GROUPS);
            stmt.setInt(1, gruposCompatibles.getIdGrupoCompatible());

            // Clausula WHERE para el eliminar los registros
            rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    ProductosCompatibles productosCompatibles = new ProductosCompatibles();
                    productosCompatibles.setIdProductosCompatibles(Integer.parseInt(rs.getString(1)));
                    productosCompatibles.setCodigoProducto(rs.getString(2));

                    // productosCompatibles.setUnidadEmpaque(Integer.parseInt(rs.getString(3)));
                    productosCompatibles.setDescripcion(rs.getString(3));
                    productosCompatibles.setLargo(Double.parseDouble(rs.getString(4)));
                    productosCompatibles.setAlto(Double.parseDouble(rs.getString(5)));
                    productosCompatibles.setAncho(Double.parseDouble(rs.getString(6)));
                    productosCompatibles.setPesoCajaMaster(Double.parseDouble(rs.getString(7)));

                    productosCompatibles.setUnidadesEmpaqueCollection(this.selectionUnityProducts(productosCompatibles));
                    listaProductosCompatiblesAux.add(productosCompatibles);
                } catch (Exception ddilefsig001) {
                    exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectionItemsGroup", "ddilefsig001", "SELECT", ddilefsig001.getMessage(), null, null));
                }
            }

        } catch (SQLException ddilefsig002) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectionItemsGroup", "ddilefsig002", "SELECT", ddilefsig002.getMessage(), null, String.valueOf(ddilefsig002.getSQLState())));
            return null;
        }
        return listaProductosCompatiblesAux;
    }

     /**
     * Este método obtiene la cabecera de una factura en específico
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param empaqueAux, objeto de tipo ListasEmpaqueFac
     * @return Devuelve una la información detallada de la cabecera de una factura
     * en específico
     */
    public Facturas selectionBillHeader(ListasEmpaqueFac empaqueAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Facturas billAux = new Facturas();
        try {
            conn = sesion.getConexionBPCS();
            stmt = conn.prepareStatement(SQL_SELECT_BILL_HEADER);
            // Clausula WHERE para el eliminar los registros
            stmt.setString(1, empaqueAux.getNroFactura());
            stmt.setString(2, empaqueAux.getPrefijo());
            rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    billAux.setIdCliente(rs.getString(1));
                    billAux.setDescripcionCliente(rs.getString(2));
                    if (rs.getString(3) != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        billAux.setFecha(formatter.parse(rs.getString(3)));
                    }
                    billAux.setOrigen(rs.getString(4));
                    billAux.setDestino(rs.getString(8));
                    billAux.setDireccion(rs.getString(5));
                    billAux.setTelefono(rs.getString(6));
                    billAux.setNroFactura(empaqueAux.getNroFactura());
                    billAux.setPrefijo(empaqueAux.getPrefijo());
                    billAux.setIdFacturaAS(empaqueAux.getPrefijo() + empaqueAux.getNroFactura());
                    billAux.setPesoTotal(Double.parseDouble(rs.getString(6)));
                    billAux.setVolumenTotal(Double.parseDouble(rs.getString(7)));
                    billAux.setTelefono(rs.getString(9).trim());
                    billAux.setCorreo(rs.getString(10).trim());
                } catch (Exception ddilefsbh001) {
                    exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectionBillHeader", "ddilefsbh001", "SELECT", ddilefsbh001.getMessage(), null, null));
                }
            }
        } catch (SQLException ddilefsbh002) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectionBillHeader", "ddilefsbh002", "SELECT", ddilefsbh002.getMessage(), null, String.valueOf(ddilefsbh002.getSQLState())));
            return null;
        }
        return billAux;
    }

    /**
     * Este método obtiene las unidades de empaque de un producto en específico
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param productoCompatibleAux, objeto de tipo Productos
     * @return Devuelve un listado con la información detallada de las unidades 
     * de empaque de un producto en específico
     */
    public List<UnidadesEmpaque> selectionUnityProducts(ProductosCompatibles productoCompatibleAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UnidadesEmpaque> listasUnidadesEmpaqueAux = new ArrayList<>();
        String SQL_SELECT_UNITIES_PRODUCTS_AUX = this.SQL_SELECT_UNITIES_PRODUCTS + " AND UPPER(TRIM(u.codigo_producto)) = UPPER(TRIM('" + productoCompatibleAux.getCodigoProducto().trim() + "')) ORDER BY u.cantidad DESC";
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_SELECT_UNITIES_PRODUCTS_AUX);
            // Clausula WHERE para el eliminar los registros

            rs = stmt.executeQuery();
            while (rs.next()) {
                UnidadesEmpaque unidadesEmpaque = new UnidadesEmpaque();
                unidadesEmpaque.setCodigoProducto(productoCompatibleAux);
                //unidadesEmpaque.getIdProductosCompatibles().setCodigoProducto(rs.getString(1));
                unidadesEmpaque.setCantidad(rs.getInt(2));
                unidadesEmpaque.setDescripcion(rs.getString(3));
                unidadesEmpaque.setCantidadDescrita(rs.getString(4));
                unidadesEmpaque.setIdGrupoCompatible(new GruposCompatibles(rs.getInt(5)));
                unidadesEmpaque.setIdGrupoCompatible(this.selectCompatibleGroup(unidadesEmpaque));
                // unidadesEmpaque.getIdGrupoCompatible().setIdGrupoCompatible(Integer.MIN_VALUE);
                listasUnidadesEmpaqueAux.add(unidadesEmpaque);
            }

        } catch (SQLException ddilefsup001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "selectionUnityProducts", "ddilefsup001", "SELECT", ddilefsup001.getMessage(), null, String.valueOf(ddilefsup001.getSQLState())));
            return null;
        }
        return listasUnidadesEmpaqueAux;
    }

    /**
     * Este método inserta en la base de datos postgresql la cabecera de una 
     * lista de empaque
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de su lista de 
     * empaque generada
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public Facturas insertListPacking(Facturas facAux) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet last_dml_execute = null;
        int last_dml_execute_id = -1;

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT_LIST_PACKAGE_HEADER);
            int index = 1;
            stmt.setString(index++, facAux.getIdFacturaAS());
            stmt.setString(index++, facAux.getPrefijo());
            stmt.setInt(index++, Integer.parseInt(facAux.getNroFactura()));
            stmt.setString(index++, facAux.getDescripcionCliente());
            stmt.setInt(index++, Integer.parseInt(facAux.getIdCliente()));
            if (facAux.getFecha() != null) {
                stmt.setDate(index++, new java.sql.Date(facAux.getFecha().getTime()));
            } else {
                stmt.setDate(index++, new java.sql.Date(new Date().getTime()));
            }
            stmt.setString(index++, facAux.getOrigen());
            stmt.setString(index++, facAux.getDestino());
            stmt.setString(index++, facAux.getDireccion());
            stmt.setInt(index++, facAux.getTotalPaquetes());
            stmt.setDouble(index++, facAux.getPesoTotal());
            stmt.setDouble(index++, facAux.getVolumenTotal());
            stmt.setString(index++, facAux.getComentario());
            stmt.setInt(index++, facAux.getEstado());
            stmt.setString(index++, facAux.getTelefono());
            stmt.setString(index++, facAux.getCorreo());
            stmt.execute();

            last_dml_execute = stmt.getResultSet();

            if (!last_dml_execute.wasNull()) {
                last_dml_execute.next();
                last_dml_execute_id = last_dml_execute.getInt(1);
                facAux.setIdListaEmpaque(last_dml_execute_id);

                this.insertListPackingDetalis(facAux);
                this.insertListPackingExcludes(facAux);
            }

        } catch (SQLException ddilefilp001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "insertListPacking", "ddilefilp001", "INSERT", ddilefilp001.getMessage(), null, String.valueOf(ddilefilp001.getSQLState())));
        }
        return facAux;
    }

    /**
     * Este método inserta en la base de datos postgresql las líneas de una 
     * lista de empaque
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de su lista de 
     * empaque generada
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int insertListPackingDetalis(Facturas facAux) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet last_dml_execute = null;
        int last_dml_execute_id = -1;

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT_LIST_PACKAGE_DETAILS);

            for (int i = 0; i < facAux.getListaLios().size(); i++) {
                try {
                    int index = 1;
                    stmt.setInt(index++, facAux.getIdListaEmpaque());
                    stmt.setInt(index++, facAux.getListaLios().get(i).getIdLio());
                    stmt.setInt(index++, facAux.getListaLios().get(i).getEmbalaje());
                    stmt.setInt(index++, facAux.getListaLios().get(i).getIdEmpaque());
                    stmt.setInt(index++, facAux.getListaLios().get(i).getIdGrupo());
                    stmt.setInt(index++, facAux.getListaLios().get(i).getIdProducto());
                    stmt.setString(index++, facAux.getListaLios().get(i).getProductoDescripcion());
                    stmt.setString(index++, facAux.getListaLios().get(i).getGrupoDescripcion());
                    stmt.setString(index++, facAux.getListaLios().get(i).getCodigoProducto());
                    stmt.setInt(index++, facAux.getListaLios().get(i).getIdUnidadEmpaque());
                    stmt.setString(index++, facAux.getListaLios().get(i).getUnidadEmpaqueDescripcion());
                    stmt.setDouble(index++, facAux.getListaLios().get(i).getPeso());
                    stmt.setDouble(index++, facAux.getListaLios().get(i).getVolumen());
                    stmt.execute();

                    last_dml_execute = stmt.getResultSet();

                    if (!last_dml_execute.wasNull()) {
                        last_dml_execute.next();
                        //last_dml_execute_id = last_dml_execute.getInt(1);
                    }
                } catch (Exception ddilefipd001) {
                    exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "insertListPackingDetalis", "ddilefipd001", "INSERT", ddilefipd001.getMessage(), null, null));
                }
            }
        } catch (SQLException ddilefipd002) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "insertListPackingDetalis", "ddilefipd002", "SELECT", ddilefipd002.getMessage(), null, String.valueOf(ddilefipd002.getSQLState())));
        }
        return last_dml_execute_id;
    }

    /**
     * Este método inserta en la base de datos postgresql las líneas eliminadas 
     * o excluidas en la generación de una lista de empaque
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de su lista de 
     * empaque generada
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int insertListPackingExcludes(Facturas facAux) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet last_dml_execute = null;
        int last_dml_execute_id = -1;

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT_LIST_PACKAGE_EXCLUDES);

            for (int i = 0; i < facAux.getListaLiosExluidos().size(); i++) {
                try {
                    int index = 1;
                    stmt.setInt(index++, facAux.getIdListaEmpaque());
                    stmt.setInt(index++, facAux.getListaLiosExluidos().get(i).getIdLio());
                    stmt.setInt(index++, facAux.getListaLiosExluidos().get(i).getEmbalaje());
                    stmt.setInt(index++, facAux.getListaLiosExluidos().get(i).getIdEmpaque());
                    stmt.setInt(index++, facAux.getListaLiosExluidos().get(i).getIdGrupo());
                    stmt.setInt(index++, facAux.getListaLiosExluidos().get(i).getIdProducto());
                    stmt.setString(index++, facAux.getListaLiosExluidos().get(i).getProductoDescripcion());
                    stmt.setString(index++, facAux.getListaLiosExluidos().get(i).getGrupoDescripcion());
                    stmt.setString(index++, facAux.getListaLiosExluidos().get(i).getCodigoProducto());
                    stmt.setInt(index++, facAux.getListaLiosExluidos().get(i).getIdUnidadEmpaque());
                    stmt.setString(index++, facAux.getListaLiosExluidos().get(i).getUnidadEmpaqueDescripcion());
                    stmt.setInt(index++, facAux.getListaLiosExluidos().get(i).getEstado());
                    stmt.setDouble(index++, facAux.getListaLios().get(i).getPeso());
                    stmt.setDouble(index++, facAux.getListaLios().get(i).getVolumen());
                    stmt.execute();

                    last_dml_execute = stmt.getResultSet();

                    if (!last_dml_execute.wasNull()) {
                        last_dml_execute.next();
                        last_dml_execute_id = last_dml_execute.getInt(1);
                    }
                } catch (Exception ddilefipe001) {
                    exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "insertListPackingExcludes", "ddilefipe001", "INSERT", ddilefipe001.getMessage(), null, null));
                }
            }
        } catch (SQLException ddilefipe002) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "insertListPackingExcludes", "ddilefipe002", "SELECT", ddilefipe002.getMessage(), null, String.valueOf(ddilefipe002.getSQLState())));
        }

        return last_dml_execute_id;
    }

    /**
     * Este método obtiene el estado de una lista de empaque
     * 0 Abierta
     * 1 Cerrada
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param empaqueAux, objeto de tipo ListasEmpaqueFac con la información 
     * temporal de cabecera de la factura relacionada a la lista de empaque
     * @return Objeto de tipo Facturas con la información detallada de la lista
     * de empaque.
     */
    public Facturas getBillState(ListasEmpaqueFac empaqueAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Facturas facAux = new Facturas(-1, "", empaqueAux.getPrefijo(), empaqueAux.getNroFactura(), new ArrayList<>());

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(this.SQL_SELECT_BILL_STATE);
            stmt.setInt(1, Integer.parseInt(empaqueAux.getNroFactura()));
            stmt.setString(2, empaqueAux.getPrefijo());
            rs = stmt.executeQuery();
            while (rs.next()) {
                facAux.setIdListaEmpaque(rs.getInt(1));
                facAux.setEstado(rs.getInt(2));
            }
        } catch (SQLException ddilefgbs001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "getBillState", "ddilefgbs001", "SELECT", ddilefgbs001.getMessage(), null, String.valueOf(ddilefgbs001.getSQLState())));
            return null;
        }
        return facAux;
    }

     /**
     * Este método obtiene la información líneas excluidas de una lista de empaque
     * cerrada para generar una nueva lista con los ítems sobrantes
     * 0 Abierta
     * 1 Cerrada
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de la factura 
     * relacionada a la lista de empaque
     * @return Objeto de tipo Facturas con la información detallada de la lista
     * de empaque.
     */
    public Facturas getBillLinesState(Facturas facAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(this.SQL_SELECT_BILL_LINES_STATE);
            stmt.setInt(1, facAux.getIdListaEmpaque());
            // stmt.setString(2, facAux.getPrefijo());
            rs = stmt.executeQuery();
            facAux.setListaLios(new ArrayList<>());
            while (rs.next()) {
                //facAux.getListaLios().add(new Lios(rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(10), rs.getString(8), rs.getInt(6), rs.getString(9), rs.getInt(7), rs.getInt(11), rs.getString(12)));
                facAux.getListaLios().add(new Lios(rs.getInt(1), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getInt(11), rs.getString(12), rs.getInt(13), rs.getDouble(14), rs.getDouble(15)));
            }
        } catch (SQLException ddilefgbl001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "getBillLinesState", "ddilefgbl001", "SELECT", ddilefgbl001.getMessage(), null, String.valueOf(ddilefgbl001.getSQLState())));
            return null;
        }
        return facAux;
    }

     /**
     * Este método obtiene la información de la lista de empaque creada
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param empaqueAux, objeto de tipo ListasEmpaqueFac con la información 
     * de la factura relacionada a la lista de empaque
     * @return Objeto de tipo Facturas con la información detallada de la lista
     * de empaque.
     */
    public Facturas getBillCreated(ListasEmpaqueFac empaqueAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Facturas facAux = new Facturas(-1, "", empaqueAux.getPrefijo(), empaqueAux.getNroFactura(), new ArrayList<>());
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(this.SQL_SELECT_BILL_HEADER_CREATED);
            stmt.setInt(1, Integer.parseInt(facAux.getNroFactura()));
            stmt.setString(2, facAux.getPrefijo());
            rs = stmt.executeQuery();
            while (rs.next()) {
                facAux.setIdListaEmpaque(rs.getInt(1));
                facAux.setIdFacturaAS(rs.getString(2));
                facAux.setPrefijo(rs.getString(3));
                facAux.setNroFactura(rs.getString(4));
                facAux.setDescripcionCliente(rs.getString(5));
                facAux.setIdCliente(rs.getString(6));
                facAux.setFecha(rs.getDate(7));
                facAux.setOrigen(rs.getString(8));
                facAux.setDestino(rs.getString(9));
                facAux.setDireccion(rs.getString(10));
                facAux.setTotalPaquetes(rs.getInt(11));
                facAux.setPesoTotal(rs.getInt(12));
                facAux.setVolumenTotal(rs.getInt(13));
                facAux.setComentario(rs.getString(14));
                facAux.setEstado(rs.getInt(15));
                facAux.setTelefono(rs.getString(16));
                facAux.setCorreo(rs.getString(17));
            }

            stmt = conn.prepareStatement(this.SQL_SELECT_BILL_LINES_CREATED);
            stmt.setInt(1, facAux.getIdListaEmpaque());
            rs = stmt.executeQuery();
            while (rs.next()) {
                facAux.getListaLios().add(new Lios(rs.getInt(1), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getInt(11), rs.getString(12), rs.getInt(13), rs.getDouble(14), rs.getDouble(15)));
            }
        } catch (SQLException ddilefgb001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "getBillCreated", "ddilefgb001", "SELECT", ddilefgb001.getMessage(), null, String.valueOf(ddilefgb001.getSQLState())));
            return null;
        }
        return facAux;
    }

     /**
     * Este método actualiza el estado de una lista de empaque de abierto a
     * cerrado
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de su lista de
     * empaque generada
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no
     * se efectuó algún cambio o el número de registros afectados en la
     * operación realizada.
     */
    public boolean updateBillHeaderState(Facturas facAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(this.SQL_UPDATE_BILL_STATE_CHANGE);
            stmt.setInt(1, 1);
            stmt.setInt(2, facAux.getListaLiosAgrupada().get(facAux.getListaLiosAgrupada().size() - 1).getIdLio());
            stmt.setInt(3, Integer.parseInt(facAux.getNroFactura()));
            stmt.setString(4, facAux.getPrefijo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ddileubh001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "updateBillHeaderState", "ddileubh001", "UPDATE", ddileubh001.getMessage(), null, String.valueOf(ddileubh001.getSQLState())));
            return false;
        }
    }

     /**
     * Este método actualiza el campo comentario de una lista de empaque en específico
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de su lista de 
     * empaque generada
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public boolean updateBillHeaderComment(Facturas facAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(this.SQL_UPDATE_BILL_COMMENT);
            stmt.setString(1, facAux.getComentario());
            stmt.setInt(2, facAux.getIdListaEmpaque());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ddileuhc001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "updateBillHeaderComment", "ddileuhc001", "UPDATE", ddileuhc001.getMessage(), null, String.valueOf(ddileuhc001.getSQLState())));
            return false;
        }
    }

    /**
     * Este método elimina líneas de una lista de empaque en específico
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de su lista de 
     * empaque generada
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public boolean deleteBillLines(Facturas facAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(this.SQL_DELETE_BILL_LINES);
            stmt.setInt(1, facAux.getIdListaEmpaque());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ddiledbl001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "deleteBillLines", "ddiledbl001", "DELETE", ddiledbl001.getMessage(), null, String.valueOf(ddiledbl001.getSQLState())));
            return false;
        }
    }

     /**
     * Este método obtiene los id de las lista de empaque creadas y cerradas
     * de una factura en específico 
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param id, lista tipo String con el id de listas de empaque
     * de la factura relacionada a la lista de empaque
     * @return String con el número de listas de empaque cerradas de una factura
     * en específico para generar la condición un query.
     */
    public String countIdInArray(List<String> id) {
        String idAux = "";
        for (int i = 0; i < id.size(); i++) {
            idAux = idAux + " ?,";

        }
        idAux = idAux.substring(0, idAux.length() - 1);
        return idAux;
    }

        /**
     * Este método inserta en la base de datos postgresql las líneas agrupadas 
     * de una lista de empaque; agrupadas por líos y sumando los valores
     * de los productos iguales en el mismo lío para visualizar un solo registro.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, objeto de tipo Facturas con la información de su lista de 
     * empaque generada
     * @return Entero con valores 0 si no se pudo ejecutar la operación o si no se efectuó
     * algún cambio o el número de registros afectados en la operación realizada.
     */
    public int insertListPackingGroup(Facturas facAux) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet last_dml_execute = null;
        int last_dml_execute_id = -1;

        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(SQL_INSERT_LIST_PACKAGE_GROUP);

            for (int i = 0; i < facAux.getListaLiosAgrupada().size(); i++) {
                try {
                    int index = 1;
                    stmt.setInt(index++, facAux.getIdListaEmpaque());
                    stmt.setInt(index++, facAux.getListaLiosAgrupada().get(i).getIdLio());
                    stmt.setInt(index++, facAux.getListaLiosAgrupada().get(i).getEmbalaje());
                    stmt.setInt(index++, facAux.getListaLiosAgrupada().get(i).getIdEmpaque());
                    stmt.setInt(index++, facAux.getListaLiosAgrupada().get(i).getIdGrupo());
                    stmt.setInt(index++, facAux.getListaLiosAgrupada().get(i).getIdProducto());
                    stmt.setString(index++, facAux.getListaLiosAgrupada().get(i).getProductoDescripcion());
                    stmt.setString(index++, facAux.getListaLiosAgrupada().get(i).getGrupoDescripcion());
                    stmt.setString(index++, facAux.getListaLiosAgrupada().get(i).getCodigoProducto());
                    stmt.setInt(index++, facAux.getListaLiosAgrupada().get(i).getIdUnidadEmpaque());
                    stmt.setString(index++, facAux.getListaLiosAgrupada().get(i).getUnidadEmpaqueDescripcion());
                    stmt.setInt(index++, facAux.getListaLiosAgrupada().get(i).getEstado());
                    stmt.setDouble(index++, facAux.getListaLios().get(i).getPeso());
                    stmt.setDouble(index++, facAux.getListaLios().get(i).getVolumen());
                    stmt.execute();

                    last_dml_execute = stmt.getResultSet();

                    if (!last_dml_execute.wasNull()) {
                        last_dml_execute.next();
                        last_dml_execute_id = last_dml_execute.getInt(1);
                    }
                } catch (Exception ddileilg001) {
                    exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "insertListPackingGroup", "ddileilg001", "INSERT", ddileilg001.getMessage(), null, null));
                }
            }
        } catch (SQLException ddileilg002) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "insertListPackingGroup", "ddileilg002", "SELECT", ddileilg002.getMessage(), null, String.valueOf(ddileilg002.getSQLState())));
        }
        return last_dml_execute_id;
    }

    
     /**
     * Este método obtiene la información de la lista de empaque cerrada lista 
     * para imprimir
     *
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @param idlistaempaque, id de la lista de empaque
     * @return Lista de Facturas con la información detallada de la lista
     * de empaque.
     */
    public List<Facturas> toPrintAndExcel(int idlistaempaque) throws SQLException {
        Connection conn = null;

        PreparedStatement stmt = null;
        PreparedStatement stmt_detalles = null;

        ResultSet rs = null;
        ResultSet rs2 = null;

        List<Facturas> listaCabecera = new ArrayList<Facturas>();

        try {
            conn = sesion.getConexionPOSTGRES();
            // Variables del condicional del WHERE
            // ***********************************
            // Cabecera
            String sql_cabecera = "SELECT * FROM distribucion.lista_empaque_cabecera WHERE idlistaempaque = " + idlistaempaque + "";
            stmt = conn.prepareStatement(sql_cabecera);
            rs = stmt.executeQuery();
            // Detalles
            String sql_detalles = "SELECT * FROM distribucion.lista_empaque_detalle_agrupada WHERE idlistaempaque = " + idlistaempaque + "";
            stmt_detalles = conn.prepareStatement(sql_detalles);
            rs2 = stmt_detalles.executeQuery();
            while (rs.next()) {
                // Cabecera lista de empaque
                Facturas listaEmpaCabeTemp = new Facturas();
                listaEmpaCabeTemp.setIdListaEmpaque(rs.getInt(1));
                listaEmpaCabeTemp.setIdFacturaAS(rs.getString(2));
                listaEmpaCabeTemp.setPrefijo(rs.getString(3));
                listaEmpaCabeTemp.setNroFactura(rs.getString(4));
                listaEmpaCabeTemp.setDescripcionCliente(rs.getString(5));
                listaEmpaCabeTemp.setIdCliente(rs.getString(6));
                listaEmpaCabeTemp.setFecha(rs.getDate(7));
                listaEmpaCabeTemp.setOrigen(rs.getString(8));
                listaEmpaCabeTemp.setDestino(rs.getString(9));
                listaEmpaCabeTemp.setDireccion(rs.getString(10));
                listaEmpaCabeTemp.setTotalPaquetes(rs.getInt(11));
                listaEmpaCabeTemp.setPesoTotal(rs.getInt(12));
                listaEmpaCabeTemp.setVolumenTotal(rs.getInt(13));
                listaEmpaCabeTemp.setComentario(rs.getString(14));
                listaEmpaCabeTemp.setEstado(rs.getInt(15));
                listaEmpaCabeTemp.setTelefono(rs.getString(16));
                listaEmpaCabeTemp.setCorreo(rs.getString(17));

                // Detalles Lista de Empaque
                while (rs2.next()) {
                    listaEmpaCabeTemp.getListaLios().add(new Lios(rs2.getInt(1), rs2.getInt(3), rs2.getInt(4), rs2.getInt(5), rs2.getInt(6), rs2.getInt(7), rs2.getString(8), rs2.getString(9), rs2.getString(10), rs2.getInt(11), rs2.getString(12), rs2.getInt(13), rs2.getDouble(14), rs2.getDouble(15)));
                }

                listaCabecera.add(listaEmpaCabeTemp);
            }

            return listaCabecera;

        } catch (SQLException ddilepae001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "toPrintAndExcel", "ddilepae001", "SELECT", ddilepae001.getMessage(), null, String.valueOf(ddilepae001.getSQLState())));
        }
        return null;
    }

    /**
     * Este método obtiene la información de una lista de empaque agrupada
     *
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @param empaqueAux, objeto con el id de la  lista de empaque
     * @return Lista de lista de empaque agrupadas con su información detallada.
     */
    public List<Facturas> getBillGroupCreated(ListasEmpaqueFac empaqueAux) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Facturas> factsAux = new ArrayList<>();
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        try {
            conn = sesion.getConexionPOSTGRES();
            stmt = conn.prepareStatement(this.SQL_SELECT_BILL_HEADER_CREATED);
            stmt.setInt(1, Integer.parseInt(empaqueAux.getNroFactura()));
            stmt.setString(2, empaqueAux.getPrefijo());
            rs = stmt.executeQuery();
            while (rs.next()) {
                Facturas facAux = new Facturas(-1, "", empaqueAux.getPrefijo(), empaqueAux.getNroFactura(), new ArrayList<>());
                facAux.setIdListaEmpaque(rs.getInt(1));
                facAux.setIdFacturaAS(rs.getString(2));
                facAux.setPrefijo(rs.getString(3));
                facAux.setNroFactura(rs.getString(4));
                facAux.setDescripcionCliente(rs.getString(5));
                facAux.setIdCliente(rs.getString(6));
                facAux.setFecha(rs.getDate(7));
                facAux.setOrigen(rs.getString(8));
                facAux.setDestino(rs.getString(9));
                facAux.setDireccion(rs.getString(10));
                facAux.setTotalPaquetes(rs.getInt(11));
                facAux.setPesoTotal(rs.getInt(12));
                facAux.setVolumenTotal(rs.getInt(13));
                facAux.setComentario(rs.getString(14));
                facAux.setEstado(rs.getInt(15));
                facAux.setTelefono(rs.getString(16));
                facAux.setCorreo(rs.getString(17));
                stmt2 = conn.prepareStatement(this.SQL_SELECT_BILL_LINES_GROUP_CREATED);
                stmt2.setInt(1, facAux.getIdListaEmpaque());
                rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    facAux.getListaLios().add(new Lios(rs2.getInt(1), rs2.getInt(3), rs2.getInt(4), rs2.getInt(5), rs2.getInt(6), rs2.getInt(7), rs2.getString(8), rs2.getString(9), rs2.getString(10), rs2.getInt(11), rs2.getString(12), rs2.getInt(13), rs2.getDouble(14), rs2.getDouble(15)));
                }
                factsAux.add(facAux);
            }
        } catch (SQLException ddilebgc001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "getBillGroupCreated", "ddilebgc001", "SELECT", ddilebgc001.getMessage(), null, String.valueOf(ddilebgc001.getSQLState())));
            return null;
        }
        return factsAux;
    }

     /**
     * Este método obtiene la información de un listado de cabeceras de listas de empaque
     * cerradas con formatos de campos específicos del archivo BillEnv
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listaEmpaqueSeleccionada, Listado de listas de empaque
     * @return Lista de lista de empaque agrupadas con formato de campos del archivo BillEnv.
     */
    public List<ListasEmpaqueFacCabecera> exportarExcelDao(List<ListasEmpaqueFac> listaEmpaqueSeleccionada) throws SQLException {
        Connection connPOSTGRESQL = Sesion.getSesion().getConexionPOSTGRES();
        PreparedStatement psItemUpda = null;
        ResultSet rs = null;
        List<String> cantItem = new ArrayList<String>();
        List<ListasEmpaqueFacCabecera> cabeceras = new ArrayList<ListasEmpaqueFacCabecera>();
        String dataVec = "";
        try {
            for (int i = 0; i < listaEmpaqueSeleccionada.size(); i++) {
                cantItem.add(listaEmpaqueSeleccionada.get(i).getFactura());
            }

            String[] cantidadVec;
            Array cantidadVecAux;

            //    String sqlUpdateUnities = "SELECT idfacturaas, idcliente, descripcioncliente, fecha, origen, destino, direccion, totalpaquetes, pesototal, volumentotal, prefijo, nrofactura FROM distribucion.lista_empaque_cabecera where idfacturaas=ANY(?::character varying[])";
            String sqlUpdateUnities = "SELECT idfacturaas, idcliente, descripcioncliente, fecha, origen, destino, direccion, totalpaquetes, pesototal, volumentotal, prefijo, nrofactura, telefono, correo FROM distribucion.lista_empaque_cabecera where idfacturaas IN(";

            cantidadVec = cantItem.toArray(new String[cantItem.size()]);
            for (int i = 0; i < cantidadVec.length; i++) {
                dataVec = dataVec + "'" + cantidadVec[i] + "',";
            }
            dataVec = dataVec.substring(0, dataVec.length() - 1);
            sqlUpdateUnities = sqlUpdateUnities + dataVec + ")";
            //cantidadVecAux = connPOSTGRESQL.createArrayOf("VARCHAR", cantidadVec);
            //    psItemUpda.setArray(1, cantidadVecAux);
            // psItemUpda.setObject(1, dataVec);

            psItemUpda = connPOSTGRESQL.prepareStatement(sqlUpdateUnities);
            rs = psItemUpda.executeQuery();

            while (rs.next()) {
                ListasEmpaqueFacCabecera listaTemp = new ListasEmpaqueFacCabecera();
                listaTemp.setFactura(rs.getString(1));
                listaTemp.setCliente(rs.getString(2));
                listaTemp.setDescripcioncliente(rs.getString(3));
                listaTemp.setFecha(rs.getString(4));
                listaTemp.setOrigen(rs.getString(5));
                listaTemp.setDestino(rs.getString(6));
                listaTemp.setDireccion(rs.getString(7));
                listaTemp.setNroLineas(rs.getInt(8));
                listaTemp.setPeso(rs.getDouble(9));
                listaTemp.setVolumen(rs.getDouble(10));
                listaTemp.setPrefijo(rs.getString(11));
                listaTemp.setNroFactura(rs.getString(12));
                listaTemp.setTelefono(rs.getString(13));
                listaTemp.setCorreo(rs.getString(14));
                cabeceras.add(listaTemp);
            }

        } catch (SQLException ddilefeed001) {
            exceptionPOSTGRES.saveInBd(new ExceptionDataOperation(-1, null, null, "dao.distribucion", "ListasEmpaqueFacDao", "exportarExcelDao", "ddilefeed001", "SELECT", ddilefeed001.getMessage(), null, String.valueOf(ddilefeed001.getSQLState())));
        }

        return cabeceras;
    }

}
