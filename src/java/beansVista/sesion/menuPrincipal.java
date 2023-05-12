/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.sesion;

import sys.util.Sesion;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author ahenao
 */
@ManagedBean
@ViewScoped
public class menuPrincipal implements Serializable{

     public static final long serialVersionUID=8721684351L;
    /**
     * Creates a new instance of menuPrincipal
     */
    private String activo;
    
    public menuPrincipal() {
        
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
    
    public void updateActivo(ActionEvent event){
        activo=event.getComponent().getId();
        System.out.println(activo);
    }
    
    public String claseStyle(){
        activo=FacesContext.getCurrentInstance().getViewRoot().getViewId();        
        UIComponent component = UIComponent.getCurrentComponent(FacesContext.getCurrentInstance());
        if(activo.contains(component.getId())){
            return "menuItemActive";
        }else return "menuItem";
        
    }  
    
    public boolean permiso(){
        if(Sesion.getSesion()!=null&!Sesion.getSesion().isLoggedIn()){
            return false;
        }
        return true;
    }
    
}
