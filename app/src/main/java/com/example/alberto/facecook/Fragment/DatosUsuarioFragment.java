package com.example.alberto.facecook.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.alberto.facecook.BaseDeDatos.BDExterna.ControlUsuariosRequest;
import com.example.alberto.facecook.BaseDeDatos.BDExterna.ExtraerUsuariosRequest;
import com.example.alberto.facecook.Dialog.LoginProgressDialog;
import com.example.alberto.facecook.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class DatosUsuarioFragment extends Fragment {

    /* Atributos */
    private CircleImageView imgUsuario;
    private TextInputEditText   txieNick,
                                txieNombre,
                                txieApellidos,
                                txieFecha,
                                txieCorreo,
                                txieTlf,
                                txieFechaCreacion,
                                txieComentarios;
    private OnFragmentInteractionListener mListener;
    LoginProgressDialog progress;

    public DatosUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_datos_usuario, container, false);

        this.imgUsuario = (CircleImageView)view.findViewById(R.id.profile_image);
        this.txieNick = (TextInputEditText)view.findViewById(R.id.txieNick);
        this.txieNombre = (TextInputEditText)view.findViewById(R.id.txieNombre);
        this.txieApellidos = (TextInputEditText)view.findViewById(R.id.txieApellidos);
        this.txieFecha = (TextInputEditText)view.findViewById(R.id.txieFecha);
        this.txieCorreo = (TextInputEditText)view.findViewById(R.id.txieCorreo);
        this.txieTlf = (TextInputEditText)view.findViewById(R.id.txieTlf);
        this.txieFechaCreacion = (TextInputEditText)view.findViewById(R.id.txieFechaCreacion);
        this.txieComentarios = (TextInputEditText)view.findViewById(R.id.txieComentarios);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void cargarUsuario(final Context context, final String user){
        progress = new LoginProgressDialog(context, "Cargando",
                "Conectando con el servidor...");
        progress.execute();

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject object = null;
                    for (int i = 0; i < response.length(); i++){
                        /* En la comparación, por seguridad las dos variables a comparar
                         * se ponen en minuscula, por si alguna no lo estuviese */
                        if (response.getJSONObject(i).getString("Login").toLowerCase()
                                .equals(user.toLowerCase())){
                            object = response.getJSONObject(i);
                        }
                    }
                    Picasso.with(context).load(object.getString("Foto")).into(imgUsuario);
                    txieNick.setText(object.getString("Login"));
                    txieNombre.setText(object.getString("Nombre"));
                    txieApellidos.setText(object.getString("Apellidos"));
                    txieFecha.setText(object.getString("Fecha de nacimiento"));
                    txieCorreo.setText(object.getString("Email"));
                    txieTlf.setText(object.getString("Telefono"));
                    txieFechaCreacion.setText(object.getString("Fecha de alta"));
                    txieComentarios.setText(object.getString("Comentarios"));
                }catch (JSONException e){
                    e.getMessage();
                }
                progress.parar();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(getActivity().getCurrentFocus(),
                        "Algo raro ha pasado con el servidor", Snackbar.LENGTH_LONG).show();
                progress.parar();
            }
        };
        ExtraerUsuariosRequest request = new ExtraerUsuariosRequest(listener, errorListener);
        RequestQueue cola = Volley.newRequestQueue(context);
        cola.add(request);
    }

    /**
     * Interfaz para conectar con la actividad principal
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteractionDatosUsuarios();
    }
}
