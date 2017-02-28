package com.sci.sponce.prjscmcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import BL.AdminBL;
import BL.ComunidadBL;
import BL.DepartamentoBL;
import BL.MunicipioBL;
import BL.UsuarioBL;
import Entidades.Comunidad;
import Entidades.Departamento;
import Entidades.Municipio;
import Entidades.Usuario;
import WS.UsuarioWs;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private String result;
    ProgressBar pg;

    private String mUser;
    private String mPassword;

    private EditText txtUsuario, txtPassword;
    private Button btnLogin;

    //private UserLoginTask mAuthTask = null;
    public static String usrGlogal = ".";

    Usuario usuario = new Usuario();
    Comunidad comunidad = new Comunidad();
    ComunidadBL comunidadBL = new ComunidadBL();

    Municipio municipio = new Municipio();
    MunicipioBL municipioBL = new MunicipioBL();
    Departamento departamento;
    DepartamentoBL departamentoBL = new DepartamentoBL();


    UsuarioWs usuarioWs = new UsuarioWs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtUsuario = (EditText) findViewById(R.id.edtUsuario);
        txtPassword = (EditText) findViewById(R.id.edtConstrasenna);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        pg = (ProgressBar) findViewById(R.id.progressBar1);

        btnLogin.setOnClickListener(this);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                UsuarioBL usuarioBL = new UsuarioBL();
                ComunidadBL comunidadBL = new ComunidadBL();
                AdminBL adminBL = new AdminBL();

                try {
                    if (adminBL.getCreateDB(getApplicationContext())) {
                        String msg = null;
                        usuario = usuarioBL.getVerificaUsuario(getApplicationContext(), txtUsuario.getText().toString(), txtPassword.getText().toString());


                        comunidad = comunidadBL.getComunidadById(getApplicationContext(), String.valueOf( usuario.getIdComunidad()));

                        msg = comunidad.getNomComunidad();

                        Toast.makeText( getApplicationContext(), msg,Toast.LENGTH_SHORT).show();

                        //Toast.makeText(getApplicationContext(), "Conexion 2: " + executeCmd("ping -c 1 -w 1 google.com", false), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(), "Conexion 2: " + adminBL.isOnline(getApplicationContext()), Toast.LENGTH_SHORT).show();

                        //validaUsuario();

                        if (usuario.getIdUsuario() != 0) {

                            if (usuario.getEstado() != true) {
                                Toast.makeText(getApplicationContext(), "Mensaje de Información: El usuario ha sido Deshabilitado", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            startActivity(new Intent(this, MDIActivity.class));
                            GuardarPreferecias();


                        } else {
                            Toast.makeText(getApplicationContext(), "Mensaje de Información: Error al ingresar las Credenciales", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Error al Crear la Base de Datos...", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public void GuardarPreferecias(){
        int IdMunicipio = 0;
        int IdDepartamento = 0;


        try {
           comunidad = comunidadBL.getComunidadById(getApplicationContext(), String.valueOf(usuario.getIdComunidad()));
            municipio = municipioBL.getMunicipioById(getApplicationContext(), String.valueOf(comunidad.getIdMunicipio()));

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        SharedPreferences sharedPreferences = getSharedPreferences("PreferenciasUsuario", getApplication().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("IdUsuario", usuario.getIdUsuario());
        editor.putString("NomUsuario", usuario.getNomUsuario());
        editor.putInt("IdComunidad", usuario.getIdComunidad());

        editor.putInt("IdMunicipio", municipio.getIdMunicipio());
        editor.putInt("IdDepartamento", municipio.getIdDepartamento());

        editor.commit();
    }




}



