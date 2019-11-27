package com.hazloakki.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.hazloakki.R;
import com.hazloakki.adaptadores.NegocioAdaptador;
import com.hazloakki.modelos.NegocioDto;
import com.hazloakki.network.ConexionServicios;
import com.hazloakki.network.ConstantesServicios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NegociosFragmento extends Fragment implements NegocioAdaptador.OnItemClickListener{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView listaUI;
    private LinearLayoutManager linearLayoutManager;
    private NegocioAdaptador negocioAdaptador;
    private static String TAG = NegociosFragmento.class.getSimpleName();
    private static Context ctx;

    public NegociosFragmento() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_negocios, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Filtro...", Snackbar.LENGTH_LONG)
                        .setAction("Acción", null).show();
            }
        });

        negocioAdaptador = new NegocioAdaptador(getContext(), this);
        // Preparar lista
        listaUI = (RecyclerView) view.findViewById(R.id.lista);
        listaUI.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        listaUI.setLayoutManager(linearLayoutManager);

        ctx = getContext();
        initNegocios();
        return view;
    }


    public void initNegocios(){

        try {
            Gson gson = new Gson();
            Map<String, String> params = new HashMap<String, String>();

            params.put("idAccion", String.valueOf(getArguments().getString("idAccion")));
            params.put("latitud", String.valueOf(getArguments().getString("latitud")));
            params.put("longitud", String.valueOf(getArguments().getString("longitud")));
            params.put("distancia", String.valueOf(getArguments().getInt("distancia")));
            params.put("estatus", String.valueOf(getArguments().getBoolean("estatus")));

            sendRequestJsonPost(ConstantesServicios.URL_NEGOCIOS,params);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al procesar la peticion: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public  void sendRequestJsonPost(String url, final Map<String, String> params ) {

        String idAccion = params.get("idAccion");
        String latitud= params.get("latitud");
        String longitud= params.get("longitud");
        String distancia= params.get("distancia");
        Boolean estatus = Boolean.parseBoolean(params.get("estatus"));
        StringBuilder urlMoreParameters = new StringBuilder(url);
        urlMoreParameters.append("?idAccion=");
        urlMoreParameters.append(idAccion);
        urlMoreParameters.append("&");
        urlMoreParameters.append("?latitud=");
        urlMoreParameters.append(latitud);
        urlMoreParameters.append("&");
        urlMoreParameters.append("?longitud=");
        urlMoreParameters.append(longitud);
        urlMoreParameters.append("&");
        urlMoreParameters.append("?distancia=");
        urlMoreParameters.append(distancia);
        urlMoreParameters.append("&");
        urlMoreParameters.append("?estatus=");
        urlMoreParameters.append(estatus);;
        try {

            JsonRequest<JSONArray> request  = new JsonRequest<JSONArray>(Request.Method.GET, urlMoreParameters.toString(), null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            negocioAdaptador.setListaNegocios(parseJsonNegocio(response));
                            listaUI.setAdapter(negocioAdaptador);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Response toString: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }

                @Override
                protected Response<JSONArray> parseNetworkResponse(
                        NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser
                                        .parseCharset(response.headers));
                        return Response.success(new JSONArray(jsonString),
                                HttpHeaderParser
                                        .parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    }
                }
            };

            ConexionServicios.getInstance(getContext()).getRequestQueue().add(request);

        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Upps algo inesperado sucedio, vuelve a intentarlo: " + e.getMessage(), Toast.LENGTH_SHORT).show();        }


    }

    public List<NegocioDto> parseJsonNegocio(JSONArray response){
        List<NegocioDto> listNegocio = new ArrayList<NegocioDto>();
        try {

            for (int i = 0; i < response.length(); i++) {

                JSONObject negocio = (JSONObject) response.get(i);

                NegocioDto negocioDto = new NegocioDto();
                negocioDto.setIdNegocio(negocio.getString("idNegocio"));
               // negocioDto.setNombre(negocio.getString("nombre"));
                negocioDto.setDescripcion(negocio.getString("descripcion"));
                //negocioDto.setEstatus(negocio.getBoolean("estatus"));
                negocioDto.setDomicilio(negocio.getString("domicilio"));
                negocioDto.setSitioWeb(negocio.getString("sitioWeb"));
                negocioDto.setCategoria(negocio.getString("nombreCategoria"));
                negocioDto.setCalificacion(negocio.getString("calificacion"));
                negocioDto.setDistancia(negocio.getString("distancia"));
                negocioDto.setNumeroOfertas(negocio.getInt("numeroOfertas"));
                negocioDto.setHorario(negocio.getString("horarioDia"));

                listNegocio.add(negocioDto);
            }
            Toast.makeText(getContext(), " Negocios : "+listNegocio.size(), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return listNegocio;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onClick(NegocioAdaptador.ViewHolder holder, String idNegocio) {

        Intent detalleNegocio = new Intent(getActivity().getApplicationContext(), null);
        detalleNegocio.putExtra("idNegocio",idNegocio);
        detalleNegocio.putExtra("nombreNegocio","");

            startActivity(detalleNegocio);

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NegociosFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static NegociosFragmento newInstance(String param1, String param2) {
        NegociosFragmento fragment = new NegociosFragmento();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
