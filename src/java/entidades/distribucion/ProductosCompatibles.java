/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.distribucion;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "productos_compatibles", catalog = "BellotaHUB", schema = "distribucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductosCompatibles.findAll", query = "SELECT p FROM ProductosCompatibles p")
    , @NamedQuery(name = "ProductosCompatibles.findByIdProductosCompatibles", query = "SELECT p FROM ProductosCompatibles p WHERE p.idProductosCompatibles = :idProductosCompatibles")
    , @NamedQuery(name = "ProductosCompatibles.findByCodigoProducto", query = "SELECT p FROM ProductosCompatibles p WHERE p.codigoProducto = :codigoProducto")
    , @NamedQuery(name = "ProductosCompatibles.findByDescripcion", query = "SELECT p FROM ProductosCompatibles p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "ProductosCompatibles.findByLargo", query = "SELECT p FROM ProductosCompatibles p WHERE p.largo = :largo")
    , @NamedQuery(name = "ProductosCompatibles.findByAlto", query = "SELECT p FROM ProductosCompatibles p WHERE p.alto = :alto")
    , @NamedQuery(name = "ProductosCompatibles.findByAncho", query = "SELECT p FROM ProductosCompatibles p WHERE p.ancho = :ancho")
    , @NamedQuery(name = "ProductosCompatibles.findByPesoCajaMaster", query = "SELECT p FROM ProductosCompatibles p WHERE p.pesoCajaMaster = :pesoCajaMaster")
    , @NamedQuery(name = "ProductosCompatibles.findByEstadoProducto", query = "SELECT p FROM ProductosCompatibles p WHERE p.estadoProducto = :estadoProducto")})
public class ProductosCompatibles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_productos_compatibles")
    private Integer idProductosCompatibles;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo_producto")
    private String codigoProducto;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "largo")
    private double largo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "alto")
    private double alto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ancho")
    private double ancho;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso_caja_master")
    private double pesoCajaMaster;
    @Size(max = 2147483647)
    @Column(name = "estado_producto")
    private String estadoProducto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoProducto", fetch = FetchType.LAZY)
    private Collection<UnidadesEmpaque> unidadesEmpaqueCollection;

    public ProductosCompatibles() {
    }

    public ProductosCompatibles(Integer idProductosCompatibles) {
        this.idProductosCompatibles = idProductosCompatibles;
    }

    public ProductosCompatibles(Integer idProductosCompatibles, String codigoProducto, double largo, double alto, double ancho, double pesoCajaMaster) {
        this.idProductosCompatibles = idProductosCompatibles;
        this.codigoProducto = codigoProducto;
        this.largo = largo;
        this.alto = alto;
        this.ancho = ancho;
        this.pesoCajaMaster = pesoCajaMaster;
    }

    public Integer getIdProductosCompatibles() {
        return idProductosCompatibles;
    }

    public void setIdProductosCompatibles(Integer idProductosCompatibles) {
        this.idProductosCompatibles = idProductosCompatibles;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLargo() {
        return largo;
    }

    public void setLargo(double largo) {
        this.largo = largo;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getPesoCajaMaster() {
        return pesoCajaMaster;
    }

    public void setPesoCajaMaster(double pesoCajaMaster) {
        this.pesoCajaMaster = pesoCajaMaster;
    }

    public String getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(String estadoProducto) {
        this.estadoProducto = estadoProducto;
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
        hash += (idProductosCompatibles != null ? idProductosCompatibles.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductosCompatibles)) {
            return false;
        }
        ProductosCompatibles other = (ProductosCompatibles) object;
        if ((this.idProductosCompatibles == null && other.idProductosCompatibles != null) || (this.idProductosCompatibles != null && !this.idProductosCompatibles.equals(other.idProductosCompatibles))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigoProducto;
    }
    
}
