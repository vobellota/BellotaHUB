/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion;

import dao.distribucion.UnidadesEmpaqueDao;
import dao.distribucion.GruposCompatiblesDao;
import dao.distribucion.EstibasDao;
import entidades.distribucion.UnidadesEmpaque;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.Estibas;
import java.sql.SQLException;
import java.sql.*;
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
@ManagedBean(name="UnidadesEmpaqueBean")
public class UnidadesEmpaqueBean {
    
    public Sesion sesion;
    private List<UnidadesEmpaque> listaUnidadesEmpaque;
    private List<UnidadesEmpaque> listaUnidadesSinParame;
    private List<GruposCompatibles> listaGruposCompatibles;
    private List<Estibas> listaEstibasAsignadas;
    private int alarmable;
    private String cadenaAlarma;
    
    public UnidadesEmpaqueBean(){
        try{
            // Obj de unidades de empaque
            UnidadesEmpaqueDao unidadestemp = new UnidadesEmpaqueDao(Sesion.getSesion());
            listaUnidadesEmpaque = unidadestemp.select();
            // obj de grupos compatibles para el select dinamico
            GruposCompatiblesDao grupocomptemp = new GruposCompatiblesDao(Sesion.getSesion());
            listaGruposCompatibles = grupocomptemp.select();
            // obj de estibas asignadas para el select dinamico
            EstibasDao estibastemp = new EstibasDao(Sesion.getSesion());
            listaEstibasAsignadas = estibastemp.select();
            Funciones funciones = new Funciones();
            // Apartado de la alarma
            alarmable = funciones.parametrizables();
            // ******************************
            if(alarmable != 0){
                setCadenaAlarma("Faltan " + alarmable + " Unidades de empaque por parametrizar");
                // Lista de unidades sin parametrizar
                // **********************************
                listaUnidadesSinParame = funciones.unitiesWithoutParams();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UnidadesEmpaqueBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    public List<UnidadesEmpaque> getListaUnidadesEmpaque() {
        return listaUnidadesEmpaque;
    }

    public void setListaUnidadesEmpaque(List<UnidadesEmpaque> listaUnidadesEmpaque) {
        this.listaUnidadesEmpaque = listaUnidadesEmpaque;
    }
    public List<UnidadesEmpaque> getListaUnidadesSinParame() {
        return listaUnidadesSinParame;
    }

    public void setListaUnidadesSinParame(List<UnidadesEmpaque> listaUnidadesSinParame) {
        this.listaUnidadesSinParame = listaUnidadesSinParame;
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

    public void setListaEstibasAsignadas(List<Estibas> listaEstibasAsignadas) {
        this.listaEstibasAsignadas = listaEstibasAsignadas;
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
    
    public void onRowEdit(RowEditEvent event){
        try{
            UnidadesEmpaqueDao objsave = new UnidadesEmpaqueDao(Sesion.getSesion());
            objsave.update((UnidadesEmpaque) event.getObject());
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha Editado exitosamente");
        } catch(SQLException ex){
            Logger.getLogger(UnidadesEmpaqueBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        // Mensaje de adicion
        ExceptionManager.addError("Se ha cancelado la edición del registro");
    }
}   
