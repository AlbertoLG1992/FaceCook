package com.example.alberto.facecook.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alberto.facecook.Adaptadores.AdapterRecetas;
import com.example.alberto.facecook.BaseDeDatos.Platos.Plato;
import com.example.alberto.facecook.Dialog.CategoriasPlatosDialog;
import com.example.alberto.facecook.R;

public class RecetasActivity extends AppCompatActivity implements CategoriasPlatosDialog.respuestaDialogCategoriasPlatos,
    AdapterView.OnItemClickListener{

    /* Elementos */
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    ListView listView;

    /* Atributos */
    AdapterRecetas adapterRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas);

        this.iniciarElementos();
        this.iniciarToolbar();
        this.cargarAdaptador();

        /* Listenner de FloatingActionButton */
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO AGREGAR NUEVA RECETA
            }
        });
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarRecetas);
        this.floatingActionButton = (FloatingActionButton)findViewById(R.id.floatBtnRecetas);
        this.listView = (ListView)findViewById(R.id.listViewRecetas);

        /* Clickables */
        this.listView.setOnItemClickListener(this);
    }

    /**
     * Carga el toolbar en la actividad
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle(R.string.activity_recetas_name);
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Método que carga en el toolbar un menú
     *
     * @param menu :Menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_recetas, menu);
        return true;
    }

    /**
     * Método para cargar el adaptador de forma normal
     */
    private void cargarAdaptador(){
        this.adapterRecetas = new AdapterRecetas(this, getApplicationContext());
        this.listView.setAdapter(this.adapterRecetas);
    }

    /**
     * Método para cargar el adaptador filtrando por categoría
     *
     * @param categoria :String
     */
    private void cargarAdaptadorFiltrado(String categoria){
        this.adapterRecetas = new AdapterRecetas(this, getApplicationContext(), categoria);
        this.listView.setAdapter(this.adapterRecetas);
    }

    /**
     * Método que se activa al pulsar sobre un item del menú
     *
     * @param item :MenuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        CategoriasPlatosDialog dialog = new CategoriasPlatosDialog();
        dialog.show(getSupportFragmentManager(), "DialogoCategoriasPlatos");

        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que se activa al volver del dialog CategoriasPlatos
     *
     * @param categoria :String
     */
    @Override
    public void onRespuestaDialogCategoriasPlatos(String categoria) {
        /* Si devuelve NoFiltrar es porque el usuario no quiere que se filtre */
        if (categoria.equals("NoFiltrar")){
            this.cargarAdaptador();
        }else {
            this.cargarAdaptadorFiltrado(categoria);
        }
    }

    /**
     * Metodo que se ejecuta al pulsar normal sobre el listView
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        /* Se extrae el plato del adaptador */
        Plato plato = (Plato)this.adapterRecetas.getItem(position);
        /* Se envia el título y la url del pdf en el intent a la nueva actividad
         * antes de iniciarla */
        Intent intent = new Intent(getApplicationContext(), VisorPdfActivity.class);
        intent.putExtra("titulo", plato.getNombre());
        intent.putExtra("url", plato.getUrlPdf());
        startActivity(intent);
    }
}
