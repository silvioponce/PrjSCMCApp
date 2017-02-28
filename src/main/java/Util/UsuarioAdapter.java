package Util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sci.sponce.prjscmcapp.R;

import java.util.ArrayList;

import Entidades.Usuario;


public class UsuarioAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Usuario> usuarios;
    LayoutInflater inflater;

    public UsuarioAdapter(Activity activity, ArrayList<Usuario> usuarios){
        this.activity = activity;
        this.usuarios = usuarios;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return usuarios.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        //MyViewHoder myViewHoder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.listview_item, null);

            //vi = inflater.inflate(R.layout.listview_item, null);
            //myViewHoder = new MyViewHoder();
            //vi.setTag(myViewHoder);

        }



        Usuario usuario = usuarios.get(position);

        TextView textview;
        textview = (TextView) vi.findViewById(R.id.viewNombre);
        textview.setText(usuario.getNomUsuario());

        //textview = (TextView) vi.findViewById(R.id.viewTelefono);
        //textview.setText(usuario.getNomUsuario());

        textview = (TextView) vi.findViewById(R.id.viewCorreo);
        textview.setText(usuario.getUsuario());

        textview = (TextView) vi.findViewById(R.id.viewDireccion);
        textview.setText(usuario.getContrasena());

        CheckBox cbx = (CheckBox) vi.findViewById(R.id.checkbox);
        cbx.setChecked(true);

        return vi;
    }

    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }

    private static class MyViewHoder{
        TextView txtNombre, txtTelefono, txtCorre, txtDireccion;
        CheckBox cbx;
    }

    private void incializarCamposTexto(View view, Usuario actual) {
        TextView textview = (TextView) view.findViewById(R.id.viewNombre);
        textview.setText(actual.getNomUsuario());

        textview = (TextView) view.findViewById(R.id.viewTelefono);
        textview.setText(actual.getNomUsuario());

        textview = (TextView) view.findViewById(R.id.viewCorreo);
        textview.setText(actual.getUsuario());

        textview = (TextView) view.findViewById(R.id.viewDireccion);
        textview.setText(actual.getContrasena());

        CheckBox cbx = (CheckBox) view.findViewById(R.id.checkbox);
        cbx.setChecked(true);

        //ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
        //ivContactImage.setImageURI(Uri.parse(actual.getImageURI()));
    }
}
