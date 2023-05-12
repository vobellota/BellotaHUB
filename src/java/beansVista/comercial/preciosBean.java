/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.comercial;

import beansNegocio.comercial.wsBusinessBean;
import dao.comercial.preciosDaoDB2;
import entidades.comercial.precios;
import entidades.comercial.preciosEntity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import sys.util.ExceptionManager;
import sys.util.Sesion;
import java.text.SimpleDateFormat;

/**
 *
 * @author ebedoya
 */
@ViewScoped
@ManagedBean(name = "preciosBean")
public class preciosBean implements Serializable {

    /**
     * Creates a new instance of preciosBean
     */
    private String tipoPrecio;
    private String moneda;
    private precios preciosObj = new precios();
    private preciosEntity selectPriceRow;
    private List<precios> preciosNuevaLista = new ArrayList<>();
    private List<preciosEntity> preciosNuevaListaTranformed = new ArrayList<>();
    private Date fechaInicio;
    private Date fechaFin;
    private List<preciosEntity> listaPrecios;
    private List<preciosEntity> filteredListaPrecios;
    private String listaOrigen;
    private String listaDestino;
    private Integer feInicial;//Fecha inicial de la copia de una lista a otra
    private Integer feFinal;//Fecha Final de la copia de una lista a otra

    private UploadedFile uploadedFile;

    /// /**********************
    // Agregar un registro completo
    // ****************************
    public void agregarPrefijo() {
        precios pres = new precios();
        pres.setCompania("2");
        pres.setMoneda(moneda);
        pres.setTipoPrecio(tipoPrecio);
        preciosNuevaLista.add(pres);
    }

    // Remover un registro completo
    // ****************************
    public void removerPrefijo() {
        if (preciosNuevaLista.size() > 1) {
            preciosNuevaLista.remove(preciosNuevaLista.size() - 1);
            preciosNuevaListaTranformed.clear();
        }
    }

    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public precios getPreciosObj() {
        return preciosObj;
    }

    public void setPreciosObj(precios preciosObj) {
        this.preciosObj = preciosObj;
    }

    public List<precios> getPreciosNuevaLista() {
        return preciosNuevaLista;
    }

    public void setPreciosNuevaLista(List<precios> preciosNuevaLista) {
        this.preciosNuevaLista = preciosNuevaLista;
    }

    public List<preciosEntity> getPreciosNuevaListaTranformed() {
        return preciosNuevaListaTranformed;
    }

    public void setPreciosNuevaListaTranformed(List<preciosEntity> preciosNuevaListaTranformed) {
        this.preciosNuevaListaTranformed = preciosNuevaListaTranformed;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<preciosEntity> getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(List<preciosEntity> listaPrecios) {
        this.listaPrecios = listaPrecios;
    }

    public List<preciosEntity> getFilteredListaPrecios() {
        return filteredListaPrecios;
    }

    public void setFilteredListaPrecios(List<preciosEntity> filteredListaPrecios) {
        this.filteredListaPrecios = filteredListaPrecios;
    }

    public preciosBean() {
        try {
            preciosDaoDB2 listatemp = new preciosDaoDB2();
            listaPrecios = listatemp.select_all();
        } catch (SQLException ex) {
            Logger.getLogger(preciosBean.class.getName()).log(Level.SEVERE, null, ex);
            ExceptionManager.addError(ex.getMessage());
        }
    }

    // Storing Method
    // ************** Método que recorre la lista temporal, la ingresa a la lista de inserción y llama el WS de validaciones e inserción
    public void onStoring() throws IOException {
        /*cuando son mas de 150 registros, lo divide en grupos de 150, el restante lo deja en un grupo mas pequeño*/
        int contador = 0;/*numero de páginas de recorridos(grupos de 150)*/
        int contador2 = 0;/*registros restantes de las páginas de los grupos de 150*/
        int contInicio = 0;/*contador total de registros del archivo o ingresados manualmente*/
        int contGeneral = 1;/*contador que va aumentando las páginas*/
        int contCiclo = 0;/*limite superior del recorrido del ciclo interno ej: del 150 al 200*/
        int colaRegistros = 100;/* limite de registros para enviar al webservice */
        String respuesta ="";

        //if (preciosNuevaLista.size() <= colaRegistros) {
        try {
            if (preciosNuevaLista.size() > colaRegistros) {
                contador = preciosNuevaLista.size() / colaRegistros;
                contador2 = preciosNuevaLista.size() % colaRegistros;
            } else {
                contador = 0;
                contador2 = preciosNuevaLista.size();
            }
            for (int g = 0; g <= contador; g++) {

                if (g == contador) {
                    contCiclo = contInicio + contador2;
                } else if (preciosNuevaLista.size() <= colaRegistros) {
                    contCiclo = preciosNuevaLista.size();
                    g++;
                } else {
                    contCiclo = contGeneral * colaRegistros;
                }

                /*FacesMessage messageLoop = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje", "Recorrido página " + g+1 + " de " + contador);
            RequestContext.getCurrentInstance().showMessageInDialog(messageLoop);*/
                for (int i = contInicio; i < contCiclo; i++) {
                    //condicional que valida que la fecha de finalización de la vigencia del precio sea mayor a la fecha de inicio
                    if (preciosNuevaLista.get(i).getFechaFin() < preciosNuevaLista.get(i).getFechaInicio()) {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje de Alerta", "La fecha de inicio es mayor que la fecha de finalización de alguno de los productos");
                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                        return;
                    }

                    //condicional que valida que se ingresen las fechas de inicio y de fin del precio
                    if (preciosNuevaLista.get(i).getFechaInicio() != 0
                            && preciosNuevaLista.get(i).getFechaFin() != 0) {
                        // Ir al método convertStringtoNeedIt y hacer otro tipo de lógica para obtener el PRKEY
                        // Método que concatena el código del ítem y la (región, cliente o instalación) para la inserción como PRKEY
                        String letraPrecio;
                        if (preciosNuevaLista.get(i).getTipoPrecio() != null) {
                            letraPrecio = preciosNuevaLista.get(i).getTipoPrecio();
                        } else {
                            letraPrecio = this.tipoPrecio;
                        }
                        //String item = preciosNuevaLista.get(i).getItem().replace(" ", "");
                        String item = preciosNuevaLista.get(i).getItem();
                        String Rci = preciosNuevaLista.get(i).getTipoPrecioSub().replace(" ", "");
                        String prKeyString = convertStringtoNeedIt(item, Rci, letraPrecio);
                        //****************

                        //** validación que el item ya no exista en la lista//
                        /*for (int j = 0; j < preciosNuevaListaTranformed.size(); j++) {
                            if (item.equals(preciosNuevaListaTranformed.get(j).getPRKEY1())
                                    && preciosNuevaLista.get(i).getTipoPrecioSub().equals(preciosNuevaListaTranformed.get(j).getPRKEY2())) {
                                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje de Alerta", "El artículo " + preciosNuevaLista.get(i).getItem() + " ya se encuentra creado en la actual lista con " + preciosNuevaLista.get(i).getTipoPrecioSub());
                                RequestContext.getCurrentInstance().showMessageInDialog(message);
                                return;
                            }
                        }*/

                        //Este condicional valida que el valor ingresado en pesos colombianos no tenga decimales, y lo redondea
                        /*if (preciosNuevaLista.get(i).getMoneda().equals("COP")) {
                        NumberFormat nf = new DecimalFormat("##.###");
                        double nuevoValor = Math.round(preciosNuevaLista.get(i).getPrecioLista() * Math.pow(10, 0)) / Math.pow(10, 0);
                        preciosNuevaLista.get(i).setPrecioLista(nuevoValor);
                    }*/
                        preciosEntity preciosObjTemp = new preciosEntity();
                        preciosObjTemp.setSPID("SP");
                        preciosObjTemp.setPMETH(preciosNuevaLista.get(i).getTipoPrecio());
                        preciosObjTemp.setPRKEY1(item);
                        preciosObjTemp.setPRKEY2(Rci);
                        preciosObjTemp.setPRKEYCONCT(prKeyString);
                        preciosObjTemp.setPFCT1(Double.toString(preciosNuevaLista.get(i).getPrecioLista()));
                        preciosObjTemp.setPSDTE(Integer.toString(preciosNuevaLista.get(i).getFechaInicio()));
                        preciosObjTemp.setPSEDT(Integer.toString(preciosNuevaLista.get(i).getFechaFin()));
                        preciosObjTemp.setPCURR(preciosNuevaLista.get(i).getMoneda());
                        preciosObjTemp.setSPENUS(Sesion.getSesion().getFormUser());
                        preciosObjTemp.setSPENTM("");
                        preciosObjTemp.setSPENDT("");
                        preciosObjTemp.setSPMNDT("");
                        preciosObjTemp.setSPMNTM("");
                        preciosObjTemp.setSPMNUS(Sesion.getSesion().getFormUser());
                        preciosObjTemp.setVencer(preciosNuevaLista.get(i).getVencer());
                        preciosObjTemp.setInactivar(preciosNuevaLista.get(i).getInactivar());

                        preciosNuevaListaTranformed.add(preciosObjTemp);
                        contInicio++;
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje de Alerta", "Debe ingresar las fechas de inicio y de fin");
                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                        return;
                    }
                }
                // Invocacion del método pasandole la lista
                respuesta = sentWebServiceMethod(preciosNuevaListaTranformed, contCiclo);
                
                if(respuesta.equals("Error")){
                    return;
                }

                if (contInicio % colaRegistros == 0) {
                    contGeneral++;
                    preciosNuevaListaTranformed.clear();
                }

            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Falló en la tanda número : "+contInicio);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }    
        /*}else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "El número de filas del archivo supera el límite permitido: "+colaRegistros);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }*/
    }

    public String sentWebServiceMethod(List<preciosEntity> preciosNuevaListaTranformed, int cola) throws IOException {
        try {
            wsBusinessBean ws = new wsBusinessBean();
            //Llamado al WS con la lista de precios
            String resultado = ws.wsSentJson(preciosNuevaListaTranformed);
            //Si el resultado del WS es 0, es porque se insertaron, modificaron e inactivaron correctamente los items
            if (resultado.equals("0") || resultado.contains("Se inactivo el precio de")) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Se han insertado todos precios");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                try {
                    ws = null;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    throw e;
                }
                return "";
            } else {
                //preciosNuevaLista.clear();
                preciosNuevaListaTranformed.clear();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "No se han insertado precios: " + resultado + ", se enviaron " +(cola-100)+" registros.");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
                return "Error";
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Fallo el WebService");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
        return null;
    }

    public String convertStringtoNeedIt(String item, String tipoPrecioSub, String letraPrecio) {
        String codigo = item;
        String region = tipoPrecioSub;
        String cadenaInsercion = codigo;

        //variable que almacena el número de caracteres del ítem
        int resultadoPrimer = 35 - codigo.length();
        //variable que almacena el número de caracteres de la region, cliente o instalación
        int resultadoSegundo = 8 - region.length();
        String cadenaInsercion2 = "";

        for (int i = 0; i < resultadoPrimer; i++) {
            String subCadena = " ";
            cadenaInsercion += subCadena;
        }

        //condicional que valida que sea tipo de precio A y que la longitud del cliente sea 6 para agregarle el 00
        if (letraPrecio.equals("A") && region.length() == 6) {
            region = "00" + tipoPrecioSub;
            cadenaInsercion2 = region;
        } else {
            for (int i = 0; i < resultadoSegundo; i++) {
                String subCadena2 = " ";
                cadenaInsercion2 = region += subCadena2;
            }
        }

        return cadenaInsercion + cadenaInsercion2;
    }

    public preciosEntity getSelectCxpRow() {
        return selectPriceRow;
    }

    public void setSelectCxpRow(preciosEntity selectPriceRow) {
        this.selectPriceRow = selectPriceRow;
    }

    public void onDeleteAcepted() throws IOException {
        preciosEntity preciosObjTemp = new preciosEntity();
        preciosObjTemp.setSPID("SP");
        preciosObjTemp.setPMETH(selectPriceRow.getPMETH());
        preciosObjTemp.setPRKEY1(selectPriceRow.getPRKEY1());
        preciosObjTemp.setPRKEY2(selectPriceRow.getPRKEY2());
        String prKeyString = convertStringtoNeedIt(selectPriceRow.getPRKEY1(), selectPriceRow.getPRKEY2(), selectPriceRow.getPMETH());
        preciosObjTemp.setPRKEYCONCT(prKeyString);
        preciosObjTemp.setPFCT1(selectPriceRow.getPFCT1());
        preciosObjTemp.setPSDTE(selectPriceRow.getPSDTE());
        preciosObjTemp.setSPENDT("");
        preciosObjTemp.setPSEDT(selectPriceRow.getPSEDT());
        preciosObjTemp.setSPENTM("");
        preciosObjTemp.setPCURR(selectPriceRow.getPCURR());
        preciosObjTemp.setSPENUS("");
        preciosObjTemp.setSPMNDT("");
        preciosObjTemp.setSPMNUS(Sesion.getSesion().getFormUser());
        preciosObjTemp.setSPMNTM("");
        preciosObjTemp.setVencer("");
        preciosObjTemp.setInactivar("Si");
        preciosNuevaListaTranformed.add(preciosObjTemp);

        sentWebServiceMethod(preciosNuevaListaTranformed,1);
    }

    //**Método que desarrolla la tarea de ller al archivo en el servidor
    public void leerArchivo(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        String contentType = uploadedFile.getContentType();
        this.uploadedFile = uploadedFile;
        try {
            preciosNuevaLista.clear();
            preciosNuevaListaTranformed.clear();
            moverArchivo(uploadedFile.getInputstream(), uploadedFile.getFileName());
        } catch (IOException e1) {
            System.out.println(e1);
        }
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //**Método que realiza la acción de mover el archivo a una ruta en la cual pueda ser leido
    private Boolean moverArchivo(InputStream inputStream, String name) {
        OutputStream outputStream = null;
        String path = "C:\\Windows\\Temp\\";
        Boolean flag = false;
        try {
            outputStream = new FileOutputStream(new File(path + name));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("Done!");
            flag = true;
        } catch (IOException e) {
            flag = false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return flag;
    }

    //**método que lee el archivo en la ruta especificada, y lo transforma en la lista necesaria para ser mostrada en la vista
    public void subirArchivo() throws FileNotFoundException, IOException {
        try {
            File f = new File("C:\\Windows\\Temp\\", uploadedFile.getFileName());
            FileInputStream fis = new FileInputStream(f);
            HSSFWorkbook wb = new HSSFWorkbook(fis);
            HSSFSheet sheet = wb.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
            String s1 = "", prKeyString = "";
            int contador = 0;

            for (Row row : sheet) {
                preciosEntity preciosObjTemp = new preciosEntity();
                for (Cell cell : row) {
                    if (row.getRowNum() != 0) {
                        if (cell != null && cell.getCellType() != 3) {
                            switch (cell.getColumnIndex()) {
                                case 0:
                                    this.tipoPrecio = cell.getStringCellValue();
                                    break;
                                case 1:
                                    preciosObjTemp.setPRKEY1(cell.getStringCellValue().toUpperCase());
                                    break;
                                case 2:
                                    preciosObjTemp.setPRKEY2(cell.getStringCellValue().toUpperCase());
                                    break;
                                case 3:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    if (validarFecha(cell.getStringCellValue())) {
                                        preciosObjTemp.setPSDTE(cell.getStringCellValue());
                                        break;
                                    } else {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Fecha de inicio inválida " + cell.getStringCellValue());
                                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                                        return;
                                    }
                                case 4:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    if (validarFecha(cell.getStringCellValue())) {
                                        preciosObjTemp.setPSEDT(cell.getStringCellValue());
                                        break;
                                    } else {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Fecha de fin inválida " + cell.getStringCellValue());
                                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                                        return;
                                    }
                                case 5:
                                    if (cell.getNumericCellValue() <= 0) {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "El valor especificado no puede ser menor o igual a 0");
                                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                                        return;
                                    } else {
                                        preciosObjTemp.setPFCT1(Double.toString(cell.getNumericCellValue()));
                                        break;
                                    }
                                case 6:
                                    preciosObjTemp.setPCURR(cell.getStringCellValue());
                                    break;
                                case 7:
                                    preciosObjTemp.setVencer(cell.getStringCellValue());
                                    break;
                                case 8:
                                    preciosObjTemp.setInactivar(cell.getStringCellValue());
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            contador++;
                        }
                    }

                }

                if (preciosObjTemp.getPRKEY1() != null) {
                    preciosObjTemp.setSPID("SP");
                    prKeyString = convertStringtoNeedIt(preciosObjTemp.getPRKEY1(), preciosObjTemp.getPRKEY2(), this.tipoPrecio);
                    preciosObjTemp.setPRKEYCONCT(prKeyString);
                    preciosObjTemp.setPMETH(this.tipoPrecio);
                    preciosObjTemp.setSPENUS(Sesion.getSesion().getFormUser());
                    preciosObjTemp.setSPENTM("");
                    preciosObjTemp.setSPENDT("");
                    preciosObjTemp.setSPMNDT("");
                    preciosObjTemp.setSPMNTM("");
                    preciosObjTemp.setSPMNUS(Sesion.getSesion().getFormUser());

                    precios pres = new precios();
                    pres.setCompania("2");
                    pres.setMoneda(preciosObjTemp.getPCURR());
                    pres.setTipoPrecio(tipoPrecio);
                    pres.setItem(preciosObjTemp.getPRKEY1());
                    pres.setTipoPrecioSub(preciosObjTemp.getPRKEY2());
                    pres.setPrecioLista(Double.parseDouble(preciosObjTemp.getPFCT1()));
                    pres.setFechaInicio(Integer.parseInt(preciosObjTemp.getPSDTE()));
                    pres.setFechaFin(Integer.parseInt(preciosObjTemp.getPSEDT()));
                    if (preciosObjTemp.getVencer().equals("Si")) {
                        preciosObjTemp.setVencer("true");
                    } else {
                        preciosObjTemp.setVencer("false");
                    }
                    if (preciosObjTemp.getInactivar().equals("Si")) {
                        preciosObjTemp.setInactivar("true");
                    } else {
                        preciosObjTemp.setInactivar("false");
                    }
                    pres.setVencer(preciosObjTemp.getVencer());
                    pres.setInactivar(preciosObjTemp.getInactivar());
                    preciosNuevaLista.add(pres);

                }

            }
            FacesMessage messageOk = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "La carga de datos a finalizado, favor verificarlos y darle click en el bogón guardar");
            RequestContext.getCurrentInstance().showMessageInDialog(messageOk);

            if (contador > 0 && preciosNuevaLista.size() == 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Los campos no pueden estar vacios");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "No se procesó el archivo," + e + ",verifique haber seleccionado el archivo y verifique que todos los campos estén correctos. El RCI debe estar en tipo texto y los valores mayores y diferentes a 0");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    private boolean validarFecha(String cadena) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
            formatoFecha.setLenient(false);
            formatoFecha.parse(cadena);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public String getListaOrigen() {
        return listaOrigen;
    }

    public void setListaOrigen(String listaOrigen) {
        this.listaOrigen = listaOrigen;
    }

    public String getListaDestino() {
        return listaDestino;
    }

    public void setListaDestino(String listaDestino) {
        this.listaDestino = listaDestino;
    }

    public Integer getFeInicial() {
        return feInicial;
    }

    public void setFeInicial(Integer feInicial) {
        this.feInicial = feInicial;
    }

    public Integer getFeFinal() {
        return feFinal;
    }

    public void setFeFinal(Integer feFinal) {
        this.feFinal = feFinal;
    }

    public void copiarLista() {
        String origen = this.listaOrigen;
        String destino = this.listaDestino;
        Integer feInicio = feInicial;

        try {
            preciosDaoDB2 copyList = new preciosDaoDB2();
            String listatemp = copyList.copiarListaPrecios(origen, destino, feInicio);
            if (listatemp.contains("Copiados")) {
                setListaOrigen("");
                setListaDestino("");
                setFeInicial(0);
                setFeFinal(0);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", listatemp);
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            } else {
                setListaOrigen("");
                setListaDestino("");
                setFeInicial(0);
                setFeFinal(0);
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "No se pudo copiar la lista de precios");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "No se pudo copiar la lista de precios");
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }

}
