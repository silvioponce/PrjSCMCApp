package com.sci.sponce.prjscmcapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MDIFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Preference mBuyPrefernce = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferencias);

        mBuyPrefernce = findPreference("prueba");

        mBuyPrefernce.setOnPreferenceClickListener(this);


    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mBuyPrefernce) {

            CopyReadAssets();
        }
        return false;
    }

    void openPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String path = getActivity().getApplicationInfo().dataDir + "/files/";

        File file = new File(path, "abc.pdf");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "No activity found", Toast.LENGTH_LONG).show(); //Display an error message
        }

    }

    private void CopyReadAssets() {
        AssetManager assetManager = getActivity().getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), "Manual.pdf");
        try {
            in = assetManager.open("Manual.pdf");
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/Manual.pdf"),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
