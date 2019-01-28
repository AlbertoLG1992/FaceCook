package com.example.alberto.facecook.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.example.alberto.facecook.Activities.ChoiceActivity;
import com.example.alberto.facecook.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* Elementos */
    Button btnEntrar;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.iniciarElementos();
        this.esconderStaturBar();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.btnEntrar = (Button)findViewById(R.id.btnEntrar);
        this.lottieAnimationView = (LottieAnimationView)findViewById(R.id.lottieViewMain);

        /* Clicklable */
        this.btnEntrar.setOnClickListener(this);
        this.lottieAnimationView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEntrar:{
                Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lottieViewMain:{
                // TODO CAMBIAR LA IMAGEN
                break;
            }
        }
    }

    /**
     * Esconde el clor del statur bar
     */
    private void esconderStaturBar(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
