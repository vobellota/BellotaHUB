/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.comercial;

import java.io.File;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ebedoya
 */
public class preciosEntity {
     
    //campos de la BD
    // **************
    private String SPID; //Estado
    private String PMETH; //Tipo de Precio
    private String PRKEY1; //Llega el # item
    private String PRKEY2; //Llega el pais
    private String PRKEYCONCT; //Llega el PRKEY concatenado
    private String PFCT1; //Precio
    private String PSDTE; //Fecha desde
    private String SPENDT; //Fecha desde 2
    private String PSEDT; //Fecha Hasta
    private String SPENTM; //Hora
    private String PCURR; // Moneda
    private String SPENUS; //Usuario
    private String SPMNDT; // Fecha 2 de insercion
    private String SPMNUS; //Modificador
    private String SPMNTM; //Hora modificacion
    
    private String vencer;//campo que selecciona el usuario para cambiar fecha de vencimiento al precio del producto ya almacenada
    private String inactivar;//campo que selecciona el usuario para inactivar el precio entre la fecha seleccionada
    
    // Constructor de la clase vacio
    // *****************************
    public preciosEntity(){}
    
    // Metodos para el encapsulamiento
    // *******************************
    
    public String getSPID() {
        return SPID;
    }

    public void setSPID(String SPID) {
        this.SPID = SPID;
    }

    public String getPMETH() {
        return PMETH;
    }

    public void setPMETH(String PMETH) {
        this.PMETH = PMETH;
    }

    public String getPRKEY1() {
        return PRKEY1;
    }

    public void setPRKEY1(String PRKEY1) {
        this.PRKEY1 = PRKEY1;
    }

    public String getPRKEY2() {
        return PRKEY2;
    }

    public void setPRKEY2(String PRKEY2) {
        this.PRKEY2 = PRKEY2;
    }

    public String getPRKEYCONCT() {
        return PRKEYCONCT;
    }

    public void setPRKEYCONCT(String PRKEYCONCT) {
        this.PRKEYCONCT = PRKEYCONCT;
    }

    public String getPFCT1() {
        return PFCT1;
    }

    public void setPFCT1(String PFCT1) {
        this.PFCT1 = PFCT1;
    }

    public String getPSDTE() {
        return PSDTE;
    }

    public void setPSDTE(String PSDTE) {
        this.PSDTE = PSDTE;
    }

    public String getSPENDT() {
        return SPENDT;
    }

    public void setSPENDT(String SPENDT) {
        this.SPENDT = SPENDT;
    }

    public String getPSEDT() {
        return PSEDT;
    }

    public void setPSEDT(String PSEDT) {
        this.PSEDT = PSEDT;
    }

    public String getSPENTM() {
        return SPENTM;
    }

    public void setSPENTM(String SPENTM) {
        this.SPENTM = SPENTM;
    }

    public String getPCURR() {
        return PCURR;
    }

    public void setPCURR(String PCURR) {
        this.PCURR = PCURR;
    }

    public String getSPENUS() {
        return SPENUS;
    }

    public void setSPENUS(String SPENUS) {
        this.SPENUS = SPENUS;
    }
    
    public String getSPMNDT() {
        return SPMNDT;
    }

    public void setSPMNDT(String SPMNDT) {
        this.SPMNDT = SPMNDT;
    }

    public String getSPMNUS() {
        return SPMNUS;
    }

    public void setSPMNUS(String SPMNUS) {
        this.SPMNUS = SPMNUS;
    }

    public String getSPMNTM() {
        return SPMNTM;
    }

    public void setSPMNTM(String SPMNTM) {
        this.SPMNTM = SPMNTM;
    }

    public String getVencer() {
        return vencer;
    }

    public void setVencer(String vencer) {
        this.vencer = vencer;
    }

    public String getInactivar() {
        return inactivar;
    }

    public void setInactivar(String inactivar) {
        this.inactivar = inactivar;
    }
    
    // Override del metodo toString
    // ****************************
    @Override
    public String toString(){
        return "Metodo toString";
    }
    
}
