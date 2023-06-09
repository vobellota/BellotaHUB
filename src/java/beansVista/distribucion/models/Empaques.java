/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion.models;

import entidades.distribucion.GruposCompatibles;
import entidades.distribucion.ProductosCompatibles;
import entidades.distribucion.UnidadesEmpaque;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author crojas
 */
@ManagedBean
@ViewScoped
public class Empaques {

    /**
     * @return the prefijo
     */
    private ProductosCompatibles productoCompatible;
    private UnidadesEmpaque unidadEmpaqueAux;
    private double cantidadVendida;
    private double unidad;
    private int idEmpaque;
    private String idFactura;
    private String descripcionFactura;
    private String prefijo;
    private String nroFactura;
    private double embalaje;
    private double peso;
    private double volumen;
    private String subNroFactura;

    public Empaques() {
    }

    public Empaques(ProductosCompatibles productoCompatible, UnidadesEmpaque unidadEmpaqueAux, double cantidadVendida, double embalaje, int idEmpaque, double peso, double volumen) {
        this.productoCompatible = productoCompatible;
        this.unidadEmpaqueAux = unidadEmpaqueAux;
        this.cantidadVendida = cantidadVendida;
        this.embalaje = embalaje;
        this.idEmpaque = idEmpaque;
        this.peso = peso;
        this.volumen = volumen;
    }

    public Empaques(ProductosCompatibles productoCompatible, UnidadesEmpaque unidadEmpaqueAux, double cantidadVendida, int idEmpaque, double peso, double volumen) {
        this.productoCompatible = productoCompatible;
        this.unidadEmpaqueAux = unidadEmpaqueAux;
        this.cantidadVendida = cantidadVendida;
        this.idEmpaque = idEmpaque;
        this.peso = peso;
        this.volumen = volumen;
    }

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

    /**
     * @return the idFactura
     */
    public String getIdFactura() {
        return idFactura;
    }

    /**
     * @param idFactura the idFactura to set
     */
    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    /**
     * @return the descripcionFactura
     */
    public String getDescripcionFactura() {
        return descripcionFactura;
    }

    /**
     * @param descripcionFactura the descripcionFactura to set
     */
    public void setDescripcionFactura(String descripcionFactura) {
        this.descripcionFactura = descripcionFactura;
    }

    public double getCantidadVendida() {
        return cantidadVendida;
    }

    /**
     * @param cantidadVendida the cantidadVendida to set
     */
    public void setCantidadVendida(double cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    /**
     * @return the unidad
     */
    public double getUnidad() {
        return unidad;
    }

    /**
     * @param unidad the unidad to set
     */
    public void setUnidad(double unidad) {
        this.unidad = unidad;
    }

    /**
     * @return the productoCompatible
     */
    public ProductosCompatibles getProductoCompatible() {
        return productoCompatible;
    }

    /**
     * @param productoCompatible the productoCompatible to set
     */
    public void setProductoCompatible(ProductosCompatibles productoCompatible) {
        this.productoCompatible = productoCompatible;
    }

    /**
     * @return the idEmpaque
     */
    public int getIdEmpaque() {
        return idEmpaque;
    }

    /**
     * @param idEmpaque the idEmpaque to set
     */
    public void setIdEmpaque(int idEmpaque) {
        this.idEmpaque = idEmpaque;
    }

    /**
     * @return the embalaje
     */
    public double getEmbalaje() {
        return embalaje;
    }

    /**
     * @param embalaje the embalaje to set
     */
    public void setEmbalaje(double embalaje) {
        this.embalaje = embalaje;
    }

    /**
     * @return the unidadEmpaqueAux
     */
    public UnidadesEmpaque getUnidadEmpaqueAux() {
        return unidadEmpaqueAux;
    }

    /**
     * @param unidadEmpaqueAux the unidadEmpaqueAux to set
     */
    public void setUnidadEmpaqueAux(UnidadesEmpaque unidadEmpaqueAux) {
        this.unidadEmpaqueAux = unidadEmpaqueAux;
    }

    /**
     * @return the peso
     */
    public double getPeso() {
        return peso;
    }

    /**
     * @param peso the peso to set
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * @return the volumen
     */
    public double getVolumen() {
        return volumen;
    }

    /**
     * @param volumen the volumen to set
     */
    public void setVolumen(double volumen) {
        this.volumen = volumen;
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
