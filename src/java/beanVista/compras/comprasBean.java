/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beanVista.compras;

import com.google.gson.Gson;
import entidades.compras.fechas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sys.util.Sesion;

/**
 *
 * @author ggaviria
 */
@ViewScoped
@ManagedBean(name = "comprasBean")
public class comprasBean implements Serializable {
    
    public Sesion sesion;
    public Connection conn = null;
    
    public comprasBean() {
        this.sesion = new Sesion();
        this.conn = sesion.getConexionBPCS();
    }
    
    private String numeroOrdenCompra;
    private String lineaItem;
    private String fechaVencimiento;
    private String fechaRequerido;
    private String fechaRecepcion;
    private List<fechas> fechasNuevaLista = new ArrayList<>();
    private List<fechas> listaFechas;
    private List<fechas> filteredListaFechas;
    private List<fechas> fechasSeleccionadas = new ArrayList<>();
    private String colorVencimiento;
    private String colorRequerido;
    private String colorRecepcion;
    
    public final String SQL_SELECT_ONE = "SELECT PDDTE,POAQDT,POACDT FROM HPO WHERE PORD=? AND PLINE=?";
    public final String SQL_SELECT_ONE_ZERO = "SELECT PHAQDT,PHACDT FROM HPH WHERE PHORD=?";
    
    private UploadedFile uploadedFile;
    
    public String getNumeroOrdenCompra() {
        return numeroOrdenCompra;
    }
    
    public void setNumeroOrdenCompra(String numeroOrdenCompra) {
        this.numeroOrdenCompra = numeroOrdenCompra;
    }
    
    public String getLineaItem() {
        return lineaItem;
    }
    
    public void setLineaItem(String lineaItem) {
        this.lineaItem = lineaItem;
    }
    
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public String getFechaRequerido() {
        return fechaRequerido;
    }
    
    public void setFechaRequerido(String fechaRequerido) {
        this.fechaRequerido = fechaRequerido;
    }
    
    public String getFechaRecepcion() {
        return fechaRecepcion;
    }
    
    public void setFechaRecepcion(String fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
    
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }
    
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    public List<fechas> getFechasNuevaLista() {
        return fechasNuevaLista;
    }
    
    public void setFechasNuevaLista(List<fechas> fechasNuevaLista) {
        this.fechasNuevaLista = fechasNuevaLista;
    }
    
    public List<fechas> getListaFechas() {
        return listaFechas;
    }
    
    public void setListaFechas(List<fechas> listaFechas) {
        this.listaFechas = listaFechas;
    }
    
    public List<fechas> getFilteredListaFechas() {
        return filteredListaFechas;
    }
    
    public void setFilteredListaFechas(List<fechas> filteredListaFechas) {
        this.filteredListaFechas = filteredListaFechas;
    }
    
    public List<fechas> getFechasSeleccionadas() {
        return fechasSeleccionadas;
    }
    
    public void setFechasSeleccionadas(List<fechas> fechasSeleccionadas) {
        this.fechasSeleccionadas = fechasSeleccionadas;
    }

    //**Método que desarrolla la tarea de ller al archivo en el servidor
    public void leerArchivo(FileUploadEvent event) throws IOException {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        String contentType = uploadedFile.getContentType();
        this.uploadedFile = uploadedFile;
        try {
            fechasNuevaLista.clear();
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
    
    public void subirArchivo() throws FileNotFoundException, IOException {
        try {
            File f = new File("C:\\Windows\\Temp\\", uploadedFile.getFileName());
            FileInputStream fis = new FileInputStream(f);
            HSSFWorkbook wb = new HSSFWorkbook(fis);
            HSSFSheet sheet = wb.getSheetAt(0);
            FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
            String s1 = "", mensaje = "";
            int contador = 0;
            
            for (Row row : sheet) {
                this.numeroOrdenCompra = null;
                this.lineaItem = null;
                this.fechaVencimiento = null;
                this.fechaRequerido = null;
                this.fechaRecepcion = null;
                fechas fechasObjTemp = new fechas();
                for (Cell cell : row) {
                    if (row.getRowNum() != 0) {
                        if (cell != null && cell.getCellType() != 3 && !cell.toString().trim().equals("")) {
                            switch (cell.getColumnIndex()) {
                                case 0:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    this.numeroOrdenCompra = cell.getStringCellValue();
                                    break;
                                case 1:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    this.lineaItem = cell.getStringCellValue();
                                    break;
                                case 2:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    if (validarFecha(cell.getStringCellValue())) {
                                        this.fechaVencimiento = (cell.getStringCellValue());
                                        break;
                                    } else if (validarFecha1(cell.getStringCellValue())) {
                                        this.fechaVencimiento = (modificarFecha1(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha2(cell.getStringCellValue())) {
                                        this.fechaVencimiento = (modificarFecha2(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha3(cell.getStringCellValue())) {
                                        this.fechaVencimiento = (modificarFecha3(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha4(cell.getStringCellValue())) {
                                        this.fechaVencimiento = (modificarFecha4(cell.getStringCellValue()));
                                        break;
                                    } else {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Fecha inválida " + cell.getStringCellValue());
                                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                                        return;
                                    }
                                case 3:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    if (validarFecha(cell.getStringCellValue())) {
                                        this.fechaRequerido = (cell.getStringCellValue());
                                        break;
                                    } else if (validarFecha1(cell.getStringCellValue())) {
                                        this.fechaRequerido = (modificarFecha1(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha2(cell.getStringCellValue())) {
                                        this.fechaRequerido = (modificarFecha2(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha3(cell.getStringCellValue())) {
                                        this.fechaRequerido = (modificarFecha3(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha4(cell.getStringCellValue())) {
                                        this.fechaRequerido = (modificarFecha4(cell.getStringCellValue()));
                                        break;
                                    } else {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Fecha inválida " + cell.getStringCellValue());
                                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                                        return;
                                    }
                                
                                case 4:
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    if (validarFecha(cell.getStringCellValue())) {
                                        this.fechaRecepcion = (cell.getStringCellValue());
                                        break;
                                    } else if (validarFecha1(cell.getStringCellValue())) {
                                        this.fechaRecepcion = (modificarFecha1(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha2(cell.getStringCellValue())) {
                                        this.fechaRecepcion = (modificarFecha2(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha3(cell.getStringCellValue())) {
                                        this.fechaRecepcion = (modificarFecha3(cell.getStringCellValue()));
                                        break;
                                    } else if (validarFecha4(cell.getStringCellValue())) {
                                        this.fechaRecepcion = (modificarFecha4(cell.getStringCellValue()));
                                        break;
                                    } else {
                                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Fecha inválida " + cell.getStringCellValue());
                                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                                        return;
                                    }
                                default:
                                    break;
                            }
                        } else {
                            contador++;
                        }
                    }
                }
                
                if (this.numeroOrdenCompra != null && this.numeroOrdenCompra.trim() != null
                        && this.lineaItem.trim() != null && !this.numeroOrdenCompra.trim().equals("") && !this.lineaItem.trim().equals("")) {
                    fechas fechaOC = new fechas();
                    fechaOC.setNumeroOrdenCompra(this.numeroOrdenCompra);
                    fechaOC.setLineaItem(this.lineaItem);
                    fechaOC.setUsuarioModifica(Sesion.getSesion().getFormUser());
                    
                    PreparedStatement stmt = null;
                    ResultSet rs = null;
                    String consulta = "";
                    
                    try {
                        if (!fechaOC.getLineaItem().equals("0")) {
                            stmt = conn.prepareStatement(SQL_SELECT_ONE);
                            stmt.setString(1, fechaOC.getNumeroOrdenCompra());
                            stmt.setString(2, fechaOC.getLineaItem());
                        } else {
                            stmt = conn.prepareStatement(SQL_SELECT_ONE_ZERO);
                            stmt.setString(1, fechaOC.getNumeroOrdenCompra());
                        }
                        
                        try {
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                if (!fechaOC.getLineaItem().equals("0")) {
                                    if (!rs.getString("PDDTE").equals("0")) {
                                        fechaOC.setActualfechaVencimiento(modificarFecha(rs.getString("PDDTE")));
                                    }
                                    if (!rs.getString("POAQDT").equals("0")) {
                                        fechaOC.setActualfechaRequerido(modificarFecha(rs.getString("POAQDT")));
                                    } else {
                                        fechaOC.setActualfechaRequerido("0");
                                    }
                                    if (!rs.getString("POACDT").equals("0")) {
                                        fechaOC.setActualfechaRecepcion(modificarFecha(rs.getString("POACDT")));
                                    } else {
                                        fechaOC.setActualfechaRecepcion("0");
                                    }
                                } else {
                                    if (!rs.getString("PHAQDT").equals("0")) {
                                        fechaOC.setActualfechaRequerido(modificarFecha(rs.getString("PHAQDT")));
                                    }else{
                                        fechaOC.setActualfechaRequerido("0");
                                    }
                                    if (!rs.getString("PHACDT").equals("0")) {
                                        fechaOC.setActualfechaRecepcion(modificarFecha(rs.getString("PHACDT")));
                                    }else{
                                        fechaOC.setActualfechaRecepcion("0");
                                    }
                                }
                                
                                boolean seModifico = false;
                                
                                if (this.fechaVencimiento != null && !this.fechaVencimiento.equals(fechaOC.getActualfechaVencimiento())) {
                                    fechaOC.setFechaVencimiento(this.fechaVencimiento);
                                    seModifico = true;
                                    String dias = diasDiferencia(fechaOC.getActualfechaVencimiento(), this.fechaVencimiento);
                                    fechaOC.setDiasVencimiento(dias);
                                    color(Integer.valueOf(dias), "vencimiento");
                                    fechaOC.setColorVencimiento(this.colorVencimiento);
                                }
                                if (this.fechaRequerido != null && !this.fechaRequerido.equals(fechaOC.getActualfechaRequerido())) {
                                    fechaOC.setFechaRequerido(this.fechaRequerido);
                                    seModifico = true;
                                    if (!fechaOC.getActualfechaRequerido().equals("0")) {
                                        String dias = diasDiferencia(fechaOC.getActualfechaRequerido(), this.fechaRequerido);
                                        fechaOC.setDiasFechaRequerido(dias);
                                        color(Integer.valueOf(dias), "requerido");
                                        fechaOC.setColorRequerido(this.colorRequerido);
                                    } else {
                                        fechaOC.setColorRequerido("");
                                    }
                                }
                                if (this.fechaRecepcion != null && !this.fechaRecepcion.equals(fechaOC.getActualfechaRecepcion())) {
                                    fechaOC.setFechaRecepcion(this.fechaRecepcion);
                                    seModifico = true;
                                    if (!fechaOC.getActualfechaRecepcion().equals("0")) {
                                        String dias = diasDiferencia(fechaOC.getActualfechaRecepcion(), this.fechaRecepcion);
                                        fechaOC.setDiasFechaRecepcion(dias);
                                        color(Integer.valueOf(dias), "recepcion");
                                        fechaOC.setColorRecepcion(this.colorRecepcion);
                                    } else {
                                        fechaOC.setColorRequerido("");
                                    }
                                }
                                
                                if (seModifico) {
                                    fechasNuevaLista.add(fechaOC);
                                }
                            } else {
                                mensaje += "La OC #:" + fechaOC.getNumeroOrdenCompra() + " no existe,";
                            }
                        } catch (SQLException sex) {
                            //Logger.getLogger(comprasBean.class.getName()).log(Level.SEVERE, null, sex);
                            int error = row.getRowNum() + 1;
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Error en la fila " + error);
                            RequestContext.getCurrentInstance().showMessageInDialog(message);
                            return;
                        }
                    } catch (SQLException ex) {
                        //Logger.getLogger(comprasBean.class.getName()).log(Level.SEVERE, null, ex);
                        int error = row.getRowNum() + 1;
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Error en la fila " + error);
                        RequestContext.getCurrentInstance().showMessageInDialog(message);
                        return;
                    }
                    
                }
                
            }
            listaFechas = fechasNuevaLista;
            FacesMessage messageOk = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "La carga de datos a finalizado (solo se mostrarán los registros que tengan fechas diferentes a las de LX), favor verificarlos y darle click en el bogón guardar, /n" + mensaje);
            RequestContext.getCurrentInstance().showMessageInDialog(messageOk);
            
            if (contador > 0 && fechasNuevaLista.size() == 0) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "Los campos no pueden estar vacios");
                RequestContext.getCurrentInstance().showMessageInDialog(message);
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "No se procesó el archivo," + e + ",verifique haber seleccionado el archivo y verifique que todos los campos estén correctos. Error en la OC:" + this.numeroOrdenCompra);
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }
    }
    
    private String diasDiferencia(String fecha1, String fecha2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date firstDate = sdf.parse(fecha1);
            Date secondDate = sdf.parse(fecha2);
            
            long diff = secondDate.getTime() - firstDate.getTime();
            
            TimeUnit time = TimeUnit.DAYS;
            long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
            return "" + diffrence;
        } catch (ParseException ex) {
            Logger.getLogger(comprasBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void color(int dias, String tipo) {
        if (dias > 30) {
            if (tipo.equals("vencimiento")) {
                this.colorVencimiento = "red";
            } else if (tipo.equals("requerido")) {
                this.colorRequerido = "red";
            } else {
                this.colorRecepcion = "red";
            }            
        } else if (dias < 30 && dias > 10) {
            if (tipo.equals("vencimiento")) {
                this.colorVencimiento = "yellow";
            } else if (tipo.equals("requerido")) {
                this.colorRequerido = "yellow";
            } else {
                this.colorRecepcion = "yellow";
            }
        } else {
            if (tipo.equals("vencimiento")) {
                this.colorVencimiento = "green";
            } else if (tipo.equals("requerido")) {
                this.colorRequerido = "green";
            } else {
                this.colorRecepcion = "green";
            }
        }
    }
    
    private boolean validarFecha(String cadena) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            formatoFecha.setLenient(false);
            formatoFecha.parse(cadena);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    private boolean validarFecha1(String cadena) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(cadena);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    private boolean validarFecha2(String cadena) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(cadena);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    private boolean validarFecha3(String cadena) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("MM/dd/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(cadena);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    private boolean validarFecha4(String cadena) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("MM-dd-yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(cadena);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    public String modificarFecha(String fecha) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(fecha);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            System.out.println("Eror:" + e.getMessage());
        }
        return "";
    }
    
    public String modificarFecha1(String fecha) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(fecha);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            System.out.println("Eror:" + e.getMessage());
        }
        return "";
    }
    
    public String modificarFecha2(String fecha) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(fecha);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            System.out.println("Eror:" + e.getMessage());
        }
        return "";
    }
    
    public String modificarFecha3(String fecha) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(fecha);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            System.out.println("Eror:" + e.getMessage());
        }
        return "";
    }
    
    public String modificarFecha4(String fecha) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("MM-dd-yyyy");
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(fecha);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            System.out.println("Eror:" + e.getMessage());
        }
        return "";
    }
    
    public String modificarFechaArchivo(String fecha) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = originalFormat.parse(fecha);
            String formattedDate = targetFormat.format(date);
            return formattedDate;
        } catch (Exception e) {
            System.out.println("Eror:" + e.getMessage());
        }
        return "";
    }
    
    public String actualizarFechasVencimiento() throws SQLException, Exception {
        
        if (fechasSeleccionadas.size() > 0) {
            
            ResourceBundle rb = ResourceBundle.getBundle("sys.util.conexiones");
            String headKey = rb.getString("headKey");
            
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(540, TimeUnit.SECONDS)
                    .writeTimeout(540, TimeUnit.SECONDS)
                    .readTimeout(540, TimeUnit.SECONDS)
                    .build();
            
            Gson gson = new Gson();
            String cadenatToJson = gson.toJson(fechasSeleccionadas);
            String res;
            
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, cadenatToJson);
            
            Request request = new Request.Builder()
                    .url("https://apilx.bellota.co/RestLXCompras/webresources/ordencompra/actualizafechas")
                    //.url("http://localhost:8080/RestLXCompras/webresources/ordencompra/actualizafechas")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", headKey)
                    .build();
            Response response = null;
            
            try {
                response = client.newCall(request).execute();
                //JSONObject json = new JSONObject(response.body().string());
                //res = json.getString("valor");

                FacesMessage messageOk = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atención", "<html>Resultados: " + response.body().string() + "</html>");
                RequestContext.getCurrentInstance().showMessageInDialog(messageOk);
                /*} catch (JSONException ex) {
            FacesMessage messageOk = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Ocurrio un error en la carga");
            RequestContext.getCurrentInstance().showMessageInDialog(messageOk);*/
            } catch (IOException ex) {
                FacesMessage messageOk = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", "No se pudo conectar con el WS");
                RequestContext.getCurrentInstance().showMessageInDialog(messageOk);
            }
            
            return "";
        } else {
            FacesMessage messageOk = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", "El archivo esta vacio");
            RequestContext.getCurrentInstance().showMessageInDialog(messageOk);
        }
        return null;
    }
    
}
