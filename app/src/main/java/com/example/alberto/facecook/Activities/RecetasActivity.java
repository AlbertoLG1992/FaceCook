package com.example.alberto.facecook.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.alberto.facecook.Adaptadores.AdapterRecetas;
import com.example.alberto.facecook.Dialog.CategoriasPlatosDialog;
import com.example.alberto.facecook.R;

public class RecetasActivity extends AppCompatActivity
        implements CategoriasPlatosDialog.respuestaDialogCategoriasPlatos{

    /* Elementos */
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    /* Atributos */
    private AdapterRecetas adapterRecetas;
    private GridLayoutManager gridLayoutManager;

    /* Variables para las ActivitiesForResult */
    private final int REQUEST_RECETA_NUEVA = 1;

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
                Intent intent = new Intent(getApplicationContext(), AgregarRecetaActivity.class);
                startActivityForResult(intent, REQUEST_RECETA_NUEVA);
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
        this.recyclerView = (RecyclerView)findViewById(R.id.rvRecetas);
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
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterRecetas = new AdapterRecetas(this);
        recyclerView.setAdapter(adapterRecetas);
    }

    /**
     * Método para cargar el adaptador filtrando por categoría
     *
     * @param categoria :String
     */
    private void cargarAdaptadorFiltrado(String categoria){
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterRecetas = new AdapterRecetas(this, categoria);
        recyclerView.setAdapter(adapterRecetas);
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
     * Método iniciado al volver de una actividad iniciada con ActivityForResult
     *
     * @param requestCode :int
     * @param resultCode :int
     * @param data :Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if ((requestCode == REQUEST_RECETA_NUEVA) && resultCode == RESULT_OK){
            this.cargarAdaptador();
        }
    }
}
