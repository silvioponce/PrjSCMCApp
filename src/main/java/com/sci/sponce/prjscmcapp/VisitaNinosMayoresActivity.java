package com.sci.sponce.prjscmcapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sci.sponce.prjscmcapp.Fragment.BuscarVisitasNinosMayores;
import com.sci.sponce.prjscmcapp.Fragment.CatNinosFragment;


public class VisitaNinosMayoresActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_ninos_mayores);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adminFrgment("BuscarVistaNinosMayores");

    }

    private void adminFrgment(String fragment) {
        Object buscarNinosFragment = getFragmentManager().findFragmentByTag("BuscarVistaNinosMayores");
        Object catNinosFragment = getFragmentManager().findFragmentByTag("CatNinos");

        if (buscarNinosFragment == null && fragment == "BuscarVistaNinosMayores") {
            getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarVisitasNinosMayores().newInstance(0, true), "BuscarVistaNinosMayores").addToBackStack(null).commit();

        }

        if (catNinosFragment == null && fragment == "CatNinos") {
            getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatNinosFragment()).addToBackStack(null).commit();

        }

    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visita_ninos_mayores, menu);
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
}
