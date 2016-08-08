package com.moreno.javier.bibliodroid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Javas on 19/01/2016.
 */
public class LibroAdaptador extends BaseAdapter {
    private Context mContext;
    private ArrayList<Libro> libros;
    private ImageView portada;

    public LibroAdaptador(Context cx, ArrayList<Libro> libros) {
        this.libros = libros;
        this.mContext = cx;
    }

    @Override
    public int getCount() {
        return libros.size();
    }

    @Override
    public Object getItem(int position) {
        return libros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fila_libro, null);
        } else {
            view = convertView;
        }
        portada = (ImageView) view.findViewById(R.id.portada);
        //Cargamos la imagen
        String imagepath = libros.get(position).getImagen();
        if (imagepath.contains("@")) {
            int imageResource = mContext.getResources().getIdentifier(imagepath, null, mContext.getPackageName());
            Drawable res = ContextCompat.getDrawable(mContext, imageResource);
            portada.setImageDrawable(res);
        }else{
            Uri uriImage = Uri.parse(imagepath);
            portada.setImageURI(uriImage);
        }

        //Cargamos el titulo
        TextView titulo = (TextView) view.findViewById(R.id.titulo);
        titulo.setText(libros.get(position).getTitulo());

        //Cargamos el autor
        TextView autor = (TextView) view.findViewById(R.id.autor);
        autor.setText(libros.get(position).getAutor());

        return view;
    }
}