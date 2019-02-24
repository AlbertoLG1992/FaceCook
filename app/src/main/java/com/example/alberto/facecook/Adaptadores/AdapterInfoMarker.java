package com.example.alberto.facecook.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.alberto.facecook.Clases.Usuario;
import com.example.alberto.facecook.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterInfoMarker implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater inflater;
    private ArrayList<Usuario> arrayListUsuarios;

    public AdapterInfoMarker(LayoutInflater inflater, ArrayList<Usuario> arrayListUsuarios){
        this.inflater = inflater;
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

        /* Para descargarse la foto de usuario es necesario saltarse las resticciones
         * de seguridad de Android */
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /* Se descarga la imagen del servidor y en vez de almacenarlo en memoria directamente lo
         * vierte en el textView */
        Bitmap bitmap = null;
        try {
            InputStream str = new URL(usuarioEscogido.getUrlFoto()).openStream();
            bitmap = BitmapFactory.decodeStream(str);
            str.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagen.setImageBitmap(bitmap);

        TextView txvNick = (TextView)view.findViewById(R.id.txvNick);
        txvNick.setText(usuarioEscogido.getNick());

        TextView txvNombre = (TextView)view.findViewById(R.id.txvNombre);
        txvNombre.setText(usuarioEscogido.getNombre() + " " + usuarioEscogido.getApellidos());

        TextView txvComentarios = (TextView)view.findViewById(R.id.txvComentarios);
        txvComentarios.setText(usuarioEscogido.getComentarios());

        return view;
    }
}
