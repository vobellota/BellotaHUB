/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion;

import dao.distribucion.ProductosCompatiblesDao;
import dao.distribucion.GruposCompatiblesDao;
import dao.distribucion.EstibasDao;
import entidades.distribucion.ProductosCompatibles;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import entidades.distribucion.Estibas;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.RowEditEvent;
import sys.util.ExceptionManager;
import sys.util.Funciones;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "ProductosCompatiblesBean")
public class ProductosCompatiblesBean {

    /**
     * Creates a new instance of GruposCompatibles
     */
    public Sesion sesion;
    private List<ProductosCompatibles> listaProductosCompatibles;
    private List<GruposCompatibles> listaGruposCompatibles;
    private List<UnidadesEmpaque> listaUnidadesEmpaque;
    private List<UnidadesEmpaque> listaUnidadesSinParame;
    private List<Estibas> listaEstibasAsignadas;
    private ProductosCompatibles nuevosProductosCompatibles = new ProductosCompatibles();
    private ProductosCompatibles selectedProductosCompRow;
    private String codigoProdTemp;
    private int alarmable;
    private String cadenaAlarma;

    public ProductosCompatiblesBean() {
        try {
            // obj de productos
            ProductosCompatiblesDao produccomprtemp = new ProductosCompatiblesDao(Sesion.getSesion());
            listaProductosCompatibles = produccomprtemp.select();
            // Asignar valor por defecto
            nuevosProductosCompatibles.setEstadoProducto("Activo");
            // obj de grupos compatibles para el select dinamico
//            GruposCompatiblesDao grupocomptemp = new GruposCompatiblesDao(Sesion.getSesion());
//            listaGruposCompatibles = grupocomptemp.select();
            // obj de estibas asignadas para el select dinamico
            EstibasDao estibastemp = new EstibasDao(Sesion.getSesion());
            listaEstibasAsignadas = estibastemp.select();
            Funciones funciones = new Funciones();
            // Apartado de la alarma
            alarmable = funciones.parametrizables();
            // ******************************
            if (alarmable != 0) {
                setCadenaAlarma("Faltan " + alarmable + " Unidades de empaque por parametrizar");
                // Lista de unidades sin parametrizar
                // **********************************
                listaUnidadesSinParame = funciones.unitiesWithoutParams();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public List<ProductosCompatibles> getListaProductosCompatibles() {
        return listaProductosCompatibles;
    }

    public void setListaProductosCompatibles(List<ProductosCompatibles> listaProductosCompatibles) {
        this.listaProductosCompatibles = listaProductosCompatibles;
    }

    public ProductosCompatibles getNuevosProductosCompatibles() {
        return nuevosProductosCompatibles;
    }

    public void setNuevosProductosCompatibles(ProductosCompatibles nuevosProductosCompatibles) {
        this.nuevosProductosCompatibles = nuevosProductosCompatibles;
    }

    public List<GruposCompatibles> getListaGruposCompatibles() {
        return listaGruposCompatibles;
    }

    public void setListaGruposCompatibles(List<GruposCompatibles> listaGruposCompatibles) {
        this.listaGruposCompatibles = listaGruposCompatibles;
    }

    public List<Estibas> getListaEstibasAsignadas() {
        return listaEstibasAsignadas;
    }

    public List<UnidadesEmpaque> getListaUnidadesEmpaque() {
        return listaUnidadesEmpaque;
    }

    public void setListaUnidadesEmpaque(List<UnidadesEmpaque> listaUnidadesEmpaque) {
        this.listaUnidadesEmpaque = listaUnidadesEmpaque;
    }

    public void setListaEstibasAsignadas(List<Estibas> listaEstibasAsignadas) {
        this.listaEstibasAsignadas = listaEstibasAsignadas;
    }

    public String getCodigoProdTemp() {
        return codigoProdTemp;
    }

    public void setCodigoProdTemp(String codigoProdTemp) {
        this.codigoProdTemp = codigoProdTemp;
    }

    // Seleccion de una row puntual Get y Set
    // **************************************
    public ProductosCompatibles getSelectedProductosCompRow() {
        return selectedProductosCompRow;
    }

    public void setSelectedProductosCompRow(ProductosCompatibles selectedProductosCompRow) {
        this.selectedProductosCompRow = selectedProductosCompRow;
    }

    public List<UnidadesEmpaque> getListaUnidadesSinParame() {
        return listaUnidadesSinParame;
    }

    public void setListaUnidadesSinParame(List<UnidadesEmpaque> listaUnidadesSinParame) {
        this.listaUnidadesSinParame = listaUnidadesSinParame;
    }

    public int getAlarmable() {
        return alarmable;
    }

    public void setAlarmable(int alarmable) {
        this.alarmable = alarmable;
    }

    public String getCadenaAlarma() {
        return cadenaAlarma;
    }

    public void setCadenaAlarma(String cadenaAlarma) {
        this.cadenaAlarma = cadenaAlarma;
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ProductosCompatiblesDao objsave = new ProductosCompatiblesDao(Sesion.getSesion());
            objsave.update((ProductosCompatibles) event.getObject());
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha Editado exitosamente");
        } catch (SQLException ex) {
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        // Mensaje de adicion
        ExceptionManager.addError("Se ha cancelado la edición del registro");
    }

    public void onStoring() {
        try {
            ProductosCompatiblesDao objsave = new ProductosCompatiblesDao(Sesion.getSesion());
            // Agregar a la lista el nuevo registro
            // Para que pueda ser renderizado en la 
            // Datatable correctamente con el :updtae
            listaProductosCompatibles.add(nuevosProductosCompatibles);
            // Llamado a insertar en el DAO
            objsave.insert(nuevosProductosCompatibles);
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha insertado un Producto");
        } catch (SQLException ex) {
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    // Metodos Ajax de los diferentes eventos
    // Metodo asignar la var que por ajax llegó el codigo producto
    // ***********************************************************
    public void codigoProduTemp(AjaxBehaviorEvent event) {
        String cadena = (String) ((javax.faces.component.html.HtmlInputText) event.getSource()).getValue();
        setCodigoProdTemp(cadena);
    }

    public void onSelectUnitiesAcepted() {
        try {
            ProductosCompatiblesDao objsave = new ProductosCompatiblesDao(Sesion.getSesion());
            listaUnidadesEmpaque = objsave.select_one(selectedProductosCompRow);
        } catch (SQLException ex) {
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public void inSynchronizingGrupos(RowEditEvent event) {
        try {
            // obj de grupos compatibles para el select dinamico
            GruposCompatiblesDao grupocomptemp = new GruposCompatiblesDao(Sesion.getSesion());
            listaGruposCompatibles = grupocomptemp.select();

//            String id_element = (String) ((javax.faces.component.html.HtmlSelectOneMenu) event.getSource()).getId();
            System.out.println(event.getComponent().getId());
            System.out.println(event.getComponent().getClientId());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    // ******************************
    // Pruebas Insert Or Update Items 
    // ******************************
    public void inSynchronizingItems() {
        try {
            Funciones funcion = new Funciones();
            Connection connBPCS = funcion.getConexionBPCS();
            Connection connPOSTGRESQL = funcion.getConexionPOSTGRES();
            List<String> cantItem = new ArrayList<String>();
            String[] cantidadVec;
            Array cantidadVecAux;
            String dataVec = "";
            String sqlItems = "select DISTINCT(IPROD) AS IPROD, IDESC,IMLONG,IMHIGH,IMWIDE,IWGHT,'Activo'\n"
                    + "from IIM\n"
                    + "left join ZLI ON LIPROD=IPROD AND REGEXP_COUNT(TRIM(LILANG),'^-?\\d+$') <> 0\n"
                    + "where IID='IM' and IITYP in('C', 'T')";

            PreparedStatement stmBPCS = connBPCS.prepareStatement(sqlItems);
            ResultSet rsBPCS = stmBPCS.executeQuery();

            while (rsBPCS.next()) {

                String codItem = rsBPCS.getString("IPROD").trim();

                String sqlOneItem = "select * from distribucion.productos_compatibles where codigo_producto=?";
                PreparedStatement psItems = connPOSTGRESQL.prepareStatement(sqlOneItem);
                psItems.setString(1, codItem);
                ResultSet rsItem = psItems.executeQuery();

                if (rsItem.next()) {
                    // *********************
                    // Existe, lo actualiza
                    // *********************
                    String sqlInsertItems = "UPDATE distribucion.productos_compatibles \n"
                            + "SET codigo_producto = ?, descripcion = ?, largo = ?, alto = ?, ancho = ?, peso_caja_master = ?\n"
                            + "WHERE codigo_producto = ?";
                    PreparedStatement stmPOST = connPOSTGRESQL.prepareStatement(sqlInsertItems);

                    stmPOST.setString(1, rsBPCS.getString("IPROD").trim());
                    stmPOST.setString(2, rsBPCS.getString("IDESC"));
                    stmPOST.setDouble(3, rsBPCS.getDouble("IMLONG"));
                    stmPOST.setDouble(4, rsBPCS.getDouble("IMHIGH"));
                    stmPOST.setDouble(5, rsBPCS.getDouble("IMWIDE"));
                    stmPOST.setDouble(6, rsBPCS.getDouble("IWGHT"));
                    stmPOST.setString(7, codItem);
                    stmPOST.executeUpdate();

                } else {
                    // *********************
                    // No existe, lo inserta
                    // *********************
                    String sqlInsertItems = "INSERT INTO distribucion.productos_compatibles (codigo_producto, descripcion, largo, alto, ancho, peso_caja_master, estado_producto) VALUES(?,?,?,?,?,?,CAST(? AS enum_4))";
                    PreparedStatement stmPOST = connPOSTGRESQL.prepareStatement(sqlInsertItems);
                    for (int i = 1; i <= rsBPCS.getMetaData().getColumnCount(); i++) {
                        String columnType = rsBPCS.getMetaData().getColumnTypeName(i);

                        switch (columnType) {
                            case "CHAR":
                                String campoChar = rsBPCS.getString(i).trim();
                                stmPOST.setString(i, campoChar);
                                break;
                            case "DECIMAL":
                                double campoDec = rsBPCS.getDouble(i);
                                stmPOST.setDouble(i, campoDec);
                                break;
                            case "VARCHAR":
                                String campoVarChar = rsBPCS.getString(i).trim();
                                stmPOST.setString(i, campoVarChar);
                                break;
                            default:
                                String campoDef = rsBPCS.getString(i).trim();
                                stmPOST.setString(i, campoDef);
                                break;
                        }
                    }

                    stmPOST.executeUpdate();
                }

                cantItem.add(rsBPCS.getString("IPROD").trim());

            }

            /* String sqlUpdateUnities = "update distribucion.productos_compatibles\n"
                    + "set estado_producto = 'Inactivo'\n"
                    + "where codigo_producto \n"
                    + "    not in(\n"
                    + "        select codigo_producto from distribucion.productos_compatibles \n"
                    + "        where codigo_producto = ANY(?::character varying[])\n"
                    + "    )";*/
            String sqlUpdateUnities = "update distribucion.productos_compatibles\n"
                    + "set estado_producto = 'Inactivo'\n"
                    + "where codigo_producto \n"
                    + "    not in(\n"
                    + "        select codigo_producto from distribucion.productos_compatibles \n"
                    + "        where codigo_producto IN (\n";

            cantidadVec = cantItem.toArray(new String[cantItem.size()]);
            for (int i = 0; i < cantidadVec.length; i++) {
                dataVec = dataVec + "'" + cantidadVec[i] + "',";
            }
            dataVec = dataVec.substring(0, dataVec.length() - 1);
            sqlUpdateUnities = sqlUpdateUnities + dataVec + "))";
            //cantidadVecAux = connPOSTGRESQL.createArrayOf("VARCHAR", cantidadVec);
            //psItemUpda.setArray(1, cantidadVecAux);
            PreparedStatement psItemUpda = connPOSTGRESQL.prepareStatement(sqlUpdateUnities);
            psItemUpda.executeUpdate();
            connBPCS.close();
            connPOSTGRESQL.close();
            /*FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Finalizó la sincronización de los ítems"));
            ExceptionManager.addError("Finalizó la sincronización de los ítems");*/
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            SQLException e2 = sqle.getNextException();
            System.out.println(e2.getMessage());
            e2.printStackTrace();
        }
    }

    public void inSynchronizingUnits() {
        try {
            Funciones funcion = new Funciones();
            Connection connBPCS = funcion.getConexionBPCS();
            Connection connPOSTGRESQL = funcion.getConexionPOSTGRES();

            int k = 0;

            String sqlUnities = "select LIPROD, cast(LILANG as INTEGER) as LILANG, LIDSC1, LIREF,LIID from ZLI\n"
                    + "inner join IIM on liprod = iprod \n"
                    + "WHERE LIID <> 'LZ' and REGEXP_COUNT(TRIM(LILANG),'^-?\\d+$') <> 0 and IID='IM' and IITYP in('C', 'T')";

            PreparedStatement stmBPCS = connBPCS.prepareStatement(sqlUnities);
            ResultSet rsBPCS = stmBPCS.executeQuery();
            String codItem = null;
            String codItem2 = null;
            List<Integer> cantItem = new ArrayList<Integer>();
            Integer[] cantidadVec;
            Array cantidadVecAux;
            String dataVec = "";
            while (rsBPCS.next()) {
                if (k == 0) {
                    codItem = rsBPCS.getString("LIPROD").trim();
                    codItem2 = rsBPCS.getString("LIPROD").trim();
                    cantItem.add(rsBPCS.getInt("LILANG"));
                } else if (!codItem.equals(codItem2)) {
                    String sqlUpdateUnities = "update distribucion.unidades_empaque\n"
                            + "set estado_unidad = 'LZ'\n"
                            + "where id_unidad_empaque \n"
                            + "     in(\n"
                            + "        select id_unidad_empaque from distribucion.unidades_empaque \n"
                            + "        where codigo_producto = ? AND \n"
                            + "        cantidad \n"
                            + "        not in(";

                    // psItemUpda.setString(1, codItem);
                    cantidadVec = cantItem.toArray(new Integer[cantItem.size()]);
                    // cantidadVecAux = connPOSTGRESQL.createArrayOf("INTEGER", cantidadVec);
                    dataVec = "";
                    for (int i = 0; i < cantidadVec.length; i++) {
                        dataVec = dataVec + "'" + cantidadVec[i] + "',";
                    }

                    dataVec = dataVec.substring(0, dataVec.length() - 1);
                    sqlUpdateUnities = sqlUpdateUnities + dataVec + "))";
                    PreparedStatement psItemUpda = connPOSTGRESQL.prepareStatement(sqlUpdateUnities);
                    //psItemUpda.setArray(2, cantidadVecAux);
                    ResultSet rsItemUpda = psItemUpda.executeQuery();
                    cantItem.clear();
                    codItem2 = codItem;
                    k++;
                } else {
                    cantItem.add(rsBPCS.getInt("LILANG"));
                    k++;
                }

                cantidadVec = new Integer[]{rsBPCS.getInt("LILANG")};
                String sqlOneItem = "select * from distribucion.unidades_empaque where codigo_producto = ? and cantidad  IN (";

                // cantidadVecAux = connPOSTGRESQL.createArrayOf("INTEGER", cantidadVec);
                dataVec = "";
                for (int i = 0; i < cantidadVec.length; i++) {
                    dataVec = dataVec + "'" + cantidadVec[i] + "',";
                }
                dataVec = dataVec.substring(0, dataVec.length() - 1);
                sqlOneItem = sqlOneItem + dataVec + ")";
                //psItem.setArray(2, cantidadVecAux);
                PreparedStatement psItem = connPOSTGRESQL.prepareStatement(sqlOneItem);
                psItem.setString(1, codItem);
                ResultSet rsItem = psItem.executeQuery();

                if (rsItem.next()) {

                    // ********************
                    // Existe, lo actualiza
                    // ********************
                    String sqlInsertUnities = "update distribucion.unidades_empaque\n"
                            + "set codigo_producto = ?, cantidad = ?, descripcion = ?, cantidad_descrita = ?\n"
                            + "where codigo_producto = ? and cantidad IN(";
                    sqlInsertUnities = sqlInsertUnities + dataVec;
                    sqlInsertUnities = sqlInsertUnities + ") and estado_unidad = ?";
                    PreparedStatement stmPOST = connPOSTGRESQL.prepareStatement(sqlInsertUnities);

                    stmPOST.setString(1, codItem);
                    stmPOST.setInt(2, cantidadVec[0]);
                    stmPOST.setString(3, rsBPCS.getString("LIDSC1").trim());
                    stmPOST.setString(4, rsBPCS.getString("LIREF").trim());
                    stmPOST.setString(5, codItem);
                    //stmPOST.setArray(6, cantidadVecAux);
                    stmPOST.setString(6, rsBPCS.getString("LIID").trim());

                    /* cantidadVecAux = connPOSTGRESQL.createArrayOf("INTEGER", cantidadVec);
                    stmPOST.setArray(6, cantidadVecAux);*/
                    stmPOST.executeUpdate();
                } else {
                    // *********************
                    // No existe, lo inserta
                    // *********************
                    String sqlInsertUnities = "INSERT INTO distribucion.unidades_empaque (codigo_producto, cantidad, descripcion, cantidad_descrita, estado_unidad) VALUES(?,CAST(? AS INTEGER),CAST(? AS VARCHAR),?,?)";
                    PreparedStatement stmPOST = connPOSTGRESQL.prepareCall(sqlInsertUnities);
                    for (int i = 1; i <= rsBPCS.getMetaData().getColumnCount(); i++) {
                        String columnType = rsBPCS.getMetaData().getColumnTypeName(i);
                        switch (columnType) {
                            case "CHAR":
                                String campoChar = rsBPCS.getString(i).trim();
                                stmPOST.setString(i, campoChar);
                                break;
                            case "DECIMAL":
                                double campoDec = rsBPCS.getDouble(i);
                                stmPOST.setDouble(i, campoDec);
                                break;
                            case "VARCHAR":
                                String campoVarChar = rsBPCS.getString(i).trim();
                                stmPOST.setString(i, campoVarChar);
                                break;
                            case "NCHAR":
                                String campoNChar = rsBPCS.getString(i);
                                stmPOST.setString(i, campoNChar);
                                break;
                            case "INTEGER":
                                int campoInteger = rsBPCS.getInt(i);
                                stmPOST.setInt(i, campoInteger);
                                break;
                            default:
                                String campoDef = rsBPCS.getString(i).trim();
                                stmPOST.setString(i, campoDef);
                                break;
                        }
                    }
                    stmPOST.executeUpdate();
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Finalizó la sincronización de las unidades de empaque"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            SQLException e2 = sqle.getNextException();
            System.out.println(e2.getMessage());
            e2.printStackTrace();
        }
    }
}
