/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.SQLException;
import java.util.List;
import entidades.financiera.PUC;

/**
 *
 * @author ebedoya
 */
public interface PucDTODao {
    
    public int insert(PUC pucdto) throws SQLException;
    public int update(PUC pucdto) throws SQLException;
    public int delete(PUC pucdto) throws SQLException;
    public List<PUC> select() throws SQLException;
}
