package com.example.alberto.facecook.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alberto.facecook.Adaptadores.AdapterRecetas;
import com.example.alberto.facecook.BaseDeDatos.Platos.Plato;
import com.example.alberto.facecook.Dialog.CategoriasPlatosDialog;
import com.example.alberto.facecook.Fragment.RecetasFragment;
import com.example.alberto.facecook.R;

public class NavigationActivity extends AppCompatActivity
        implements RecetasFragment.OnFragmentInteractionListener,
        AdapterRecetas.respuestaOnClickRecyclerViewRecetas,
        CategoriasPlatosDialog.respuestaDialogCategoriasPlatos {

    /* Elementos */
    private Toolbar toolbar;

    /* Fragmets */
    private FragmentTransaction fragmentTransaction;
    private RecetasFragment recetasFragment;

    /* Variables para las ActivitiesForResult */
    private final int REQUEST_RECETA_NUEVA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        this.iniciarElementos();
        this.iniciarToolbar("Navigation Activity");
        this.iniciarFragments();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarNavigationActivity);
    }

    /**
     * Carga el toolbar en la actividad
     */
    private void iniciarToolbar(String titulo){
        this.toolbar.setTitle(titulo);
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Inicia los fragments
     */
    private void iniciarFragments(){
        /* Iniciar los fragments */
        this.recetasFragment = new RecetasFragment();
        /* Se inicializa el objeto sobre el que se va a cargar los fragment */
        this.fragmentTransaction= getSupportFragmentManager().beginTransaction();
        //getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragment, this.recetasFragment).commit();
        /* Cargar fragment por defecto */
        this.fragmentTransaction.add(R.id.contenedorFragment, this.recetasFragment).commit();

    }

    /**
     * Método que carga en el toolbar un menú
     *
     * @param menu :Menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_navigation_activity, menu);
        return true;
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
     * Método que es ejecutado desde RecetasFragment, se sabe que desde donde se ejecuta
     * es porque se quiere abrir la actividad Nueva Receta
     */
    @Override
    public void onFragmentInteraction() {
        Intent intent = new Intent(getApplicationContext(), AgregarRecetaActivity.class);
        startActivityForResult(intent, REQUEST_RECETA_NUEVA);
    }

    /**
     * Método que se ejecuta desde AdapterRecetas, lo que hace es abrir una actividad
     * para visualizar la receta
     *
     * @param plato :Plato
     */
    @Override
    public void onRespuestaOnClickRecyclerViewRecetas(Plato plato) {
        Intent intent = new Intent(getApplicationContext(), VisorPdfActivity.class);
        intent.putExtra("titulo", plato.getNombre());
        intent.putExtra("url", plato.getUrlPdf());
        startActivity(intent);
    }

    /**
     * Método que se ejecuta desde el DialogCategoriasPlatos, lo que hace es cargar
     * el adaptador filtrado
     *
     * @param categoria :String
     */
    @Override
    public void onRespuestaDialogCategoriasPlatos(String categoria) {
        this.recetasFragment.cargarAdaptador(categoria);
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
            this.recetasFragment.cargarAdaptador();
        }
    }
}
