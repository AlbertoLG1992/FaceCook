package com.example.alberto.facecook.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.alberto.facecook.Clases.Usuario;
import com.example.alberto.facecook.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterInfoMarker implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater inflater;
    private ArrayList<Usuario> arrayListUsuarios;
    private Context context;

    public AdapterInfoMarker(LayoutInflater inflater, Context context, ArrayList<Usuario> arrayListUsuarios){
        this.inflater = inflater;
        this.context = context;
        this.arrayListUsuarios = arrayListUsuarios;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = inflater.inflate(R.layout.adapter_info_marcker, null);

        /* Se busca en el arrayList el usuario que se ha pulsado */
        Usuario usuarioEscogido = null;
        for (int i = 0; i < arrayListUsuarios.size(); i++){
            if (arrayListUsuarios.get(i).getNick().equals(marker.getTitle())){
                usuarioEscogido = arrayListUsuarios.get(i);
            }
        }

        /* Se inserta en los elementos del xml */
        CircleImageView imagen = (CircleImageView)view.findViewById(R.id.profile_image);
        Picasso.with(context).load(usuarioEscogido.getUrlFoto()).into(imagen);

        TextView txvNick = (TextView)view.findViewById(R.id.txvNick);
        txvNick.setText(usuarioEscogido.getNick());

        TextView txvNombre = (TextView)view.findViewById(R.id.txvNombre);
        txvNombre.setText(usuarioEscogido.getNombre() + " " + usuarioEscogido.getApellidos());

        TextView txvComentarios = (TextView)view.findViewById(R.id.txvComentarios);
        txvComentarios.setText(usuarioEscogido.getComentarios());

        return view;
    }
}
