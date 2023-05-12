/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.financiera;

/**
 *
 * @author ebedoya
 */
public class REFERENCIAS {
    
    // Campos de la BD
    // ***************
    private String llaveUnica;
    private String ledger;
    private String libro;
    private int ano;
    private int periodo;
    private String comprobante;
    private int linea;
    private int compania;
    private int cuenta;
    private String cto_costo;
    private String area;
    private int tercero;
    private String documento;
    private String ref1;
    private String ref2;
    private int fecha;
    
    // Constructor de la clase
    // ***********************
    public REFERENCIAS(){}
    
    // Metodos para el encapsulamiento
    // *******************************
    public String getLedger() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = ledger;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getCompania() {
        return compania;
    }

    public void setCompania(int compania) {
        this.compania = compania;
    }

    public int getCuenta() {
        return cuenta;
    }

    public void setCuenta(int cuenta) {
        this.cuenta = cuenta;
    }

    public String getCto_costo() {
        return cto_costo;
    }

    public void setCto_costo(String cto_costo) {
        this.cto_costo = cto_costo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getTercero() {
        return tercero;
    }

    public void setTercero(int tercero) {
        this.tercero = tercero;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public String getRef2() {
        return ref2;
    }

    public void setRef2(String ref2) {
        this.ref2 = ref2;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }
    
    public String getLlaveUnica() {
        return llaveUnica;
    }

    public void setLlaveUnica(String llaveUnica) {
        this.llaveUnica = llaveUnica;
    }
    
    // Override del metodo toString
    // ****************************
    @Override
    public String toString(){
        return llaveUnica;
    }
}
