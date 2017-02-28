package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sci.sponce.prjscmcapp.R;

import java.util.ArrayList;

import Entidades.Usuario;

public class UsersAdapter extends ArrayAdapter<Usuario> {
    public UsersAdapter(Context context, ArrayList<Usuario> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Usuario user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.viewNombre);
        TextView tvHome = (TextView) convertView.findViewById(R.id.viewTelefono);
        // Populate the data into the template view using the data object
        tvName.setText(user.getNomUsuario());
        tvHome.setText(user.getUsuario());
        // Return the completed view to render on screen
        return convertView;
    }
}