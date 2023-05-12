package sys.util;

import dao.distribucion.EstibasDao;
import entidades.distribucion.Estibas;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "converterEstibasAsignadasMenu")

public class converterEstibasAsignadasMenu implements Converter {
    
    @Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String idEstiba) {
        try {
            EstibasDao estibasdao = new EstibasDao(Sesion.getSesion());
            return estibasdao.selectOne(Integer.parseInt(idEstiba));
        } catch (SQLException ex) {
            ExceptionManager.addError(ex.getMessage() + "Señor usuario hubo un error");
            Logger.getLogger(converterGruposCompMenu.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object estiba) {
        if(estiba instanceof Estibas){
            return ((Estibas)estiba).getIdEstiba().toString();
        } else {
            return estiba.toString();
        }
    }
}
