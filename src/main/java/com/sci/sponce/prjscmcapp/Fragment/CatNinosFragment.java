package com.sci.sponce.prjscmcapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.sci.sponce.prjscmcapp.R;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import BL.CCMNinoBL;
import BL.CCMRecienNacidoBL;
import BL.ComunidadBL;
import BL.DepartamentoBL;
import BL.MunicipioBL;
import BL.NinoBL;
import Entidades.Nino;

public class CatNinosFragment extends Fragment implements View.OnClickListener {

    NinoBL ninoBL = new NinoBL();
    Nino nino = new Nino();

    DepartamentoBL departamentoBL = new DepartamentoBL();
    MunicipioBL municipioBL = new MunicipioBL();
    ComunidadBL comunidadBL = new ComunidadBL();

    DateFormat formate = DateFormat.getDateInstance(DateFormat.SHORT);
    Calendar calendar = Calendar.getInstance();
    Calendar calendarFechaActual = Calendar.getInstance();
    EditText edt_fecnacmimiento;
    ImageButton bt_fecnacmimiento;

    Spinner spnDepartamento;
    Spinner spnMunicipio;
    Spinner spnComunidad;

    EditText txtNomBrigadista, txtNomMadre, txtNomNino, txtEdadMeses, txtFecNac;
    RadioButton radio_F, radio_M, radio_mayor2500, radio_menor2500;

    Button btnGuardarNino;

    DatePickerDialog.OnDateSetListener d;

    Cursor cursorDepartamentos;
    Cursor cursorMunicipios;
    Cursor cursorComunidades;

    int idDepartamentoPref, idMunicipioPref, idComunidadPref, idUsuarioPref;

    String idNino = null;

    int idDepartamento, idMunicipio, idComunidad;
    int idPosicionDepartamento, idPosicionMunicipio, idPosicionComunidad;

    boolean ModoEdit = false;

    String nomUsuarioPref;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            String Id = String.valueOf(getArguments().getInt("someInt", 0));
            String someTitle = getArguments().getString("someTitle", "");

            idNino = Id;
            ModoEdit = true;


        } catch (Exception e) {
            e.printStackTrace();
        }

        getActivity().setTitle("Ingresar Nino(a)");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        //inflater.inflate(R.menu.menu_cat_ninos, menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat_ninos, container, false);

        btnGuardarNino = (Button) view.findViewById(R.id.btnGuardarNino);
        btnGuardarNino.setOnClickListener(this);

        bt_fecnacmimiento = (ImageButton) view.findViewById(R.id.bt_fecnacmimiento);
        txtFecNac = (EditText) view.findViewById(R.id.txtFecNac);
        spnDepartamento = (Spinner) view.findViewById(R.id.spnDepartamento);
        spnMunicipio = (Spinner) view.findViewById(R.id.spnMunicipio);
        spnComunidad = (Spinner) view.findViewById(R.id.spnComunidad);
        txtEdadMeses = (EditText) view.findViewById(R.id.txtEdadMeses);
        txtNomBrigadista = (EditText) view.findViewById(R.id.txtNomBrigadista);
        txtNomMadre = (EditText) view.findViewById(R.id.txtNomMadre);
        txtNomNino = (EditText) view.findViewById(R.id.txtNomNino);
        txtFecNac = (EditText) view.findViewById(R.id.txtFecNac);

        radio_F = (RadioButton) view.findViewById(R.id.radio_F);
        radio_M = (RadioButton) view.findViewById(R.id.radio_M);
        radio_mayor2500 = (RadioButton) view.findViewById(R.id.radio_mayor2500);
        radio_menor2500 = (RadioButton) view.findViewById(R.id.radio_menor2500);

        CargarDepartamento();

        //CargarMunicipios();

        d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
                obtenerMeses(calendar.getTime(), calendarFechaActual.getTime());
            }
        };

        bt_fecnacmimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        spnDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                idDepartamento = Integer.valueOf(cursorDepartamentos.getString(cursorDepartamentos.getColumnIndex("IdDepartamento")));
                CargarMunicipios();
                idPosicionDepartamento = getPositionCursor(cursorDepartamentos, "IdDepartamento", idDepartamentoPref);
                idPosicionMunicipio = getPositionCursor(cursorMunicipios, "IdMunicipio", idMunicipioPref);
                spnDepartamento.setSelection(idPosicionDepartamento);
                spnMunicipio.setSelection(idPosicionMunicipio);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idMunicipio = Integer.valueOf(cursorMunicipios.getString(cursorMunicipios.getColumnIndex("IdMunicipio")));
                CargarComunidades();
                idPosicionComunidad = getPositionCursor(cursorComunidades, "IdComunidad", idComunidadPref);
                spnComunidad.setSelection(idPosicionComunidad);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnComunidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idComunidad = Integer.valueOf(cursorComunidades.getString(cursorComunidades.getColumnIndex("IdComunidad")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CargarPreferencias();

        if (ModoEdit) CargarNino(idNino);

        spnDepartamento.setSelection(idDepartamento);
        txtNomBrigadista.setText(nomUsuarioPref);

        return view;
    }

    private void obtenerMeses1(Calendar calendar) {
        //String fecha = formate.format(calendar.getTime()); // 5 de junio 2004
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

        txtEdadMeses.setText(String.valueOf(meses));
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
        int mesActual = jCal.MONTH;
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



        txtEdadMeses.setText(String.valueOf(dato));

        return dato;

    }

    public void updateDate() {
        txtFecNac.setText(formate.format(calendar.getTime()));
    }

    public void setDate() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(calendarFechaActual.getTimeInMillis());
        dialog.show();

    }

    public static CatNinosFragment newInstance(int someInt, String someTitle) {
        CatNinosFragment fragmentDemo = new CatNinosFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        args.putString("someTitle", someTitle);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    public void CargarDepartamento() {

        try {
            cursorDepartamentos = departamentoBL.getAllDepartamentosCursor(getActivity());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] adapterCols = new String[]{"NomDepartamento"};
        int[] adapterRowViews = new int[]{android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter sca = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cursorDepartamentos, adapterCols, adapterRowViews, 0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnDepartamento.setAdapter(sca);

    }

    public void CargarMunicipios() {

        try {
            cursorMunicipios = municipioBL.getAllMunicipiosCursor(getActivity(), "IdDepartamento = " + idDepartamentoPref);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] adapterCols = new String[]{"NomMunicipio"};
        int[] adapterRowViews = new int[]{android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter sca = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cursorMunicipios, adapterCols, adapterRowViews, 0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnMunicipio.setAdapter(sca);

    }

    public void CargarComunidades() {

        try {
            cursorComunidades = comunidadBL.getAllComunidadesCursor(getActivity(), "IdMunicipio = " + idMunicipioPref);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] adapterCols = new String[]{"NomComunidad"};
        int[] adapterRowViews = new int[]{android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter sca = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_spinner_item, cursorComunidades, adapterCols, adapterRowViews, 0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnComunidad.setAdapter(sca);

    }

    public void CargarPreferencias() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PreferenciasUsuario", getActivity().getApplication().MODE_PRIVATE);
        idUsuarioPref = sharedPreferences.getInt("IdUsuario", 0);
        idDepartamentoPref = sharedPreferences.getInt("IdDepartamento", 0);
        idMunicipioPref = sharedPreferences.getInt("IdMunicipio", 0);
        idComunidadPref = sharedPreferences.getInt("IdComunidad", 0);
        nomUsuarioPref = sharedPreferences.getString("NomUsuario", "");

        idPosicionDepartamento = getPositionCursor(cursorDepartamentos, "IdDepartamento", idDepartamentoPref);
        spnDepartamento.setSelection(idDepartamentoPref);

    }

    private int getPositionCursor(Cursor cursor, String column, Integer value) {
        int index = 0;

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Integer id = Integer.valueOf(cursor.getString(cursor.getColumnIndex(column)));

            if (id.equals(value)) {
                index = cursor.getPosition();
            }

        }

        return index;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnGuardar) {

            if (!verificarDatos()) return true;

            if (idNino != null)
                ActualizarNino(idNino);
            else
                GuardarNino();

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Información...");
            alertDialog.setMessage("El Niño se Guardo Correctamente!!!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    verificarRespuesta();
                }

            });
            alertDialog.setIcon(R.mipmap.ic_save);
            alertDialog.show();
            return true;
        }

        //if (id == R.id.btnCancelar)
        //getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarNinosFragment(), "BuscarNinos").addToBackStack(null).commit();

        return super.onOptionsItemSelected(item);
    }

    private void ActualizarNino(String idNino) {
        Boolean resultado = false;
        nino.set_id(Integer.parseInt(idNino));

        nino.setNomNino(txtNomNino.getText().toString());
        nino.setIdComunidad(idComunidad);
        nino.setNomMadre(txtNomMadre.getText().toString());

        Date fecha = calendar.getTime();

        nino.setFechaNac(fecha);

        if (radio_F.isChecked())
            nino.setSexo("F");

        if (radio_M.isChecked())
            nino.setSexo("M");

        if (radio_mayor2500.isChecked()) {
            nino.setPesoMas2500(true);
        } else {
            nino.setPesoMas2500(false);
        }

        nino.setIdUsuario(idUsuarioPref);

        try {
            idNino = String.valueOf(ninoBL.GuardarNino(getActivity(), nino));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void verificarRespuesta() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("Información...");
        alertDialog.setMessage("¿Desea crear un Caso para estes Niño?");
        alertDialog.setIcon(R.mipmap.ic_save);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Date fechaNacimiento = nino.getFechaNac();

                Calendar calendar1 = Calendar.getInstance();
                GregorianCalendar fechaActual = new GregorianCalendar();
                calendar1.setTime(fechaNacimiento);

                long meses = ((fechaActual.getTimeInMillis() - calendar1.getTimeInMillis()) / 262975000) / 10;

                //Toast.makeText(getActivity(), String.valueOf((int) meses), Toast.LENGTH_SHORT).show();

                if (meses < 2) {
                    getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CasoNinosMenoresFragment().newInstance(Integer.parseInt(idNino)), "CasoNinosMenores").addToBackStack(null).commit();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatCasoNinosFragment().newInstance(Integer.parseInt(idNino), 0, false), "CatCasoNinos").addToBackStack(null).commit();
                }

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarNinosFragment(), "BuscarNinos").addToBackStack(null).commit();
            }
        });
        alertDialog.show();
    }

    private void GuardarNino() {
        Boolean resultado = false;

        nino.setNomNino(txtNomNino.getText().toString());
        nino.setIdComunidad(idComunidad);
        nino.setNomMadre(txtNomMadre.getText().toString());

        Date fecha = calendar.getTime();
        Date fechaRegistro = calendarFechaActual.getTime();

        nino.setFechaRegistro(fechaRegistro);

        nino.setFechaNac(fecha);

        if (radio_F.isChecked())
            nino.setSexo("F");

        if (radio_M.isChecked())
            nino.setSexo("M");

        if (radio_mayor2500.isChecked()) {
            nino.setPesoMas2500(true);
        } else {
            nino.setPesoMas2500(false);
        }

        nino.setIdUsuario(idUsuarioPref);

        try {
            idNino = String.valueOf(ninoBL.GuardarNino(getActivity(), nino));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void CargarNino(String id) {


        try {
            nino = ninoBL.getNinoById(getActivity(), id);

            txtNomBrigadista.setText(nomUsuarioPref);
            txtNomMadre.setText(nino.getNomMadre());
            txtNomNino.setText(nino.getNomNino());

            if (nino.getSexo() == "F") {
                radio_F.setChecked(true);
            } else {
                radio_M.setChecked(true);
            }

            if (nino.getPesoMas2500()) {
                radio_mayor2500.setChecked(true);
            } else {
                radio_menor2500.setChecked(true);
            }

            Date date = new Date(String.valueOf(nino.getFechaNac()));
            calendar.setTime(date);

            Calendar calendarActual = Calendar.getInstance();

            // calendar.set(Calendar.YEAR, nino.getFechaNac().getYear());
            //calendar.set(Calendar.MONTH, nino.getFechaNac().getMonth());
            //calendar.set(Calendar.DAY_OF_MONTH, nino.getFechaNac().getDay());

            updateDate();
            obtenerMeses(calendar.getTime(), calendarActual.getTime());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private boolean verificarDatos() {
        Boolean reslt = false;
        String msg = null;

        if (txtNomBrigadista.getText().length() == 0) {
            msg = "Por favor digite el Nombre del Brigadista";
            txtNomBrigadista.setFocusable(true);
        }

        if (txtNomNino.getText().length() == 0) {
            msg = "Por favor digite el Nombre del Niño";
            txtNomNino.setFocusable(true);
        }

        if (txtNomMadre.getText().length() == 0) {
            msg = "Por favor digite el Nombre del la Madre";
        }

        if (txtFecNac.getText().length() == 0) {
            msg = "Por favor digite la Fecha de Nacimiento";
        }


        if (msg == null) {
            reslt = true;
        } else {
            reslt = false;
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }

        return reslt;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGuardarNino:


                if (!verificarDatos() || verficaCaso()) {

                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getActivity());

                    alertDialog1.setTitle("Informacion...");
                    alertDialog1.setMessage("El Niño no se puede actualizar, por que tiene un Caso Registrado");
                    alertDialog1.setIcon(R.mipmap.ic_save);
                    alertDialog1.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment f = new Fragment();
                            f = new BuscarNinosFragment();
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getFragmentManager().beginTransaction().replace(R.id.Contenedor, f, "BuscarNinos").addToBackStack(null).commit();

                        }
                    });
                    alertDialog1.show();
                    break;

                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                alertDialog.setTitle("Guardar Registro...");
                alertDialog.setMessage("¿Desea guardar este registro?");
                alertDialog.setIcon(R.mipmap.ic_save);
                alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (idNino != null) {
                            ActualizarNino(idNino);
                            Fragment f = new Fragment();
                            f = new BuscarNinosFragment();

                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getFragmentManager().beginTransaction().replace(R.id.Contenedor, f, "BuscarNinos").addToBackStack(null).commit();
                        } else {
                            GuardarNino();
                            verificarRespuesta();
                        }

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

    private boolean verficaCaso() {
        boolean flag = false;

        CCMRecienNacidoBL ccmRecienNacidoBL = new CCMRecienNacidoBL();
        CCMNinoBL ccmNinoBL = new CCMNinoBL();

        try {
            flag = ccmRecienNacidoBL.getExisteCCMRecienNacidoByCustomer(getActivity(), "IdNino = " + idNino);
            if (flag)
                return true;

            flag = ccmNinoBL.getExisteCCMNinoByCustomer(getActivity(), "IdNino = " + idNino);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }
}
