package com.example.alberto.facecook.BaseDeDatos.BDExterna;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class ExtraerUsuariosRequest extends JsonArrayRequest {

    private static final String URL = "http://192.168.1.148/apiFaceCook/";

    /**
     * Constructor para traer todos los usuarios en un Json
     *
     * @param listener :Response.Listener<JSONArray>
     * @param errorListener :Response.ErrorListener
     */
    public ExtraerUsuariosRequest(Response.Listener<JSONArray> listener,
                                  Response.ErrorListener errorListener) {
        super(URL, listener, errorListener);
    }
}
