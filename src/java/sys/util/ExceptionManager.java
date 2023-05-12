/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

 
import javax.el.ExpressionFactory;  
import javax.el.ValueExpression;  
import javax.faces.application.FacesMessage;  
import javax.faces.application.FacesMessage.Severity;  
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
  
import javax.faces.context.FacesContext;  

/**
 *
 * @author ahenao
 */

@ManagedBean
@RequestScoped  
public class ExceptionManager {
    
    protected static FacesContext getCurrentContext() {  
        return FacesContext.getCurrentInstance();  
    }  
    
     public boolean isError() {  
        return !getCurrentContext().getMessageList().isEmpty();  
    } 
    
     public static void addInfo(String msg,Sesion cont) {  
        addMessage(msg, FacesMessage.SEVERITY_INFO,cont); 
     }
    
     public static void addError(String msg,Sesion cont) {  
        addMessage(msg, FacesMessage.SEVERITY_ERROR,cont);  
    }
     
     public static void addInfo(String msg) {  
        addMessage(msg, FacesMessage.SEVERITY_INFO,null);  
    }  
      
    public static void addError(String msg) {  
        addMessage(msg, FacesMessage.SEVERITY_ERROR,null);  
    }
    
    public static void addWarning(String msg,Sesion cont) {  
        addMessage(msg, FacesMessage.SEVERITY_WARN,cont);  
    }
    
    public static void addWarning(String msg) {  
        addMessage(msg, FacesMessage.SEVERITY_WARN,null);  
    }
      
    private static void addMessage(String msg,Severity severity,Sesion cont) {  
        FacesMessage message=new FacesMessage(msg);  
        message.setSeverity(severity);  
        FacesContext ctx=getCurrentContext();
        try{
            if(ctx!=null){
                ctx.addMessage(null, message);  
            }else{
               cont.getMensajePendientes().add(message);
            }
        }catch(Exception e){
            e.printStackTrace();
            cont.getMensajePendientes().add(message);
        }
    }
    
    public static void enviarMensajesPendientes(Sesion ses){
        FacesContext ctx=getCurrentContext();
        for(int i=0;i<ses.getMensajePendientes().size();i++){
            ctx.addMessage(null, ses.getMensajePendientes().get(i));            
        }
        ses.getMensajePendientes().clear();
    }
      
    public String getMessage(String key) {  
        return (String)getExpression("label['"+key+"']");  
    }  
      
    private Object getExpression(String expression) {  
        FacesContext ctx=getCurrentContext();  
        ExpressionFactory factory=ctx.getApplication().getExpressionFactory();  
        ValueExpression ex=factory.createValueExpression(ctx.getELContext(), "#{"+expression+"}", Object.class);  
        return ex.getValue(ctx.getELContext());  
          
    } 
}
