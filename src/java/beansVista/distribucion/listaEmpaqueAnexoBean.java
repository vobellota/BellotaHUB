/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion;

import beansVista.distribucion.models.Facturas;
import beansVista.distribucion.models.Lios;
import beansVista.distribucion.models.ListasEmpaqueFac;
import dao.distribucion.ListasEmpaqueFacDao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "listaEmpaqueAnexoBean")
public class listaEmpaqueAnexoBean {

    private List<Facturas> facturaImprimirAnexo;
     private List<Lios> listatemporalLios;
     
    public listaEmpaqueAnexoBean() {
        System.out.println("");
    }

    @PostConstruct
    public void listaEmpaqueAnexoBeanPost() {
        listatemporalLios = new ArrayList<>();
        ListasEmpaqueFacDao listasDao = new ListasEmpaqueFacDao();
       String param = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idlistaempaque");
        try {
           facturaImprimirAnexo = listasDao.toPrintAndExcel(Integer.parseInt(param));
        } catch (SQLException ex) {
            Logger.getLogger(listaEmpaqueAnexoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public listaEmpaqueAnexoBean(List<Facturas> facturaImprimir) {
    }

    public List<Facturas> getFacturaImprimirAnexo() {
        return facturaImprimirAnexo;
    }

    public void setFacturaImprimirAnexo(List<Facturas> facturaImprimirAnexo) {
        this.facturaImprimirAnexo = facturaImprimirAnexo;
    }
    
    public List<Lios> getListatemporalLios() {
        return listatemporalLios;
    }

    public void setListatemporalLios(List<Lios> listatemporalLios) {
        this.listatemporalLios = listatemporalLios;
    }
}
