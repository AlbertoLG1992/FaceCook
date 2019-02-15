package com.example.alberto.facecook.BaseDeDatos.BDExterna;

import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.alberto.facecook.Clases.Usuario;

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
     * @param listenerError :Response.ErrorListener
     */
    public ControlUsuariosRequest(String login, String pass, Response.Listener<String> listener ,
                                  Response.ErrorListener listenerError){
        super(Method.POST, URL, listener, listenerError);
        this.parametros = new HashMap<>();
        this.parametros.put("login", login);
        this.parametros.put("pass", pass);
    }

    /**
     * Constructor para instanciar un usuario en la base de datos
     *
     * @param usuario :Usuario
     * @param listener :Response.Listener<String>
     * @param listenerError :Response.ErrorListener
     */
    public ControlUsuariosRequest(Usuario usuario, Response.Listener<String> listener ,
                                  Response.ErrorListener listenerError){
        super(Method.POST, URL, listener, listenerError);
        this.parametros = new HashMap<>();
        this.parametros.put("imagen", usuario.getFotoUsuario().convertirString());
        this.parametros.put("nick", usuario.getNick());
        this.parametros.put("pass", usuario.getPass());
        this.parametros.put("nombre", usuario.getNombre());
        this.parametros.put("apellidos", usuario.getApellidos());
        this.parametros.put("fecha_nacimiento", usuario.getFechaNacimiento());
        this.parametros.put("correo", usuario.getCorreoElectronico());
        this.parametros.put("tlf", usuario.getTlf());
        this.parametros.put("comentarios", usuario.getComentarios());
        this.parametros.put("latitud", String.valueOf(usuario.getLatitud()));
        this.parametros.put("longitud", String.valueOf(usuario.getLongitud()));
    }

    @Override
    protected Map<String, String> getParams(){
        return parametros;
    }


}
