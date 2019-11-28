package com.hazloakki.view;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import com.hazloakki.R;
import com.hazloakki.adaptadores.AccionesAdapter;
import com.hazloakki.modelos.AccionesDto;
import com.hazloakki.modelos.AccionesItem;
import com.hazloakki.modelos.Footer;
import com.hazloakki.modelos.Header;
import com.hazloakki.modelos.RecyclerViewItem;
import com.hazloakki.network.ConexionServicios;
import com.hazloakki.network.ConstantesServicios;
import com.hazloakki.utils.RecyclerItemClickListener;
import com.hazloakki.utils.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccionesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private String TAG = AccionesFragment.class.getSimpleName();
    private AccionesAdapter adaptadorAcciones = null;

    public AccionesFragment() {
    }

    public static AccionesFragment newInstance(String param1, String param2) {
        AccionesFragment fragment = new AccionesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_recyclerview_acciones, container, false);
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            //add space item decoration and pass space you want to give
            recyclerView.addItemDecoration(new Space(20));

        seleccionarAccion();
        //dataDummyRecyclerView(view);
        obtenerAcciones();
       return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void dataDummyRecyclerView(View view ) {
        //finally set adapter
        recyclerView.setAdapter(new AccionesAdapter(createDummyList(), getContext(),null));
    }
    //Method to create dummy data
    private List<RecyclerViewItem> createDummyList() {
        List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();
        Header header = new Header("Hola Jovani!! ¿Qué necesitas en estos momentos!!?", "HazloAkki",
                "https://cdn.pixabay.com/photo/2017/09/30/15/10/pizza-2802332_640.jpg");
        //add header
        recyclerViewItems.add(header);

        String[] imageUrls = {"https://cdn.pixabay.com/photo/2016/11/18/17/42/barbecue-1836053_640.jpg",
                "https://cdn.pixabay.com/photo/2016/07/11/03/23/chicken-rice-1508984_640.jpg",
                "https://cdn.pixabay.com/photo/2017/03/30/08/10/chicken-intestine-2187505_640.jpg",
                "https://cdn.pixabay.com/photo/2017/02/15/15/17/meal-2069021_640.jpg",
                "https://cdn.pixabay.com/photo/2017/06/01/07/15/food-2362678_640.jpg"};

        String[] titles = {
                "Comer",
                "Beber",
                "Bailar",
                "Descanzar",
                "Divertirse"
        };

        String[] descriptions = {"Encuentra cualquier tipo de lugar para comer",
                "Todos los lugares donde puedes encontrar tu cerveza favorito o un buen vino",
                "Te gusta el karaoke? o simplementa quiere bailar",
                "Encuentra los mejores lugares para descanzar",
                "Aburrido? aqui los mejores lugares, encuentra lo que buscas"};
        String[] price = {"10", "300", "500", "1500", "4"};

        boolean[] isHot = {true, false, true, true, false};
        for (int i = 0; i < imageUrls.length; i++) {
            AccionesItem accionesItem = new AccionesItem(null,titles[i], descriptions[i], imageUrls[i]);
            //add food items
            recyclerViewItems.add(accionesItem);
        }

        Footer footer = new Footer("Ofertas para decir SI!!",
                "Dale antes de que se acabe!!", "https://cdn.pixabay.com/photo/2016/12/26/17/28/background-1932466_640.jpg");
        //add footer
        recyclerViewItems.add(footer);
        return recyclerViewItems;
    }

    public List<RecyclerViewItem> viewCategoriasDetalle(JSONArray response){

        List<RecyclerViewItem> recyclerViewItems = new ArrayList<>();

        //Toast.makeText(getActivity(),"Datos API: "+ response.toString(), Toast.LENGTH_LONG).show();

        List<AccionesDto> listAcciones = jsonAcciones(response);

        /*
        Header
         */
        Header header = new Header("Hola Jovani!! ¿Qué necesitas en estos momentos!!?", "HazloAkki",
                "https://cdn.pixabay.com/photo/2017/09/30/15/10/pizza-2802332_640.jpg");
        recyclerViewItems.add(header);

        String[] imageUrls = {"https://cdn.pixabay.com/photo/2016/11/18/17/42/barbecue-1836053_640.jpg"
        };

        for (int i = 0; i < listAcciones.size(); i++) {
            AccionesDto accionesDto = listAcciones.get(i);
            AccionesItem accionesItem = new AccionesItem(accionesDto.getIdAccion(),accionesDto.getNombre(), accionesDto.getDescripcion(), accionesDto.getUrlImagen());

            recyclerViewItems.add(accionesItem);
        }
        return recyclerViewItems;
    }

    public void seleccionarAccion() {
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Toast.makeText(getActivity(),"Accion Id: "+position, Toast.LENGTH_LONG).show();

                        Fragment fragmentoGenerico = null;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                        Bundle bundle=new Bundle();
                        bundle.putString("idAccion", adaptadorAcciones.getItems().get(position).getIdAccion());
                        bundle.putString("latitud", "193277");
                        bundle.putString("longitud", "991517");
                        bundle.putInt("distancia", 1);
                        bundle.putBoolean("estatus", true);

                        fragmentoGenerico = new NegociosFragmento();
                        fragmentoGenerico.setArguments(bundle);

                        if (fragmentoGenerico != null) {
                            fragmentManager.beginTransaction().replace(R.id.contenedor_tabs_principal, fragmentoGenerico).addToBackStack(null).commit();
                        }
                    }
                })
        );
    }

    public void obtenerAcciones() {

        JsonArrayRequest req = new JsonArrayRequest(ConstantesServicios.URL_ACCIONES,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        List<RecyclerViewItem> datosAdaptador =  viewCategoriasDetalle(response);
                        adaptadorAcciones = new AccionesAdapter(datosAdaptador, getContext(),jsonAcciones(response));
                        recyclerView.setAdapter(adaptadorAcciones);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error Conexion" + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ConexionServicios.getInstance(getActivity().getApplicationContext()).getRequestQueue().add(req);
    }

    public List<AccionesDto> jsonAcciones(JSONArray response) {
        List<AccionesDto> listaDeAcciones = new ArrayList<AccionesDto>();
        try {
            for (int i = 0; i < response.length(); i++) {

                JSONObject acciones = (JSONObject) response.get(i);
                AccionesDto accionesDto = new AccionesDto();
                accionesDto.setIdAccion(acciones.getString("idAccion"));
                accionesDto.setNombre(acciones.getString("nombre"));
                accionesDto.setDescripcion(acciones.getString("descripcion"));
                accionesDto.setEstatus(acciones.getBoolean("estatus"));
                accionesDto.setUrlImagen(acciones.getString("urlImagen"));
                listaDeAcciones.add(accionesDto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return listaDeAcciones;
    }
}