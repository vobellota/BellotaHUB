/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion;

import dao.distribucion.EstibasDao;
import entidades.distribucion.Estibas;
import entidades.distribucion.UnidadesEmpaque;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
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
@ManagedBean(name="EstibasBean")
public class EstibasBean {

    /**
     * Creates a new instance of EstibasBean
     */
    private List<Estibas> listaEstibas;
    private List<UnidadesEmpaque> listaUnidadesSinParame;
    private Estibas nuevasEstibas = new Estibas();
    private Estibas selectedEstibasRow;
    private double pesoMaximoTemp;
    private double anchoTemp;
    private double largoTemp;
    private double altoTemp;
    private double resultadoTemp;
    private int alarmable;
    private String cadenaAlarma;

    public EstibasBean() {
        try{
            Funciones funciones = new Funciones();
            EstibasDao estibastemp = new EstibasDao(Sesion.getSesion());
            listaEstibas = estibastemp.select();
            nuevasEstibas.setEstadoEstiba("Activo");
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
    
    public List<Estibas> getListaEstibas() {
        return listaEstibas;
    }

    public void setListaEstibas(List<Estibas> listaEstibas) {
        this.listaEstibas = listaEstibas;
    }
    
    public List<UnidadesEmpaque> getListaUnidadesSinParame() {
        return listaUnidadesSinParame;
    }

    public void setListaUnidadesSinParame(List<UnidadesEmpaque> listaUnidadesSinParame) {
        this.listaUnidadesSinParame = listaUnidadesSinParame;
    }

    public Estibas getNuevasEstibas() {
        return nuevasEstibas;
    }

    public void setNuevasEstibas(Estibas nuevasEstibas) {
        this.nuevasEstibas = nuevasEstibas;
    }
    
    // Seleccion de una row puntual Get y Set
    // **************************************
    
    public Estibas getSelectedEstibasRow() {
        return selectedEstibasRow;
    }

    public void setSelectedEstibasRow(Estibas selectedEstibasRow) {
        this.selectedEstibasRow = selectedEstibasRow;
    }
    
    // ***************************************
    
    public double getLargoTemp() {
        return largoTemp;
    }

    public void setLargoTemp(double largoTemp) {
        this.largoTemp = largoTemp;
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

    public double getPesoMaximoTemp() {
        return pesoMaximoTemp;
    }

    public void setPesoMaximoTemp(double pesoMaximoTemp) {
        this.pesoMaximoTemp = pesoMaximoTemp;
    }
    
    public double getResultadoTemp() {
        return resultadoTemp;
    }

    public void setResultadoTemp(double resultadoTemp) {
        this.resultadoTemp = resultadoTemp;
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
            EstibasDao objsave = new EstibasDao(Sesion.getSesion());
            objsave.update((Estibas) event.getObject());
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha Editado exitosamente");
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
            EstibasDao objsave = new EstibasDao(Sesion.getSesion());
            // Asignación de la var temporal a la instancia de la entidad
            nuevasEstibas.setFactorEstiba(resultadoTemp);
            // Agregar a la lista el nuevo registro
            // Para que pueda ser renderizado en la 
            // Datatable correctamente con el :updtae
            listaEstibas.add(nuevasEstibas);
            // Llamado a insertar en el DAO
            objsave.insert(nuevasEstibas);
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha insertado una Estiba");
        } catch(SQLException ex){
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // Aceptar de la ventana modal
    // ****************************
    
    public void onDeletedAcepted(){
        try{
            EstibasDao objsave = new EstibasDao(Sesion.getSesion());
            objsave.delete(selectedEstibasRow);
            // obj de estibas
            EstibasDao estibastemp = new EstibasDao(Sesion.getSesion());
            listaEstibas = estibastemp.select();
            // Mensaje de adicion
            ExceptionManager.addInfo("Se ha eliminado correctamente la Estiba");
        } catch(SQLException ex){
            Logger.getLogger(GruposCompatiblesBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }
    
    // Metodos Ajax de los diferentes eventos
    
    // Metodo asignar la var que por ajax llegó el resultado de la operacion
    // *********************************************************************
    public void pesoMaximoVar(AjaxBehaviorEvent event){
        double pesoMaximoVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setPesoMaximoTemp(pesoMaximoVar1);
    }
    
    // Metodo asignar la var que por ajax llegó el resultado de la operacion
    // *********************************************************************
    public void anchoTempVar(AjaxBehaviorEvent event){
        double anchoTempVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setAnchoTemp(anchoTempVar1);
    }
    
    // Metodo asignar la var que por ajax llegó el resultado de la operacion
    // *********************************************************************
    public void largoTempVar(AjaxBehaviorEvent event){
        double largoTempVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setLargoTemp(largoTempVar1);
    }
    
    // Metodo asignar la var que por ajax llegó el resultado de la operacion
    // *********************************************************************
    public void altoTempVar(AjaxBehaviorEvent event){
        double altoTempVar1 = (double) ((UIOutput) event.getSource()).getValue();
        setAltoTemp(altoTempVar1);
        // Disparar el metodo de concatenacion
        sumaryTempVar();
    }
    
    // Metodo asignar la var que por ajax llegó el resultado de la operacion
    // *********************************************************************
    public void sumaryTempVar(){
        double sumarytempvar1 = largoTemp * anchoTemp * altoTemp / pesoMaximoTemp;
        setResultadoTemp(sumarytempvar1);
    }
    
}
