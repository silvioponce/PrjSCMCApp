package com.sci.sponce.prjscmcapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sci.sponce.prjscmcapp.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import BL.CCMRecienNacidoBL;
import BL.NinoBL;
import BL.TratamientoRecienNacidoBL;
import Entidades.CCMRecienNacido;
import Entidades.Nino;
import Entidades.TratamientoRecienNacido;


public class CasoNinosMenoresFragment extends Fragment implements View.OnClickListener {

    NinoBL ninoBL = new NinoBL();
    CCMRecienNacidoBL ccmRecienNacidoBL = new CCMRecienNacidoBL();
    TratamientoRecienNacidoBL tratamientoRecienNacidoBL = new TratamientoRecienNacidoBL();

    DateFormat formate = DateFormat.getDateInstance(DateFormat.SHORT);
    Calendar calendar = Calendar.getInstance();
    Calendar calendarFechaActual = Calendar.getInstance();
    Calendar calendarFechaNacNino = Calendar.getInstance();

    TextView lblNomNino;

    Button btnContinuar;

    EditText edt_fecatencion_menores, edt_fecatencion_Meses;
    ImageButton bt_fecatencion_menores;

    RadioButton radio_sesion_menores, radio_madrebuscabps_menores, radio_visitadomiciliar_menores, radio_primera_vez_si_menores, radio_primera_vez_no_menores;

    DatePickerDialog.OnDateSetListener d;

    ToggleButton tbn_nopuedetomarpecho_menores, tbn_convuliones_menores, tbn_hundepiel_menores, tbn_ruidoraro_menores, tbn_respirarapida_menores, tbn_fiebre_menores, tbn_tempbaja_menores,
            tbn_pielojoamarillo_menores, tbn_MovEstimulos, tbn_OmbligoPus, tbn_PielUmbilicalRoja, tbn_PielGranos, tbn_OjosPus, tbn_otra;

    String idNino, lugarAtencion, nomPregunta, grupo;
    int idCCMRecienNacido;
    Boolean entregoReferencia;
    String strTratamiento;
    boolean ModoEdit = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_caso_ninos_menores, container, false);

        InicializarComponentes(view);

        if (ModoEdit) {
            CargaCasoNinoMenor();
        } else {
            CargarNino(idNino);
        }


        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_cat_ninos, menu);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            String Id = String.valueOf(getArguments().getInt("idNino", 0));
            idNino = Id;

            if (getArguments().getBoolean("modoEdit", false)) {
                idCCMRecienNacido = getArguments().getInt("idCCMRecienNacido");
                ModoEdit = true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        getActivity().setTitle("Ingresar Caso de Nino(a)");
    }

    public static CasoNinosMenoresFragment newInstance(int idNino, int idCCMRecienNacido, boolean modoEdit) {
        CasoNinosMenoresFragment fragmentDemo = new CasoNinosMenoresFragment();
        Bundle args = new Bundle();
        args.putInt("idNino", idNino);
        args.putInt("idCCMRecienNacido", idCCMRecienNacido);
        args.putBoolean("modoEdit", modoEdit);

        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    public static CasoNinosMenoresFragment newInstance(int idNino) {
        CasoNinosMenoresFragment fragmentDemo = new CasoNinosMenoresFragment();
        Bundle args = new Bundle();
        args.putInt("idNino", idNino);

        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }


    private void InicializarComponentes(View view) {

        lblNomNino = (TextView) view.findViewById(R.id.lblNomNino);

        radio_sesion_menores = (RadioButton) view.findViewById(R.id.radio_sesion_menores);
        radio_madrebuscabps_menores = (RadioButton) view.findViewById(R.id.radio_madrebuscabps_menores);
        radio_visitadomiciliar_menores = (RadioButton) view.findViewById(R.id.radio_visitadomiciliar_menores);
        radio_primera_vez_si_menores = (RadioButton) view.findViewById(R.id.radio_primera_vez_si_menores);
        radio_primera_vez_no_menores = (RadioButton) view.findViewById(R.id.radio_primera_vez_no_menores);

        bt_fecatencion_menores = (ImageButton) view.findViewById(R.id.bt_fecatencion_menores);
        edt_fecatencion_menores = (EditText) view.findViewById(R.id.edt_fecatencion_menores);
        edt_fecatencion_Meses = (EditText) view.findViewById(R.id.edt_fecatencion_Meses);

        tbn_nopuedetomarpecho_menores = (ToggleButton) view.findViewById(R.id.tbn_nopuedetomarpecho_menores);
        tbn_convuliones_menores = (ToggleButton) view.findViewById(R.id.tbn_convuliones_menores);
        tbn_hundepiel_menores = (ToggleButton) view.findViewById(R.id.tbn_hundepiel_menores);
        tbn_ruidoraro_menores = (ToggleButton) view.findViewById(R.id.tbn_ruidoraro_menores);
        tbn_respirarapida_menores = (ToggleButton) view.findViewById(R.id.tbn_respirarapida_menores);
        tbn_fiebre_menores = (ToggleButton) view.findViewById(R.id.tbn_fiebre_menores);
        tbn_tempbaja_menores = (ToggleButton) view.findViewById(R.id.tbn_tempbaja_menores);
        tbn_pielojoamarillo_menores = (ToggleButton) view.findViewById(R.id.tbn_pielojoamarillo_menores);
        tbn_MovEstimulos = (ToggleButton) view.findViewById(R.id.tbn_MovEstimulos);
        tbn_OmbligoPus = (ToggleButton) view.findViewById(R.id.tbn_OmbligoPus);
        tbn_PielUmbilicalRoja = (ToggleButton) view.findViewById(R.id.tbn_PielUmbilicalRoja);
        tbn_PielGranos = (ToggleButton) view.findViewById(R.id.tbn_PielGranos);
        tbn_OjosPus = (ToggleButton) view.findViewById(R.id.tbn_OjosPus);
        tbn_otra = (ToggleButton) view.findViewById(R.id.tbn_otra);

        btnContinuar = (Button) view.findViewById(R.id.btnContinuar);

        tbn_nopuedetomarpecho_menores.setOnClickListener(this);
        tbn_convuliones_menores.setOnClickListener(this);
        tbn_hundepiel_menores.setOnClickListener(this);
        tbn_ruidoraro_menores.setOnClickListener(this);
        tbn_respirarapida_menores.setOnClickListener(this);
        tbn_fiebre_menores.setOnClickListener(this);
        tbn_tempbaja_menores.setOnClickListener(this);
        tbn_pielojoamarillo_menores.setOnClickListener(this);
        tbn_MovEstimulos.setOnClickListener(this);
        tbn_OmbligoPus.setOnClickListener(this);
        tbn_PielUmbilicalRoja.setOnClickListener(this);
        tbn_PielGranos.setOnClickListener(this);
        tbn_OjosPus.setOnClickListener(this);
        tbn_otra.setOnClickListener(this);

        btnContinuar.setOnClickListener(this);


        d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
                obtenerMeses(calendarFechaNacNino.getTime(), calendar.getTime());
            }
        };

        bt_fecatencion_menores.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_fecatencion_menores:
                setDate();
                break;

            case R.id.btnContinuar:

                if (radio_madrebuscabps_menores.isChecked())
                    lugarAtencion = "Madre busca a BPS";
                if (radio_sesion_menores.isChecked())
                    lugarAtencion = "Sesion de Pesaje";
                if (radio_visitadomiciliar_menores.isChecked())
                    lugarAtencion = "Visita Domiciliar";


                if (verificarDatos()) {

                    if (ModoEdit) {
                        getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CasoNinosMenoresTratamientoFragment().newInstance(idNino, lugarAtencion, nomPregunta, grupo, idCCMRecienNacido, entregoReferencia), "CasoNinosMenoresTratamiento").addToBackStack("").commit();
                    } else {
                        getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CasoNinosMenoresTratamientoFragment().newInstance(idNino, lugarAtencion, nomPregunta, grupo), "CasoNinosMenoresTratamiento").addToBackStack("").commit();
                    }

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Información...");
                    alertDialog.setMessage("Seleccine una Enfermedad!!!");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setIcon(R.mipmap.ic_casos);
                    alertDialog.show();
                }

                break;

            case R.id.tbn_nopuedetomarpecho_menores:
                CheckedTobutton(tbn_nopuedetomarpecho_menores);
                nomPregunta = "NoPuedeTomarPecho";
                grupo = "ninguno";
                break;
            case R.id.tbn_convuliones_menores:
                CheckedTobutton(tbn_convuliones_menores);
                nomPregunta = "Convulsiones";
                grupo = "ninguno";
                break;
            case R.id.tbn_hundepiel_menores:
                CheckedTobutton(tbn_hundepiel_menores);
                nomPregunta = "HundePiel";
                grupo = "Amoxicilina";
                break;
            case R.id.tbn_ruidoraro_menores:
                CheckedTobutton(tbn_ruidoraro_menores);
                nomPregunta = "RuidosRespirar";
                grupo = "Amoxicilina";
                break;
            case R.id.tbn_respirarapida_menores:
                CheckedTobutton(tbn_respirarapida_menores);
                nomPregunta = "RespRapida";
                grupo = "Amoxicilina";
                break;
            case R.id.tbn_fiebre_menores:
                CheckedTobutton(tbn_fiebre_menores);
                nomPregunta = "Fibre";
                grupo = "Acetaminofen";
                break;
            case R.id.tbn_tempbaja_menores:
                CheckedTobutton(tbn_tempbaja_menores);
                nomPregunta = "Temperatura";
                grupo = "Acetaminofen";
                break;
            case R.id.tbn_pielojoamarillo_menores:
                CheckedTobutton(tbn_pielojoamarillo_menores);
                nomPregunta = "PielOjosAmarillos";
                grupo = "ninguno";
                break;
            case R.id.tbn_MovEstimulos:
                CheckedTobutton(tbn_MovEstimulos);
                nomPregunta = "MovEstimulos";
                grupo = "ninguno";
                break;
            case R.id.tbn_OmbligoPus:
                CheckedTobutton(tbn_OmbligoPus);
                nomPregunta = "OmbligoPus";
                grupo = "Amoxicilina";
                break;
            case R.id.tbn_PielUmbilicalRoja:
                CheckedTobutton(tbn_PielUmbilicalRoja);
                nomPregunta = "PielUmbilicalRoja";
                grupo = "Amoxicilina";
                break;
            case R.id.tbn_PielGranos:
                CheckedTobutton(tbn_PielGranos);
                nomPregunta = "PielGranos";
                grupo = "Amoxicilina";
                break;
            case R.id.tbn_OjosPus:
                CheckedTobutton(tbn_OjosPus);
                nomPregunta = "OjosPus";
                grupo = "Tetraciclina";
                break;
            case R.id.tbn_otra:
                CheckedTobutton(tbn_otra);
                nomPregunta = "Otra";
                grupo = "ninguno";
                break;
            default:
                return;
        }

    }

    private void CheckedTobutton(ToggleButton tbn) {

        tbn_nopuedetomarpecho_menores.setChecked(false);
        tbn_convuliones_menores.setChecked(false);
        tbn_hundepiel_menores.setChecked(false);
        tbn_ruidoraro_menores.setChecked(false);
        tbn_respirarapida_menores.setChecked(false);
        tbn_fiebre_menores.setChecked(false);
        tbn_tempbaja_menores.setChecked(false);
        tbn_pielojoamarillo_menores.setChecked(false);
        tbn_MovEstimulos.setChecked(false);
        tbn_OmbligoPus.setChecked(false);
        tbn_PielUmbilicalRoja.setChecked(false);
        tbn_PielGranos.setChecked(false);
        tbn_OjosPus.setChecked(false);
        tbn_otra.setChecked(false);

        tbn.setChecked(true);
    }

    private void obtenerMeses1(Calendar calendar) {
        String fecha = formate.format(calendar.getTime()); // 5 de junio 2004
        // Formar el objeto con los parámetros (year, month, dayOfMonth)
        //int y1 = Integer.parseInt(fecha.substring(0, 4));
        //int m1 = Integer.parseInt(fecha.substring(4, 6));
        //int d1 = Integer.parseInt(fecha.substring(6, 8));

        int y1 = calendar.get(Calendar.YEAR);
        int m1 = calendar.get(Calendar.MONTH);
        int d1 = calendar.get(Calendar.DAY_OF_MONTH);

        GregorianCalendar fechaAnterior = new GregorianCalendar(y1, m1, d1);
        // Objeto con la fecha actual
        GregorianCalendar fechaActual = new GregorianCalendar();
        int y2 = fechaActual.get(GregorianCalendar.YEAR);
        int m2 = fechaActual.get(GregorianCalendar.MONTH);
        int d2 = fechaActual.get(GregorianCalendar.DAY_OF_MONTH);
        long meses = ((fechaActual.getTimeInMillis() - calendar.getTimeInMillis()) / 262975000) / 10;
        //int diffYears = (y2 - y1 - 1) + (m2 == m1 ? (d2 >= d1 ? 1 : 0) : m2 >= m1 ? 1 : 0);

        edt_fecatencion_Meses.setText(String.valueOf(meses));
        //Toast.makeText(getActivity(), String.valueOf(meses), Toast.LENGTH_SHORT).show();

    }

    private int obtenerMeses(Date dateInicial, Date dateFinal) {


        java.util.GregorianCalendar jCal = new java.util.GregorianCalendar();
        java.util.GregorianCalendar jCal2 = new java.util.GregorianCalendar();

        jCal.setTime(dateInicial);
        jCal2.setTime(dateFinal);

        long diferencia = jCal2.getTime().getTime() - jCal.getTime().getTime();
        double minutos = diferencia / (1000 * 60);
        long horas = (long) (minutos / 60);
        long minuto = (long) (minutos % 60);
        long segundos = (long) diferencia % 1000;
        long dias = horas / 24;

        //Calcular meses...
        //Crear vector para almacenar los diferentes dias maximos segun correponda
        String[] mesesAnio = new String[12];
        mesesAnio[0] = "31";
        //validacion de los años bisiestos
        if (jCal.isLeapYear(jCal.YEAR)) {
            mesesAnio[1] = "29";
        } else {
            mesesAnio[1] = "28";
        }
        mesesAnio[2] = "31";
        mesesAnio[3] = "30";
        mesesAnio[4] = "31";
        mesesAnio[5] = "30";
        mesesAnio[6] = "31";
        mesesAnio[7] = "31";
        mesesAnio[8] = "30";
        mesesAnio[9] = "31";
        mesesAnio[10] = "30";
        mesesAnio[11] = "31";
        int diasRestantes = (int) dias;
        //variable almacenará el total de meses que hay en esos dias
        int totalMeses = 0;
        int mesActual = jCal.MONTH - 1;
        //Restar los dias de cada mes desde la fecha de ingreso hasta que ya no queden sufcientes dias para
        // completar un mes.
        for (int i = 0; i <= 11; i++) {
            //Validar año, si sumando 1 al mes actual supera el fin de año,
            // setea la variable a principio de año
            if ((mesActual + 1) >= 12) {
                mesActual = i;
            }
            //Validar que el numero de dias resultantes de la resta de las 2 fechas, menos los dias
            //del mes correspondiente sea mayor a cero, de ser asi totalMeses aumenta,continuar hasta
            //que ya nos se cumpla.
            if ((diasRestantes - Integer.parseInt(mesesAnio[mesActual])) >= 0) {
                totalMeses++;
                diasRestantes = diasRestantes - Integer.parseInt(mesesAnio[mesActual]);
                mesActual++;
            } else {
                break;
            }
        }
        //Resto de horas despues de sacar los dias
        horas = horas % 24;
        String salida = "";
        if (totalMeses > 0) {
            if (totalMeses > 1)
                salida = salida + String.valueOf(totalMeses) + " Meses,  ";
            else
                salida = salida + String.valueOf(totalMeses) + " Mes, ";
        }
        if (diasRestantes > 0) {
            if (diasRestantes > 1)
                salida = salida + String.valueOf(diasRestantes) + " Dias, ";
            else
                salida = salida + String.valueOf(diasRestantes) + " Dia, ";
        }

        int dato = (dateFinal.getMonth() - dateInicial.getMonth()) + ((dateFinal.getYear() - dateInicial.getYear()) * 12);

        if ((dateInicial.getDate() - dateFinal.getDate()) < 0)
        {
            //dato = dato + 1;
        }else
        {
            dato = dato - 1;
        }

        return dato;

    }

    public void updateDate() {
        edt_fecatencion_menores.setText(formate.format(calendar.getTime()));
    }

    public void setDate() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(calendarFechaActual.getTimeInMillis());
        dialog.show();

    }

    private void CargarNino(String id) {

        Nino nino = new Nino();

        try {
            nino = ninoBL.getNinoById(getActivity(), id);


            lblNomNino.setText(nino.getNomNino());

            Calendar calendar1 = Calendar.getInstance();
            Date date = new Date(String.valueOf(nino.getFechaNac()));
            calendar1.setTime(date);

            calendarFechaNacNino.setTime(date);

            updateDate();


            obtenerMeses(date, calendarFechaActual.getTime());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean verificarDatos() {
        Boolean reslt = false;
        String msg = null;

        if (tbn_nopuedetomarpecho_menores.isChecked()){
            reslt = true;
            nomPregunta = "NoPuedeTomarPecho";
            grupo = "ninguno";
        }


        if (tbn_convuliones_menores.isChecked()){
            reslt = true;
            nomPregunta = "Convulsiones";
            grupo = "ninguno";
        }


        if (tbn_hundepiel_menores.isChecked()){
            reslt = true;
            nomPregunta = "HundePiel";
            grupo = "Amoxicilina";
        }


        if (tbn_ruidoraro_menores.isChecked()) {
            reslt = true;
            nomPregunta = "RuidosRespirar";
            grupo = "Amoxicilina";
        }


        if (tbn_respirarapida_menores.isChecked()){
            reslt = true;
            nomPregunta = "RespRapida";
            grupo = "Amoxicilina";
        }


        if (tbn_fiebre_menores.isChecked()){
            reslt = true;
            nomPregunta = "Fibre";
            grupo = "Acetaminofen";
        }


        if (tbn_tempbaja_menores.isChecked()){
            reslt = true;
            nomPregunta = "Temperatura";
            grupo = "Acetaminofen";
        }


        if (tbn_pielojoamarillo_menores.isChecked()){
            reslt = true;
            nomPregunta = "PielOjosAmarillos";
            grupo = "ninguno";
        }


        if (tbn_MovEstimulos.isChecked()){
            reslt = true;
            nomPregunta = "MovEstimulos";
            grupo = "ninguno";
        }


        if (tbn_OmbligoPus.isChecked()){
            reslt = true;
            nomPregunta = "OmbligoPus";
            grupo = "Amoxicilina";
        }


        if (tbn_PielUmbilicalRoja.isChecked()){
            reslt = true;
            nomPregunta = "PielUmbilicalRoja";
            grupo = "Amoxicilina";
        }

        if (tbn_PielGranos.isChecked()){
            reslt = true;
            nomPregunta = "PielGranos";
            grupo = "Amoxicilina";
        }


        if (tbn_OjosPus.isChecked()) {
            reslt = true;
            nomPregunta = "OjosPus";
            grupo = "Tetraciclina";
        }


        if (tbn_otra.isChecked()) {
            reslt = true;
            nomPregunta = "Otra";
            grupo = "ninguno";
        }


        return reslt;
    }

    private void CargaCasoNinoMenor() {

        Nino nino = new Nino();
        CCMRecienNacido ccmRecienNacido = new CCMRecienNacido();
        TratamientoRecienNacido tratamientoRecienNacido = new TratamientoRecienNacido();

        try {
            ccmRecienNacido = ccmRecienNacidoBL.getCCMRecienNacidoById(getActivity(), String.valueOf(idCCMRecienNacido));
            nino = ninoBL.getNinoById(getActivity(), idNino);
            //tratamientoRecienNacido = tratamientoRecienNacidoBL.getTratamientoRecienNacidoByCustomer(getActivity(), "IdCCMRecienNacido = " + String.valueOf(ccmRecienNacido.get_id()));
        } catch (SQLException e) {
            e.printStackTrace();
        }



        if (ccmRecienNacido.getNoPuedeTomarPecho()) {
            tbn_nopuedetomarpecho_menores.setChecked(true);
            nomPregunta = "NoPuedeTomarPecho";
            grupo = "ninguno";
        }

        if (ccmRecienNacido.getConvulsiones()) {
            tbn_convuliones_menores.setChecked(true);
            nomPregunta = "Convulsiones";
            grupo = "ninguno";
        }

        if (ccmRecienNacido.getHundePiel()) {
            tbn_hundepiel_menores.setChecked(true);
            nomPregunta = "HundePiel";
            grupo = "Amoxicilina";
        }

        if (ccmRecienNacido.getRuidosRespirar()) {
            tbn_ruidoraro_menores.setChecked(true);
            nomPregunta = "RuidosRespirar";
            grupo = "Amoxicilina";
        }

        if (ccmRecienNacido.getRespRapida()) {
            tbn_respirarapida_menores.setChecked(true);
            nomPregunta = "RespRapida";
            grupo = "Amoxicilina";
        }

        if (ccmRecienNacido.getFibre()) {
            tbn_fiebre_menores.setChecked(true);
            nomPregunta = "Fibre";
            grupo = "Acetaminofen";
        }

        if (ccmRecienNacido.getTemperatura()) {
            tbn_tempbaja_menores.setChecked(true);
            nomPregunta = "Temperatura";
            grupo = "Acetaminofen";
        }

        if (ccmRecienNacido.getPielOjosAmarillos()) {
            tbn_pielojoamarillo_menores.setChecked(true);
            nomPregunta = "PielOjosAmarillos";
            grupo = "ninguno";
        }

        if (ccmRecienNacido.getMovEstimulos()) {
            tbn_MovEstimulos.setChecked(true);
            nomPregunta = "MovEstimulos";
            grupo = "ninguno";
        }

        if (ccmRecienNacido.getOmbligoPus()) {
            tbn_OmbligoPus.setChecked(true);
            nomPregunta = "OmbligoPus";
            grupo = "Amoxicilina";
        }

        if (ccmRecienNacido.getPielUmbilicalRoja()) {
            tbn_PielUmbilicalRoja.setChecked(true);
            nomPregunta = "PielUmbilicalRoja";
            grupo = "Amoxicilina";
        }

        if (ccmRecienNacido.getPielGranos()) {
            tbn_PielGranos.setChecked(true);
            nomPregunta = "PielGranos";
            grupo = "Amoxicilina";
        }

        if (ccmRecienNacido.getOjosPus()) {
            tbn_OjosPus.setChecked(true);
            nomPregunta = "OjosPus";
            grupo = "Tetraciclina";
        }

        if (ccmRecienNacido.getOtra()) {
            tbn_otra.setChecked(true);
            nomPregunta = "Otra";
            grupo = "ninguno";
        }

        if (ccmRecienNacido.getLugarAtencion().equals("Madre busca a BPS"))
            radio_madrebuscabps_menores.setChecked(true);
        if (ccmRecienNacido.getLugarAtencion().equals("Sesion de Pesaje"))
            radio_sesion_menores.setChecked(true);
        if (ccmRecienNacido.getLugarAtencion().equals("Visita Domiciliar"))
            radio_visitadomiciliar_menores.setChecked(true);

        lblNomNino.setText(nino.getNomNino());

        Date date = new Date(String.valueOf(ccmRecienNacido.getFechaCCM()));
        calendar.setTime(date);

        Date dateFechaNac = new Date(String.valueOf(nino.getFechaNac()));
        calendarFechaNacNino.setTime(dateFechaNac);

        updateDate();
        int meses = obtenerMeses(date, dateFechaNac);

        edt_fecatencion_Meses.setText(String.valueOf(meses));

    }


}
