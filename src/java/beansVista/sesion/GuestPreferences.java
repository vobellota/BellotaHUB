/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansVista.sesion;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    private String menuMode = "layout-menu-static";

    private String theme = "absolution";

    private String layout = "absolution";

    private boolean orientationRTL;
    
    private String template="/WEB-INF/template.xhtml"; //"./plantilla.xhtml"

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
    
    

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
        this.layout = theme.split("-")[0];
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getMenuMode() {
        return menuMode;
    }

    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;
    }

    public boolean isOrientationRTL() {
        return orientationRTL;
    }

    public void setOrientationRTL(boolean orientationRTL) {
        this.orientationRTL = orientationRTL;
    }
}
