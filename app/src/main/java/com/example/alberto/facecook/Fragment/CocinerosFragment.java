package com.example.alberto.facecook.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.alberto.facecook.Adaptadores.AdapterInfoMarker;
import com.example.alberto.facecook.BaseDeDatos.BDExterna.ExtraerUsuariosRequest;
import com.example.alberto.facecook.Clases.Usuario;
import com.example.alberto.facecook.Dialog.LoginProgressDialog;
import com.example.alberto.facecook.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CocinerosFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, View.OnClickListener {

    /* Atributos */
    private GoogleMap mMap; //Mapa
    private LinearLayout layoutImagenes;

    private LoginProgressDialog progress; //DialogProgress
    private ArrayList<Usuario> arrayListUsuarios; //ArrayList de usuarios
    private Context context; //Contexto de la actividad
    private LayoutInflater inflater;
    private AdapterInfoMarker adapterInfoMarker; //Adaptador de los Marker

    private OnFragmentInteractionListener mListener;

    /**
     * Constructor de clase
     */
    public CocinerosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;

        View view = inflater.inflate(R.layout.fragment_cocineros, container, false);

        /* Cargar elementos XML */
        this.layoutImagenes = (LinearLayout)view.findViewById(R.id.layoutImagenes);
        ImageView imgFicha = (ImageView)view.findViewById(R.id.imgFicha);
        ImageView imgSms = (ImageView)view.findViewById(R.id.imgSms);
        ImageView imgEmail = (ImageView)view.findViewById(R.id.imgEmail);
        ImageView imgLlamar = (ImageView)view.findViewById(R.id.imgLlamar);

        /* Hacer los elementos Clickables */
        imgFicha.setOnClickListener(this);
        imgSms.setOnClickListener(this);
        imgEmail.setOnClickListener(this);
        imgLlamar.setOnClickListener(this);

        /* Se inicia la actividad con el layout de las imagenes en invisible */
        layoutImagenes.setVisibility(View.INVISIBLE);

        /* Carga el fragment del mapa */
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

    /**
     * Clase que se ejecuta cuando el mapa se carga
     *
     * @param googleMap :GoogleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /* Se iguala el mapa con el que trabajaremos
         * al que nos da el fragment al cargarlo */
        this.mMap = googleMap;
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setOnMapClickListener(this);

        /* Se cargan los datos en el mapa y se coloca la cámara en la posición inicial */
        cargarDatosMapa();
        colocarCamaraInicial();

        /* Se crea y carga el adaptador en el que se visualizarán los Marker */
        this.adapterInfoMarker = new AdapterInfoMarker(inflater, arrayListUsuarios);
        this.mMap.setInfoWindowAdapter(this.adapterInfoMarker);
    }

    /**
     * Carga los datos de usuario de la base de datos
     */
    private void cargarDatosMapa(){
        progress = new LoginProgressDialog(this.context, "Cargando usuarios",
                "Conectando con el servidor...");
        progress.execute();

        // Se inicia el arrayList que se usuará para almacenar los usuarios
        arrayListUsuarios = new ArrayList<Usuario>();

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    /* Se recorre la respuesta entera  y se almacenan los datos en el
                     * arrayList */
                    for (int i = 0; i < response.length(); i++){

                        Usuario usuario = new Usuario(
                                response.getJSONObject(i).getString("Foto"),
                                response.getJSONObject(i).getString("Login"),
                                response.getJSONObject(i).getString("Nombre"),
                                response.getJSONObject(i).getString("Apellidos"),
                                response.getJSONObject(i).getString("Fecha de nacimiento"),
                                response.getJSONObject(i).getString("Email"),
                                response.getJSONObject(i).getString("Telefono"),
                                response.getJSONObject(i).getString("Fecha de alta"),
                                response.getJSONObject(i).getString("Comentarios"),
                                Double.parseDouble(response.getJSONObject(i).getString("Longitud")),
                                Double.parseDouble(response.getJSONObject(i).getString("Latitud")));

                        arrayListUsuarios.add(usuario);

                        /* Se pinta en el mapa el usuario */
                        addMarkerMap(usuario);
                    }
                }catch (JSONException e){
                    Log.e("EXCEPCION", e.getMessage());
                }
                progress.parar();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Algo raro ha pasado con el servidor",
                        Toast.LENGTH_LONG).show();
                progress.parar();
            }
        };
        ExtraerUsuariosRequest request = new ExtraerUsuariosRequest(listener, errorListener);
        RequestQueue cola = Volley.newRequestQueue(this.context);
        cola.add(request);
    }

    /**
     * Añade en el mapa un usuario
     *
     * @param usuario :Usuario
     */
    private void addMarkerMap(Usuario usuario){
        /* Se crea la localizazión a donde se situará al usuario */
        LatLng loc = new LatLng(usuario.getLatitud(), usuario.getLongitud());
        /* Se añade el marcador en el mapa usando el nick de usuario y su localizacion */
        mMap.addMarker(new MarkerOptions()
                .position(loc)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(usuario.getNick()));
    }

    /**
     * Mueve la cámara a un usuario en concreto
     *
     * @param user :String
     */
    public void moverCamaraMarker(String user){
        for (int i = 0; i < arrayListUsuarios.size(); i++){
            if (arrayListUsuarios.get(i).getNick().equals(user)){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(arrayListUsuarios.get(i).getLatitud(),
                                    arrayListUsuarios.get(i).getLongitud()), 6));
            }
        }
    }

    /**
     * Coloca la cámará en la posición inicial que se ha decidido
     */
    private void colocarCamaraInicial(){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.436770, -3.707009), 6));
    }

    /**
     * Al hacer un click en un Marker
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        this.layoutImagenes.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * Al hacer un click en el mapa
     */
    @Override
    public void onMapClick(LatLng latLng) {
        this.layoutImagenes.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgFicha:{
                this.mListener.onFragmentInteractionCocineros("Ficha",
                        this.adapterInfoMarker.getUsuarioEscogido().getNombre());
                break;
            }
            case R.id.imgSms:{
                this.mListener.onFragmentInteractionCocineros("Sms",
                        this.adapterInfoMarker.getUsuarioEscogido().getTlf());
                break;
            }
            case R.id.imgEmail:{
                this.mListener.onFragmentInteractionCocineros("Email",
                        this.adapterInfoMarker.getUsuarioEscogido().getCorreoElectronico());
                break;
            }
            case R.id.imgLlamar:{
                this.mListener.onFragmentInteractionCocineros("Llamar",
                        this.adapterInfoMarker.getUsuarioEscogido().getTlf());
                break;
            }
        }
    }

    /**
     * Interfaz para enviar datos a NavigationActivity
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteractionCocineros(String modo, String info);
    }
}
