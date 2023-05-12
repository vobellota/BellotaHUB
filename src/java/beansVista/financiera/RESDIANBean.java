/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.financiera;

import dao.financiera.RESDIANDaoDB2;
import entidades.financiera.RESDIAN;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sys.util.ExceptionManager;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "RESDIANBean")
public class RESDIANBean {

    /**
     * Creates a new instance of RESDIANBean
     */
    public Sesion sesion;
    private List<RESDIAN> resdianLista;
    private List<RESDIAN> resdianListaValidate;
    private List<RESDIAN> resdianListaValidateNros;
    private List<RESDIAN> filteredresdianLista;
    private List<RESDIAN> nuevaResdianLista = new ArrayList<>();
    private String resolucionTmp;
    private String renderGrid = "No";
    private RESDIAN selectedResdianRow;
    private Date fechaInicial = new Date();
    private Date fechaFinal;
    SimpleDateFormat sdfBPCS = new SimpleDateFormat("yyyyMMdd");
    private boolean swtichValidateFechas;
    private boolean swtichValidateRange;
    private StreamedContent fileRESDIAN;

    public boolean isSwtichValidateFechas() {
        return swtichValidateFechas;
    }

    public void setSwtichValidateFechas(boolean swtichValidateFechas) {
        this.swtichValidateFechas = swtichValidateFechas;
    }

    public boolean isSwtichValidateRange() {
        return swtichValidateRange;
    }

    public void setSwtichValidateRange(boolean swtichValidateRange) {
        this.swtichValidateRange = swtichValidateRange;
    }
    
    public List<RESDIAN> getFilteredresdianLista() {
        return filteredresdianLista;
    }

    public void setFilteredresdianLista(List<RESDIAN> filteredresdianLista) {
        this.filteredresdianLista = filteredresdianLista;
    }
    
    public List<RESDIAN> getResdianListaValidate() {
        return resdianListaValidate;
    }

    public void setResdianListaValidate(List<RESDIAN> resdianListaValidate) {
        this.resdianListaValidate = resdianListaValidate;
    }
    
    public List<RESDIAN> getResdianLista() {
        return resdianLista;
    }
    
    public List<RESDIAN> getResdianListaValidateNros() {
        return resdianListaValidateNros;
    }

    public void setResdianListaValidateNros(List<RESDIAN> resdianListaValidateNros) {
        this.resdianListaValidateNros = resdianListaValidateNros;
    }

    public void setResdianLista(List<RESDIAN> resdianLista) {
        this.resdianLista = resdianLista;
    }

    public List<RESDIAN> getNuevaResdianLista() {
        return nuevaResdianLista;
    }

    public void setNuevaResdianLista(List<RESDIAN> nuevaResdianLista) {
        this.nuevaResdianLista = nuevaResdianLista;
    }
    
    // Agregar un registro completo
    // ****************************
    
    public void agregarPrefijo() {
        RESDIAN res = new RESDIAN();
        nuevaResdianLista.add(res);
    }
    
    // Remover un registro completo
    // ****************************
    public void removerPrefijo() {
        if (nuevaResdianLista.size() > 1) {
            nuevaResdianLista.remove(nuevaResdianLista.size() - 1);
        }
    }

    public String getResolucionTmp() {
        return resolucionTmp;
    }

    public void setResolucionTmp(String resolucionTmp) {
        this.resolucionTmp = resolucionTmp;
    }
    
    public String getRenderGrid() {
        return renderGrid;
    }

    public void setRenderGrid(String renderGrid) {
        this.renderGrid = renderGrid;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
        Calendar calFechaFinal = Calendar.getInstance();
        calFechaFinal.setTime(fechaInicial);
        calFechaFinal.add(Calendar.MONTH, 12);
        fechaFinal = calFechaFinal.getTime();
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    
    // Seleccion de una row puntual Get y Set
    // **************************************
    
    public RESDIAN getSelectedResdianRow() {
        return selectedResdianRow;
    }
 
    public void setSelectedResdianRow(RESDIAN selectedResdianRow) {
        this.selectedResdianRow = selectedResdianRow;
    }

    public RESDIANBean() {
        // Lineas para generar el PDF que se descarga como manual de usuario
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/manuales/RESOLUCIONES_FACTURACION_MANUAL_USUARIO.pdf");
        fileRESDIAN = new DefaultStreamedContent(stream, "application/pdf", "RESOLUCIONES_FACTURACION_MANUAL_USUARIO.pdf");
        
        try {
            RESDIANDaoDB2 resdiandaotemp = new RESDIANDaoDB2(Sesion.getSesion());
            resdianLista = resdiandaotemp.select();
            RESDIAN res = new RESDIAN();
            nuevaResdianLista.add(res);
            Calendar calFechaFinal = Calendar.getInstance();
            calFechaFinal.add(Calendar.MONTH, 12);
            fechaFinal = calFechaFinal.getTime();
        } catch (SQLException ex) {
            Logger.getLogger(CXPBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            RESDIANDaoDB2 objsave = new RESDIANDaoDB2(Sesion.getSesion());
            objsave.update((RESDIAN) event.getObject());
            // Consultar de nuevo el OBJ
            resdianLista.clear();
            resdianLista = objsave.select();
            // Limpiar los Filtros
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('resdianTable').clearFilters()");
            // Mensaje de accion
            ExceptionManager.addInfo("Se Editaron los registros");
        } catch (SQLException ex) {
            Logger.getLogger(RESDIAN.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        ExceptionManager.addError("Se Cancelo la edición");
    }

    public void onStoring() {
        try{
            RESDIANDaoDB2 objsave = new RESDIANDaoDB2(Sesion.getSesion());
            resdianLista.clear();
            llenarResolucionYFechas();
            for(RESDIAN resdianl: nuevaResdianLista){
                // 
                System.out.println("Metodo Store");
                // Fechas validate
                onValidateDate();
                // onValidateDate() retorna swtichValidate
                if(swtichValidateFechas == false){
                    // Nros rango
                    ExceptionManager.addWarning("¡Atención ya hay registros en este rango de Fechas!");
                    onValidateDateNros(resdianl.getN_inicial(),resdianl.getN_final());
                    if(swtichValidateRange == false){
                        ExceptionManager.addError("Hay consecutivos en el rango que se seleccionó");
                    } else {
                        objsave.insert(resdianl);
                        ExceptionManager.addInfo("Se adicionaron los registros");
                    }
                } else {
                    objsave.insert(resdianl);
                    ExceptionManager.addInfo("Se adicionaron los registros");
                } 
            }
            RESDIANDaoDB2 resdiandaotemp = new RESDIANDaoDB2(Sesion.getSesion());
            resdianLista = resdiandaotemp.select();
        } catch(SQLException ex){
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public void llenarResolucionYFechas() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String fechaInicialCadena = sdf.format(fechaInicial);
        int fechaBPCSInicial = Integer.parseInt(sdfBPCS.format(fechaInicial));
        int fechaBPCSFinal = Integer.parseInt(sdfBPCS.format(fechaFinal));
        for (RESDIAN registro : nuevaResdianLista) {
            registro.setResolucion(resolucionTmp + " de " + fechaInicialCadena);
            registro.setFech_ini(fechaBPCSInicial);
            registro.setFech_venc(fechaBPCSFinal);
        }
    }
    // Aceptar de la ventana modal
    // ****************************
    
    public void onDeletedAcepted(){
        try{
            RESDIANDaoDB2 objtempupdt = new RESDIANDaoDB2(Sesion.getSesion());
            resdianLista.remove(selectedResdianRow);
            objtempupdt.delete(selectedResdianRow);
            // Limpiar los Filtros
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("PF('resdianTable').clearFilters()");
        } catch(SQLException ex){
            Logger.getLogger(RESDIANBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // Metodo ajax para cambiar cuando el select haga on change
    // ********************************************************
    public void onChangeNroform(AjaxBehaviorEvent event){
        setRenderGrid("Si");
    }
    
    // Metodo ajax para cambiar cuando el select haga on change la fecha
    // *****************************************************************
    public void onChangeDateOne(SelectEvent event){
        // Obtiene fecha
        Date dateSelected = (Date) event.getObject();
        // Llamado de la libreria Calendar y conversion del 
        // obj de fecha
        Calendar calFechaFinalRenew = Calendar.getInstance();
        calFechaFinalRenew.setTime(dateSelected);
        // Sumatoria de 12 meses de la fecha selecionada
        calFechaFinalRenew.add(Calendar.MONTH, 12);
        fechaFinal = calFechaFinalRenew.getTime();
        // Asignacion de la fecha
        setFechaFinal(fechaFinal);
    }
    
    
    public void onValidateDate(){
        try{
            RESDIANDaoDB2 objsave = new RESDIANDaoDB2(Sesion.getSesion());
            resdianListaValidate = objsave.select_range_dates(Integer.parseInt(sdfBPCS.format(fechaInicial)), Integer.parseInt(sdfBPCS.format(fechaFinal)));
            if(resdianListaValidate.size() > 0){
                // Valida fuera de Fechas rango de consecutivos
                swtichValidateFechas = false;
            } else {
                // Llama al metodo de Inserción
                swtichValidateFechas = true;
            }
        } catch(SQLException ex){
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public void onValidateDateNros(int nroIni, int nroFin){
        try{
            RESDIANDaoDB2 objsave = new RESDIANDaoDB2(Sesion.getSesion());
            resdianListaValidateNros = objsave.select_range_dates_nros(nroIni, nroFin);
            if(resdianListaValidateNros.size() > 0){
                swtichValidateRange = false;
            } else {
                swtichValidateRange = true;
            }
        } catch(SQLException ex){
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public StreamedContent getFileRESDIAN() {
        return fileRESDIAN;
    }
}













