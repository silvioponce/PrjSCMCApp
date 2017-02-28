package com.sci.sponce.prjscmcapp.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.sci.sponce.prjscmcapp.R;

import java.sql.SQLException;
import java.util.ArrayList;

import Adapter.CasoNinosMenoresAdapter;
import BL.CCMRecienNacidoBL;
import Entidades.CCMRecienNacido;


public class BuscarCasoNinosMenores extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Boolean mParam2;

    CCMRecienNacidoBL ccmRecienNacidoBL = new CCMRecienNacidoBL();
    CasoNinosMenoresAdapter adapter;

    EditText txtBuscarCasoNinoMenores;

    ImageButton btnBuscarCasoNinoMenores;

    int idNino = 0;

    public int selectedItem = -1;

    private ListView lstCasoNinosMenores;
    ArrayList<CCMRecienNacido> arrayOfCasoNinosMenores;

    public static BuscarCasoNinosMenores newInstance(String param1, Boolean param2) {
        BuscarCasoNinosMenores fragment = new BuscarCasoNinosMenores();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BuscarCasoNinosMenores() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            idNino = Integer.parseInt(getArguments().getString(ARG_PARAM1));
            mParam2 = getArguments().getBoolean(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_caso_ninos_menores, container, false);

        lstCasoNinosMenores = (ListView) view.findViewById(R.id.lstCasoNinosMenores);

        txtBuscarCasoNinoMenores = (EditText) view.findViewById(R.id.txtBuscarCasoNinoMenores);
        btnBuscarCasoNinoMenores = (ImageButton) view.findViewById(R.id.btnBuscarCasoNinoMenores);

        btnBuscarCasoNinoMenores.setOnClickListener(this);

        arrayOfCasoNinosMenores = new ArrayList<CCMRecienNacido>();
        try {

            if (idNino>0){
                arrayOfCasoNinosMenores = ccmRecienNacidoBL.getAllCCMRecienNacidosArrayListCustom(getActivity(), "IdNino = " + idNino);
            }else {
                arrayOfCasoNinosMenores = ccmRecienNacidoBL.getAllCCMRecienNacidosArrayList(getActivity());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new CasoNinosMenoresAdapter(getActivity(), arrayOfCasoNinosMenores);
        lstCasoNinosMenores.setAdapter(adapter);

        lstCasoNinosMenores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = adapter.getItem(position).get_id();
            }
        });

        getActivity().setTitle("Caso Nino(a) Menores");

        registerForContextMenu(lstCasoNinosMenores);

        if (!mParam2){
            txtBuscarCasoNinoMenores.setVisibility(View.GONE);
            btnBuscarCasoNinoMenores.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_cat_ninos, menu);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String titulo;
        //titulo = (String) ((TextView) info.targetView.findViewById(R.id.viewNomNino)).getText();
        titulo = "Seleccione una Opción";

        menu.setHeaderTitle(titulo);
        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_ctx_lista_casos_ninos_menores, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.ctxModificarCasoNinoMenor:
                selectedItem = adapter.getItem(info.position).get_id();
                int idnino = adapter.getItem(info.position).getIdNino();

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CasoNinosMenoresFragment().newInstance(idnino, selectedItem,true), "CasoNinosMenores").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ctxCrearNuevaVisitaMenores:
                selectedItem = adapter.getItem(info.position).get_id();
                idNino = adapter.getItem(info.position).getIdNino();

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatVisitaNinosMenores().newInstance(idNino, selectedItem), "CatVistaNinosMenores").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ctxVerVisitaMenores:
                selectedItem = adapter.getItem(info.position).get_id();

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarVisitaNinosMenores().newInstance(selectedItem, false), "BuscarVistaNinosMenores").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscarCasoNinoMenores:
                BuscarCasoNinosMenores();

            default:
                return;
        }
    }

    private void BuscarCasoNinosMenores() {
        arrayOfCasoNinosMenores = new ArrayList<CCMRecienNacido>();
        try {
            arrayOfCasoNinosMenores = ccmRecienNacidoBL.getAllCCMRecienNacidosArrayListByNomNino(getActivity(), txtBuscarCasoNinoMenores.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new CasoNinosMenoresAdapter(getActivity(), arrayOfCasoNinosMenores);
        lstCasoNinosMenores.setAdapter(adapter);
    }
}
