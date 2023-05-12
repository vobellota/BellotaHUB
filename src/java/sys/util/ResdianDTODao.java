/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.sql.SQLException;
import java.util.List;
import entidades.financiera.RESDIAN;

/**
 *
 * @author ebedoya
 */
public interface ResdianDTODao {
    
    public int insert(RESDIAN resdian) throws SQLException;
    public int update(RESDIAN resdian) throws SQLException;
    public int delete(RESDIAN resdian) throws SQLException;
    public List<RESDIAN> select() throws SQLException;
    public List<RESDIAN> select_range_dates(int fecha1, int fecha2) throws SQLException;
    public List<RESDIAN> select_range_dates_nros(int nroini, int nrofnl) throws SQLException;
}
