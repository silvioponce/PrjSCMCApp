package com.sci.sponce.prjscmcapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sci.sponce.prjscmcapp.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import BL.CCMNinoBL;
import BL.NinoBL;
import BL.TratamientoBL;
import BL.TratamientoNinoBL;
import BL.VisitasNinosMayorBL;
import Entidades.CCMNino;
import Entidades.Nino;
import Entidades.Tratamiento;
import Entidades.TratamientoNino;
import Entidades.TratamientoRecienNacido;
import Entidades.VisitasNinosMayor;

public class CatVisitaNinos extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    NinoBL ninoBL = new NinoBL();
    Nino nino = new Nino();

    CCMNinoBL ccmNinoBL = new CCMNinoBL();
    CCMNino ccmNino = new CCMNino();

    VisitasNinosMayorBL visitasNinosMayorBL = new VisitasNinosMayorBL();


    TratamientoNinoBL tratamientoNinoBL = new TratamientoNinoBL();
    TratamientoBL tratamientoBL = new TratamientoBL();

    DateFormat formate = DateFormat.getDateInstance(DateFormat.SHORT);
    Calendar calendar = Calendar.getInstance();
    Calendar calendarFechaActual = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener d;
    TextView txtnomNinoVisitaNino, txtEnfermedadVisitaNino, txtTratamientoVisitaNino;
    EditText txtFechaVisitaNino, txtObservacionesNino;
    ImageButton btnFechaVisitaNino;
    ToggleButton tbn_CumpMedRectNino, tbn_TomaDosisIndicadaNino;
    Button btnGuardarVisitaNino;
    Spinner spnResultadoVisitaNino;
    String ResultadoVisita;
    int idNino, idCCMNino, idUsuarioPref, idVisitaNino;
    Boolean modoEidt;

    public static CatVisitaNinos newInstance(int param1, int param2) {
        CatVisitaNinos fragment = new CatVisitaNinos();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CatVisitaNinos newInstance(int param1, int param2, int param3, Boolean modoEdit) {
        CatVisitaNinos fragment = new CatVisitaNinos();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        args.putBoolean(ARG_PARAM4, modoEdit);
        fragment.setArguments(args);
        return fragment;
    }

    public CatVisitaNinos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {

            idNino = getArguments().getInt(ARG_PARAM1);
            idCCMNino = getArguments().getInt(ARG_PARAM2);

            modoEidt = getArguments().getBoolean(ARG_PARAM4, false);

            if (modoEidt) {
                idVisitaNino = getArguments().getInt(ARG_PARAM3);

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cat_visita_ninos, container, false);

        inicializarComponentes(v);

        CargarPreferencias();

        CargarDatosGenerales();

        updateDate();

        getActivity().setTitle("Visita Nino(a) 2 Meses");

        if (modoEidt)
            CargarVisitaNinosMayores(idVisitaNino);

        return v;
    }

    private void inicializarComponentes(View v) {
        txtnomNinoVisitaNino = (TextView) v.findViewById(R.id.txtnomNinoVisitaNino);
        txtEnfermedadVisitaNino = (TextView) v.findViewById(R.id.txtEnfermedadVisitaNino);
        txtTratamientoVisitaNino = (TextView) v.findViewById(R.id.txtTratamientoVisitaNino);

        txtFechaVisitaNino = (EditText) v.findViewById(R.id.txtFechaVisitaNino);
        txtObservacionesNino = (EditText) v.findViewById(R.id.txtObservacionesNino);

        btnFechaVisitaNino = (ImageButton) v.findViewById(R.id.btnFechaVisitaNino);

        tbn_CumpMedRectNino = (ToggleButton) v.findViewById(R.id.tbn_CumpMedRectNino);
        tbn_TomaDosisIndicadaNino = (ToggleButton) v.findViewById(R.id.tbn_TomaDosisIndicadaNino);

        btnGuardarVisitaNino = (Button) v.findViewById(R.id.btnGuardarVisitaNino);

        btnGuardarVisitaNino.setOnClickListener(this);

        spnResultadoVisitaNino = (Spinner) v.findViewById(R.id.spnResultadoVisitaNino);

        CargarResultadoVisita();

        d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();

            }
        };

        spnResultadoVisitaNino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ResultadoVisita = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void CargarDatosGenerales() {


        TratamientoRecienNacido tratamientoRecienNacido = new TratamientoRecienNacido();
        Tratamiento tratamiento = new Tratamiento();
        int intIdTratamiento;
        String strTratamiento = "";

        List<TratamientoNino> listTratatamientoNino = new ArrayList<TratamientoNino>();

        try {
            nino = ninoBL.getNinoById(getActivity(), String.valueOf(idNino));
            ccmNino = ccmNinoBL.getCCMNinoById(getActivity(), String.valueOf(idCCMNino));

            listTratatamientoNino = tratamientoNinoBL.getAllTratamientoNinosListCustom(getActivity(), "IdCCMNino = " + String.valueOf(ccmNino.get_id()));

            for (int x = 0; x < listTratatamientoNino.size(); x++) {
                intIdTratamiento = listTratatamientoNino.get(x).getIdTratamiento();
                tratamiento = tratamientoBL.getTratamientoById(getActivity(), String.valueOf(intIdTratamiento));

                strTratamiento += tratamiento.getTratamiento().toString() + " - " + tratamiento.getPregunta().toString() + ".\n";
            }

            txtnomNinoVisitaNino.setText(nino.getNomNino());
            txtTratamientoVisitaNino.setText(strTratamiento);
            txtEnfermedadVisitaNino.setText(ccmNinoBL.clasificarEnfermedad(ccmNino));

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void setDate() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(calendarFechaActual.getTimeInMillis());
        dialog.show();

    }

    public void updateDate() {
        txtFechaVisitaNino.setText(formate.format(calendarFechaActual.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGuardarVisitaNino:

                if (verficaVisita())
                    break;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                alertDialog.setTitle("Guardar Registro...");
                alertDialog.setMessage("¿Desea guardar este registro?");
                alertDialog.setIcon(R.mipmap.ic_save);
                alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        GuardarVisitaNino();
                        Fragment f = new Fragment();
                        f = new BuscarVisitasNinosMayores().newInstance(idCCMNino, false);

                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getFragmentManager().beginTransaction().replace(R.id.Contenedor, f, "BuscarVistaNinosMayores").addToBackStack(null).commit();

                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

                break;
            case R.id.btnFechaVisitaMenor:
                setDate();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    public void CargarResultadoVisita() {
        String ResultadoVisita[] = new String[]{"Mejoro", "No Mejoro", "Fallecio"};
        ;
        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ResultadoVisita);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spnResultadoVisitaNino.setAdapter(spinnerArrayAdapter);
    }

    private void GuardarVisitaNino() {
        Date fecha = calendarFechaActual.getTime();

        VisitasNinosMayor visitasNinosMayor = new VisitasNinosMayor();

        if (modoEidt)
            visitasNinosMayor.set_id(idVisitaNino);

        visitasNinosMayor.setIdCCMNino(idCCMNino);

        if (tbn_CumpMedRectNino.isChecked()) {
            visitasNinosMayor.setCumpMedRect(true);
        } else {
            visitasNinosMayor.setCumpMedRect(false);
        }

        if (tbn_TomaDosisIndicadaNino.isChecked()) {
            visitasNinosMayor.setTomandoDosisIndicada(true);
        } else {
            visitasNinosMayor.setTomandoDosisIndicada(false);
        }

        visitasNinosMayor.setObservacion(txtObservacionesNino.getText().toString());

        visitasNinosMayor.setFechaVisita(fecha);

        visitasNinosMayor.setResultadoVisita(ResultadoVisita);

        visitasNinosMayor.setIdUsuario(idUsuarioPref);

        visitasNinosMayorBL.GuardarVisitaNinosMayores(getActivity(), visitasNinosMayor);

    }

    private void CargarVisitaNinosMayores(int idVisitaNinosMayores) {

        VisitasNinosMayor visitasNinosMayor = new VisitasNinosMayor();

        try {
            visitasNinosMayor = visitasNinosMayorBL.getVisitasNinosMayorById(getActivity(), String.valueOf(idVisitaNinosMayores));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Calendar calendar2 = Calendar.getInstance();
        Date dateCasoNinoMayor = new Date(String.valueOf(visitasNinosMayor.getFechaVisita()));
        calendar2.setTime(dateCasoNinoMayor);

        calendarFechaActual = calendar2;

        txtFechaVisitaNino.setText(formate.format(calendar2.getTime()));

        if (visitasNinosMayor.getCumpMedRect()) {
            tbn_CumpMedRectNino.setChecked(true);
        }

        if (visitasNinosMayor.getTomandoDosisIndicada()) {
            tbn_TomaDosisIndicadaNino.setChecked(true);
        }

        txtObservacionesNino.setText(visitasNinosMayor.getObservacion());

        for (int i = 0; i < spnResultadoVisitaNino.getCount(); i++) {
            String value = String.valueOf(spnResultadoVisitaNino.getItemAtPosition(i));

            if (value.equals(visitasNinosMayor.getResultadoVisita()))
                spnResultadoVisitaNino.setSelection(i);

        }

    }

    public void CargarPreferencias() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PreferenciasUsuario", getActivity().getApplication().MODE_PRIVATE);
        idUsuarioPref = sharedPreferences.getInt("IdUsuario", 0);

    }

    private boolean verficaVisita() {
        boolean flag = false;
        String strMensaje = "";

        VisitasNinosMayorBL visitasNinosMayorBL = new VisitasNinosMayorBL();

        try {

            if (visitasNinosMayorBL.getVisitasNinosMayorById(getActivity(), String.valueOf(idVisitaNino)).getIdVisita()>0)
            {
                strMensaje = "La Visita del Niño no se puede actualizar, por que ya esta registrada en el Servidor";
                flag = true;
            }

            if (flag) {
                AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getActivity());

                alertDialog1.setTitle("Informacion...");
                alertDialog1.setMessage(strMensaje);
                alertDialog1.setIcon(R.mipmap.ic_save);
                alertDialog1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment f = new Fragment();
                        f = new BuscarVisitasNinosMayores();

                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        getFragmentManager().beginTransaction().replace(R.id.Contenedor, f, "BuscarVistaNinosMayores").addToBackStack(null).commit();

                    }
                });
                alertDialog1.show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }
}
