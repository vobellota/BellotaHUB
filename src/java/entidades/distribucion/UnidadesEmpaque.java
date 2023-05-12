/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.distribucion;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ebedoya
 */
@Entity
@Table(name = "unidades_empaque", catalog = "BellotaHUB", schema = "distribucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadesEmpaque.findAll", query = "SELECT u FROM UnidadesEmpaque u")
    , @NamedQuery(name = "UnidadesEmpaque.findByIdUnidadEmpaque", query = "SELECT u FROM UnidadesEmpaque u WHERE u.idUnidadEmpaque = :idUnidadEmpaque")
    , @NamedQuery(name = "UnidadesEmpaque.findByCantidad", query = "SELECT u FROM UnidadesEmpaque u WHERE u.cantidad = :cantidad")
    , @NamedQuery(name = "UnidadesEmpaque.findByDescripcion", query = "SELECT u FROM UnidadesEmpaque u WHERE u.descripcion = :descripcion")
    , @NamedQuery(name = "UnidadesEmpaque.findByCantidadDescrita", query = "SELECT u FROM UnidadesEmpaque u WHERE u.cantidadDescrita = :cantidadDescrita")})
public class UnidadesEmpaque implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_unidad_empaque")
    private Integer idUnidadEmpaque;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 50)
    @Column(name = "cantidad_descrita")
    private String cantidadDescrita;
    @JoinColumn(name = "id_estiba_asignada", referencedColumnName = "id_estiba")
    @ManyToOne(fetch = FetchType.LAZY)
    private Estibas idEstibaAsignada;
    @JoinColumn(name = "id_grupo_compatible", referencedColumnName = "id_grupo_compatible")
    @ManyToOne(fetch = FetchType.LAZY)
    private GruposCompatibles idGrupoCompatible;
    @JoinColumn(name = "codigo_producto", referencedColumnName = "codigo_producto")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProductosCompatibles codigoProducto;

    public UnidadesEmpaque() {
    }

    public UnidadesEmpaque(Integer idUnidadEmpaque) {
        this.idUnidadEmpaque = idUnidadEmpaque;
    }

    public Integer getIdUnidadEmpaque() {
        return idUnidadEmpaque;
    }

    public void setIdUnidadEmpaque(Integer idUnidadEmpaque) {
        this.idUnidadEmpaque = idUnidadEmpaque;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidadDescrita() {
        return cantidadDescrita;
    }

    public void setCantidadDescrita(String cantidadDescrita) {
        this.cantidadDescrita = cantidadDescrita;
    }

    public Estibas getIdEstibaAsignada() {
        return idEstibaAsignada;
    }

    public void setIdEstibaAsignada(Estibas idEstibaAsignada) {
        this.idEstibaAsignada = idEstibaAsignada;
    }

    public GruposCompatibles getIdGrupoCompatible() {
        return idGrupoCompatible;
    }

    public void setIdGrupoCompatible(GruposCompatibles idGrupoCompatible) {
        this.idGrupoCompatible = idGrupoCompatible;
    }

    public ProductosCompatibles getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(ProductosCompatibles codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUnidadEmpaque != null ? idUnidadEmpaque.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadesEmpaque)) {
            return false;
        }
        UnidadesEmpaque other = (UnidadesEmpaque) object;
        if ((this.idUnidadEmpaque == null && other.idUnidadEmpaque != null) || (this.idUnidadEmpaque != null && !this.idUnidadEmpaque.equals(other.idUnidadEmpaque))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "entidades.distribucion.UnidadesEmpaque[ idUnidadEmpaque=" + idUnidadEmpaque + " ]";
        return descripcion;
    }
    
}
