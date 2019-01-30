package com.example.alberto.facecook.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alberto.facecook.Activities.VisorPdfActivity;
import com.example.alberto.facecook.BaseDeDatos.Platos.Plato;
import com.example.alberto.facecook.BaseDeDatos.Platos.TablaPlato;
import com.example.alberto.facecook.R;

import java.util.ArrayList;

public class AdapterRecetas extends RecyclerView.Adapter<AdapterRecetas.RecetasViewHolder> {

    private ArrayList<Plato> listaPlatos;
    private Context context;

    /**
     * Constructor de clase
     *
     * @param context :Context
     */
    public AdapterRecetas(Context context){
        this.context = context;
        TablaPlato tablaPlato = new TablaPlato(context);
        this.listaPlatos = tablaPlato.verTodosPlatos();
    }

    /**
     * Constructor de clase para filtrar los platos por categorias
     *
     * @param context :Context
     * @param categoria :String
     */
    public AdapterRecetas(Context context, String categoria){
        this.context = context;
        TablaPlato tablaPlato = new TablaPlato(context);
        this.listaPlatos = tablaPlato.verTodosPlatosFiltrados(categoria);
    }

    @NonNull
    @Override
    public RecetasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecetasViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_recetas, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecetasViewHolder recetasViewHolder, int i) {
        final Plato plato = listaPlatos.get(i);
        recetasViewHolder.imgReceta.setImageBitmap(plato.getCategoriaPlato().getFoto());
        recetasViewHolder.txvNombreReceta.setText(plato.getNombre());

        /* Listener del item pulsado que abrirá la actividad de VisorPdfActivity */
        recetasViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VisorPdfActivity.class);
                intent.putExtra("titulo", plato.getNombre());
                intent.putExtra("url", plato.getUrlPdf());
                context.startActivity(intent);
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
}
