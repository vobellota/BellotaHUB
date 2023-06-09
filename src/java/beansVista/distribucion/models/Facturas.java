/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion.models;

import entidades.distribucion.ProductosCompatibles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author crojas
 */
public class Facturas {

    private int idListaEmpaque;
    private String idFacturaAS;
    private String prefijo;
    private String nroFactura;
    private String descripcionCliente;
    private String idCliente;
    private Date fecha;
    private String origen;
    private String destino;
    private String direccion;
    private String telefono;
    private int totalPaquetes;
    private double pesoTotal;
    private double volumenTotal;
    private List<Lios> listaLios = new ArrayList<>();
    private List<Lios> listaLiosAgrupada = new ArrayList<>();
    private String comentario;
    private int estado;
     private List<Lios> listaLiosExluidos = new ArrayList<>();
    private String correo;
    private String subNroFactura;
    // private ProductosCompatibles productoCompatible;

    /**
     * @return the idFacturaAS
     */
    public int getIdListaEmpaque() {
        return idListaEmpaque;
    }

    /**
     * @param idListaEmpaque the idFacturaAS to set
     */
    public void setIdListaEmpaque(int idListaEmpaque) {
        this.idListaEmpaque = idListaEmpaque;
    }

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

    public Facturas() {
        this.estado = 0;
    }

    public Facturas(int idListaEmpaque, String idFacturaAS, String prefijo, String nroFactura, String subnroFactura, String descripcionCliente, String idCliente, Date fecha, String origen, 
            String destino, String direccion, int totalPaquetes, double pesoTotal, double volumenTotal, List<Lios> listaLios, String telefono, String correo) {
        this.idListaEmpaque = idListaEmpaque;
        this.idFacturaAS = idFacturaAS;
        this.prefijo = prefijo;
        this.nroFactura = nroFactura;
        this.subNroFactura = subnroFactura;
        this.descripcionCliente = descripcionCliente;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.origen = origen;
        this.destino = destino;
        this.direccion = direccion;
        this.totalPaquetes = totalPaquetes;
        this.pesoTotal = pesoTotal;
        this.volumenTotal = volumenTotal;
        this.listaLios = listaLios;
        this.estado = 0;
        this.telefono = telefono;
        this.correo = correo;        
    }

    public Facturas(int idListaEmpaque, String idFacturaAS, String prefijo, String nroFactura, String subnroFactura, List<Lios> listaLios) {
        this.idListaEmpaque = idListaEmpaque;
        this.idFacturaAS = idFacturaAS;
        this.prefijo = prefijo;
        this.nroFactura = nroFactura;
        this.subNroFactura = subnroFactura;
        this.listaLios = listaLios;
        this.estado = 0;
    }

    public Facturas(String idFacturaAS, String prefijo, String nroFactura, String subnroFactura, List<Lios> listaLios, String descripcionCliente, String idCliente, Date fecha, String origen, String destino, String direccion, int totalPaquetes) {
        this.idFacturaAS = idFacturaAS;
        this.prefijo = prefijo;
        this.nroFactura = nroFactura;
        this.subNroFactura = subnroFactura;
        this.listaLios = listaLios;
        this.descripcionCliente = descripcionCliente;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.origen = origen;
        this.destino = destino;
        this.direccion = direccion;
        this.totalPaquetes = totalPaquetes;
        this.estado = 0;
    }

    public Facturas(String idFacturaAS, String prefijo, String nroFactura, String subnroFactura, List<Lios> listaLios, List<Lios> listaLiosAgrupada, String descripcionCliente, String idCliente, Date fecha, 
            String origen, String destino, String direccion, int totalPaquetes, double pesoTotal, double volumenTotal, String telefono, String correo) {
        this.idFacturaAS = idFacturaAS;
        this.prefijo = prefijo;
        this.nroFactura = nroFactura;
        this.subNroFactura = subnroFactura;
        this.listaLios = listaLios;
        this.listaLiosAgrupada = listaLiosAgrupada;
        this.descripcionCliente = descripcionCliente;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.origen = origen;
        this.destino = destino;
        this.direccion = direccion;
        this.totalPaquetes = totalPaquetes;
        this.pesoTotal = pesoTotal;
        this.volumenTotal = volumenTotal;
        this.estado = 0;
        this.telefono = telefono;
        this.correo = correo;
    }

    /**
     * @return the descripcionCliente
     */
    public String getDescripcionCliente() {
        return descripcionCliente;
    }

    /**
     * @param descripcionCliente the descripcionCliente to set
     */
    public void setDescripcionCliente(String descripcionCliente) {
        this.descripcionCliente = descripcionCliente;
    }

    /**
     * @return the idCliente
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * @param idCliente the idCliente to set
     */
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the origen
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * @return the destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the totalPaquetes
     */
    public int getTotalPaquetes() {
        return totalPaquetes;
    }

    /**
     * @param totalPaquetes the totalPaquetes to set
     */
    public void setTotalPaquetes(int totalPaquetes) {
        this.totalPaquetes = totalPaquetes;
    }

    /**
     * @return the pesoTotal
     */
    public double getPesoTotal() {
        return pesoTotal;
    }

    /**
     * @param pesoTotal the pesoTotal to set
     */
    public void setPesoTotal(double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    /**
     * @return the volumenTotal
     */
    public double getVolumenTotal() {
        return volumenTotal;
    }

    /**
     * @param volumenTotal the volumenTotal to set
     */
    public void setVolumenTotal(double volumenTotal) {
        this.volumenTotal = volumenTotal;
    }

    /**
     * @return the listaLios
     */
    public List<Lios> getListaLios() {
        return listaLios;
    }

    /**
     * @param listaLios the listaLios to set
     */
    public void setListaLios(List<Lios> listaLios) {
        this.listaLios = listaLios;
    }

    /**
     * @return the idFacturaAS
     */
    public String getIdFacturaAS() {
        return idFacturaAS;
    }

    /**
     * @param idFacturaAS the idFacturaAS to set
     */
    public void setIdFacturaAS(String idFacturaAS) {
        this.idFacturaAS = idFacturaAS;
    }

    /**
     * @return the listaLiosAgrupada
     */
    public List<Lios> getListaLiosAgrupada() {
        return listaLiosAgrupada;
    }

    /**
     * @param listaLiosAgrupada the listaLiosAgrupada to set
     */
    public void setListaLiosAgrupada(List<Lios> listaLiosAgrupada) {
        this.listaLiosAgrupada = listaLiosAgrupada;
    }

    /**
     * @return the Comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param Comentario the Comentario to set
     */
    public void setComentario(String Comentario) {
        this.comentario = Comentario;
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
     * @return the listaLiosExluidos
     */
    public List<Lios> getListaLiosExluidos() {
        return listaLiosExluidos;
    }

    /**
     * @param listaLiosExluidos the listaLiosExluidos to set
     */
    public void setListaLiosExluidos(List<Lios> listaLiosExluidos) {
        this.listaLiosExluidos = listaLiosExluidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
