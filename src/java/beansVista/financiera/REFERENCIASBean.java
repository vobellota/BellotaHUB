/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.financiera;

import beansNegocio.financiera.ReferenciasBusinessBean;
import beansVista.distribucion.models.ListasEmpaqueFac;
import dao.financiera.REFERENCIASDaoDB2;
import entidades.financiera.REFERENCIAS;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import sys.util.ExceptionManager;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "REFERENCIASBean")
public class REFERENCIASBean {
    
    private List<REFERENCIAS> listaReferencias;
    private Map<REFERENCIAS, Boolean> checked;
    private List<REFERENCIAS> listatemporal;
    private ReferenciasBusinessBean referenciaBuss;
    private String libro;
    private String periodo;
    private int anio; 
    private String nroComprobante;
    private boolean renderTable;
    //Vars cuando se Actualiza
    private String documentoUpdate;
    private String ref1Update;
    private String ref2Update;
    private StreamedContent fileREFERENCIAS;

    public String getDocumentoUpdate() {
        return documentoUpdate;
    }

    public void setDocumentoUpdate(String documentoUpdate) {
        this.documentoUpdate = documentoUpdate;
    }

    public String getRef1Update() {
        return ref1Update;
    }

    public void setRef1Update(String ref1Update) {
        this.ref1Update = ref1Update;
    }

    public String getRef2Update() {
        return ref2Update;
    }

    public void setRef2Update(String ref2Update) {
        this.ref2Update = ref2Update;
    }
    
    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }
    
    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getNroComprobante() {
        return nroComprobante;
    }

    public void setNroComprobante(String nroComprobante) {
        this.nroComprobante = nroComprobante;
    }
    
    public List<REFERENCIAS> getListaReferencias() {
        return listaReferencias;
    }

    public void setListaReferencias(List<REFERENCIAS> listaReferencias) {
        this.listaReferencias = listaReferencias;
    }
    
    public Map<REFERENCIAS, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(Map<REFERENCIAS, Boolean> checked) {
        this.checked = checked;
    }
    
    public boolean isRenderTable() {
        return renderTable;
    }

    public void setRenderTable(boolean renderTable) {
        this.renderTable = renderTable;
    }
    
    public List<REFERENCIAS> getListatemporal() {
        return listatemporal;
    }

    public void setListatemporal(List<REFERENCIAS> listatemporal) {
        this.listatemporal = listatemporal;
    }
    
    public ReferenciasBusinessBean getReferenciaBuss() {
        return referenciaBuss;
    }

    public void setReferenciaBuss(ReferenciasBusinessBean referenciaBuss) {
        this.referenciaBuss = referenciaBuss;
    }
    
    // Constructor
    // ***********
    public REFERENCIASBean(){
        // Lineas para generar el PDF que se descarga como manual de usuario
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/manuales/RESOLUCIONES_MANUAL_USUARIO.pdf");
        fileREFERENCIAS = new DefaultStreamedContent(stream, "application/pdf", "RESOLUCIONES_MANUAL_USUARIO.pdf");
        checked = new HashMap<REFERENCIAS, Boolean>();
        listatemporal = new ArrayList<>();
        referenciaBuss = new ReferenciasBusinessBean();
    }
    
    public void onFilterRows(){
        try{
            REFERENCIASDaoDB2 objreferencias = new REFERENCIASDaoDB2(Sesion.getSesion());
            listaReferencias = objreferencias.filter(libro, periodo, anio, nroComprobante);
        } catch (SQLException ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public void onUpdated(){
        try{    
            Iterator<Map.Entry<REFERENCIAS, Boolean>> it = getChecked().entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<REFERENCIAS, Boolean> ent = it.next();
                if (ent.getValue()) {
                    listatemporal.add(ent.getKey());
                    referenciaBuss.onUpdatedToDao(listatemporal, documentoUpdate, ref1Update, ref2Update);
                }
            }
            listaReferencias.clear();
            // Instancia del Nuevo Objeto para consultar
            REFERENCIASDaoDB2 objreferencias = new REFERENCIASDaoDB2(Sesion.getSesion());
            listaReferencias = objreferencias.filter(libro, periodo, anio, nroComprobante);
            // Mensaje de accion
            ExceptionManager.addInfo("Se Editaron los registros");
        } catch (Exception ex) {
            Logger.getLogger(PUCBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public StreamedContent getFileREFERENCIAS() {
        return fileREFERENCIAS;
    }
}
