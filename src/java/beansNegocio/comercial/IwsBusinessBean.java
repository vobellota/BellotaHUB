/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansNegocio.comercial;

import entidades.comercial.preciosEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ebedoya
 */
@Local
public interface IwsBusinessBean {
//    public List<Respuesta> wsSentJson(List<precios> preciosNuevaLista);
    public String wsSentJson(List<preciosEntity> preciosNuevaLista);
}
