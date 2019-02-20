package com.example.alberto.facecook.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.alberto.facecook.BaseDeDatos.BDExterna.ControlUsuariosRequest;
import com.example.alberto.facecook.Dialog.LoginProgressDialog;
import com.example.alberto.facecook.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* Elementos */
    private Button btnEntrar;
    private LottieAnimationView lottieAnimationView;
    private EditText edtUsuario, edtPassword;
    private TextView txvRegistro;

    /* Atributos */
    private int loopCount = 1;

    /* Dialog */
    private LoginProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.iniciarElementos();
        this.esconderStaturBar();
        this.pedirPermisosLocation();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.btnEntrar = (Button)findViewById(R.id.btnEntrar);
        this.lottieAnimationView = (LottieAnimationView)findViewById(R.id.lottieViewMain);
        this.edtUsuario = (EditText)findViewById(R.id.edtUsuario);
        this.edtPassword = (EditText)findViewById(R.id.edtPassword);
        this.txvRegistro = (TextView)findViewById(R.id.txvRegistro);

        /* Clicklable */
        this.btnEntrar.setOnClickListener(this);
        this.lottieAnimationView.setOnClickListener(this);
        this.txvRegistro.setOnClickListener(this);

        /* Añade un listenner a LottieAnimationView */
        this.lottieAnimationView.addAnimatorListener(loopListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEntrar:{
                if (this.comprobarCamposRellenos()) {

                    progress = new LoginProgressDialog(this, "Autenticando",
                            "Conectando con el servidor...");
                    progress.execute();

                    /* Listener para la petición php de login de usuario */
                    Response.Listener<String> respuesta = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonRespuesta = new JSONObject(response);
                                String mensaje = jsonRespuesta.getString("mensaje");
                                switch (mensaje){
                                    case "Usuario no existe":{
                                        mostrarMensajeError("usuario");
                                        break;
                                    }
                                    case "Pass erronea":{
                                        mostrarMensajeError("pass");
                                        break;
                                    }
                                    case "Pass correcta":{
                                        Intent intent = new Intent(getApplicationContext(),
                                                NavigationActivity.class);
                                        intent.putExtra("nombre", edtUsuario.getText().toString());
                                        startActivity(intent);
                                        vaciarCampos();
                                        break;
                                    }
                                }
                            }catch (JSONException e){
                                e.getMessage();
                            }
                            progress.parar(); //Para parar el progress dialog
                        }
                    };
                    /* Listener para cuando la petición de php no puede llegar a su destino */
                    Response.ErrorListener respuestaError = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mostrarMensajeError("servidor");
                            progress.parar(); //Para parar el progress dialog
                        }
                    };

                    ControlUsuariosRequest request = new ControlUsuariosRequest(
                            edtUsuario.getText().toString(),
                            edtPassword.getText().toString(),
                            respuesta, respuestaError);
                    RequestQueue cola = Volley.newRequestQueue(this);
                    cola.add(request);
                }
                break;
            }
            case R.id.lottieViewMain:{
                this.cargarAnimacion("Enfadado");
                break;
            }
            case R.id.txvRegistro:{
                Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    /**
     * Vacía los campo de usuario y contraseña
     */
    private void vaciarCampos(){
        this.edtUsuario.setText("");
        this.edtPassword.setText("");
        this.edtUsuario.requestFocus();
    }

    /**
     * Comprueba que los EditText de usuario y contraseña no estan vacíos
     *
     * @return true en caso de que contienen caracteres en su interior y false si estan vacíos
     */
    private boolean comprobarCamposRellenos(){
        if (!this.edtUsuario.getText().toString().isEmpty()){
            if (!this.edtPassword.getText().toString().isEmpty()){
                return true;
            }
        }
        this.mostrarMensajeError("rellenar");
        return false;
    }

    /**
     * Muestra un mensaje de error en un Snackbar con una animación de error
     *
     * @param error :String
     */
    private void mostrarMensajeError(String error){
        String animacion = "Error";

        if (error.equals("usuario")){
            this.edtUsuario.setError("El usuario no existe");
            this.edtUsuario.requestFocus();
            error = "Error al logear";

        } else if (error.equals("pass")){
            this.edtPassword.setError("La contraseña es erronea");
            this.edtPassword.requestFocus();
            error = "Error al logear";

        } else if (error.equals("servidor")){
            error = "No se puede conectar con el servidor";
            animacion = "conexion";

        } else if (error.equals("rellenar")){
            error = "El usuario y la contraseña tienen que estar rellenos";

        }
        Snackbar.make(getCurrentFocus(), error, Snackbar.LENGTH_LONG).show();
        this.cargarAnimacion(animacion);
    }

    /**
     * Cambia la animación del LottieAnimationView
     *
     * @param opcion :String
     */
    private void cargarAnimacion(String opcion){
        if (opcion.equals("Enfadado")){
            this.lottieAnimationView.setAnimation("a_very_angry_sushi.json");

        }else if (opcion.equals("Error")){
            this.lottieAnimationView.setAnimation("x_pop.json");

        }else if (opcion.equals("conexion")){
            this.lottieAnimationView.setAnimation("network-error.json");

        }
        this.lottieAnimationView.loop(true);
        this.lottieAnimationView.playAnimation();

        this.loopCount = 1; //Para que vuelva a cargar la animación como en la primera vuelta
    }

    /**
     * Listenner de LottieAnimationView
     */
    final Animator.AnimatorListener loopListener = new AnimatorListenerAdapter() {
        @Override public void onAnimationRepeat(Animator animation) {
            /* Para que en cada vuelta no haga las mismas instrucciones, de esta forma
             * solo se cargarán en la primera vuelta */
            if (loopCount == 1) {
                lottieAnimationView.setAnimation("sushi_fitness.json");
                lottieAnimationView.loop(true);
                lottieAnimationView.playAnimation();
            }
            loopCount++;
        }
    };

    /**
     * Esconde el clor del statur bar
     */
    private void esconderStaturBar(){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * Método para pedir los permisos de localización, de esta forma no molestará al usuario
     * al entrar en con su cuenta y podrá enviar sus coordenadas desde el principio
     */
    private void pedirPermisosLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //PERMISOS DE LOCALIZACIÓN
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                Log.i("checkPermisionLocation", "Se han pedido los permisos de localización");
            }else{
                Log.i("checkPermisionLocation", "Permisos de localización cargados " +
                        "correctamente");
            }
        }
    }
}
