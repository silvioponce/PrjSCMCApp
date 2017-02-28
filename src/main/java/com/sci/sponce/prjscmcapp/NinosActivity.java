package com.sci.sponce.prjscmcapp;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sci.sponce.prjscmcapp.Fragment.BuscarNinosFragment;
import com.sci.sponce.prjscmcapp.Fragment.CatNinosFragment;


public class NinosActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninos);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adminFrgment("BuscarNinos");

    }

    private void adminFrgment(String fragment) {
        Object buscarNinosFragment = getFragmentManager().findFragmentByTag("BuscarNinos");
        Object catNinosFragment = getFragmentManager().findFragmentByTag("CatNinos");


        if (buscarNinosFragment == null && fragment == "BuscarNinos") {
            getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarNinosFragment(), "BuscarNinos").addToBackStack(null).commit();


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
        getMenuInflater().inflate(R.menu.menu_ninos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnNuevo) {
            getFragmentManager().beginTransaction().replace(R.id.Contenedor, new CatNinosFragment()).addToBackStack(null).commit();
            return true;
        }

        if (id == R.id.btnCancelar) {
            getFragmentManager().beginTransaction().replace(R.id.Contenedor, new BuscarNinosFragment(), "BuscarNinos").addToBackStack(null).commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
