/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.financiera;

import dao.financiera.PUCDaoDB2;
import entidades.financiera.PUC;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.event.CellEditEvent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
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
@ManagedBean(name = "PUCBean")
public class PUCBean {

    /**
     * Creates a new instance of PUCBean
     */
    private List<PUC> listaCuentas;
    private List<PUC> filteredListaCuentas;
    private PUC nuevaCuenta = new PUC();
    private PUC selectPucRow;
    private StreamedContent filePUC;

    public PUCBean() {
        // Lineas para generar el PDF que se descarga como manual de usuario
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/manuales/PUC_MANUAL_USUARIO.pdf");
        filePUC = new DefaultStreamedContent(stream, "application/pdf", "PUC_MANUAL_USUARIO.pdf");
        
        try {
            PUCDaoDB2 pucdaotemp = new PUCDaoDB2(Sesion.getSesion());
            listaCuentas = pucdaotemp.select();
            //listaCuentas = new PUCDaoDB2().select();

        } catch (SQLException ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public PUC getNuevaCuenta() {
        return nuevaCuenta;
    }

    public void setNuevaCuenta(PUC nuevaCuenta) {
        this.nuevaCuenta = nuevaCuenta;
    }

    public List<PUC> getListaCuentas() {
        return listaCuentas;
    }

    public void setListaCuentas(List<PUC> listaCuentas) {
        this.listaCuentas = listaCuentas;
    }
    
    public List<PUC> getFilteredListaCuentas() {
        return filteredListaCuentas;
    }

    public void setFilteredListaCuentas(List<PUC> filteredListaCuentas) {
        this.filteredListaCuentas = filteredListaCuentas;
    }
    
    // Seleccion de una row puntual Get y Set
    // **************************************
    
    public PUC getSelectPucRow() {
        return selectPucRow;
    }

    public void setSelectPucRow(PUC selectPucRow) {
        this.selectPucRow = selectPucRow;
    }
    
    public void onRowEdit(RowEditEvent event) {
        try {
            PUCDaoDB2 obtsave = new PUCDaoDB2(Sesion.getSesion());
            obtsave.update((PUC) event.getObject());
            // limpiar el obj para cargar lo que nercesito
            listaCuentas.clear();
            listaCuentas = obtsave.select();
            // Limpiar los Filtros
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('pucTable').clearFilters()");
            // Mensaje de accion
            ExceptionManager.addInfo("Registro Editado con Exito");
        } catch (SQLException ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }

    }

    public void onRowCancel(RowEditEvent event) {
        ExceptionManager.addError("Se canceló la edición del Registro");
    }

    public void onStoring() {
        try{
            PUCDaoDB2 objsave = new PUCDaoDB2(Sesion.getSesion());
            listaCuentas.clear();
            objsave.insert(nuevaCuenta);
            // *************************
            listaCuentas = objsave.select();
            // *************************
            ExceptionManager.addInfo("Registro Adicionado con Exito");
        } catch(SQLException ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
        
    }
    
    public void onSelectedOne(){
        try{
            PUCDaoDB2 objsave = new PUCDaoDB2(Sesion.getSesion());
            if(objsave.select_one(nuevaCuenta) > 0){
                // Mensaje de excepcion para no guardar.
                ExceptionManager.addError("Ya existe un registro con estos datos, por favor verifiquelo de nuevo");
            } else {
                // Llamado del metodo para guardarlo
                onStoring();
            }
        } catch(SQLException ex){
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // Aceptar de la ventana modal
    // ****************************
    
    public void onDeletedAcepted(){
        try{
            PUCDaoDB2 objtemp = new PUCDaoDB2(Sesion.getSesion());
            objtemp.delete(selectPucRow);
            listaCuentas.remove(selectPucRow);
            // Limpiar los Filtros
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('pucTable').clearFilters()");
            // Mensaje de accion
            ExceptionManager.addInfo("Registro Eliminado con Exito");
        } catch(SQLException ex){
            Logger.getLogger(RESDIANBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
     
    public StreamedContent getFilePUC() {
        return filePUC;
    }

}
