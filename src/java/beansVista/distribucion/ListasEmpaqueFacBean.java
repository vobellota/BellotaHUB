/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion;

import beansNegocio.distribucion.ListasEmpaqueBusinessBean;
import dao.distribucion.ListasEmpaqueFacDao;
import beansVista.distribucion.models.Empaques;
import beansVista.distribucion.models.Facturas;
import beansVista.distribucion.models.Lios;
import beansVista.distribucion.listaEmpaqueAnexoBean;
import dao.distribucion.GruposCompatiblesDao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import beansVista.distribucion.models.ListasEmpaqueFac;
import beansVista.distribucion.models.ListasEmpaqueFacCabecera;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.Collections;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import static org.apache.poi.hssf.usermodel.HeaderFooter.date;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.data.PageEvent;
import sys.util.ExceptionManager;
import sys.util.Funciones;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "ListasEmpaqueFacBean")
public class ListasEmpaqueFacBean {
    /**
     * Creates a new instance of ListasEmpaqueFac
     */
    public Sesion sesion;
    private String mostrarSelecciones;

    // Variables originales 
    // ********************
    private String prefijoFactura;
    private String subOrdenF;
    private int numeroInicial;
    private int numeroFinal;
    private Date fechaInicio;
    private Date fechaFin;

    private List<ListasEmpaqueFac> listaEmpaqueConsultada;
    private List<ListasEmpaqueFac> listaEmpaqueSeleccionada;
    private List<ListasEmpaqueFacCabecera> listaEmpaqueCabeceras;
    private List<UnidadesEmpaque> listaUnidadesSinParame;
    private Facturas temporalFactura;
    private List<Facturas> facturaImprimir;
    private List<Facturas> facturaImprimirPreview;
    private List<Lios> listatemporalLios;
    private List<GruposCompatibles> listaGruposCompatibles;
    private Map<ListasEmpaqueFac, Boolean> checked;
    private ListasEmpaqueBusinessBean listasEmpaqueBusinessBean;
    private List<Facturas> listaFacturas;
//    private List<ListasEmpaqueFac> listaEmpaqueCabeceraExport;
    int facIt = 0;
    int contL = 0;
    private int alarmable;
    private String cadenaAlarma;

    public ListasEmpaqueFacBean() {
        listaEmpaqueConsultada = new ArrayList<ListasEmpaqueFac>();
        listaEmpaqueSeleccionada = new ArrayList<ListasEmpaqueFac>();
        facturaImprimirPreview = new ArrayList<Facturas>();
        listatemporalLios = new ArrayList<>();

        try {
            GruposCompatiblesDao grupocomptemp = new GruposCompatiblesDao(Sesion.getSesion());
            listaGruposCompatibles = grupocomptemp.select();
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
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        checked = new HashMap<ListasEmpaqueFac, Boolean>();
        sesion = Sesion.getSesion();
        listasEmpaqueBusinessBean = new ListasEmpaqueBusinessBean();
        listaFacturas = new ArrayList<>();

    }

    // ********************************
    // Encapsulamiento de las variables
    // ********************************
    public String getPrefijoFactura() {
        return prefijoFactura;
    }

    public void setPrefijoFactura(String prefijoFactura) {
        this.prefijoFactura = prefijoFactura;
    }

    public int getNumeroInicial() {
        return numeroInicial;
    }

    public void setNumeroInicial(int numeroInicial) {
        this.numeroInicial = numeroInicial;
    }

    public int getNumeroFinal() {
        return numeroFinal;
    }

    public void setNumeroFinal(int numeroFinal) {
        this.numeroFinal = numeroFinal;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    
    /**
     * @return the subOrdenF
     */
    public String getSubOrdenF() {
        return subOrdenF;
    }

    /**
     * @param subOrdenF the subOrdenF to set
     */
    public void setSubOrdenF(String subOrdenF) {
        this.subOrdenF = subOrdenF;
    }

    public String getMostrarSelecciones() {
        return mostrarSelecciones;
    }

    public void setMostrarSelecciones(String mostrarSelecciones) {
        this.mostrarSelecciones = mostrarSelecciones;
    }

    public List<ListasEmpaqueFac> getListaEmpaqueConsultada() {
        return listaEmpaqueConsultada;
    }

    public void setListaEmpaqueConsultada(List<ListasEmpaqueFac> listaEmpaqueConsultada) {
        this.listaEmpaqueConsultada = listaEmpaqueConsultada;
    }

//    public List<ListasEmpaqueFac> getListaEmpaqueCabeceraExport() {
//        return listaEmpaqueCabeceraExport;
//    }
//
//    public void setListaEmpaqueCabeceraExport(List<ListasEmpaqueFac> listaEmpaqueCabeceraExport) {
//        this.listaEmpaqueCabeceraExport = listaEmpaqueCabeceraExport;
//    }
    /**
     * @return the listaEmpaqueSeleccionada
     */
    public List<ListasEmpaqueFac> getListaEmpaqueSeleccionada() {
        return listaEmpaqueSeleccionada;
    }

    /**
     * @param listaEmpaqueSeleccionada the listaEmpaqueSeleccionada to set
     */
    public void setListaEmpaqueSeleccionada(List<ListasEmpaqueFac> listaEmpaqueSeleccionada) {
        this.listaEmpaqueSeleccionada = listaEmpaqueSeleccionada;
    }

    public Facturas getTemporalFactura() {
        return temporalFactura;
    }

    public List<ListasEmpaqueFacCabecera> getListaEmpaqueCabeceras() {
        return listaEmpaqueCabeceras;
    }

    public void setListaEmpaqueCabeceras(List<ListasEmpaqueFacCabecera> listaEmpaqueCabeceras) {
        this.listaEmpaqueCabeceras = listaEmpaqueCabeceras;
    }

    public void setTemporalFactura(Facturas temporalFactura) {
        this.temporalFactura = temporalFactura;
    }

    public List<Facturas> getFacturaImprimir() {
        return facturaImprimir;
    }

    public void setFacturaImprimir(List<Facturas> facturaImprimir) {
        this.facturaImprimir = facturaImprimir;
    }

    public List<Lios> getListatemporal() {
        return listatemporalLios;
    }

    public void setListatemporal(List<Lios> listatemporalLios) {
        this.listatemporalLios = listatemporalLios;
    }

    public List<GruposCompatibles> getListaGruposCompatibles() {
        return listaGruposCompatibles;
    }

    public void setListaGruposCompatibles(List<GruposCompatibles> listaGruposCompatibles) {
        this.listaGruposCompatibles = listaGruposCompatibles;
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

    /**
     * @return the checked
     */
    public Map<ListasEmpaqueFac, Boolean> getChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(Map<ListasEmpaqueFac, Boolean> checked) {
        this.checked = checked;
    }

    public List<Facturas> getFacturaImprimirPreview() {
        return facturaImprimirPreview;
    }

    public void setFacturaImprimirPreview(List<Facturas> facturaImprimirPreview) {
        this.facturaImprimirPreview = facturaImprimirPreview;
    }

    // *****************
    // Logica en general
    // *****************
    public void onSearchinReal() {
        this.getChecked().clear();
        listaEmpaqueConsultada = listasEmpaqueBusinessBean.onConvertVariables(prefijoFactura, subOrdenF, numeroInicial, numeroFinal, fechaInicio, fechaFin);
    }

    /**
     * Metodo ajax para cambiar cuando el select haga on change
     */
    public void onChangeSelect(AjaxBehaviorEvent event) {
        String cadena = (String) ((javax.faces.component.html.HtmlSelectOneMenu) event.getSource()).getValue();
        setMostrarSelecciones(cadena);
    }

    /**
     * Este método obtiene las facturas marcadas en el check de paqueteo y se
     * generan las listas de empaque a partir de dicha selección
     */
    public void onSelectedBills() {
        try {
            listaEmpaqueSeleccionada.clear();
            listaFacturas.clear();
            if (getChecked().isEmpty()) {
                ExceptionManager.addError("Debe seleccionar al menos una factura");
            } else {
                // Se contruye una lista con las facturas seleccionadas
                // a partir de un objeto de un Map identificando las claves únicas
                // entre todo el listado de facturas
                Iterator<Map.Entry<ListasEmpaqueFac, Boolean>> it = getChecked().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<ListasEmpaqueFac, Boolean> ent = it.next();
                    if (ent.getValue()) {
                        listaEmpaqueSeleccionada.add(ent.getKey());
                    }
                }
                //Inovación al método de ordenamiento de facturas
                listasEmpaqueBusinessBean.quickSortBillSelected(listaEmpaqueSeleccionada, 0, listaEmpaqueSeleccionada.size() - 1);

                //Se invoca el método inicial de generación de listas de empaque
                this.setListaFacturas(listasEmpaqueBusinessBean.getBillsLios(listaEmpaqueSeleccionada));

                //Se calcula el peso y volumen de cada lista de empaque
                for (int i = 0; i < this.getListaFacturas().size(); i++) {
                    this.getListaFacturas().get(i).setPesoTotal(0);
                    this.getListaFacturas().get(i).setVolumenTotal(0);
                    for (int j = 0; j < this.getListaFacturas().get(i).getListaLios().size(); j++) {
                        this.getListaFacturas().get(i).setPesoTotal(this.getListaFacturas().get(i).getPesoTotal() + this.getListaFacturas().get(i).getListaLios().get(j).getPeso());
                        this.getListaFacturas().get(i).setVolumenTotal(this.getListaFacturas().get(i).getVolumenTotal() + this.getListaFacturas().get(i).getListaLios().get(j).getVolumen());
                    }

                }

            }

            Sesion.getSesion().getParametros().put("" + this.hashCode(), getListaFacturas());
            temporalFactura = getListaFacturas().get(0);
            this.changeStateButtons(0);
            //  return "imprimirProforma";
        } catch (Exception e) {
            System.out.println("e2: " + e.toString());
        }
    }

    /**
     * Este método obtiene las facturas marcadas en el check de paqueteo y se
     * generan las listas de empaque a partir de dicha selección
     */
    public void onSelectCreatedBills() {
        listaEmpaqueSeleccionada.clear();
        listaFacturas.clear();
        if (getChecked().isEmpty()) {
            ExceptionManager.addError("Debe seleccionar al menos una factura");
        } else {
            // Se contruye una lista con las facturas seleccionadas
            // a partir de un objeto de un Map identificando las claves únicas
            // entre todo el listado de facturas
            Iterator<Map.Entry<ListasEmpaqueFac, Boolean>> it = getChecked().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<ListasEmpaqueFac, Boolean> ent = it.next();
                if (ent.getValue()) {
                    listaEmpaqueSeleccionada.add(ent.getKey());
                }
            }
            //Inovación al método de ordenamiento de facturas
            listasEmpaqueBusinessBean.quickSortBillSelected(listaEmpaqueSeleccionada, 0, listaEmpaqueSeleccionada.size() - 1);
            
            //Se invoca el método consulta de listas de empaque generadas
            this.setListaFacturas(listasEmpaqueBusinessBean.getCreatedBillsLios(listaEmpaqueSeleccionada));
        }

        Sesion.getSesion().getParametros().put("" + this.hashCode(), getListaFacturas());
        temporalFactura = getListaFacturas().get(0);
        this.changeStateButtons(0);
    }

    /**
     * @return the listasEmpaqueBusinessBean
     */
    public ListasEmpaqueBusinessBean getListasEmpaqueBusinessBean() {
        return listasEmpaqueBusinessBean;
    }

    /**
     * @param listasEmpaqueBusinessBean the listasEmpaqueBusinessBean to set
     */
    public void setListasEmpaqueBusinessBean(ListasEmpaqueBusinessBean listasEmpaqueBusinessBean) {
        this.listasEmpaqueBusinessBean = listasEmpaqueBusinessBean;
    }

    public List<Facturas> getListaFacturas() {
        return listaFacturas;
    }

    public void setListaFacturas(List<Facturas> listaFacturas) {
        this.listaFacturas = listaFacturas;
    }

    public void onRowEdit(RowEditEvent event) {
//        try{
//            EstibasDao objsave = new EstibasDao(Sesion.getSesion());
//            objsave.update((Estibas) event.getObject());
//            // Mensaje de adicion
//            ExceptionManager.addInfo("Se ha Editado exitosamente");
//        } catch(SQLException ex){
//            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
//            ExceptionManager.addError(ex.getMessage());
//        }
    }

    public void onRowCancel(RowEditEvent event) {
//        // Mensaje de adicion
//        ExceptionManager.addError("Se ha cancelado la edición del registro");
    }

    /**
     * Este método recibe la instrucción y datos de modifivcación de un lío
     */
    public void onCellEditChangeLio(CellEditEvent event) {
        contL++;
        int index = event.getRowIndex();

        System.out.println("index: " + index);
        FacesContext context = FacesContext.getCurrentInstance();
        temporalFactura = context.getApplication().evaluateExpressionGet(context, "#{listafacturas}", Facturas.class);
        Lios lioTemp = context.getApplication().evaluateExpressionGet(context, "#{listalios}", Lios.class);
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:datatablelios");

        if (newValue != null && !newValue.equals(oldValue)) {

            for (int i = 0; i < listaFacturas.size(); i++) {
                if (listaFacturas.get(i).getNroFactura().equals(temporalFactura.getNroFactura())) {
                    facIt = i;
                }
            }
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form_lio:datatablefacturas:datatablelios");

            // this.mtdChangePackageOfLioGroup(facIt, Integer.parseInt(oldValue.toString()), Integer.parseInt(newValue.toString()), lioTemp);
        }

    }

     /**
     * Este método cambia un  paquete de lío a partir del id del lío
     * 
     */
    public void mtdChangePackageOfLioGroup(int facIt, int oldValue, int newValue, Lios empaqueAux) {
        try {
            int i = 0;
            boolean packExist = false;
            while (listaFacturas.get(facIt).getListaLiosAgrupada().get(i).getIdLio() != oldValue) {
                i++;
            }
            int j = 0;
            while (listaFacturas.get(facIt).getListaLiosAgrupada().get(j).getIdLio() != newValue) {
                j++;
            }

            if (listaFacturas.get(facIt).getListaLiosAgrupada().get(i).getCodigoProducto().equals(empaqueAux.getCodigoProducto())) {
                listaFacturas.get(facIt).getListaLiosAgrupada().get(i).setEmbalaje(listaFacturas.get(facIt).getListaLiosAgrupada().get(i).getEmbalaje() + empaqueAux.getEmbalaje());
                listaFacturas.get(facIt).getListaLiosAgrupada().get(j).setEmbalaje(listaFacturas.get(facIt).getListaLiosAgrupada().get(j).getEmbalaje() - empaqueAux.getEmbalaje());
                if (listaFacturas.get(facIt).getListaLiosAgrupada().get(j).getEmbalaje() == 0) {
                    listaFacturas.get(facIt).getListaLiosAgrupada().remove(j);
                }
                packExist = true;
            }

            if (!packExist) {
                listaFacturas.get(facIt).getListaLiosAgrupada().add(empaqueAux);
            }
        } catch (Exception e) {

        }
    }

     /**
     * Este método invoca al método de negocio para el guardado de la lista de empaque
     * 
     */
    public void guardarListaEmpaque() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (this.temporalFactura.getIdListaEmpaque() < 1) {
            if (listasEmpaqueBusinessBean.postGuardarListaEmpaque(this.temporalFactura)) {
                context.addMessage(null, new FacesMessage("", "Se ha guardado con éxito la lista de empaque"));
            } else {
                context.addMessage(null, new FacesMessage("Error", "ha ocurrido un conflicto al guardar la lista de empaque"));
            }
        } else {
            listasEmpaqueBusinessBean.updateBillAllLines(this.temporalFactura);
            context.addMessage(null, new FacesMessage("", "Se ha guardado con éxito la lista de empaque"));
        }
        this.temporalFactura.getListaLiosExluidos().clear();
        this.changeStateButtons(-1);
    }

      /**
     * Este método invoca al método de negocio para el ordenamiento de los 
     * líos de una la lista de empaque
     * 
     */
    public void reordenarLista() {

        listasEmpaqueBusinessBean.quickSortLios(listaFacturas.get(facIt).getListaLios(), 0, listaFacturas.get(facIt).getListaLios().size() - 1);
        listasEmpaqueBusinessBean.changeIdLios(listaFacturas.get(facIt).getListaLios());

        // listasEmpaqueBusinessBean.quickSortLios(listaFacturas.get(facIt).getListaLiosAgrupada(), 0, listaFacturas.get(facIt).getListaLios().size() - 1);
        //listasEmpaqueBusinessBean.changeIdLios(listaFacturas.get(facIt).getListaLiosAgrupada());
        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:datatablelios");
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form_lio:datatablefacturas:datatablelios");
        facIt = 0;
        this.changeStateButtons(-1);
    }
    
     /**
     * Este método invoca al método de negocio para excluir  líneas 
     * de la lista de empaque
     * 
     */
    public void eliminarDatosSeleccion() {
        List<Integer> posiciones = new ArrayList<>();

        for (int i = 0; i < listaFacturas.size(); i++) {
            if (listaFacturas.get(i).getNroFactura().equals(temporalFactura.getNroFactura())) {
                facIt = i;
            }
        }

        for (int i = 0; i < this.listaFacturas.size(); i++) {
            if (this.listaFacturas.get(i).equals(this.temporalFactura)) {
                for (int j = this.listaFacturas.get(i).getListaLios().size() - 1; j >= 0; j--) {
                    if (this.listatemporalLios.contains(this.listaFacturas.get(i).getListaLios().get(j))) {
                        this.listaFacturas.get(i).getListaLios().get(j).setEstado(1);
                        this.listaFacturas.get(i).getListaLiosExluidos().add(this.listaFacturas.get(i).getListaLios().get(j));
                        this.listaFacturas.get(i).setPesoTotal(this.listaFacturas.get(i).getPesoTotal() - this.listaFacturas.get(i).getListaLios().get(j).getPeso());
                        this.listaFacturas.get(i).setVolumenTotal(this.listaFacturas.get(i).getVolumenTotal() - this.listaFacturas.get(i).getListaLios().get(j).getVolumen());
                        this.listaFacturas.get(i).getListaLios().remove(j);
                        //   this.temporalFactura.getListaLios().remove(j);
                    }
                }
                this.listaFacturas.get(i).setTotalPaquetes(this.listaFacturas.get(i).getTotalPaquetes() - this.listatemporalLios.size());
            }

        }

        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:datatablelios");
        this.reordenarLista();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("", "Se ha completado el proceso de eliminación"));
        this.listatemporalLios = new ArrayList<>();
        facIt = 0;
        this.changeStateButtons(-1);

    }

     /**
     * Este método invoca al método de negocio para cambiar el estado
     * de la lista de empaque
     * 
     */
    public void changeBillHeaderState() {
        FacesContext context = FacesContext.getCurrentInstance();
        for (int i = 0; i < listaFacturas.size(); i++) {
            if (listaFacturas.get(i).equals(temporalFactura)) {
                if (listasEmpaqueBusinessBean.putBillHeaderState(listaFacturas.get(i))) {
                    listasEmpaqueBusinessBean.postBillGroupList(listaFacturas.get(i));
                    temporalFactura.setEstado(1);
                    context.addMessage(null, new FacesMessage("", "Se ha cerrado la lista de Empaque"));
                } else {
                    context.addMessage(null, new FacesMessage("", "Error al cerrar la lista de Empaque"));
                }
            }
        }
        this.changeStateButtons(-1);
    }

     /**
     * Este método permite visualizar la lista de empaque agrupada antes
     * de ser cerrada
     * 
     */
    public void preview() {
        facturaImprimirPreview.clear();
        temporalFactura.getListaLiosAgrupada().clear();
        FacesContext context = FacesContext.getCurrentInstance();
        // temporalFactura       
        for (int i = 0; i < listaFacturas.size(); i++) {
            if (listaFacturas.get(i).equals(temporalFactura)) {
                //   listasEmpaqueBusinessBean.mtdDoesGroupList(listaFacturas.get(i));       
                listasEmpaqueBusinessBean.mtdDoesGroupList(temporalFactura);
                facturaImprimirPreview.add(temporalFactura);
            }
        }
    }

    public void facturasImprimir() {
        try {
            RequestContext.getCurrentInstance().execute("window.open('http://" + FacesContext.getCurrentInstance().getExternalContext().getRequestServerName() + ":" + FacesContext.getCurrentInstance().getExternalContext().getRequestServerPort() + "/BellotaHUB/HUB/Distribucion/ListasDeEmpaque/ListaEmpaqueAnexo.xhtml?idlistaempaque=" + temporalFactura.getIdListaEmpaque() + "')");
        } catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    
     /**
     * Este método habilita o inhabilita los botones Guardar, Imprimir, Finalizar,etc.
     * De acuerdo al estado de la lista de empaque
     * 
     */
    public void changeStateButtons(int i) {
        if (i == -1) {
            i = 0;
            while (i < listaFacturas.size()) {
                if (listaFacturas.get(i).equals(temporalFactura)) {
                    break;
                } else {
                    i++;
                }
            }
        }
        if (listaFacturas.get(i).getEstado() == 1) {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_eliminar\").disabled = true;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_eliminar\").style.opacity = 0.7;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_eliminar\").style.cursor = \"unset\";");
        } else {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_eliminar\").disabled = false;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_eliminar\").style.opacity = 1;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_eliminar\").style.cursor = \"pointer\";");
        }

        if (listaFacturas.get(i).getEstado() == 1) {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_refrescar\").disabled = true;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_refrescar\").style.opacity = 0.7;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_refrescar\").style.cursor = \"unset\";");
        } else {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_refrescar\").disabled = false;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_refrescar\").style.opacity = 1;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_refrescar\").style.cursor = \"pointer\";");
        }

        if (listaFacturas.get(i).getEstado() == 1) {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_guardar\").disabled = true;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_guardar\").style.opacity = 0.7;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_guardar\").style.cursor = \"unset\";");
        } else {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_guardar\").disabled = false;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_guardar\").style.opacity = 1;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_guardar\").style.cursor = \"pointer\";");
        }

        if (listaFacturas.get(i).getEstado() == 1) {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").disabled = true;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").style.opacity = 0.7;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").style.cursor = \"unset\";");
        } else {
            if (listaFacturas.get(i).getIdListaEmpaque() > 0) {
                RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").disabled = false;");
                RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").style.opacity = 1;");
                RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").style.cursor = \"pointer\";");
            } else {
                RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").disabled = true;");
                RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").style.opacity = 0.7;");
                RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_finalizar\").style.cursor = \"unset\";");
            }
        }

        if (listaFacturas.get(i).getEstado() == 1) {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_imprimir\").disabled = false;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_imprimir\").style.opacity = 1;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_imprimir\").style.cursor = \"pointer\";");
        } else {
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_imprimir\").disabled = true;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_imprimir\").style.opacity = 0.7;");
            RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_imprimir\").style.cursor = \"unset\";");
        }
        RequestContext.getCurrentInstance().execute("document.getElementById(\"form_lio:datatablefacturas:" + i + ":btn_lista_lios_refrescar\").click();");

        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:btn_lista_lios_eliminar");
        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:btn_lista_lios_refrescar");
        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:btn_lista_lios_guardar");
        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:btn_lista_lios_finalizar");
        RequestContext.getCurrentInstance().update("form_lio:datatablefacturas:btn_lista_lios_imprimir");
    }

     /**
     * Este método invoca al método de negocio para generar el Excel de listas
     * de empaque
     * 
     */
    public void getGenerateXls() {
        try {
            Date fecha = new Date();
            String strDateFormat = "yyyy-MM-dd_hh-mm-ss";
            SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

            //String filename = "FacturaXEmpaque" + objSDF.format(fecha) + ".xls";
            String filename = "OrdenXEmpaque.xls";
            HSSFWorkbook workbook = this.listasEmpaqueBusinessBean.generateXLSDetailsList(listaFacturas);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();
        } catch (Exception sqle) {
            sqle.printStackTrace();
        }

    }

    public void exportarExcel() throws SQLException {
        ListasEmpaqueFacDao listasEmpa = new ListasEmpaqueFacDao();
        listaEmpaqueCabeceras = listasEmpa.exportarExcelDao(listaEmpaqueSeleccionada);

    }

    public void onPageChangeListFacturas(PageEvent event) {
        //DataTable dt = (DataTable) event.getSource();
        changeStateButtons(event.getPage());
    }

     public void onClearData() {
        try {
            listaEmpaqueSeleccionada.clear();
            listaEmpaqueConsultada.clear();
            listaFacturas.clear();
            numeroInicial = 0;
            numeroFinal = 0;
            this.changeStateButtons(0);
            //  return "imprimirProforma";
        } catch (Exception e) {
            System.out.println("e2: " + e.toString());
        }
    }
}
