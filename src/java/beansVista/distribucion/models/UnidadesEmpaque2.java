/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion.models;

import entidades.distribucion.ProductosCompatibles;

/**
 *
 * @author crojas
 */
public class UnidadesEmpaque2 {

    private String idUnidadEmpaque;
    private ProductosCompatibles idProductosCompatibles;
    private double cantidad;
    private String descripcion;
    private String cantidadDescrita;

    public UnidadesEmpaque2() {
    }

    /**
     * @return the idUnidadEmpaque
     */
    public String getIdUnidadEmpaque() {
        return idUnidadEmpaque;
    }

    /**
     * @param idUnidadEmpaque the idUnidadEmpaque to set
     */
    public void setIdUnidadEmpaque(String idUnidadEmpaque) {
        this.idUnidadEmpaque = idUnidadEmpaque;
    }

    public UnidadesEmpaque2(String idUnidadEmpaque, ProductosCompatibles idProductosCompatibles, double cantidad, String descripcion, String cantidadDescrita) {
        this.idUnidadEmpaque = idUnidadEmpaque;
        this.idProductosCompatibles = idProductosCompatibles;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.cantidadDescrita = cantidadDescrita;
    }

    /**
     * @return the cantidad
     */
    public double getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the cantidadDescrita
     */
    public String getCantidadDescrita() {
        return cantidadDescrita;
    }

    /**
     * @param cantidadDescrita the cantidadDescrita to set
     */
    public void setCantidadDescrita(String cantidadDescrita) {
        this.cantidadDescrita = cantidadDescrita;
    }

    /**
     * @return the idProductosCompatibles
     */
    public ProductosCompatibles getIdProductosCompatibles() {
        return idProductosCompatibles;
    }

    /**
     * @param idProductosCompatibles the idProductosCompatibles to set
     */
    public void setIdProductosCompatibles(ProductosCompatibles idProductosCompatibles) {
        this.idProductosCompatibles = idProductosCompatibles;
    }

}
