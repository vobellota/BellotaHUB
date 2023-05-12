/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.financiera;

import dao.financiera.CXPDaoDB2;
import entidades.financiera.CXP;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
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
@ManagedBean(name = "CXPBean")
public class CXPBean {
    
    /**
     * Creates a new instance of CXPBean
     */
    private List<CXP> listaCuentasx;
    private List<CXP> filteredListaCuentasx;
    private CXP nuevaCxp        = new CXP();
    private CXP nuevaCxpInversa = new CXP();
    private CXP selectCxpRow;
    private double tasaRealInvert;
    private double tasaPresupInvert;
    private double tasaForecaInvert;
    private String terminal;
    private String sesionUsuario;
    private int fechaInsercion;
    private int horaInsercion;
    private String tasaInv = "Si";
    private StreamedContent fileCXP;
    
    public CXPBean(){
        // Lineas para generar el PDF que se descarga como manual de usuario
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/manuales/TASAS_CAMBIO_MANUAL_USUARIO.pdf");
        fileCXP = new DefaultStreamedContent(stream, "application/pdf", "TASAS_CAMBIO_MANUAL_USUARIO.pdf");
        try{
            CXPDaoDB2 cxpdaotemp = new CXPDaoDB2(Sesion.getSesion());
            listaCuentasx = cxpdaotemp.select();
            // Valor por defecto para una var
            // ******************************
            nuevaCxp.setCmid("CM");
            nuevaCxp.setCmwsid("BELLOTAHUB");
            nuevaCxp.setCmwusr(Sesion.getSesion().getFormUser());
            // aca esta el error 
            nuevaCxp.setCmdat(getFechaActual());
            nuevaCxp.setCmwtim(getHoraActual());
        } catch(SQLException ex){
            Logger.getLogger(CXPBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public CXP getNuevaCxp() {
        return nuevaCxp;
    }

    public void setNuevaCxp(CXP nuevaCxp) {
        this.nuevaCxp = nuevaCxp;
    }
    
    public List<CXP> getListaCuentasx() {
        return listaCuentasx;
    }

    public void setListaCuentasx(List<CXP> listaCuentasx) {
        this.listaCuentasx = listaCuentasx;
    }
    
    public List<CXP> getFilteredListaCuentasx() {
        return filteredListaCuentasx;
    }

    public void setFilteredListaCuentasx(List<CXP> filteredListaCuentasx) {
        this.filteredListaCuentasx = filteredListaCuentasx;
    }
    
    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getSesionUsuario() {
        return sesionUsuario;
    }

    public void setSesionUsuario(String sesionUsuario) {
        this.sesionUsuario = sesionUsuario;
    }

    public int getFechaInsercion() {
        return fechaInsercion;
    }

    public void setFechaInsercion(int fechaInsercion) {
        this.fechaInsercion = fechaInsercion;
    }

    public int getHoraInsercion() {
        return horaInsercion;
    }

    public void setHoraInsercion(int horaInsercion) {
        this.horaInsercion = horaInsercion;
    }
    
    public String getTasaInv() {
        return tasaInv;
    }

    public void setTasaInv(String tasaInv) {
        this.tasaInv = tasaInv;
    }
    
    public double getTasaRealInvert() {
        return tasaRealInvert;
    }

    public void setTasaRealInvert(double tasaRealInvert) {
        this.tasaRealInvert = tasaRealInvert;
    }

    public double getTasaPresupInvert() {
        return tasaPresupInvert;
    }

    public void setTasaPresupInvert(double tasaPresupInvert) {
        this.tasaPresupInvert = tasaPresupInvert;
    }

    public double getTasaForecaInvert() {
        return tasaForecaInvert;
    }

    public void setTasaForecaInvert(double tasaForecaInvert) {
        this.tasaForecaInvert = tasaForecaInvert;
    }
    
    // Seleccion de una row puntual Get y Set
    // **************************************
    
    public CXP getSelectCxpRow() {
        return selectCxpRow;
    }

    public void setSelectCxpRow(CXP selectCxpRow) {
        this.selectCxpRow = selectCxpRow;
    }
    
    public CXP getNuevaCxpInversa() {
        return nuevaCxpInversa;
    }

    public void setNuevaCxpInversa(CXP nuevaCxpInversa) {
        this.nuevaCxpInversa = nuevaCxpInversa;
    }
    
    public void onRowEdit(RowEditEvent event) {
        try {
           CXPDaoDB2 objsave = new CXPDaoDB2(Sesion.getSesion());
           objsave.update((CXP) event.getObject());
           // Actualizar tasa inversa
           objsave.select_one_to_update((CXP) event.getObject());
           // Consultar de nuevo el OBJ
           listaCuentasx.clear();
           listaCuentasx = objsave.select();
           // Limpiar los Filtros
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('tasasTable').clearFilters()");
            // Mensaje de accion
           ExceptionManager.addInfo("Tasa actualizada con Exito");
           ExceptionManager.addWarning("Tasa inversa actualizada con Exito");
        } catch (SQLException ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        ExceptionManager.addError("Edicion Cancelada");
    }
    
    public void onStoring(){
        try{
            // Condicional si la tasa de cambio inversa es necesario crearla
            // **************************************************************
            if(tasaInv == "No"){
                CXPDaoDB2 objsave = new CXPDaoDB2(Sesion.getSesion());
                objsave.insert(nuevaCxp);
            } else {
                // Objeto de la tasa Normal
                // ************************
                CXPDaoDB2 objsave = new CXPDaoDB2(Sesion.getSesion());
                objsave.insert(nuevaCxp);
                // Ingreso de los datos para el nuevo objeto y su inserción
                // ********************************************************
                nuevaCxpInversa.setCmid("CM");
                nuevaCxpInversa.setCmfrc(nuevaCxp.getCmtoc());
                nuevaCxpInversa.setCmtoc(nuevaCxp.getCmfrc());
                nuevaCxpInversa.setCmdat(nuevaCxp.getCmdat());
                nuevaCxpInversa.setCmexc(tasaRealInvert);
                nuevaCxpInversa.setCmexp(tasaPresupInvert);
                nuevaCxpInversa.setCmexf(tasaForecaInvert);
                nuevaCxpInversa.setCmwsid("BELLOTAHUB");
                nuevaCxpInversa.setCmwusr(Sesion.getSesion().getFormUser());
                nuevaCxpInversa.setCmwdat(nuevaCxp.getCmwdat());
                nuevaCxpInversa.setCmwtim(nuevaCxp.getCmwtim());
                // Objeto de la tasa inversa
                // *************************
                CXPDaoDB2 objsaveInv = new CXPDaoDB2(Sesion.getSesion());
                // Agregar a la lista el nuevo registro
                // Para que pueda ser renderizado en la 
                // Datatable correctamente con el :updtae
//                listaCuentasx.add(nuevaCxpInversa);
                listaCuentasx.clear();
                // Llamado a insertar en el DAO
                objsaveInv.insert(nuevaCxpInversa);
                listaCuentasx = objsaveInv.select();
            }
            ExceptionManager.addInfo("Registro Adicionado con Exito");
        } catch(SQLException ex){
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public void onSelectedOne(){
        try{
            CXPDaoDB2 objsave = new CXPDaoDB2(Sesion.getSesion());
            if(objsave.select_one(nuevaCxp) > 0){
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
    
    // Metodo personalizado para acceder a la fecha tal cual la necesito
    // *****************************************************************
    public static int getFechaActual(){
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("YYYYMM");
        return Integer.parseInt(formateador.format(fechaActual));
    }
    
    // Metodo personalizado para acceder a la fecha tal cual la necesito
    // *****************************************************************
    public static int getHoraActual(){
        Date horaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("hmmss");
        return Integer.parseInt(formateador.format(horaActual));
    }
    
    // Aceptar de la ventana modal
    // ****************************
    
    public void onDeleteAcepted(){
        try{
            listaCuentasx.clear();
            CXPDaoDB2 objtemp = new CXPDaoDB2(Sesion.getSesion());
            objtemp.delete(selectCxpRow);
            // Obj de CXP}
            listaCuentasx = objtemp.select();
            // Limpiar los Filtros
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('tasasTable').clearFilters()");
            // Mensaje de accion
            ExceptionManager.addInfo("Registro Eliminado con Exito");
        } catch(SQLException ex){
            Logger.getLogger(CXPBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // Metodo ajax para cambiar cuando el select haga on change
    // ********************************************************
    public void onChangeSelect(AjaxBehaviorEvent event){
        String cadena = (String) ((javax.faces.component.html.HtmlSelectOneMenu) event.getSource()).getValue();
        setTasaInv(cadena);
    }
    
    // Metodos Ajax para los procedimientos
    // ************************************
    
    public void tasaRealInvertTemp(AjaxBehaviorEvent event){
        double tasaRealInvertTemp1 = (double) ((UIOutput) event.getSource()).getValue();
        setTasaRealInvert(1/tasaRealInvertTemp1);
    }
    
    public void tasaPresupInvert(AjaxBehaviorEvent event){
        double tasaPresupInvert1 = (double) ((UIOutput) event.getSource()).getValue();
        setTasaPresupInvert(1 / tasaPresupInvert1);
    }
    
    public void tasaForecaInvert(AjaxBehaviorEvent event){
        double tasaForecaInvert1 = (double) ((UIOutput) event.getSource()).getValue();
        setTasaForecaInvert(1 / tasaForecaInvert1);
    }
    
    public StreamedContent getFileCXP() {
        return fileCXP;
    }
    
}
