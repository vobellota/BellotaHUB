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
public class CXP {
    
    // Campos de la BD
    // ***************
    private String cmid;
    
    private String cmfrc;
    
    private String cmtoc;
    
    private int cmdat;
    
    private double cmexc;
    
    private double cmexp;
    
    private double cmexf;
    
    private String cmwsid;
    
    private String cmwusr;
    
    private int cmwdat;
    
    private int cmwtim;
    
    // Constructor de la clase
    // ***********************
    
    public CXP(){}
    
    // Constructor de la clase que recibe args
    // ***************************************
    
    public CXP(String cmid, String cmfrc, String cmtoc, int cmdat){
        this.cmid  = cmid;
        this.cmfrc = cmfrc;
        this.cmtoc = cmtoc;
        this.cmdat = cmdat;
    }
    
    // Metodos para el encapsulamiento
    // *******************************
    public String getCmid(){
        return cmid;
    }
    public void setCmid(String cmid){
        this.cmid = cmid;
    }
    
    // ----------------------------
    public String getCmfrc(){
        return cmfrc;
    }
    public void setCmfrc(String cmfrc){
        this.cmfrc = cmfrc;
    }
    
    // ----------------------------
    public String getCmtoc(){
        return cmtoc;
    }
    public void setCmtoc(String cmtoc){
        this.cmtoc = cmtoc;
    }
    
    // ----------------------------
    public int getCmdat(){
        return cmdat;
    }
    public void setCmdat(int cmdat){
        this.cmdat = cmdat;
    }
    
    // ----------------------------
    public double getCmexc(){
        return cmexc;
    }
    public void setCmexc(double cmexc){
        this.cmexc = cmexc;
    }
    
    // ----------------------------
    public double getCmexp(){
        return cmexp;
    }
    public void setCmexp(double cmexp){
        this.cmexp = cmexp;
    }
    
    // ----------------------------
    public double getCmexf(){
        return cmexf;
    }
    public void setCmexf(double cmexf){
        this.cmexf = cmexf;
    }
    
    // ----------------------------
    public String getCmwsid(){
        return cmwsid;
    }
    public void setCmwsid(String cmwsid){
        this.cmwsid = cmwsid;
    }
    
    // ----------------------------
    public String getCmwusr(){
        return cmwusr;
    }
    public void setCmwusr(String cmwusr){
        this.cmwusr = cmwusr;
    }
    
    // ----------------------------
    public int getCmwdat(){
        return cmwdat;
    }
    public void setCmwdat(int cmwdat){
        this.cmwdat = cmwdat;
    }
    
    // ----------------------------
    public int getCmwtim(){
        return cmwtim;
    }
    public void setCmwtim(int cmwtim){
        this.cmwtim = cmwtim;
    }
    
    // Override del metodo toString
    // ****************************
    @Override
    public String toString(){
        return "Cartera: {"+cmid+" "+cmfrc+" "+cmtoc+" "+cmdat+" "+cmexc+" "+cmexp+" "+cmexf+" "+cmwsid+" "+cmwusr+" "+cmwdat+" "+cmwtim+" "+"}";
    }
    
}
