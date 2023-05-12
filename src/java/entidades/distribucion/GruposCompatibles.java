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
@Table(name = "grupos_compatibles", catalog = "BellotaHUB", schema = "distribucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GruposCompatibles.findAll", query = "SELECT g FROM GruposCompatibles g")
    , @NamedQuery(name = "GruposCompatibles.findByIdGrupoCompatible", query = "SELECT g FROM GruposCompatibles g WHERE g.idGrupoCompatible = :idGrupoCompatible")
    , @NamedQuery(name = "GruposCompatibles.findByCodigoGrupo", query = "SELECT g FROM GruposCompatibles g WHERE g.codigoGrupo = :codigoGrupo")
    , @NamedQuery(name = "GruposCompatibles.findByDescripcion", query = "SELECT g FROM GruposCompatibles g WHERE g.descripcion = :descripcion")
    , @NamedQuery(name = "GruposCompatibles.findByPermiteAgruparReferencias", query = "SELECT g FROM GruposCompatibles g WHERE g.permiteAgruparReferencias = :permiteAgruparReferencias")
    , @NamedQuery(name = "GruposCompatibles.findByMetodoCalculo", query = "SELECT g FROM GruposCompatibles g WHERE g.metodoCalculo = :metodoCalculo")
    , @NamedQuery(name = "GruposCompatibles.findByMaximoCajasAgrupar", query = "SELECT g FROM GruposCompatibles g WHERE g.maximoCajasAgrupar = :maximoCajasAgrupar")
    , @NamedQuery(name = "GruposCompatibles.findByPesoMaximoLio", query = "SELECT g FROM GruposCompatibles g WHERE g.pesoMaximoLio = :pesoMaximoLio")
    , @NamedQuery(name = "GruposCompatibles.findByAncho", query = "SELECT g FROM GruposCompatibles g WHERE g.ancho = :ancho")
    , @NamedQuery(name = "GruposCompatibles.findByAlto", query = "SELECT g FROM GruposCompatibles g WHERE g.alto = :alto")
    , @NamedQuery(name = "GruposCompatibles.findByLargo", query = "SELECT g FROM GruposCompatibles g WHERE g.largo = :largo")
    , @NamedQuery(name = "GruposCompatibles.findByFactorPesoVolumen", query = "SELECT g FROM GruposCompatibles g WHERE g.factorPesoVolumen = :factorPesoVolumen")
    , @NamedQuery(name = "GruposCompatibles.findByEstadoGruposCompatibles", query = "SELECT g FROM GruposCompatibles g WHERE g.estadoGruposCompatibles = :estadoGruposCompatibles")})
public class GruposCompatibles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_grupo_compatible")
    private Integer idGrupoCompatible;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "codigo_grupo")
    private String codigoGrupo;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 2147483647)
    @Column(name = "permite_agrupar_referencias")
    private String permiteAgruparReferencias;
    @Size(max = 2147483647)
    @Column(name = "metodo_calculo")
    private String metodoCalculo;
    @Column(name = "maximo_cajas_agrupar")
    private Integer maximoCajasAgrupar;
    @Column(name = "peso_maximo_lio")
    private double pesoMaximoLio;
    @Column(name = "ancho")
    private double ancho;
    @Column(name = "alto")
    private double alto;
    @Column(name = "largo")
    private double largo;
    @Column(name = "factor_peso_volumen")
    private double factorPesoVolumen;
    @Size(max = 2147483647)
    @Column(name = "estado_grupos_compatibles")
    private String estadoGruposCompatibles;
    @OneToMany(mappedBy = "idGrupoCompatible", fetch = FetchType.LAZY)
    private Collection<UnidadesEmpaque> unidadesEmpaqueCollection;

    public GruposCompatibles() {
    }

    public GruposCompatibles(Integer idGrupoCompatible) {
        this.idGrupoCompatible = idGrupoCompatible;
    }

    public GruposCompatibles(Integer idGrupoCompatible, String codigoGrupo) {
        this.idGrupoCompatible = idGrupoCompatible;
        this.codigoGrupo = codigoGrupo;
    }

    public Integer getIdGrupoCompatible() {
        return idGrupoCompatible;
    }

    public void setIdGrupoCompatible(Integer idGrupoCompatible) {
        this.idGrupoCompatible = idGrupoCompatible;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(String codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPermiteAgruparReferencias() {
        return permiteAgruparReferencias;
    }

    public void setPermiteAgruparReferencias(String permiteAgruparReferencias) {
        this.permiteAgruparReferencias = permiteAgruparReferencias;
    }

    public String getMetodoCalculo() {
        return metodoCalculo;
    }

    public void setMetodoCalculo(String metodoCalculo) {
        this.metodoCalculo = metodoCalculo;
    }

    public Integer getMaximoCajasAgrupar() {
        return maximoCajasAgrupar;
    }

    public void setMaximoCajasAgrupar(Integer maximoCajasAgrupar) {
        this.maximoCajasAgrupar = maximoCajasAgrupar;
    }

    public double getPesoMaximoLio() {
        return pesoMaximoLio;
    }

    public void setPesoMaximoLio(double pesoMaximoLio) {
        this.pesoMaximoLio = pesoMaximoLio;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public double getLargo() {
        return largo;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public double getFactorPesoVolumen() {
        return factorPesoVolumen;
    }

    public void setFactorPesoVolumen(double factorPesoVolumen) {
        this.factorPesoVolumen = factorPesoVolumen;
    }

    public String getEstadoGruposCompatibles() {
        return estadoGruposCompatibles;
    }

    public void setEstadoGruposCompatibles(String estadoGruposCompatibles) {
        this.estadoGruposCompatibles = estadoGruposCompatibles;
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
        hash += (idGrupoCompatible != null ? idGrupoCompatible.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GruposCompatibles)) {
            return false;
        }
        GruposCompatibles other = (GruposCompatibles) object;
        if ((this.idGrupoCompatible == null && other.idGrupoCompatible != null) || (this.idGrupoCompatible != null && !this.idGrupoCompatible.equals(other.idGrupoCompatible))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Grupo: " +descripcion+"";
    }
    
}
