/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.comercial;

import entidades.comercial.preciosEntity;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ggaviria
 */
public interface PreciosDTODaoDB2 {
    
    public List<preciosEntity> select_all() throws SQLException;
    
}
