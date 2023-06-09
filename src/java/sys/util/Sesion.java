/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ahenao
 */
@ManagedBean
@SessionScoped
public class Sesion implements Serializable {

    public static final long serialVersionUID = 186514631468746L;
    /**
     * Creates a new instance of Sesion
     */
    private Usuario logged;
    private boolean loggedIn;
    private boolean loginFailed;
    private transient Connection conexionBPCS;
    private transient Connection conexionMySQL;
    private transient Connection conexionPOSTGRES;
    private String formUser;
    private String formPass;
    private boolean debug = true;
    private transient ArrayList<FacesMessage> mensajePendientes = new ArrayList<FacesMessage>();
    private transient HashMap<String, Object> parametros = new HashMap<String, Object>();
    private String token = "";
    private String iss = "";
    private String libreriaAux;

    public ArrayList<FacesMessage> getMensajePendientes() {
        return mensajePendientes;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Usuario getLogged() {
        return logged;
    }

    public void setLogged(Usuario logged) {
        this.logged = logged;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(boolean loginFailed) {
        this.loginFailed = loginFailed;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Connection getConexionPOSTGRES() {
        if (conexionPOSTGRES == null) {
            try {
                ResourceBundle rb = ResourceBundle.getBundle("sys.util.conexiones");
                String user = rb.getString("usuarioPOSTGRES");
                String password = rb.getString("clavePOSTGRES");
                String server = rb.getString("servidorPOSTGRES");
                String db = rb.getString("bdPOSTGRES");
                String urlDatabase = server + db;
                conexionPOSTGRES = DriverManager.getConnection(urlDatabase, user, password);
            } catch (SQLException sqle) {
                Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, sqle);
                sqle.printStackTrace();
            }
        }

        return conexionPOSTGRES;
    }

    public void setConexionPOSTGRES(Connection conexionPOSTGRES) {
        this.conexionPOSTGRES = conexionPOSTGRES;
    }

    public Connection getConexionMySQL() {
        if (conexionMySQL == null) {
            //TODO: Implementar método
        }
        return conexionMySQL;
    }

    public void setConexionMySQL(Connection conexioMySQL) {
        this.conexionMySQL = conexioMySQL;
    }

    public HashMap<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(HashMap<String, Object> parametros) {
        this.parametros = parametros;
    }

    public Connection getConexionBPCS() {
        if (conexionBPCS == null) {
            ResourceBundle rb = ResourceBundle.getBundle("sys.util.conexiones");
            String user = rb.getString("usuarioBPCS");
            String clave = rb.getString("claveBPCS");
            String server = rb.getString("servidor");
            String libreria = rb.getString("libreria");
            libreriaAux = rb.getString("libreriaAux");
            Datos datos = new Datos();
            try {
                conexionBPCS = datos.db2Conection(user, clave, server, libreria);
            } catch (SQLException ex) {
                Logger.getLogger(Sesion.class
                        .getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        return conexionBPCS;
    }

    public void setConexionBPCS(Connection conexionBPCS) {
        this.conexionBPCS = conexionBPCS;
    }

    public String getFormUser() {
        return formUser;
    }

    public void setFormUser(String formUser) {
        this.formUser = formUser;
    }

    public String getFormPass() {
        return formPass;
    }

    public void setFormPass(String formPass) {
        this.formPass = formPass;
    }

    public String iniciarSesion() {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("sys.util.conexiones");
            String urlKEYCLOAK = rb.getString("urlKEYCLOAK");
            String realmKEYCLOAK = rb.getString("realmKEYCLOAK");
            String grantTypeKEYCLOAKLogin = rb.getString("grantTypeKEYCLOAKLogin");
            String clientIdKEYCLOAK = rb.getString("clientIdKEYCLOAK");

            String clientSecretKEYCLOAK = rb.getString("clientSecretKEYCLOAK");
            String scopeKEYCLOAKLogin = rb.getString("scopeKEYCLOAKLogin");
            String responseTypeKEYCLOAKLogin = rb.getString("responseTypeKEYCLOAKLogin");
            libreriaAux = rb.getString("libreriaAux");
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=" + grantTypeKEYCLOAKLogin
                    + "&client_id=" + clientIdKEYCLOAK + "&username=" + this.formUser + "&password="
                    + this.formPass + "&client_secret=" + clientSecretKEYCLOAK + "&scope="
                    + scopeKEYCLOAKLogin + "&response_type=" + responseTypeKEYCLOAKLogin);
            Request request = new Request.Builder()
                    .url(urlKEYCLOAK + "auth/realms/" + realmKEYCLOAK + "/protocol/openid-connect/token")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            
            JSONObject myObject = null;
            try {
                String result = response.body().string();
                myObject = new JSONObject(result);
                setToken(myObject.getString("access_token"));
                if (getToken() != null && !token.equals("")) {
                    loggedIn = true;
                    loginFailed = false;
                    return "/dashboard_bellota";
                } else {
                    loggedIn = false;
                    loginFailed = true;
                }

            } catch (JSONException ex) {
                System.out.println("Excepcion: " + ex.toString());
            }
        } catch (Exception e) {
            System.out.println("Excepcion: " + e.toString());
        }
        // Mensaje de validacion 
        ExceptionManager.addError("Usuario o contraseña inválidos, por favor verifique de nuevo");
        return null;
    }

    public String iniciarSesionToken() {
        if (getToken() != null && !token.equals("")&&verificarToken()) {
            loggedIn = true;
            loginFailed = false;
            return null;
//return "/dashboard_bellota";
        } else {
            loggedIn = false;
            loginFailed = true;
    }
        // Mensaje de validacion 
        ExceptionManager.addError("Usuario o contraseña inválidos, por favor verifique de nuevo");
        return null;
    }

    public boolean verificarToken() {
        /*RSAPrivateKey privateKey
                = //Get the key instance*/
        try {
            //Se convierte la llave pública de Base64 a RSAPublicKey
            KeyFactory kf = KeyFactory.getInstance("RSA");


            String publicKeyContent="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArCa+R8PDjXrLlQyFXd0V53"
                    + "Im5CNayiguWUuRUiJ9uzquygBivyi5dNXBlDKs2P9/cB9jQsyrGgvXy1WYBTBjGpVyDXGJbGl0rSwOgMY"
                    + "IvWzbKcQ4VvzohVpTf/QFIX7KWmeideFeYmH4oMPU4slE/xnxDJQaJnJF2/1Ix0u83skZzPfDvsUZBOFR2"
                    + "+rjuIhbZi6Wp2jcQPEWys0+9q6Zbl/f+5o4fSnRmgP5S1xzoX1Zhytogqn+57jVCJTc6hDxsebF2l1Lnaew"
                    + "MFH2wjgRctvwwopjn9ByEAtO0mwUKi25ifCsG0nwywTRx1gB4MU+ATYnwWYKJ6EJoNGfUwrXnQIDAQAB";

            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
            RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

            //Se verifica el token con la llave pública (El Issuer debe coincider con el token)
            Algorithm algorithm = Algorithm.RSA256(publicKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withIssuer("https://auth.bellota.co:10443/auth/realms/bellotahub")
                    .withIssuer(iss)
                    .build(); //Reusable verifier instance
            System.out.println(new Date().getTime());
            DecodedJWT jwt2=JWT.decode(token);
            System.out.println("not before" +jwt2.getIssuedAt().getTime());
            DecodedJWT jwt = verifier.verify(token);
            
            //Al verificar se obtiene el token decodificado
            //Validar que el token no haya expirado
            Date now=new Date();
            if(now.after(jwt.getExpiresAt())){
                return false;
            }
            System.out.println(jwt.getClaim("preferred_username").asString());
            System.out.println(jwt.getClaim("resource_access").as(LinkedHashMap.class));
            logged=new Usuario(jwt.getClaim("preferred_username").asString(), jwt.getClaim("name").asString(), jwt.getClaim("resource_access").asString(), jwt.getClaim("preferred_username").asString());
            System.out.println(jwt);
            return true;
        } catch (JWTVerificationException ex) {
            Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Sesion.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private RSAPublicKey getKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        String modulusBase64 = "rCa-R8PDjXrLlQyFXd0V53Im5CNayiguWUuRUiJ9uzquygBivyi5dNXBlDKs2P9_cB9jQsyrGgvXy1WYBTBjGpVyDXGJbGl0rSwOgMYIvWzbKcQ4VvzohVpTf_QFIX7KWmeideFeYmH4oMPU4slE_xnxDJQaJnJF2_1Ix0u83skZzPfDvsUZBOFR2-rjuIhbZi6Wp2jcQPEWys0-9q6Zbl_f-5o4fSnRmgP5S1xzoX1Zhytogqn-57jVCJTc6hDxsebF2l1LnaewMFH2wjgRctvwwopjn9ByEAtO0mwUKi25ifCsG0nwywTRx1gB4MU-ATYnwWYKJ6EJoNGfUwrXnQ";
        String exponentBase64 = "AQAB";

        // see org.keycloak.jose.jwk.JWKBuilder#rs256
        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        BigInteger modulus = new BigInteger(1, urlDecoder.decode(modulusBase64));
        BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exponentBase64));
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));

        return publicKey;
    }

    public String logOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return null;
    }

    @PreDestroy
    public void cerrarConexiones() {
        try {
            if (conexionBPCS != null) {
                conexionBPCS.close();
            }
            if(conexionMySQL != null){
                conexionMySQL.close();
            }
            if(conexionPOSTGRES != null){
                conexionPOSTGRES.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Sesion getSesion() {
        ExternalContext contexto
                = FacesContext.getCurrentInstance().getExternalContext();
        Sesion beanSesion
                = (Sesion) contexto.getSessionMap().get("sesion");
        return beanSesion;
    }
    
    public String getURL() {
        ExternalContext contexto
                = FacesContext.getCurrentInstance().getExternalContext();
        return contexto.getRequestScheme()+"://"+contexto.getRequestServerName()+":"+contexto.getRequestServerPort()+contexto.getRequestContextPath();
    }

    public static Sesion getSesion(FacesContext faceContext) {
        ExternalContext contexto
                = faceContext.getExternalContext();
        Sesion beanSesion
                = (Sesion) contexto.getSessionMap().get("sesion");
        return beanSesion;
    }

    public Sesion() {
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    /**
     * @return the libreriaAux
     */
    public String getLibreriaAux() {
        return libreriaAux;
    }
    
    
}
