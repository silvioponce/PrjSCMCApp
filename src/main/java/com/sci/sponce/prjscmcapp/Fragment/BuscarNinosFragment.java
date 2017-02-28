package com.sci.sponce.prjscmcapp.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ActionMode;
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
import android.widget.TextView;
import android.widget.Toast;

import com.sci.sponce.prjscmcapp.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import Adapter.NinosAdapter;
import BL.CCMNinoBL;
import BL.CCMRecienNacidoBL;
import BL.NinoBL;
import Entidades.Nino;


public class BuscarNinosFragment extends Fragment implements View.OnClickListener {
    private NinoBL ninoBL = new NinoBL();
    private ListView lista;
    EditText txtBuscar;
    NinosAdapter adapter;
    ImageButton btnBuscar;

    private ActionMode mActionMode;
    public int selectedItem = -1;
    ArrayList<Nino> arrayOfNinos;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        String titulo = (String) ((TextView) info.targetView
                .findViewById(R.id.viewNomNino)).getText();

        menu.setHeaderTitle(titulo);
        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_ctx_lista_ninos, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.CtxLstOpc1:
                selectedItem = adapter.getItem(info.position).get_id();

                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatNinosFragment().newInstance(selectedItem, "IdNino"), "CatNinos").addToBackStack(null).commit();
                //Toast.makeText(getActivity(), "Lista[" + info.position + " " + selectedItem + ": Opcion 1 pulsada!",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.CtxLstOpc2:
                selectedItem = adapter.getItem(info.position).get_id();

                Date fechaNacimiento = adapter.getItem(info.position).getFechaNac();

                Calendar calendar1 = Calendar.getInstance();
                GregorianCalendar fechaActual = new GregorianCalendar();
                calendar1.setTime(fechaNacimiento);

                long meses = ((fechaActual.getTimeInMillis() - calendar1.getTimeInMillis()) / 262975000) / 10;

                //Toast.makeText(getActivity(), String.valueOf((int) meses), Toast.LENGTH_SHORT).show();

                if (meses < 2) {
                    getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CasoNinosMenoresFragment().newInstance(selectedItem), "CasoNinosMenores").addToBackStack(null).commit();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatCasoNinosFragment().newInstance(selectedItem, 0, false), "CatCasoNinos").addToBackStack(null).commit();
                }

                return true;
            case R.id.ctxListCasoNinosMenores:
                selectedItem = adapter.getItem(info.position).get_id();

                verificaCasos(selectedItem);

                //showDialog();


                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void verificaCasos(int idNino) {
        boolean flag = false;
        boolean flag2 = false;

        CCMRecienNacidoBL ccmRecienNacidoBL = new CCMRecienNacidoBL();
        CCMNinoBL ccmNinoBL = new CCMNinoBL();

        try {
            flag = ccmRecienNacidoBL.getExisteCCMRecienNacidoByCustomer(getActivity(), "IdNino = " + idNino);
            flag2 = ccmNinoBL.getExisteCCMNinoByCustomer(getActivity(), "IdNino = " + idNino);
            if (flag && flag2) {
                showDialog();
                return;
            }

            if (flag) {
                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarCasoNinosMenores().newInstance(String.valueOf(selectedItem), false), "BuscarCasoNinosMenores").addToBackStack(null).commit();
                return;
            }

            if (flag2) {
                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarCasoNinos().newInstance(String.valueOf(selectedItem), false), "BuscarCasoNinos").addToBackStack(null).commit();
                return;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_buscar_ninos, container, false);
        lista = (ListView) v.findViewById(R.id.lstNinos);
        txtBuscar = (EditText) v.findViewById(R.id.txtBuscar);
        btnBuscar = (ImageButton) v.findViewById(R.id.btnBuscar);

        lista.setSelected(true);

        getActivity().setTitle("Buscar Nino(a)");

        arrayOfNinos = new ArrayList<Nino>();
        try {
            arrayOfNinos = ninoBL.getAllNinosArrayList(getActivity());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new NinosAdapter(getActivity(), arrayOfNinos);
        lista.setAdapter(adapter);

        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = adapter.getItem(position).get_id();
                view.setSelected(true);
                if (mActionMode != null) mActionMode.finish();


            }
        });


        btnBuscar.setOnClickListener(this);

        registerForContextMenu(lista);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    private ActionMode.Callback amc = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_buscar_ninos, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.btnNuevo:
                    getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatNinosFragment().newInstance(selectedItem, "IdNino"), "CatNinos").addToBackStack(null).commit();

                    mode.finish();
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    public void BuscarUsuarios() {
        arrayOfNinos = new ArrayList<Nino>();
        try {
            arrayOfNinos = ninoBL.getAllNinosByName(getActivity(), txtBuscar.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new NinosAdapter(getActivity(), arrayOfNinos);
        lista.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuscar:
                BuscarUsuarios();

            default:
                return;
        }
    }

    private void showDialog() {
        final String[] commandArray = new String[]{"Casos Recien Nacidos", "", "Casos 2 Meses"};
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Single Choice");
        builder.setItems(commandArray, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),
                        commandArray[which] + " Selected", Toast.LENGTH_LONG)
                        .show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
