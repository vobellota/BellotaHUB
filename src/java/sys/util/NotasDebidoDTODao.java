/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.SQLException;
import java.util.List;
import entidades.financiera.NOTASDEBITO;
/**
 *
 * @author ebedoya
 */
public interface NotasDebidoDTODao {
    
    public int update(NOTASDEBITO notasdebito, int fechaVencEntero) throws SQLException;
    public List<NOTASDEBITO> select() throws SQLException;
}
