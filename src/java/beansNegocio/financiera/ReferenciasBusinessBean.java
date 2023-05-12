/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansNegocio.financiera;

import dao.financiera.REFERENCIASDaoDB2;
import entidades.financiera.REFERENCIAS;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import sys.util.Sesion;

/**
 *
 * @author ebedoya
 */
@Stateless
public class ReferenciasBusinessBean implements IReferenciasBusinessBean{
    @Override
    public void onUpdatedToDao(List<REFERENCIAS> referenciasdto, String documentoUpdate, String ref1Update, String ref2Update) {
        REFERENCIASDaoDB2 objupdate = new REFERENCIASDaoDB2(Sesion.getSesion());
        Date fecha = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < referenciasdto.size(); i++) {
            objupdate.update(referenciasdto.get(i), Integer.parseInt(objSDF.format(fecha)),documentoUpdate, ref1Update, ref2Update);
        }
    }
    
    
}
