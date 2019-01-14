package com.example.alberto.facecook.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alberto.facecook.BaseDeDatos.Platos.Plato;
import com.example.alberto.facecook.BaseDeDatos.Platos.TablaPlato;
import com.example.alberto.facecook.R;

import java.util.ArrayList;

public class AdapterRecetas extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Plato> items;

    /**
     * Constructor de clase
     *
     * @param activity :Activity
     * @param context :Context
     */
    public AdapterRecetas(Activity activity, Context context){
        /* En el mismo constructor se realiza la consulta a la base de datos
         * y se carga en el arrayList */
        TablaPlato tablaPlato = new TablaPlato(context);
        this.items = tablaPlato.verTodosPlatos();
        this.activity = activity;
    }

    /**
     * Constructor de clase parametrizado para poder filtrarse
     *
     * @param activity :Activity
     * @param context :Context
     */
    public AdapterRecetas(Activity activity, Context context, String categoria){
        /* En el mismo constructor se realiza la consulta a la base de datos
         * y se carga en el arrayList */
        TablaPlato tablaPlato = new TablaPlato(context);
        this.items = tablaPlato.verTodosPlatosFiltrados(categoria);
        this.activity = activity;
    }

    /**
     * Devuelve el total de los elementos cargados en el listView
     *
     * @return int
     */
    @Override
    public int getCount() {
        return this.items.size();
    }

    /**
     * Devuelve el item seleccionado
     *
     * @param position :int
     * @return Object
     */
    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    /**
     * Devuelve el id del item seleccionado
     *
     * @param position :int
     * @return long
     */
    @Override
    public long getItemId(int position) {
        return this.items.get(position).getId();
    }

    /**
     * Carga los datos del ArrayList en el ListView
     *
     * @param position :int
     * @param convertView :View
     * @param parent :ViewGroup
     * @return View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Se carga el View, y en caso de que no se cargue bien se introduce manualmente
         * los parametros para que detecte el XML el cual tiene que cargar */
        View view = convertView;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_recetas, null);
        }

        /* Se extrae el item especifico que toca cargar y se añade a los objetos del XML */
        Plato plato = this.items.get(position);

        ImageView imgAdapRecetas = (ImageView)view.findViewById(R.id.imgAdapRecetas);
        imgAdapRecetas.setImageBitmap(plato.getCategoriaPlato().getFoto());

        TextView txvNombreAdapterReceta = (TextView)view.findViewById(R.id.txvNombAdapRecetas);
        txvNombreAdapterReceta.setText(plato.getNombre());

        return view;
    }
}
