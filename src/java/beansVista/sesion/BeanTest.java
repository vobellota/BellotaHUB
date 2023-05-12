/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.sesion;

import sys.util.ExceptionManager;
import sys.util.Sesion;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Session;

/**
 *
 * @author ahenao
 */
@ManagedBean
@RequestScoped
public class BeanTest {

    private String parametro;

    /**
     * Creates a new instance of BeanTest
     */
    public BeanTest() {
    }

    public String getParametro() {
        Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String xml = requestParams.get("xml");
        parametro = xml;
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }
    
    private String message;
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
     
    public void saveMessage() {
        FacesContext context = FacesContext.getCurrentInstance();
         
        //context.addMessage(null, new FacesMessage("Successful",  "Your message: " + message) );
        //context.addMessage(null, new FacesMessage("Second Message", "Additional Message Detail"));
        ArrayList<FacesMessage> msgs= Sesion.getSesion().getMensajePendientes();
        msgs.add( new FacesMessage("Successful",  "Your message: " + message));
        msgs.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Second Message", "Additional Message Detail"));
        
        ExceptionManager.enviarMensajesPendientes(Sesion.getSesion());
    }
}
