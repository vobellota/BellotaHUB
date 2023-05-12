/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.financiera;

/**
 *
 * @author ebedoya
 */
public class RESDIAN {
    
    // Campos de la BD
    // ***************
    
    private String resolucion;
    
    private String prefijo;
    
    private int n_inicial;
            
    private int n_final;
    
    private int fech_ini;
    
    private int fech_venc;
    
    // Constructor de la clase
    // ***********************
    
    public RESDIAN(){}
    
    // Constructor de la clase que recibe args
    // ***************************************
    
    public RESDIAN(String resolucion, String prefijo){
        this.resolucion = resolucion;
        this.prefijo = prefijo;
    }
    
    // Metodos para el encapsulamiento
    // *******************************
    public String getResolucion(){
        return resolucion;
    }
    public void setResolucion(String resolucion){
        this.resolucion = resolucion;
    }
    
    // ----------------------------
    public String getPrefijo(){
        return prefijo;
    }
    public void setPrefijo(String prefijo){
        this.prefijo = prefijo;
    }
    
    // ----------------------------
    public int getN_inicial(){
        return n_inicial;
    }
    public void setN_inicial(int n_inicial){
        this.n_inicial = n_inicial;
    }
    
    // ----------------------------
    public int getN_final(){
        return n_final;
    }
    public void setN_final(int n_final){
        this.n_final = n_final;
    }
    
    // ----------------------------
    public int getFech_ini(){
        return fech_ini;
    }
    public void setFech_ini(int fech_ini){
        this.fech_ini = fech_ini;
    }
    
    // ----------------------------
    public int getFech_venc(){
        return fech_venc;
    }
    public void setFech_venc(int fech_venc){
        this.fech_venc = fech_venc;
    }
    
    // Override del metodo toString
    // ****************************
    @Override
    public String toString(){
        return "Resdian :{"+resolucion+" "+prefijo+" "+n_inicial+" "+n_final+" "+fech_ini+" "+fech_venc+"}";
    }
}
