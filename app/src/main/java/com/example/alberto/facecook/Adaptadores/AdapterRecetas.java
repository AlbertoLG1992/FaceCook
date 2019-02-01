package com.example.alberto.facecook.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alberto.facecook.BaseDeDatos.Platos.Plato;
import com.example.alberto.facecook.BaseDeDatos.Platos.TablaPlato;
import com.example.alberto.facecook.R;

import java.util.ArrayList;

public class AdapterRecetas extends RecyclerView.Adapter<AdapterRecetas.RecetasViewHolder> {

    /* Atributos */
    private ArrayList<Plato> listaPlatos;
    private respuestaOnClickRecyclerViewRecetas respuesta;

    /**
     * Constructor de clase
     *
     * @param context :Context
     */
    public AdapterRecetas(Context context){
        respuesta = (respuestaOnClickRecyclerViewRecetas)context;
        TablaPlato tablaPlato = new TablaPlato(context);
        this.listaPlatos = tablaPlato.verTodosPlatos();
    }

    /**
     * Acualiza el Adapter para filtrar por categorias
     *
     * @param context :Context
     * @param categoria :String
     */
    public void filtrarPorCategoria(Context context, String categoria){
        TablaPlato tablaPlato = new TablaPlato(context);
        this.listaPlatos = tablaPlato.verTodosPlatosFiltradosCategoria(categoria);
    }

    /**
     * Acualiza el Adapter para filtrar por nombres
     *
     * @param context :Context
     * @param nombre :String
     */
    public void filtrarPorNombre(Context context, String nombre){
        TablaPlato tablaPlato = new TablaPlato(context);
        this.listaPlatos = tablaPlato.verTodosPlatosFiltradosNombre(nombre);
    }

    @NonNull
    @Override
    public RecetasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecetasViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_recetas, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecetasViewHolder recetasViewHolder, int i) {
        final Plato plato = listaPlatos.get(i); //El plato pulsado
        recetasViewHolder.imgReceta.setImageBitmap(plato.getCategoriaPlato().getFoto());
        recetasViewHolder.txvNombreReceta.setText(plato.getNombre());

        /* Listener del item pulsado que abrirá la actividad de VisorPdfActivity */
        recetasViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Envia la respuesta a la actividad desde donde ha sido invocado */
                respuesta.onRespuestaOnClickRecyclerViewRecetas(plato);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPlatos.size();
    }

    class RecetasViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgReceta;
        private TextView txvNombreReceta;
        private View view;

        public RecetasViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imgReceta = (ImageView)itemView.findViewById(R.id.imgAdapRecetas);
            txvNombreReceta = (TextView)itemView.findViewById(R.id.txvNombAdapRecetas);
        }
    }

    /**
     * Interface para poner comunicarse con la actividad que lo ha llamado
     */
    public interface respuestaOnClickRecyclerViewRecetas{
        public void onRespuestaOnClickRecyclerViewRecetas(Plato plato);
    }
}
