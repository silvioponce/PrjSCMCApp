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

import Adapter.VisitaNinosMayoresAdapter;
import BL.VisitasNinosMayorBL;
import DAO.CCMNinoDao;
import Entidades.VisitasNinosMayor;

public class BuscarVisitasNinosMayores extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Boolean mParam2;

    VisitaNinosMayoresAdapter adapter;

    int idCCMNino = 0;

    VisitasNinosMayorBL visitasNinosMayorBL = new VisitasNinosMayorBL();
    CCMNinoDao ccmNinoDao = new CCMNinoDao();

    ListView lstVistaNinosMayores;
    ArrayList<VisitasNinosMayor> arrayOfVisitasNinosMayor;

    EditText txtBuscarVisitaNinoMayores;
    ImageButton btnBuscarVisitaNinoMayores;

    public static BuscarVisitasNinosMayores newInstance(int param1, Boolean param2) {
        BuscarVisitasNinosMayores fragment = new BuscarVisitasNinosMayores();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        args.putBoolean(ARG_PARAM2, param2);
        return fragment;
    }

    public BuscarVisitasNinosMayores() {
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
            idCCMNino = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
        }else {
            mParam2 = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar_visitas_ninos_mayores, container, false);

        lstVistaNinosMayores = (ListView) view.findViewById(R.id.lstVisitaNinosMayores);

        txtBuscarVisitaNinoMayores = (EditText) view.findViewById(R.id.txtBuscarVisitaNinoMayores);
        btnBuscarVisitaNinoMayores = (ImageButton) view.findViewById(R.id.btnBuscarVisitaNinoMayores);

        btnBuscarVisitaNinoMayores.setOnClickListener(this);

        arrayOfVisitasNinosMayor = new ArrayList<VisitasNinosMayor>();
        try {

            if (idCCMNino > 0) {
                arrayOfVisitasNinosMayor = visitasNinosMayorBL.getAllVisitasNinosMayoresArrayListCustom(getActivity(), "IdCCMNino = " + idCCMNino);
            } else {
                arrayOfVisitasNinosMayor = visitasNinosMayorBL.getAllVisitasNinosMayoresArrayList(getActivity());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new VisitaNinosMayoresAdapter(getActivity(), arrayOfVisitasNinosMayor);
        lstVistaNinosMayores.setAdapter(adapter);


        getActivity().setTitle("Buscar Visita Nino(a) 2 Meses");

        registerForContextMenu(lstVistaNinosMayores);

        if (!mParam2){
            txtBuscarVisitaNinoMayores.setVisibility(View.GONE);
            btnBuscarVisitaNinoMayores.setVisibility(View.GONE);
        }


        return  view;
    }


    private void BuscarVisitaNinosMayores() {
        arrayOfVisitasNinosMayor = new ArrayList<VisitasNinosMayor>();
        try {
            arrayOfVisitasNinosMayor = visitasNinosMayorBL.getAllVisitasNinosMayoresArrayListCustomNomNino(getActivity(), txtBuscarVisitaNinoMayores.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new VisitaNinosMayoresAdapter(getActivity(), arrayOfVisitasNinosMayor);
        lstVistaNinosMayores.setAdapter(adapter);
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
        inflater.inflate(R.menu.menu_ctx_lista_visita_ninos_mayores, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.ctxActualizarVisitaMayores:

                int idnino = 0, idVisitaNinosMayores, idCasoNinos;

                idVisitaNinosMayores = adapter.getItem(info.position).get_id();

                idCasoNinos = adapter.getItem(info.position).getIdCCMNino();

                try {
                    idnino = ccmNinoDao.getCCMNinoById(getActivity(), String.valueOf(idCasoNinos)).getIdNino();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatVisitaNinos().newInstance(idnino, idCasoNinos, idVisitaNinosMayores, true), "CatVistaNinosMayores").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscarVisitaNinoMayores:
                BuscarVisitaNinosMayores();

            default:
                return;
        }
    }
}
