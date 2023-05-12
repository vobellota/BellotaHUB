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
public class PUC {
    
    //campos de la BD
    // **************
    
    private String ctcta;
    
    private String ctdesc;
    
    private String ctctyp;
    
    private String ctterc;
    
    private String ctinve;
    
    // Constructor de la clase vacio
    // *****************************
    public PUC(){}
    
    // Constructor de la clase con args
    // ********************************
    public PUC(String ctcta){
        this.ctcta = ctcta;
    } 
    
    // Metodos para el encapsulamiento
    // *******************************
    public String getCtcta(){
        return ctcta;
    }
    public void setCtcta(String ctcta){
        this.ctcta = ctcta;
    }
    
    // ----------------------------
    public String getCtdesc(){
        return ctdesc;
    }
    public void setCtdesc(String ctdesc){
        this.ctdesc = ctdesc;
    }
    
    // ----------------------------
    public String getCtctyp(){
        return ctctyp;
    }
    public void setCtctyp(String ctctyp){
        this.ctctyp = ctctyp;
    }
    
    // ----------------------------
    public String getCtterc(){
        return ctterc;
    }
    public void setCtterc(String ctterc){
        this.ctterc = ctterc;
    }
    
    // ----------------------------
    public String getCtinve(){
        return ctinve;
    }
    public void setCtinve(String ctinve){
        this.ctinve = ctinve;
    }
    
    // Override del metodo toString
    // ****************************
    @Override
    public String toString(){
        return "{Cuenta PUC:"+ctcta+" "+ctdesc+" "+ctctyp+" "+ctterc+" "+ctinve+"}";
    }
}
