package com.example.alberto.facecook.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.alberto.facecook.Adaptadores.AdapterRecetas;
import com.example.alberto.facecook.Clases.Plato;
import com.example.alberto.facecook.Dialog.CategoriasPlatosDialog;
import com.example.alberto.facecook.Fragment.CocinerosFragment;
import com.example.alberto.facecook.Fragment.DatosUsuarioFragment;
import com.example.alberto.facecook.Fragment.RecetasFragment;
import com.example.alberto.facecook.R;

public class NavigationActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        RecetasFragment.OnFragmentInteractionListener,
        CocinerosFragment.OnFragmentInteractionListener,
        DatosUsuarioFragment.OnFragmentInteractionListener,
        AdapterRecetas.respuestaOnClickRecyclerViewRecetas,
        CategoriasPlatosDialog.respuestaDialogCategoriasPlatos {

    /* Elementos */
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    /* Fragmets */
    private RecetasFragment recetasFragment;
    private CocinerosFragment cocinerosFragment;
    private DatosUsuarioFragment datosUsuarioFragment;

    /* Variables para las ActivitiesForResult */
    private final int REQUEST_RECETA_NUEVA = 1;

    /* Variables */
    private boolean searchViewCerrado = false;
    private Menu menuDeItems;
    private String nombreUsuarioActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        /* Se extrae del intent el usuario que se ha logueado */
        this.nombreUsuarioActivo = getIntent().getStringExtra("nombre");

        this.iniciarElementos();
        this.iniciarToolbar("Ver Recetas");
        this.iniciarFragments();
        this.enviarCoordenadasServidor();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarNavigationActivity);
        this.bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);

        /* Clickable */
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    /**
     * Carga el toolbar en la actividad
     */
    private void iniciarToolbar(String titulo){
        this.toolbar.setTitle(titulo);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Inicia los fragments
     */
    private void iniciarFragments(){
        /* Iniciar los fragments */
        this.recetasFragment = new RecetasFragment();
        this.cocinerosFragment = new CocinerosFragment();
        this.datosUsuarioFragment = new DatosUsuarioFragment();

        /* Cargar fragment por defecto */
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragment, this.recetasFragment).commit();
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
        this.menuDeItems = menu;

        /* Listener de Search View para buscar recetas */
        MenuItem searchViewItem = menu.findItem(R.id.itemBuscar);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Buscar recetas...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextChange(String s) {
                /* La variable searchViewCerrado existe porque al cerrar el searchView
                 * vuelve a ejecutar este método, por lo que para evitar que al volverse
                 * a ejecutar vuelva a filtrar sin valores y borre el filtrado */
                if (!searchViewCerrado){
                    recetasFragment.cargarAdaptador("Nombre",s);
                }else{
                    /* Este else es para que una vez se haya pulsado a intro y se cierre el
                     * searchView se pueda acceder otra vez que se pulse */
                    searchViewCerrado = false;
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewCerrado = true;
                recetasFragment.cargarAdaptador("Nombre",query);
                searchView.onActionViewCollapsed();
                return true;
            }
        });
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
        switch (item.getItemId()){
            case R.id.itemFiltrarCategorias:{
                CategoriasPlatosDialog dialog = new CategoriasPlatosDialog();
                dialog.show(getSupportFragmentManager(), "DialogoCategoriasPlatos");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.itemRecetas:{
                getSupportActionBar().setTitle("Ver Recetas");
                this.mostrarItemsMenu("recetas");
                this.cargarFragmentRecetas();
                return true;
            }
            case R.id.itemVerCocineros:{
                getSupportActionBar().setTitle("Ver Cocineros");
                this.mostrarItemsMenu("cocineros");
                this.cargarFragmentCocineros();
                return true;
            }
            case R.id.itemDatosUsuario:{
                getSupportActionBar().setTitle("Mis Datos");
                this.mostrarItemsMenu("usuario");
                this.cargarFragmentDatosUsuario();
                return true;
            }
        }
        return false;
    }

    /**
     * Muestra y oculta los items de los menús dependiendo del fragment actual
     *
     * @param ver :string
     */
    private void mostrarItemsMenu(String ver){
        MenuItem searchViewItemRecetas = menuDeItems.findItem(R.id.itemBuscar);
        MenuItem menuItemFiltrarRecetas = menuDeItems.findItem(R.id.itemFiltrarCategorias);

        if (ver.equals("recetas")){
            searchViewItemRecetas.setVisible(true);
            menuItemFiltrarRecetas.setVisible(true);
        }else {
            searchViewItemRecetas.setVisible(false);
            menuItemFiltrarRecetas.setVisible(false);
        }
    }

    /**
     * Carga el fragment RecetasFragment
     */
    private void cargarFragmentRecetas(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, this.recetasFragment).commit();
    }

    /**
     * Carga el fragment CocinerosFragment
     */
    private void cargarFragmentCocineros(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, this.cocinerosFragment).commit();
    }

    /**
     * Carga el fragment DatosUsuario
     */
    private void cargarFragmentDatosUsuario(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, this.datosUsuarioFragment).commit();
    }

    /**
     * Método que es ejecutado desde RecetasFragment, se sabe que desde donde se ejecuta
     * es porque se quiere abrir la actividad Nueva Receta
     */
    @Override
    public void onFragmentInteractionRecetas() {
        Intent intent = new Intent(getApplicationContext(), AgregarRecetaActivity.class);
        startActivityForResult(intent, REQUEST_RECETA_NUEVA);
    }

    /**
     * Método que es ejecutado desde CocinerosFragment
     */
    @Override
    public void onFragmentInteractionCocineros(Uri uri) {

    }

    /**
     * Método que se ejecuta desde DatosUsuariosFragment
     */
    @Override
    public void onFragmentInteractionDatosUsuarios(Uri uri) {

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
        this.recetasFragment.cargarAdaptador("Categoria", categoria);
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

    private void enviarCoordenadasServidor(){
        //TODO ENVIAR COORDENADAS AL SERVIDOR
    }
}
