/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansNegocio.distribucion;

import beansVista.distribucion.ListasEmpaqueFacBean;
import beansVista.distribucion.models.Empaques;
import beansVista.distribucion.models.Facturas;
import beansVista.distribucion.models.Lios;
import beansVista.distribucion.models.ListasEmpaqueFac;
import dao.distribucion.ListasEmpaqueFacDao;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.ProductosCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

/**
 * Esta clase procesa la información del objeto listas de empaque e interconecta
 * su capa de prsistencia con su capa de presentación
 *
 * @author Camilo Rojas
 * @version 1.0 2019-09-13
 */
@Stateless
public class ListasEmpaqueBusinessBean implements IListasEmpaqueBusinessBean {

     /**
     * @see dao.distribucion.ListasEmpaqueFacDao Objeto de lista de empaque
     */
    private ListasEmpaqueFacDao listaEmpaqueDao;
    
    int contEmpaque = 1;

    // Variables espejo
    // ****************
    private String enteronumeroInicial;
    private String enteronumeroFinal;
    private String enterofechaInicio;
    private String enterofechaFin;

    /**
     * @return the enterofechaInicio
     */
    public String getEnterofechaInicio() {
        return enterofechaInicio;
    }

    /**
     * @param enterofechaInicio the enterofechaInicio to set
     */
    public void setEnterofechaInicio(String enterofechaInicio) {
        this.enterofechaInicio = enterofechaInicio;
    }

    /**
     * @return the enterofechaFin
     */
    public String getEnterofechaFin() {
        return enterofechaFin;
    }

    /**
     * @param enterofechaFin the enterofechaFin to set
     */
    public void setEnterofechaFin(String enterofechaFin) {
        this.enterofechaFin = enterofechaFin;
    }

    /**
     * @return the enteronumeroInicial
     */
    public String getEnteronumeroInicial() {
        return enteronumeroInicial;
    }

    /**
     * @param enteronumeroInicial the enteronumeroInicial to set
     */
    public void setEnteronumeroInicial(String enteronumeroInicial) {
        this.enteronumeroInicial = enteronumeroInicial;
    }

    /**
     * @return the enteronumeroFinal
     */
    public String getEnteronumeroFinal() {
        return enteronumeroFinal;
    }

    /**
     * @param enteronumeroFinal the enteronumeroFinal to set
     */
    public void setEnteronumeroFinal(String enteronumeroFinal) {
        this.enteronumeroFinal = enteronumeroFinal;
    }

    /**
     * Este método obtiene la información de los ítems de una factura a partir 
     * de su método de persistencia relacionado
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listaEmpaqueSeleccionada, objeto unidadempaque
     * @see dao.distribucion.ListasEmpaqueFacDao#selectionItemsBills(beansVista.distribucion.models.ListasEmpaqueFac listaEmpaqueSeleccionada) selectionItemsBills
     * @return Devuelve la información de los pitems de una factura sin transformar
     * data obtenida de persistencia
     */
    public List<Empaques> mtdItemsBills(ListasEmpaqueFac listaEmpaqueSeleccionada) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        return listaEmpaqueDao.selectionItemsBills(listaEmpaqueSeleccionada);
    }

    /**
     * Este método crea la lista de items organizados por cantidad de productos 
     * en una caja de acuerdo sus unidades de empaque.   
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listaEmpaqueSeleccionada, objeto ListasEmpaqueFac objeto con información de la factura a consultar
     * @return Devuelve la lista de ítems organizados   por cantidad de productos 
     * en una caja de acuerdo sus unidades de empaque.
     */
    public List<Empaques> mtdPackingBills(ListasEmpaqueFac listaEmpaqueSeleccionada) {
        List<Empaques> listEmpaque = new ArrayList<>();
        List<Empaques> listEmpaquesAux = this.mtdItemsBills(listaEmpaqueSeleccionada);

        for (int i = 0; i < listEmpaquesAux.size(); i++) {
            try {
                listEmpaque.addAll(this.mtdFillPacking(listEmpaquesAux.get(i), 0, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.contEmpaque = 1;
        return listEmpaque;
    }

     /**
     * Este método crea la lista de líos organizados por grupos de compatibilidad
     * evaluando cada caja de productos.   
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listaEmpaqueSeleccionada, objeto ListasEmpaqueFac objeto con información de la factura a consultar
     * @return Devuelve la lista de líos organizados por grupos de compatibilidad
     * evaluando cada caja de productos.   
     */
    public List<List<Lios>> getBagLios(ListasEmpaqueFac listaEmpaqueSeleccionada) {
        List<List<Lios>> listLiosAux = new ArrayList<>();
        List<Lios> listLiosAuxPaquete = new ArrayList<>();
        List<Lios> listLiosAuxAgrupada = new ArrayList<>();
        List<Empaques> listEmpaquesAux = this.mtdPackingBills(listaEmpaqueSeleccionada);
        Empaques empaqueAux = new Empaques();
        int contLio = 1;
        int contLioEmpaque = 1;
        for (int i = 0; i < listEmpaquesAux.size(); i++) {
            try {
                if (i == 0) {
                    listLiosAuxPaquete.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                    listLiosAuxAgrupada.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                    contLioEmpaque++;
                } else {
                    if (listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getPermiteAgruparReferencias().toUpperCase().equals("SI")) {
                        if (empaqueAux.getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible() == listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible()) {
                            //empaqueAux.getProductoCompatible().getIdGrupoCompatible().setProductosCompatiblesCollection(listaEmpaqueDao.selectionItemsGroup(empaqueAux.getProductoCompatible().getIdGrupoCompatible()));
                            //  if (this.itemInCompatibleGroup((List<ProductosCompatibles>) empaqueAux.getProductoCompatible().getIdGrupoCompatible().getProductosCompatiblesCollection(), listEmpaquesAux.get(i).getProductoCompatible())) {
                            
                             listLiosAuxPaquete.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                           
                            if (listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getMaximoCajasAgrupar() <= contLioEmpaque) {
                                contLio++;
                                contLioEmpaque = 0;
                            }
                            if (empaqueAux.getProductoCompatible().getCodigoProducto().equals(listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto()) && listLiosAuxAgrupada.get(listLiosAuxAgrupada.size() - 1).getIdLio() == contLio) {
                                listLiosAuxAgrupada.get(listLiosAuxAgrupada.size() - 1).setEmbalaje((listLiosAuxAgrupada.get(listLiosAuxAgrupada.size() - 1).getEmbalaje() + (int) listEmpaquesAux.get(i).getCantidadVendida()));
                            } else {
                                listLiosAuxAgrupada.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                            }
                            
                            contLioEmpaque++;
                        } else {
                            contLio++;
                            contLioEmpaque = 1;
                            listLiosAuxPaquete.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                            listLiosAuxAgrupada.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                            //listLiosAux.add(new Lios(contLio, listEmpaquesAux.get(i)));

                        }
                    } else {
                        contLio++;
                        contLioEmpaque = 1;
                        listLiosAuxPaquete.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                        listLiosAuxAgrupada.add(new Lios(contLio, (int) listEmpaquesAux.get(i).getCantidadVendida(), listEmpaquesAux.get(i).getIdEmpaque(), listEmpaquesAux.get(i).getProductoCompatible().getCodigoProducto(), listEmpaquesAux.get(i).getProductoCompatible().getDescripcion(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getIdGrupoCompatible(), listEmpaquesAux.get(i).getUnidadEmpaqueAux().getIdGrupoCompatible().getDescripcion(), listEmpaquesAux.get(i).getPeso(), listEmpaquesAux.get(i).getVolumen()));
                        //listLiosAux.add(new Lios(contLio, listEmpaquesAux.get(i)));

                    }
                }
                empaqueAux = listEmpaquesAux.get(i);
            } catch (Exception e) {
                System.out.println("Exception e" + e.toString());
            }
        }
        listLiosAux.add(listLiosAuxPaquete);
        listLiosAux.add(listLiosAuxAgrupada);
        return listLiosAux;
    }

      /**
     * Este método indica si un item pertenece al mismo grupo de compatiblidad que 
     * otro producto antecesor en la lista de items organizados.   
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param productosCompatibles, listado de items en un grupo compatible
     * @param productoCompatible, item que se evaluará
     * @return Devuelve un valor true si el item pertenece al mismo grupo compatible 
     * y false sino pertenece.   
     */
    public boolean itemInCompatibleGroup(List<ProductosCompatibles> productosCompatibles, ProductosCompatibles productoCompatible) {
        for (int i = 0; i < productosCompatibles.size(); i++) {
            if (productosCompatibles.get(i).getCodigoProducto().trim().equals(productoCompatible.getCodigoProducto().trim())) {
                return true;
            }
        }
        return false;
    }

     /**
     * Este método obtiene las listas de empaque para un rango de facturas dadas.   
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listaEmpaqueSeleccionada, lista de ListasEmpaqueFac con información 
     * de las facturas a consultar su lista de empaque
     * @return Devuelve la lista de listas de empaque de un rango de facturas dadas.
     */
    @Override
    public List<Facturas> getBillsLios(List<ListasEmpaqueFac> listaEmpaqueSeleccionada) {
        List<Facturas> listFacturasAux = new ArrayList<>();

        for (int i = 0; i < listaEmpaqueSeleccionada.size(); i++) {
            try {
                //consultar si la lista se encuentra abierta, cerrada o si no existe
                Facturas facCabAux = this.mtdBillStateVerification(listaEmpaqueSeleccionada.get(i));
                this.mtdBillLinesVerification(facCabAux);
                if (facCabAux.getEstado() == 0 && facCabAux.getIdListaEmpaque() > 0) {
                    Facturas fac = this.mtdGetBillCreated(listaEmpaqueSeleccionada.get(i));
                    listFacturasAux.add(fac);
                } else {
                    listaEmpaqueSeleccionada.get(i).setListaLios(this.mtdGetIdItems(facCabAux));
                    if (facCabAux.getIdListaEmpaque() > 0 && listaEmpaqueSeleccionada.get(i).getListaLios().size() < 1) {
                    } else {
                        List<List<Lios>> listaLiosAux = this.getBagLios(listaEmpaqueSeleccionada.get(i));
                        if (listaLiosAux.size() > 0) {
                            if (listaLiosAux.get(0).size() > 0) {
                                Facturas fac = this.mtdGetBillHeader(listaEmpaqueSeleccionada.get(i));
                                    listFacturasAux.add(new Facturas(fac.getIdFacturaAS(), fac.getPrefijo(), fac.getNroFactura(), fac.getSubNroFactura(),listaLiosAux.get(0), listaLiosAux.get(1), fac.getDescripcionCliente(), fac.getIdCliente(), 
                                            fac.getFecha(), "MANIZALES - CALDAS", fac.getDestino(), fac.getDireccion(), listaLiosAux.get(0).get(listaLiosAux.get(0).size() - 1).getIdLio(), fac.getPesoTotal(), 
                                            fac.getVolumenTotal(), fac.getTelefono(), fac.getCorreo()));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("ex" + e.toString());
            }
        }
        return listFacturasAux;
    }

    /**
     * Consultar las listas de empaque agrupadas y cerradas de un rango de facturas
     * en específico.   
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listaEmpaqueSeleccionada, lista de ListasEmpaqueFac con información 
     * de las facturas a consultar su lista de empaque
     * @return Devuelve la lista de listas de empaque agrupadas y cerradas de un rango de facturas dadas.
     */
    @Override
    public List<Facturas> getCreatedBillsLios(List<ListasEmpaqueFac> listaEmpaqueSeleccionada) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        List<Facturas> listFacturasAux = new ArrayList<>();
        for (int i = 0; i < listaEmpaqueSeleccionada.size(); i++) {
            try {
                //consultar si la lista se encuentra abierta, cerrada o si no existe
                listFacturasAux.addAll(this.listaEmpaqueDao.getBillGroupCreated(listaEmpaqueSeleccionada.get(i)));
            } catch (Exception e) {
                System.out.println("ex" + e.toString());
            }
        }
        return listFacturasAux;
    }

    /**
     * Este método obtiene la cabecera de una lista de empaque en particular.   
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listaEmpaqueSeleccionada, objeto de ListasEmpaqueFac con información 
     * de la factura a consultar su lista de empaque
     * @return Devuelve la cabecera de la lista de empaque de una factura en particular.
     */
    public Facturas mtdGetBillHeader(ListasEmpaqueFac listaEmpaqueSeleccionada) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        return listaEmpaqueDao.selectionBillHeader(listaEmpaqueSeleccionada);
    }

    /**
     * Este método evalúa si la cantidad de items entrantes es menor que 
     * la cantidad que debe tener una caja (unidad de empaque determinada) e 
     * indica la unidad de empaque que debe tomar un item en ese instante.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param empaqueAux, Item a evaluar su cantidad
     * @param pos, posición de lista de unidades de la última unidad de empaque evaluada
     * @return Devuelve la posición en la lista de unidades de empaque
     * de una producto determinado. Cada producto tiene su propia lista de unidades
     * de empaque.
     */
    public double mtdGetUnityPacking(Empaques empaqueAux, double pos) {
        List<UnidadesEmpaque> listUnityPackingAux = (List<UnidadesEmpaque>) empaqueAux.getProductoCompatible().getUnidadesEmpaqueCollection();
        while (empaqueAux.getCantidadVendida() < listUnityPackingAux.get((int) pos).getCantidad()) {
            pos++;
        }
        return pos;
    }

    /**
     * Este método evalúa como se deben particionar las cantidades ordenadas
     * de un producto, de acuerdo a sus unidades de empaque, teniendo en cuenta 
     * que debe comenzar de mayor a menor unidad.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param packingAux, Item a evaluar su cantidad
     * @param nPacking, Cantidad restante a acomodar en una unidad de empaque
     * @param pos, posición de lista de unidades de la última unidad de empaque evaluada
     * @return Devuelve una lista con la distribución de la cantidad ordenada 
     * de un producto de acuerdo a su unidad de empaque.
     */
    public List<Empaques> mtdFillPacking(Empaques packingAux, double nPacking, double pos) {
        List<Empaques> listPackingAux = new ArrayList<>();
        if (packingAux.getCantidadVendida() == 1) {
            pos = this.mtdGetUnityPacking(packingAux, pos);
            List<UnidadesEmpaque> listUnityPackingAux = (List<UnidadesEmpaque>) packingAux.getProductoCompatible().getUnidadesEmpaqueCollection();
            listPackingAux.add(new Empaques(packingAux.getProductoCompatible(), listUnityPackingAux.get((int) pos), listUnityPackingAux.get((int) pos).getCantidad(), contEmpaque, packingAux.getPeso() * listUnityPackingAux.get((int) pos).getCantidad(), packingAux.getVolumen() * listUnityPackingAux.get((int) pos).getCantidad()));
            return listPackingAux;
        } else if (packingAux.getCantidadVendida() < 1) {
            return listPackingAux;
        } else {
            pos = this.mtdGetUnityPacking(packingAux, pos);
            List<UnidadesEmpaque> listUnityPackingAux = (List<UnidadesEmpaque>) packingAux.getProductoCompatible().getUnidadesEmpaqueCollection();
            nPacking = packingAux.getCantidadVendida() / listUnityPackingAux.get((int) pos).getCantidad();
            packingAux.setCantidadVendida(packingAux.getCantidadVendida() % listUnityPackingAux.get((int) pos).getCantidad());
            listPackingAux.addAll(this.mtdFillPacking(packingAux, nPacking, pos));
            for (int i = 0; i < Math.floor(nPacking); i++) {
                listPackingAux.add(new Empaques(packingAux.getProductoCompatible(), listUnityPackingAux.get((int) pos), listUnityPackingAux.get((int) pos).getCantidad(), contEmpaque++, packingAux.getPeso() * listUnityPackingAux.get((int) pos).getCantidad(), packingAux.getVolumen() * listUnityPackingAux.get((int) pos).getCantidad()));
            }
            return listPackingAux;
        }
    }

    // Logica de Conversion de datos
    // *****************************
    
     /**
     * Este método convierte los formatos de rangos fechas y números de las
     * facturas a cosnultar.
     *
     * @author Edison Bedoya
     * @version 1.0 2019-09-13
     * @param prefijoFactura, prefijo de facturación
     * @param numeroInicial, número inicial del rango de facturas
     * @param numeroFinal, número final del rango de facturas
     * @param fechaInicio, fecha inicial del rango de facturas
     * @param fechaFin, fecha final del rango de facturas
     * @return Devuelve una lista con las facturas obtenidas en el rango dado.
     */
    @Override
    public List<ListasEmpaqueFac> onConvertVariables(String prefijoFactura, int numeroInicial, int numeroFinal, Date fechaInicio, Date fechaFin) {

        // Creacion de lista auxiliar para retornar
        // ****************************************
        List<ListasEmpaqueFac> listaAux = null;
        if (numeroInicial != 0 && numeroFinal != 0) {
            setEnteronumeroInicial(String.valueOf(numeroInicial));
            setEnteronumeroFinal(String.valueOf(numeroFinal));
        } else {
            setEnteronumeroInicial(null);
            setEnteronumeroFinal(null);
        }

        // Conversion del Objeto Date a String
        if (fechaInicio != null && fechaFin != null) {
            DateFormat dt = new SimpleDateFormat("yyyyMMdd");
            setEnterofechaInicio(dt.format(fechaInicio));
            setEnterofechaFin(dt.format(fechaFin));
        } else {
            setEnterofechaInicio(null);
            setEnterofechaFin(null);
        }
        // Objeto del DAO
        ListasEmpaqueFacDao listaempaquedao = new ListasEmpaqueFacDao();
        try {
            // Arreglo que declaré arriba
            listaAux = listaempaquedao.real_range(prefijoFactura, enteronumeroInicial, enteronumeroFinal, enterofechaInicio, enterofechaFin);
        } catch (SQLException ex) {
            Logger.getLogger(ListasEmpaqueFacBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaAux;
    }

     /**
     * Este método organiza de menor a mayor el listado de listas de empaque 
     * de acuerdo a los identificadores de facturas dadas; Es el inicio de 
     * la invocación recursiva para el ordenamiento de la lista
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param liosAux, Item a evaluar su cantidad
     * @param begin, inicio del listado de facturas
     * @param end, final del listado de facturas
     */
    public void quickSortLios(List<Lios> liosAux, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partitionLios(liosAux, begin, end);

            quickSortLios(liosAux, begin, partitionIndex - 1);
            quickSortLios(liosAux, partitionIndex + 1, end);
        }
    }

    /**
     * Este método evalúa si un identificador de factura es menor o mayor que su antecesor.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param liosAux, Item a evaluar su cantidad
     * @param begin, inicio del listado de facturas
     * @param end, final del listado de facturas
     */
    private int partitionLios(List<Lios> liosAux, int begin, int end) {
        int pivot = liosAux.get(end).getIdLio();
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (liosAux.get(j).getIdLio() <= pivot) {
                i++;
                Lios swapTemp = liosAux.get(i);
                liosAux.set(i, liosAux.get(j));
                liosAux.set(j, swapTemp);
            }
        }
        Lios swapTemp = liosAux.get(i + 1);
        liosAux.set(i + 1, liosAux.get(end));
        liosAux.set(end, swapTemp);
        return i + 1;
    }

    /**
     * Este método organiza de menor a mayor el listado de facturas dadas;
     * Es el inicio de la invocación recursiva para el ordenamiento de la lista
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param objAux, Item a evaluar su cantidad
     * @param begin, inicio del listado de facturas
     * @param end, final del listado de facturas
     */
    public void quickSortBillSelected(List<ListasEmpaqueFac> objAux, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partitionBillsSelected(objAux, begin, end);

            quickSortBillSelected(objAux, begin, partitionIndex - 1);
            quickSortBillSelected(objAux, partitionIndex + 1, end);
        }
    }

     /**
     * Este método evalúa si un identificador de factura es menor o mayor que su antecesor.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param liosAux, Item a evaluar su cantidad
     * @param begin, inicio del listado de facturas
     * @param end, final del listado de facturas
     */
    private int partitionBillsSelected(List<ListasEmpaqueFac> objAux, int begin, int end) {
        int pivot = Integer.parseInt(objAux.get(end).getNroFactura().trim());
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (Integer.parseInt(objAux.get(j).getNroFactura().trim()) <= pivot) {
                i++;
                ListasEmpaqueFac swapTemp = objAux.get(i);
                objAux.set(i, objAux.get(j));
                objAux.set(j, swapTemp);
            }
        }
        ListasEmpaqueFac swapTemp = objAux.get(i + 1);
        objAux.set(i + 1, objAux.get(end));
        objAux.set(end, swapTemp);
        return i + 1;
    }

     /**
     * Este método permite cambiar de lío una caja de los item solicitados; este cambio
     * es de forma manual.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param liosAux, lío completo al que pertenece el item que se desea cambiar
     */
    public void changeIdLios(List<Lios> liosAux) {
        liosAux.get(0).setIdLio(1);
        for (int i = 0; i < liosAux.size(); i++) {
            if ((i < liosAux.size() - 1) && (liosAux.get(i + 1).getIdLio() != (liosAux.get(i).getIdLio() + 1)) && liosAux.get(i + 1).getIdLio() != (liosAux.get(i).getIdLio())) {
                //liosAux.get(i + 1).setIdLio(liosAux.get(i).getIdLio() + 1);
                this.changeOrderSimilarIdLios(liosAux,liosAux.get(i+1).getIdLio(),liosAux.get(i).getIdLio() + 1);
            }
        }
    }
    
    /**
     * Este método cambiar el id del lío de una caja determinada; este cambio
     * es de forma manual.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param liosAux, lío completo al que pertenece el item que se desea cambiar
     * @param oldIdLio, id del lío en el que se encuentra
     * @param newIdLio, id del lío por el que se desea cambiar
     */
      public void changeOrderSimilarIdLios(List<Lios> liosAux, int oldIdLio, int newIdLio) {
        for (int i = 0; i < liosAux.size(); i++) {
            if (liosAux.get(i).getIdLio() == oldIdLio) {
                liosAux.get(i).setIdLio(newIdLio);
            }
        }
    }

     /**
     * Este método invoca la inserción de la lista de empaque en la capa de
     * persistencia.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux, Cabecera de la lista de empaque
     * @return Boolean indicando si se guardó con éxito o no.
     */
    @Override
    public boolean postGuardarListaEmpaque(Facturas facAux) {
        try {
            this.listaEmpaqueDao.insertListPacking(facAux);
            if (facAux.getIdListaEmpaque() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListasEmpaqueBusinessBean.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

     /**
     * Este método invoca la consulta de estado de la lista de empaque en la capa de persistencia.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param empaqueAux, Cabecera de la lista de empaque
     * @return objeto de tipo lista de empaque con su estado.
     */
    public Facturas mtdBillStateVerification(ListasEmpaqueFac empaqueAux) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        Facturas facAux = listaEmpaqueDao.getBillState(empaqueAux);
        return facAux;
    }

    /**
     * Este método invoca la consulta de estado de líneas excluidas
     * de lista de empaque en la capa de persistencia.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux,  Cabecera de la lista de empaque
     * @return objeto de tipo lista de empaque con el detalle de las líneas
     * excluidas.
     */
    public Facturas mtdBillLinesVerification(Facturas facAux) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        listaEmpaqueDao.getBillLinesState(facAux);
        return facAux;
    }

    /**
     * Este método obtiene los códigos de productos en la lista de líos organizada
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux,  Cabecera de la lista de empaque
     * @return listado con los códigos de productos en la lista de líos organizada
     */
    public List<String> mtdGetIdItems(Facturas facAux) {
        List<String> idItems = new ArrayList<>();
        for (int i = 0; i < facAux.getListaLios().size(); i++) {
            idItems.add(facAux.getListaLios().get(i).getCodigoProducto().replace(" ", ""));
        }
        return idItems;
    }

    /**
     * Este método invoca la consulta de listas de empaque cerradas
     * en la capa de persistencia.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param empaqueAux,  Cabecera de la lista de empaque
     * @return listado con las listas de empaque ya cerradas según un rango 
     * de facturas dadas.
     */
    public Facturas mtdGetBillCreated(ListasEmpaqueFac empaqueAux) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        Facturas facAux = listaEmpaqueDao.getBillCreated(empaqueAux);
        return facAux;
    }

     /**
     * Este método invoca la actualización de estado de una lista de empaque
     * en la capa de persistencia.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux,  Cabecera de la lista de empaque
     * @return valor binario indicando si la operación fue exitosa o no.
     */
    @Override
    public boolean putBillHeaderState(Facturas facAux) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        try {
            return listaEmpaqueDao.updateBillHeaderState(facAux);
        } catch (Exception ex) {
            Logger.getLogger(ListasEmpaqueBusinessBean.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Este método actualiza la lista de empaque guardada y abierta de acuerdo 
     * a una nueva instruccion de guardado.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux,  Cabecera de la lista de empaque
     */
    @Override
    public void updateBillAllLines(Facturas facAux) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        try {
            listaEmpaqueDao.updateBillHeaderComment(facAux);
            listaEmpaqueDao.deleteBillLines(facAux);
            listaEmpaqueDao.insertListPackingDetalis(facAux);
            listaEmpaqueDao.insertListPackingExcludes(facAux);
        } catch (Exception ex) {
            Logger.getLogger(ListasEmpaqueBusinessBean.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

     /**
     * Este método agrupa las líneas de un lista de empaque.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux,  Cabecera de la lista de empaque
     */
    public void mtdDoesGroupList(Facturas facAux) {
        facAux.getListaLiosAgrupada().clear();
        int j = 0;
        for (int i = 0; i < facAux.getListaLios().size(); i++) {
            try {
                if (i == 0) {
                    facAux.getListaLiosAgrupada().add(new Lios(facAux.getListaLios().get(i).getIdLineaLio(), facAux.getListaLios().get(i).getIdLio(), facAux.getListaLios().get(i).getEmbalaje(), facAux.getListaLios().get(i).getIdEmpaque(), facAux.getListaLios().get(i).getIdGrupo(), facAux.getListaLios().get(i).getIdProducto(), facAux.getListaLios().get(i).getProductoDescripcion(), facAux.getListaLios().get(i).getGrupoDescripcion(), facAux.getListaLios().get(i).getCodigoProducto(), facAux.getListaLios().get(i).getIdUnidadEmpaque(), facAux.getListaLios().get(i).getUnidadEmpaqueDescripcion(), facAux.getListaLios().get(i).getEstado(), facAux.getListaLios().get(i).getPeso(), facAux.getListaLios().get(i).getVolumen()));
                    j++;
                } else {

                    if (facAux.getListaLios().get(i).getCodigoProducto().equals(facAux.getListaLios().get(i - 1).getCodigoProducto()) && facAux.getListaLios().get(i).getIdLio() == facAux.getListaLios().get(i - 1).getIdLio()) {
                        facAux.getListaLiosAgrupada().get(j - 1).setEmbalaje((facAux.getListaLiosAgrupada().get(j - 1).getEmbalaje() + facAux.getListaLios().get(i).getEmbalaje()));
                    } else {
                        facAux.getListaLiosAgrupada().add(new Lios(facAux.getListaLios().get(i).getIdLineaLio(), facAux.getListaLios().get(i).getIdLio(), facAux.getListaLios().get(i).getEmbalaje(), facAux.getListaLios().get(i).getIdEmpaque(), facAux.getListaLios().get(i).getIdGrupo(), facAux.getListaLios().get(i).getIdProducto(), facAux.getListaLios().get(i).getProductoDescripcion(), facAux.getListaLios().get(i).getGrupoDescripcion(), facAux.getListaLios().get(i).getCodigoProducto(), facAux.getListaLios().get(i).getIdUnidadEmpaque(), facAux.getListaLios().get(i).getUnidadEmpaqueDescripcion(), facAux.getListaLios().get(i).getEstado(), facAux.getListaLios().get(i).getPeso(), facAux.getListaLios().get(i).getVolumen()));
                        j++;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception e" + e.toString());
            }
        }
    }

        /**
     * Este método invoca la inserción de la lista agrupada
     * en la capa de persistencia.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param facAux,  Cabecera de la lista de empaque
     */
    @Override
    public void postBillGroupList(Facturas facAux) {
        listaEmpaqueDao = new ListasEmpaqueFacDao();
        try {
            this.mtdDoesGroupList(facAux);
            listaEmpaqueDao.insertListPackingGroup(facAux);
        } catch (Exception ex) {
            Logger.getLogger(ListasEmpaqueBusinessBean.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

       /**
     * Este método genera un excel de acuerdo al formato del archivo BillEnv.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @param listBills,  lista de listad de empaque
     * @return excel de listas de empaque generado.
     */
    public HSSFWorkbook generateXLSDetailsList(List<Facturas> listBills) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Listas de Empaque");
        int k = 0;

        HSSFCellStyle style = workbook.createCellStyle();

        HSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setBold(true);

        HSSFFont fontBody = workbook.createFont();
        fontBody.setFontHeightInPoints((short) 11);
        fontBody.setBold(false);

        for (int i = 0; i < listBills.size(); i++) {
            if (listBills.get(i).getEstado() > 0) {
                style.setFont(fontHeader);
                style.setBorderTop(BorderStyle.NONE);
                style.setBorderBottom(BorderStyle.NONE);
                style.setBorderRight(BorderStyle.NONE);
                style.setBorderLeft(BorderStyle.NONE);
                style.setFillBackgroundColor(HSSFColor.WHITE.index);

                Row header = sheet.createRow(k++);
                Cell cell = header.createCell(0);
                cell.setCellStyle(style);
                cell.setCellValue("Orden:" + listBills.get(i).getNroFactura() + " - SubOrden:" + listBills.get(i).getSubNroFactura());
                cell.setCellStyle(style);

                style.setBorderTop(BorderStyle.THIN);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);

                header = sheet.createRow(k++);
                int l = 0;
                cell = header.createCell(l++);
                cell.setCellStyle(style);
                cell.setCellValue("Paquete");
                cell.setCellStyle(style);
                cell = header.createCell(l++);
                cell.setCellStyle(style);
                cell.setCellValue("Código Item");
                cell.setCellStyle(style);
                cell = header.createCell(l++);
                cell.setCellStyle(style);
                cell.setCellValue("Descripción Item");
                cell.setCellStyle(style);
                cell = header.createCell(l++);
                cell.setCellStyle(style);
                cell.setCellValue("Cantidad");
                cell.setCellStyle(style);

                style.setFont(fontBody);
                style.setBorderTop(BorderStyle.NONE);
                style.setBorderBottom(BorderStyle.NONE);
                style.setBorderRight(BorderStyle.NONE);
                style.setBorderLeft(BorderStyle.NONE);
                style.setFillBackgroundColor(HSSFColor.WHITE.index);

                for (int j = 0; j < listBills.get(i).getListaLios().size(); j++) {
                    l = 0;
                    Row dataRow = sheet.createRow(k);
                    Cell cellD = dataRow.createCell(l++);
                    cellD.setCellStyle(style);
                    cellD.setCellValue(listBills.get(i).getListaLios().get(j).getIdLio());

                    cellD = dataRow.createCell(l++);
                    cellD.setCellStyle(style);
                    cellD.setCellValue(listBills.get(i).getListaLios().get(j).getCodigoProducto());

                    cellD = dataRow.createCell(l++);
                    cellD.setCellStyle(style);
                    cellD.setCellValue(listBills.get(i).getListaLios().get(j).getProductoDescripcion());

                    cellD = dataRow.createCell(l++);
                    cellD.setCellStyle(style);
                    cellD.setCellValue(listBills.get(i).getListaLios().get(j).getEmbalaje());
                    k++;
                }
            }
        }
        return workbook;
    }
}
