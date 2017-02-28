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

import Adapter.VisitaNinosMenoresAdapter;
import BL.VisitasNinosMenorBL;
import DAO.CCMRecienNacidoDao;
import Entidades.VisitasNinosMenor;

public class BuscarVisitaNinosMenores extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Boolean mParam2;

    VisitaNinosMenoresAdapter adapter;

    int idCCMRecienNacido = 0;

    VisitasNinosMenorBL visitasNinosMenorBL = new VisitasNinosMenorBL();
    CCMRecienNacidoDao ccmRecienNacidoDao = new CCMRecienNacidoDao();

    ListView lstVistaNinosMenores;
    ArrayList<VisitasNinosMenor> arrayOfVisitasNinosMenor;

    EditText txtBuscarVisitaNinoMenores;
    ImageButton btnBuscarVisitaNinoMenores;

    public static BuscarVisitaNinosMenores newInstance(int param1, Boolean param2) {
        BuscarVisitaNinosMenores fragment = new BuscarVisitaNinosMenores();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BuscarVisitaNinosMenores() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            idCCMRecienNacido = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_visita_ninos_menores, container, false);

        lstVistaNinosMenores = (ListView) view.findViewById(R.id.lstVisitaNinosMenores);

        txtBuscarVisitaNinoMenores = (EditText) view.findViewById(R.id.txtBuscarVisitaNinoMenores);
        btnBuscarVisitaNinoMenores = (ImageButton) view.findViewById(R.id.btnBuscarVisitaNinoMenores);

        btnBuscarVisitaNinoMenores.setOnClickListener(this);

        arrayOfVisitasNinosMenor = new ArrayList<VisitasNinosMenor>();
        try {

            if (idCCMRecienNacido > 0) {
                arrayOfVisitasNinosMenor = visitasNinosMenorBL.getAllVisitasNinosMenoresArrayListCustom(getActivity(), "IdCCMRecienNacido = " + idCCMRecienNacido);
            } else {
                arrayOfVisitasNinosMenor = visitasNinosMenorBL.getAllVisitasNinosMenoresArrayList(getActivity());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new VisitaNinosMenoresAdapter(getActivity(), arrayOfVisitasNinosMenor);
        lstVistaNinosMenores.setAdapter(adapter);

        if (!mParam2){
            txtBuscarVisitaNinoMenores.setVisibility(View.GONE);
            btnBuscarVisitaNinoMenores.setVisibility(View.GONE);
        }

        getActivity().setTitle("Buscar Visita Nino(a) Menores");

        registerForContextMenu(lstVistaNinosMenores);

        return view;
    }

    private void BuscarVisitaNinosMenores() {
        arrayOfVisitasNinosMenor = new ArrayList<VisitasNinosMenor>();
        try {
            arrayOfVisitasNinosMenor = visitasNinosMenorBL.getAllVisitasNinosMenoresArrayListByNomNino(getActivity(), txtBuscarVisitaNinoMenores.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new VisitaNinosMenoresAdapter(getActivity(), arrayOfVisitasNinosMenor);
        lstVistaNinosMenores.setAdapter(adapter);
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
        inflater.inflate(R.menu.menu_ctx_lista_visita_ninos_menores, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.ctxActualizarVisitaMenores:

                int idnino = 0, idVisitaNinosMenores, idCasoRecienNacido;

                idVisitaNinosMenores = adapter.getItem(info.position).get_id();

                idCasoRecienNacido = adapter.getItem(info.position).getIdCCMRecienNacido();

                try {
                    idnino = ccmRecienNacidoDao.getCCMRecienNacidoById(getActivity(), String.valueOf(idCasoRecienNacido)).getIdNino();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatVisitaNinosMenores().newInstance(idnino, idCasoRecienNacido, idVisitaNinosMenores, true), "CatVistaNinosMenores").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscarVisitaNinoMenores:
                BuscarVisitaNinosMenores();

            default:
                return;
        }
    }
}
