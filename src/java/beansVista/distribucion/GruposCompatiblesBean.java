/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion;

import dao.distribucion.GruposCompatiblesDao;
import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import java.sql.SQLException;
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
import sys.util.Sesion;
import sys.util.Funciones;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "GruposCompatiblesBean")
public class GruposCompatiblesBean {

/**
 * Creates a new instance of GruposCompatibles
 */
private List<GruposCompatibles> listaGruposCompatibles;
private List<UnidadesEmpaque> listaUnidadesSinParame;
private GruposCompatibles nuevosGruposCompatibles = new GruposCompatibles();
private GruposCompatibles selectedGruposCompRow;
private String mostrarDimensiones;
private double pesoTemp;
private double anchoTemp;
private double altoTemp;
private double largoTemp;
private double resultadoTemp;
private int alarmable;
private String cadenaAlarma;
    
    public GruposCompatiblesBean() {
        try{
            Funciones funciones = new Funciones();
            GruposCompatiblesDao gruposcompatemp = new GruposCompatiblesDao(Sesion.getSesion());
            listaGruposCompatibles = gruposcompatemp.select();
            // Valor por defecto para una var
            // ******************************
            nuevosGruposCompatibles.setEstadoGruposCompatibles("Activo");
            // ******************************
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
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // ********************************
    // Encapsulamiento de las variables
    // ********************************

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

    public GruposCompatibles getNuevosGruposCompatibles() {
        return nuevosGruposCompatibles;
    }

    public void setNuevosGruposCompatibles(GruposCompatibles nuevosGruposCompatibles) {
        this.nuevosGruposCompatibles = nuevosGruposCompatibles;
    }
    
    public String getMostrarDimensiones() {
        return mostrarDimensiones;
    }

    public void setMostrarDimensiones(String mostrarDimensiones) {
        this.mostrarDimensiones = mostrarDimensiones;
    }
    
    public double getPesoTemp() {
        return pesoTemp;
    }

    public void setPesoTemp(double pesoTemp) {
        this.pesoTemp = pesoTemp;
    }
    
    public double getAnchoTemp() {
        return anchoTemp;
    }

    public void setAnchoTemp(double anchoTemp) {
        this.anchoTemp = anchoTemp;
    }
    
    public double getAltoTemp() {
        return altoTemp;
    }

    public void setAltoTemp(double altoTemp) {
        this.altoTemp = altoTemp;
    }

    public double getLargoTemp() {
        return largoTemp;
    }

    public void setLargoTemp(double largoTemp) {
        this.largoTemp = largoTemp;
    }
    
    public double getResultadoTemp() {
        return resultadoTemp;
    }

    public void setResultadoTemp(double resultadoTemp) {
        this.resultadoTemp = resultadoTemp;
    }
    
    // Seleccion de una row puntual Get y Set
    // **************************************
    
    public GruposCompatibles getSelectedGruposCompRow() {
        return selectedGruposCompRow;
    }

    public void setSelectedGruposCompRow(GruposCompatibles selectedGruposCompRow) {
        this.selectedGruposCompRow = selectedGruposCompRow;
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
    
    // *****************
    // Logica en general
    // *****************
    
    public void onRowEdit(RowEditEvent event){
        try{
            GruposCompatiblesDao objsave = new GruposCompatiblesDao(Sesion.getSesion());
            objsave.update((GruposCompatibles) event.getObject());
            // Mensaje de adicion
            ExceptionManager.addInfo("Grupos Compatibles Editados exitosamente");
        } catch(SQLException ex){
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        // Mensaje de adicion
        ExceptionManager.addError("Se ha cancelado la edición del registro");
    }
    
    public void onStoring(){
        try{
            GruposCompatiblesDao objsave = new GruposCompatiblesDao(Sesion.getSesion());
            // Validador si el campo viene vacío no arroje el null pointer
            // ***********************************************************
            if(nuevosGruposCompatibles.getMaximoCajasAgrupar() == null){
                nuevosGruposCompatibles.setMaximoCajasAgrupar(0);
                nuevosGruposCompatibles.setFactorPesoVolumen(pesoTemp);
            }
            // Agregar a la lista el nuevo registro
            // Para que pueda ser renderizado en la 
            // Datatable correctamente con el :updtae
            listaGruposCompatibles.add(nuevosGruposCompatibles);
            // Llamado a insertar en el DAO
            objsave.insert(nuevosGruposCompatibles);
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha insertado una Grupo compatible");
        } catch(SQLException ex){
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // Aceptar de la ventana modal
    // ****************************
    
    public void onDeletedAcepted(){
        try{
            // obj de GruposCompatiblesDao
            GruposCompatiblesDao objsave = new GruposCompatiblesDao(Sesion.getSesion());
            objsave.delete(selectedGruposCompRow);
            // obj de grupos
            GruposCompatiblesDao gruposcompatemp = new GruposCompatiblesDao(Sesion.getSesion());
            listaGruposCompatibles = gruposcompatemp.select();
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha eliminado correctamente el Grupo Compatible");
        } catch(SQLException ex){
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // Metodos Ajax de los diferentes eventos
    
    // Metodo ajax para cambiar cuando el select haga on change
    // ********************************************************
    public void onChangeSelect(AjaxBehaviorEvent event){
        String cadena = (String) ((javax.faces.component.html.HtmlSelectOneMenu) event.getSource()).getValue();
        setMostrarDimensiones(cadena);  
    }
    
    // Metodo asignar la var que por ajax llegó el peso
    // ************************************************
    public void pesoTempVar(AjaxBehaviorEvent event){
        double pesoTempVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setPesoTemp(pesoTempVar1);
    }
    
    // Metodo asignar la var que por ajax llegó el peso
    // ************************************************
    public void anchoTempVar(AjaxBehaviorEvent event){
        double anchoTempVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setAnchoTemp(anchoTempVar1);
    }
    
    // Metodo asignar la var que por ajax llegó el alto
    // ************************************************
    public void altoTempVar(AjaxBehaviorEvent event){
        double altoTempVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setAltoTemp(altoTempVar1);
    }
    
    // Metodo asignar la var que por ajax llegó el largo
    // *************************************************
    public void largoTempVar(AjaxBehaviorEvent event){
        double largoTempVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setLargoTemp(largoTempVar1);
        // Disparar el metodo de concatenacion
        sumaryTempVar();
    }
    
    // Metodo asignar la var que por ajax llegó el resultado de la operacion
    // *********************************************************************
    public void sumaryTempVar(){
        double sumaryTempVar = largoTemp * anchoTemp * altoTemp / pesoTemp;
        setResultadoTemp(sumaryTempVar);
    }
    
}