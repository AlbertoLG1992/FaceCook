package com.example.alberto.facecook.BaseDeDatos.BDExterna;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ControlUsuariosRequest extends StringRequest {

    private static final String URL = "http://192.168.1.148/apiFaceCook/";
    private Map<String, String> parametros;

    /**
     * Constructor para logear usuarios
     *
     * @param login :String
     * @param pass :String
     * @param listener :Response.Listener<String>
     */
    public ControlUsuariosRequest(String login, String pass, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        this.parametros = new HashMap<>();
        this.parametros.put("login", login+"");
        this.parametros.put("pass", pass+"");
    }

    @Override
    protected Map<String, String> getParams(){
        return parametros;
    }


}
