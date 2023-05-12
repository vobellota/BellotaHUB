
package sys.util;

import dao.distribucion.GruposCompatiblesDao;
import entidades.distribucion.GruposCompatibles;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "converterGruposCompMenu")
public class converterGruposCompMenu implements Converter {
    
    @Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String idGrupoCompatible) {
        try {
            GruposCompatiblesDao gruposcompabeans = new GruposCompatiblesDao(Sesion.getSesion());
            return gruposcompabeans.selectOne(Integer.parseInt(idGrupoCompatible));
        } catch (SQLException ex) {
            ExceptionManager.addError(ex.getMessage() + "Señor usuario hubo un error");
            Logger.getLogger(converterGruposCompMenu.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object grupo) {
        if(grupo instanceof GruposCompatibles){
            return ((GruposCompatibles)grupo).getIdGrupoCompatible().toString();
        } else {
            return grupo.toString();
        }
    }

}
