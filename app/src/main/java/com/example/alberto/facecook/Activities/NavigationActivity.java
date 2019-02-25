package com.example.alberto.facecook.Activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.alberto.facecook.Adaptadores.AdapterRecetas;
import com.example.alberto.facecook.Clases.Plato;
import com.example.alberto.facecook.Dialog.CategoriasPlatosDialog;
import com.example.alberto.facecook.Fragment.CocinerosFragment;
import com.example.alberto.facecook.Fragment.DatosUsuarioFragment;
import com.example.alberto.facecook.Fragment.RecetasFragment;
import com.example.alberto.facecook.Localizacion.LocalizacionListener;
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
    private Menu menuDeItems;
    private String nombreUsuarioActivo;
    private LocalizacionListener localizacionListener;

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
                recetasFragment.cargarAdaptador("Nombre",s);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                recetasFragment.cargarAdaptador("Nombre",query);
                closeKeyboard();
                return true;
            }
        });

        /* Listenner de Search View para buscar usuarios */
        MenuItem buscarUsuariosItem = menu.findItem(R.id.itemBuscarUsuarios);
        final SearchView searchViewUsuarios = (SearchView) buscarUsuariosItem.getActionView();
        searchViewUsuarios.setQueryHint("Buscar usuarios...");
        searchViewUsuarios.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                cocinerosFragment.moverCamaraMarker(s);
                closeKeyboard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    /**
     * Método para ocultar el teclado
     */
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager inp = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inp.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
            case R.id.itemCancelarVerUsuario:{
                this.bottomNavigationView.setSelectedItemId(R.id.itemVerCocineros);
                break;
            }
            case R.id.itemLocalizarme:{
                this.cocinerosFragment.moverCamaraMarker(this.nombreUsuarioActivo);
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
                this.cargarFragmentDatosUsuario(this.nombreUsuarioActivo);
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
        MenuItem menuItemCancelarVerUsuario = menuDeItems.findItem(R.id.itemCancelarVerUsuario);
        MenuItem menuItemLocalizarme = menuDeItems.findItem(R.id.itemLocalizarme);
        MenuItem menuItemBuscarUsuarios = menuDeItems.findItem(R.id.itemBuscarUsuarios);

        /* Fragment de recetas */
        if (ver.equals("recetas")){
            searchViewItemRecetas.setVisible(true);
            menuItemFiltrarRecetas.setVisible(true);
        }else {
            searchViewItemRecetas.setVisible(false);
            menuItemFiltrarRecetas.setVisible(false);
        }

        /* Fragment viendo datos del perfil de usuarios */
        if (ver.equals("Usuario")){
            menuItemCancelarVerUsuario.setVisible(true);
        }else {
            menuItemCancelarVerUsuario.setVisible(false);
        }

        /* Fragment viendo los cocineros */
        if (ver.equals("cocineros")){
            menuItemLocalizarme.setVisible(true);
            menuItemBuscarUsuarios.setVisible(true);
        }else {
            menuItemLocalizarme.setVisible(false);
            menuItemBuscarUsuarios.setVisible(false);
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
    private void cargarFragmentDatosUsuario(String user){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contenedorFragment, this.datosUsuarioFragment).commit();
        this.datosUsuarioFragment.cargarUsuario(this, user);
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
    public void onFragmentInteractionCocineros(String modo, String info) {
        switch (modo){
            case "Ficha":{
                getSupportActionBar().setTitle("Ficha de usuario");
                this.mostrarItemsMenu("Usuario");
                this.cargarFragmentDatosUsuario(info);
                break;
            }
            case "Sms":{
                if (checkPermisionSendSMS()){
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + info));
                    intent.putExtra("sms_body", "Saludos!, \nSoy " + nombreUsuarioActivo +
                            " y me gustaria contactar contigo. \nᕙ(⇀‸↼‶)ᕗ");
                    startActivity(intent);
                }
                break;
            }
            case "Email":{
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", info, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Holi");
                intent.putExtra(Intent.EXTRA_TEXT, "Saludos!, \nSoy " + nombreUsuarioActivo +
                        " y me gustaria contactar contigo. \nᕙ(⇀‸↼‶)ᕗ");
                startActivity(intent);
                break;
            }
            case "Llamar":{
                if (checkPermisionCallPhone()){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + info));
                    startActivity(intent);
                }
                break;
            }
        }
    }

    /**
     * Método que se ejecuta desde DatosUsuariosFragment
     */
    @Override
    public void onFragmentInteractionDatosUsuarios() {

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

    /**
     * Inicializa el LocalizationListener y lo fuerza a enviar las coordenadas al servidor
     */
    private void enviarCoordenadasServidor(){
        if (checkPermisionLocation()) {
            localizacionListener = new LocalizacionListener(getApplicationContext(), nombreUsuarioActivo);
            if (localizacionListener.getIsGPSTrackingEnabled()) {
                localizacionListener.actualizarCoordServidor();
            }
        }
    }

    /**
     * Valida los permisos para acceder a la localización
     *
     * @return :True si los permisos estan cargados, en caso negativo los pide
     */
    private boolean checkPermisionLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                Log.i("checkPermisionLocation", "Se han pedido los permisos de localización");
                return false;
            }else{
                Log.i("checkPermisionLocation", "Permisos de localización cargados " +
                        "correctamente");
                return true;
            }
        }
        return true;
    }

    /**
     * Valida los permisos para enviar sms
     *
     * @return :True si los permisos estan cargados, en caso negativo los pide
     */
    private boolean checkPermisionSendSMS(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 100);
                Log.i("checkPermisionLocation", "Se han pedido los permisos de localización");
                Toast.makeText(this, "Inténtelo de nuevo", Toast.LENGTH_LONG).show();
                return false;
            }else{
                Log.i("checkPermisionLocation", "Permisos de SMS cargados " +
                        "correctamente");
                return true;
            }
        }
        return true;
    }

    /**
     * Valida los permisos para llamar por tlf
     *
     * @return :True si los permisos estan cargados, en caso negativo los pide
     */
    private boolean checkPermisionCallPhone(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 100);
                Log.i("checkPermisionLocation", "Se han pedido los permisos de localización");
                Toast.makeText(this, "Inténtelo de nuevo", Toast.LENGTH_LONG).show();
                return false;
            }else{
                Log.i("checkPermisionLocation", "Permisos de CALL PHONE cargados " +
                        "correctamente");
                return true;
            }
        }
        return true;
    }
}
