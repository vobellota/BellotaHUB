/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion.models;

/**
 *
 * @author ebedoya
 */
public class ListasEmpaqueFacCabecera {
    
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
    private String descripcioncliente;
    private String fecha;
    private String origen;
    private String destino;
    private String direccion;
    private int nroLineas;
    private double peso;
    private double volumen;
    private String prefijo;
    private String nroFactura;
    private String telefono;
    private String correo;
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
    
    public String getDescripcioncliente() {
        return descripcioncliente;
    }

    public void setDescripcioncliente(String descripcioncliente) {
        this.descripcioncliente = descripcioncliente;
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
    
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
