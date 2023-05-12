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
public class NOTASDEBITO {
    
    // Campos de la BD
    // ***************
    
    private String rcust;
    private String ardpfx;
    private int rinvc;
    private double ramt;
    private int ridte;
    private int rddte;
    
    // Constructor de la clase
    // ***********************
    
    public NOTASDEBITO(){}
    
    // Metodos para el encapsulamiento
    // *******************************

    public String getRcust() {
        return rcust;
    }

    public void setRcust(String rcust) {
        this.rcust = rcust;
    }

    public String getArdpfx() {
        return ardpfx;
    }

    public void setArdpfx(String ardpfx) {
        this.ardpfx = ardpfx;
    }

    public int getRinvc() {
        return rinvc;
    }

    public void setRinvc(int rinvc) {
        this.rinvc = rinvc;
    }

    public double getRamt() {
        return ramt;
    }

    public void setRamt(double ramt) {
        this.ramt = ramt;
    }

    public int getRidte() {
        return ridte;
    }

    public void setRidte(int ridte) {
        this.ridte = ridte;
    }

    public int getRddte() {
        return rddte;
    }

    public void setRddte(int rddte) {
        this.rddte = rddte;
    }
    
    // Override del metodo toString
    // ****************************
    @Override
    public String toString(){
        return "Notas Debito: {"+rcust+" "+ardpfx+" "+ramt+" "+rinvc+" "+ridte+" "+rddte+" "+"}";
    }
}
