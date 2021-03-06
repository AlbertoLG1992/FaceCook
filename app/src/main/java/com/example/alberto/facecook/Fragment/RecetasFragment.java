package com.example.alberto.facecook.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alberto.facecook.Adaptadores.AdapterRecetas;
import com.example.alberto.facecook.R;

public class RecetasFragment extends Fragment{

    /* Atributos */
    private RecyclerView recyclerView;
    private AdapterRecetas adapterRecetas;
    private GridLayoutManager gridLayoutManager;
    private FloatingActionButton floatingActionButton;
    private OnFragmentInteractionListener mListener;

    /**
     * Constructor de clase
     */
    public RecetasFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Se infla el view donde vamos a trabajar */
        View view = inflater.inflate(R.layout.fragment_recetas, container, false);

        /* Se asigna el recyclerView al xml */
        this.recyclerView = (RecyclerView)view.findViewById(R.id.rvRecetasFragment);
        this.floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatBtnRecetasFragment);

        /* Se carga el adaptador */
        this.cargarAdaptador();

        /* Listener de FloatingActionButton */
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteractionRecetas();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interfaz para comunicarse con quien lo ha invocado, no se envia ningun dato
     * porque no es necesario, solo será invocado desde el FloatingActionButton
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteractionRecetas();
    }

    /**
     * Carga el adaptador sin filtrar en el recyclerView
     */
    public void cargarAdaptador(){
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapterRecetas = new AdapterRecetas(getContext());
        recyclerView.setAdapter(adapterRecetas);
    }

    /**
     * Carga el adaptador filtrado por categoria o nombre en el recyclerView
     *
     * @param categoria :filtraPor
     * @param categoria :String
     */
    public void cargarAdaptador(String filtraPor, String categoria){
        if (filtraPor.equals("Categoria")) {
            if (!categoria.equals("NoFiltrar")) {
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
                adapterRecetas = new AdapterRecetas(getContext());

                adapterRecetas.filtrarPorCategoria(getContext(), categoria);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapterRecetas);
            } else {
                this.cargarAdaptador();
            }
        }else{ //Se filtra por nombre de plato
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            adapterRecetas = new AdapterRecetas(getContext());

            adapterRecetas.filtrarPorNombre(getContext(), categoria);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapterRecetas);
        }
    }

}
