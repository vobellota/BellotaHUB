/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.io.Serializable;


/**
 *
 * @author ahenao
 */

public class Usuario implements Serializable{

    public static final long serialVersionUID = 173514654389752L;
    /**
     * Creates a new instance of Usuario
     */
    private String login;
    private String nombre;
    private String perfil;
    private String codigo;

    public Usuario(String login, String nombre, String perfil, String codigo) {
        this.login = login;
        this.nombre = nombre;
        this.perfil = perfil;
        this.codigo = codigo;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
}
