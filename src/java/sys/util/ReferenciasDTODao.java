/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.SQLException;
import java.util.List;
import entidades.financiera.REFERENCIAS;
/**
 *
 * @author ebedoya
 */
public interface ReferenciasDTODao {
    
    public List<REFERENCIAS> filter(String libro, String periodo, int anio, String nroComprobante) throws SQLException;
    public int update(REFERENCIAS referencias, int fecha, String documentoUpdate, String ref1Update, String ref2Update);
    
}
