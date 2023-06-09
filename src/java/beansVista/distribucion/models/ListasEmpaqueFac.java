/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebedoya
 */
public class ListasEmpaqueFac {

    /**
     * @return the prefijo
     */
    public String getPrefijo() {
        return prefijo;
    }

    /**
     * @param prefijo the prefijo to set
     */
    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    /**
     * @return the nroFactura
     */
    public String getNroFactura() {
        return nroFactura;
    }

    /**
     * @param nroFactura the nroFactura to set
     */
    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }
    
    private String factura;
    private String cliente;
    private String destino;
    private int nroLineas;
    private double peso;
    private double volumen;
    private String prefijo;
    private String nroFactura;
    private int estado = -1;
    private String subNroFactura;
     private List<String> listaLios = new ArrayList<>();
    // Encapsulamiento de las variables
    // ********************************
    
    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getNroLineas() {
        return nroLineas;
    }

    public void setNroLineas(int nroLineas) {
        this.nroLineas = nroLineas;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**
     * @return the listaLios
     */
    public List<String> getListaLios() {
        return listaLios;
    }

    /**
     * @param listaLios the listaLios to set
     */
    public void setListaLios(List<String> listaLios) {
        this.listaLios = listaLios;
    }

    /**
     * @return the subNroFactura
     */
    public String getSubNroFactura() {
        return subNroFactura;
    }

    /**
     * @param subNroFactura the subNroFactura to set
     */
    public void setSubNroFactura(String subNroFactura) {
        this.subNroFactura = subNroFactura;
    }    
}
