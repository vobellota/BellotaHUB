/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansNegocio.distribucion;

import beansVista.distribucion.models.Empaques;
import beansVista.distribucion.models.Facturas;
import beansVista.distribucion.models.Lios;
import beansVista.distribucion.models.ListasEmpaqueFac;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Esta clase procesa la información del objeto listas de empaque e interconecta
 * su capa de prsistencia con su capa de presentación
 *
 * @author Camilo Rojas
 * @version 1.0 2019-09-13
 */
@Local
public interface IListasEmpaqueBusinessBean {

    public List<Facturas> getBillsLios(List<ListasEmpaqueFac> listaEmpaqueSeleccionada);

    public List<ListasEmpaqueFac> onConvertVariables(String prefijoFactura, String subOrdenF, int numeroInicial, int numeroFinal, Date fechaInicio, Date fechaFin);

    public boolean postGuardarListaEmpaque(Facturas facAux);
    
    public boolean putBillHeaderState(Facturas facAux);
    
    public void updateBillAllLines(Facturas facAux);
    
    public void postBillGroupList(Facturas facAux);
    
    public List<Facturas> getCreatedBillsLios(List<ListasEmpaqueFac> listaEmpaqueSeleccionada);
    
    public HSSFWorkbook generateXLSDetailsList(List<Facturas> listBills);
}
