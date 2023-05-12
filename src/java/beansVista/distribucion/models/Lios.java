/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.distribucion.models;

import entidades.distribucion.ProductosCompatibles;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author crojas
 */
@ManagedBean
@ViewScoped
public class Lios {

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

    private int idLio;
    private List<Empaques> empaques;
    private int embalaje;
    private int idEmpaque;
    private int idGrupo;
    private int idProducto;
    private String productoDescripcion;
    private String grupoDescripcion;
    private String codigoProducto;
    private int idUnidadEmpaque;
    private String unidadEmpaqueDescripcion;
    private int estado = -1;
    private int idLineaLio;
    private double peso;
    private double volumen;

    public Lios() {
        idLio = 0;
        empaques = new ArrayList<>();
        this.estado = -1;
    }

    public Lios(int idLio, List<Empaques> empaques) {
        this.idLio = idLio;
        this.empaques = empaques;
        this.estado = -1;
    }

    public Lios(int idLio, Empaques empaques) {
        this.idLio = idLio;
        this.empaques = new ArrayList<>();
        this.empaques.add(empaques);
        this.estado = -1;
    }

    public Lios(int idLio, int embalaje, int idEmpaque, String codigoProducto, String productoDescripcion, int idGrupo, String grupoDescripcion, Double peso, Double volumen) {
        this.idLio = idLio;
        this.embalaje = embalaje;
        this.idEmpaque = idEmpaque;
        this.idGrupo = idGrupo;
        this.codigoProducto = codigoProducto;
        this.productoDescripcion = productoDescripcion;
        this.grupoDescripcion = grupoDescripcion;
        this.estado = -1;
        this.peso = peso;
        this.volumen = volumen;
    }

    public Lios(int idLio, int embalaje, int idEmpaque, String codigoProducto, String productoDescripcion, int idGrupo, String grupoDescripcion, int idProducto, int idUnidadEmpaque, String unidadEmpaqueDescripcion, Double peso, Double volumen) {
        this.idLio = idLio;
        this.embalaje = embalaje;
        this.idEmpaque = idEmpaque;
        this.idGrupo = idGrupo;
        this.codigoProducto = codigoProducto;
        this.productoDescripcion = productoDescripcion;
        this.grupoDescripcion = grupoDescripcion;
        this.idProducto = idProducto;
        this.idUnidadEmpaque = idUnidadEmpaque;
        this.unidadEmpaqueDescripcion = unidadEmpaqueDescripcion;
        this.estado = -1;
        this.peso = peso;
        this.volumen = volumen;
    }

    public Lios(int idLineaLio, int idLio, int embalaje, int idEmpaque, int idGrupo, int idProducto, String productoDescripcion, String grupoDescripcion, String codigoProducto, int idUnidadEmpaque, String unidadEmpaqueDescripcion, int estado, Double peso, Double volumen) {
        this.idLineaLio = idLineaLio;
        this.idLio = idLio;
        this.embalaje = embalaje;
        this.idEmpaque = idEmpaque;
        this.idGrupo = idGrupo;
        this.idProducto = idProducto;
        this.productoDescripcion = productoDescripcion;
        this.grupoDescripcion = grupoDescripcion;
        this.codigoProducto = codigoProducto;
        this.idUnidadEmpaque = idUnidadEmpaque;
        this.unidadEmpaqueDescripcion = unidadEmpaqueDescripcion;
        this.estado = estado;
        this.peso = peso;
        this.volumen = volumen;
    }

    /**
     * @return the idLio
     */
    public int getIdLio() {
        return idLio;
    }

    /**
     * @param idLio the idLio to set
     */
    public void setIdLio(int idLio) {
        this.idLio = idLio;
    }

    /**
     * @return the empaques
     */
    public List<Empaques> getEmpaques() {
        return empaques;
    }

    /**
     * @param empaques the empaques to set
     */
    public void setEmpaques(List<Empaques> empaques) {
        this.empaques = empaques;
    }

    /**
     * @return the embalaje
     */
    public int getEmbalaje() {
        return embalaje;
    }

    /**
     * @param embalaje the embalaje to set
     */
    public void setEmbalaje(int embalaje) {
        this.embalaje = embalaje;
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
     * @return the idGrupo
     */
    public int getIdGrupo() {
        return idGrupo;
    }

    /**
     * @param idGrupo the idGrupo to set
     */
    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * @return the idProducto
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * @return the productoDescripcion
     */
    public String getProductoDescripcion() {
        return productoDescripcion;
    }

    /**
     * @param productoDescripcion the productoDescripcion to set
     */
    public void setProductoDescripcion(String productoDescripcion) {
        this.productoDescripcion = productoDescripcion;
    }

    /**
     * @return the grupoDescripcion
     */
    public String getGrupoDescripcion() {
        return grupoDescripcion;
    }

    /**
     * @param grupoDescripcion the grupoDescripcion to set
     */
    public void setGrupoDescripcion(String grupoDescripcion) {
        this.grupoDescripcion = grupoDescripcion;
    }

    /**
     * @return the codigoProducto
     */
    public String getCodigoProducto() {
        return codigoProducto;
    }

    /**
     * @param codigoProducto the codigoProducto to set
     */
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    /**
     * @return the idUnidadEmpaque
     */
    public int getIdUnidadEmpaque() {
        return idUnidadEmpaque;
    }

    /**
     * @param idUnidadEmpaque the idUnidadEmpaque to set
     */
    public void setIdUnidadEmpaque(int idUnidadEmpaque) {
        this.idUnidadEmpaque = idUnidadEmpaque;
    }

    /**
     * @return the unidadEmpaqueDescripcion
     */
    public String getUnidadEmpaqueDescripcion() {
        return unidadEmpaqueDescripcion;
    }

    /**
     * @param unidadEmpaqueDescripcion the unidadEmpaqueDescripcion to set
     */
    public void setUnidadEmpaqueDescripcion(String unidadEmpaqueDescripcion) {
        this.unidadEmpaqueDescripcion = unidadEmpaqueDescripcion;
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
     * @return the idLineaLio
     */
    public int getIdLineaLio() {
        return idLineaLio;
    }

    /**
     * @param idLineaLio the idLineaLio to set
     */
    public void setIdLineaLio(int idLineaLio) {
        this.idLineaLio = idLineaLio;
    }

}
