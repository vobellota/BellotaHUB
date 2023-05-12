/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.distribucion;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ebedoya
 */
@Entity
@Table(name = "estibas", catalog = "BellotaHUB", schema = "distribucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estibas.findAll", query = "SELECT e FROM Estibas e")
    , @NamedQuery(name = "Estibas.findByIdEstiba", query = "SELECT e FROM Estibas e WHERE e.idEstiba = :idEstiba")
    , @NamedQuery(name = "Estibas.findByCodigoEstiba", query = "SELECT e FROM Estibas e WHERE e.codigoEstiba = :codigoEstiba")
    , @NamedQuery(name = "Estibas.findByDescripcionEstiba", query = "SELECT e FROM Estibas e WHERE e.descripcionEstiba = :descripcionEstiba")
    , @NamedQuery(name = "Estibas.findByPermiteAgruparReferencias", query = "SELECT e FROM Estibas e WHERE e.permiteAgruparReferencias = :permiteAgruparReferencias")
    , @NamedQuery(name = "Estibas.findByPesoMaximoSoportado", query = "SELECT e FROM Estibas e WHERE e.pesoMaximoSoportado = :pesoMaximoSoportado")
    , @NamedQuery(name = "Estibas.findByAncho", query = "SELECT e FROM Estibas e WHERE e.ancho = :ancho")
    , @NamedQuery(name = "Estibas.findByLargo", query = "SELECT e FROM Estibas e WHERE e.largo = :largo")
    , @NamedQuery(name = "Estibas.findByAlturaMaximaEstiba", query = "SELECT e FROM Estibas e WHERE e.alturaMaximaEstiba = :alturaMaximaEstiba")
    , @NamedQuery(name = "Estibas.findByFactorEstiba", query = "SELECT e FROM Estibas e WHERE e.factorEstiba = :factorEstiba")
    , @NamedQuery(name = "Estibas.findByEstadoEstiba", query = "SELECT e FROM Estibas e WHERE e.estadoEstiba = :estadoEstiba")})
public class Estibas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_estiba")
    private Integer idEstiba;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "codigo_estiba")
    private String codigoEstiba;
    @Size(max = 2147483647)
    @Column(name = "descripcion_estiba")
    private String descripcionEstiba;
    @Size(max = 2147483647)
    @Column(name = "permite_agrupar_referencias")
    private String permiteAgruparReferencias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso_maximo_soportado")
    private double pesoMaximoSoportado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ancho")
    private double ancho;
    @Basic(optional = false)
    @NotNull
    @Column(name = "largo")
    private double largo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "altura_maxima_estiba")
    private double alturaMaximaEstiba;
    @Basic(optional = false)
    @NotNull
    @Column(name = "factor_estiba")
    private double factorEstiba;
    @Size(max = 2147483647)
    @Column(name = "estado_estiba")
    private String estadoEstiba;
    @OneToMany(mappedBy = "idEstibaAsignada", fetch = FetchType.LAZY)
    private Collection<UnidadesEmpaque> unidadesEmpaqueCollection;

    public Estibas() {
    }

    public Estibas(Integer idEstiba) {
        this.idEstiba = idEstiba;
    }

    public Estibas(Integer idEstiba, String codigoEstiba, double pesoMaximoSoportado, double ancho, double largo, double alturaMaximaEstiba, double factorEstiba) {
        this.idEstiba = idEstiba;
        this.codigoEstiba = codigoEstiba;
        this.pesoMaximoSoportado = pesoMaximoSoportado;
        this.ancho = ancho;
        this.largo = largo;
        this.alturaMaximaEstiba = alturaMaximaEstiba;
        this.factorEstiba = factorEstiba;
    }

    public Integer getIdEstiba() {
        return idEstiba;
    }

    public void setIdEstiba(Integer idEstiba) {
        this.idEstiba = idEstiba;
    }

    public String getCodigoEstiba() {
        return codigoEstiba;
    }

    public void setCodigoEstiba(String codigoEstiba) {
        this.codigoEstiba = codigoEstiba;
    }

    public String getDescripcionEstiba() {
        return descripcionEstiba;
    }

    public void setDescripcionEstiba(String descripcionEstiba) {
        this.descripcionEstiba = descripcionEstiba;
    }

    public String getPermiteAgruparReferencias() {
        return permiteAgruparReferencias;
    }

    public void setPermiteAgruparReferencias(String permiteAgruparReferencias) {
        this.permiteAgruparReferencias = permiteAgruparReferencias;
    }

    public double getPesoMaximoSoportado() {
        return pesoMaximoSoportado;
    }

    public void setPesoMaximoSoportado(double pesoMaximoSoportado) {
        this.pesoMaximoSoportado = pesoMaximoSoportado;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getLargo() {
        return largo;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public double getAlturaMaximaEstiba() {
        return alturaMaximaEstiba;
    }

    public void setAlturaMaximaEstiba(double alturaMaximaEstiba) {
        this.alturaMaximaEstiba = alturaMaximaEstiba;
    }

    public double getFactorEstiba() {
        return factorEstiba;
    }

    public void setFactorEstiba(double factorEstiba) {
        this.factorEstiba = factorEstiba;
    }

    public String getEstadoEstiba() {
        return estadoEstiba;
    }

    public void setEstadoEstiba(String estadoEstiba) {
        this.estadoEstiba = estadoEstiba;
    }

    @XmlTransient
    public Collection<UnidadesEmpaque> getUnidadesEmpaqueCollection() {
        return unidadesEmpaqueCollection;
    }

    public void setUnidadesEmpaqueCollection(Collection<UnidadesEmpaque> unidadesEmpaqueCollection) {
        this.unidadesEmpaqueCollection = unidadesEmpaqueCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstiba != null ? idEstiba.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estibas)) {
            return false;
        }
        Estibas other = (Estibas) object;
        if ((this.idEstiba == null && other.idEstiba != null) || (this.idEstiba != null && !this.idEstiba.equals(other.idEstiba))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Estiba: " +descripcionEstiba+ "";
    }
    
}
