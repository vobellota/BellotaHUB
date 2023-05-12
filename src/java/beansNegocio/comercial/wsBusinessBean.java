/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beansNegocio.comercial;

import com.google.gson.Gson;
import entidades.comercial.preciosEntity;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class wsBusinessBean implements IwsBusinessBean {

    @Override
    public String wsSentJson(List<preciosEntity> preciosNuevaLista) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(540, TimeUnit.SECONDS)
                .writeTimeout(540, TimeUnit.SECONDS)
                .readTimeout(540, TimeUnit.SECONDS)
                .build();

        Gson gson = new Gson();
        String cadenatToJson = gson.toJson(preciosNuevaLista);
        String res;

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, cadenatToJson);

        Request request = new Request.Builder()
                //.url("http://213.62.216.84:10013/preciosRest/webresources/Prices/create")
                .url("http://localhost:8080/preciosRest/webresources/Prices/create")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
            JSONObject json = new JSONObject(response.body().string());
            res = json.getString("valor");
        } catch (JSONException ex) {
            Logger.getLogger(wsBusinessBean.class.getName()).log(Level.SEVERE, null, ex);
            return "Err";
        } catch (IOException ex) {
            Logger.getLogger(wsBusinessBean.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        }
        
        return res;
    }
}
