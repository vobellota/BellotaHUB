/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.SQLException;
import java.util.List;
import entidades.financiera.CXP;
/**
 *
 * @author ebedoya
 * Metodos abstractos del CRUD
 */
public interface CxpDTODao {
    
    public int insert(CXP cxp) throws SQLException;
    public int update(CXP cxp) throws SQLException;
    public int delete(CXP cxp) throws SQLException;
    public List<CXP> select() throws SQLException;
    public void select_one_to_update(CXP cxp) throws SQLException;
    public int update_one(CXP cxp, double tasa1, double tasa2, double tasa3) throws SQLException;
}