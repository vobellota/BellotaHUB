/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.comercial;

import org.primefaces.model.UploadedFile;

/**
 *
 * @author ebedoya
 */
 public class precios {
    
    // Campos BD
    // *********
    private String compania;
    private String moneda;
    private String item;
    private double precioLista;
    private int fechaInicio;
    private int fechaFin;
    private String tipoPrecio;
    private String tipoPrecioSub;
    private String prKey;
    private String vencer;//variable que almacena la seleccion del usuario para cambiar la fecha de vencimiento a una anterior
    private String inactivar;//variable que almacena la opcion del usuario para inactivar el precio "EZ"
    private UploadedFile file;
    
    // Constructor de la clase
    // ***********************
    public precios(){}
    
    // Metodos para el encapsulamiento
    // *******************************

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrecioLista() {
        return precioLista;
    }

    public void setPrecioLista(double precioLista) {
        this.precioLista = precioLista;
    }

    public int getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(int fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(int fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    public String getTipoPrecioSub() {
        return tipoPrecioSub;
    }

    public void setTipoPrecioSub(String tipoPrecioSub) {
        this.tipoPrecioSub = tipoPrecioSub;
    }
    
    public String getPrKey() {
        return prKey;
    }

    public void setPrKey(String prKey) {
        this.prKey = prKey;
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
        return "Metodo to String";
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
}
