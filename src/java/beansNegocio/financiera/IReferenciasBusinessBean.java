/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansNegocio.financiera;

import entidades.financiera.REFERENCIAS;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author ebedoya
 */
@Local
public interface IReferenciasBusinessBean {
    
    public void onUpdatedToDao(List<REFERENCIAS> referenciasdto, String documentoUpdate, String ref1Update, String ref2Update);
}
