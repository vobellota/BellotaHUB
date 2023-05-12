/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.financiera;

import dao.financiera.NOTASDEBITODaoBD2;
import entidades.financiera.NOTASDEBITO;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sys.util.ExceptionManager;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "NOTASDEBITOBean")
public class NOTASDEBITOBean {

    private List<NOTASDEBITO> listaCuentas;
    private List<NOTASDEBITO> filteredListaCuentas;
    private Map<String, NOTASDEBITO> filtersGlobally;
    private Date fechaVenc = new Date();
    private int fechaVencEntero;
    private StreamedContent fileNOTASDEBITO;

    // Constructor
    // ***********
    public NOTASDEBITOBean() {
        // Lineas para generar el PDF que se descarga como manual de usuario
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/manuales/NOTASDEBITO_MANUAL_USUARIO.pdf");
        fileNOTASDEBITO = new DefaultStreamedContent(stream, "application/pdf", "NOTASDEBITO_MANUAL_USUARIO.pdf");
        
        try {
            NOTASDEBITODaoBD2 notasdebitotemp = new NOTASDEBITODaoBD2(Sesion.getSesion());
            listaCuentas = notasdebitotemp.select();
        } catch (SQLException ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public List<NOTASDEBITO> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<NOTASDEBITO> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }
    
    public List<NOTASDEBITO> getFilteredListaCuentas() {
        return filteredListaCuentas;
    }

    public void setFilteredListaCuentas(List<NOTASDEBITO> filteredListaCuentas) {
        this.filteredListaCuentas = filteredListaCuentas;
    }
    
    public Map<String, NOTASDEBITO> getFiltersGlobally() {
        return filtersGlobally;
    }

    public void setFiltersGlobally(Map<String, NOTASDEBITO> filtersGlobally) {
        this.filtersGlobally = filtersGlobally;
    }

    public Date getFechaVenc() {
        return fechaVenc;
    }

    public void setFechaVenc(Date fechaVenc) {
        this.fechaVenc = fechaVenc;
    }

    public int getFechaVencEntero() {
        return fechaVencEntero;
    }

    public void setFechaVencEntero(int fechaVencEntero) {
        this.fechaVencEntero = fechaVencEntero;
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            convertDateToInt(fechaVenc);
            NOTASDEBITODaoBD2 objsave = new NOTASDEBITODaoBD2(Sesion.getSesion());
            objsave.update((NOTASDEBITO) event.getObject(), fechaVencEntero);
            // limpiar el obj para cargar lo que nercesito
            listaCuentas.clear();
            listaCuentas = objsave.select();
            // Limpiar los Filtros
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('NOTASDEBITOTable').clearFilters()");
            // Mensaje de accion
            ExceptionManager.addInfo("Registro Editado con Exito");
        } catch (SQLException ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    // Metodo personalizado para acceder a la fecha tal cual la necesito
    // *****************************************************************
    public String convertDateToInt(Date fechaVenc) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String enterofechaVenc = df.format(fechaVenc);
        setFechaVencEntero(Integer.valueOf(enterofechaVenc));
        return enterofechaVenc;
    }

    public void onRowCancel(RowEditEvent event) {
        ExceptionManager.addWarning("Se canceló la edición del Registro");
    }

    public Map<String, NOTASDEBITO> onFilter(AjaxBehaviorEvent event) {
//        System.out.println("Filter called");
        DataTable table = (DataTable) event.getSource();
//        System.out.println("filter value------ " + table.getFilters().size());

        /*List<NOTASDEBITO> obj =   table.getFilteredValue();
        for(NOTASDEBITO obj1 : obj){
            System.out.println("Account Number after filter ----" + obj1.getRcust());
        }*/
        Map<String, NOTASDEBITO> filters = (Map<String, NOTASDEBITO>) ((Object) table.getFilters());

//        for (Map.Entry<String, NOTASDEBITO> entry : filters.entrySet()) {
//            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
//        }
        setFiltersGlobally(filters);
        return filters;
    }
    
    public StreamedContent getFileNOTASDEBITO() {
        return fileNOTASDEBITO;
    }
}
