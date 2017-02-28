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

import Adapter.CasoNinosAdapter;
import BL.CCMNinoBL;
import Entidades.CCMNino;

public class BuscarCasoNinos extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Boolean mParam2;

    CCMNinoBL ccmNinoBL = new CCMNinoBL();
    CasoNinosAdapter adapter;

    EditText txtBuscarCasoNino;

    ImageButton btnBuscarCasoNino;

    int idNino = 0;

    public int selectedItem = -1;

    private ListView lstCasoNinos;
    ArrayList<CCMNino> arrayOfCasoNinos;

    public static BuscarCasoNinos newInstance(String param1, Boolean param2) {
        BuscarCasoNinos fragment = new BuscarCasoNinos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BuscarCasoNinos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            idNino = Integer.parseInt(getArguments().getString(ARG_PARAM1));
            mParam2 = getArguments().getBoolean(ARG_PARAM2);

        }else {
            mParam2 = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_buscar_caso_ninos, container, false);

        lstCasoNinos = (ListView) view.findViewById(R.id.lstCasoNinos);

        txtBuscarCasoNino = (EditText) view.findViewById(R.id.txtBuscarCasoNino);
        btnBuscarCasoNino = (ImageButton) view.findViewById(R.id.btnBuscarCasoNino);

        btnBuscarCasoNino.setOnClickListener(this);

        arrayOfCasoNinos = new ArrayList<CCMNino>();
        try {

            if (idNino>0){
                arrayOfCasoNinos = ccmNinoBL.getAllCCMNinosArrayListCustom(getActivity(), "IdNino = " + idNino);
            }else {
                arrayOfCasoNinos = ccmNinoBL.getAllCCMNinosArrayList(getActivity());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new CasoNinosAdapter(getActivity(), arrayOfCasoNinos);
        lstCasoNinos.setAdapter(adapter);

        lstCasoNinos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = adapter.getItem(position).get_id();
            }
        });

        getActivity().setTitle("Caso Nino(a) 2 Meses");

        registerForContextMenu(lstCasoNinos);

        if (!mParam2){
            txtBuscarCasoNino.setVisibility(View.GONE);
            btnBuscarCasoNino.setVisibility(View.GONE);
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
        inflater.inflate(R.menu.menu_ctx_lista_casos_ninos, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.ctxModificarCasoNino:
                selectedItem = adapter.getItem(info.position).get_id();
                int idnino = adapter.getItem(info.position).getIdNino();

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatCasoNinosFragment().newInstance(idnino, selectedItem,true), "CatCasoNinos").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ctxCrearNuevaVisitaNinos:
                selectedItem = adapter.getItem(info.position).get_id();
                idNino  = adapter.getItem(info.position).getIdNino();
                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatVisitaNinos().newInstance(idNino, selectedItem), "CatVistaNinos").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ctxVerVisitaNinos:
                selectedItem = adapter.getItem(info.position).get_id();
                idNino  = adapter.getItem(info.position).getIdNino();

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarVisitasNinosMayores().newInstance(selectedItem, false), "BuscarVistaNinosMayores").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscarCasoNino:
                BuscarCasoNinosMayores();

            default:
                return;
        }
    }

    private void BuscarCasoNinosMayores() {
        arrayOfCasoNinos = new ArrayList<CCMNino>();
        try {
            arrayOfCasoNinos = ccmNinoBL.getAllCCMNinosArrayListByName(getActivity(), txtBuscarCasoNino.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new CasoNinosAdapter(getActivity(), arrayOfCasoNinos);
        lstCasoNinos.setAdapter(adapter);
    }


}
