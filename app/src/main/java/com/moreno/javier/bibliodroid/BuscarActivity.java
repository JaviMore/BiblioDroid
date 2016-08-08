package com.moreno.javier.bibliodroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Javas on 20/01/2016.
 */
public class BuscarActivity extends AppCompatActivity {

    private BDAdapter mDBHelper; //Adaptador BD
    private Cursor lCursor;
    private static ListView listaLibros;
    private static LibroAdaptador mAdapter;
    private ArrayList<Libro> libros = new ArrayList<Libro>(); //Array de libros
    private EditText titulo;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_buscar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        error = (TextView) findViewById(R.id.error);
        listaLibros = (ListView) findViewById(R.id.lista_libros_buscar);
        Button btn_buscar = (Button) findViewById(R.id.button_buscar);
        titulo = (EditText) findViewById(R.id.editText_titulo);


        //Abrimos conexion BD
        mDBHelper = new BDAdapter(this);
        try {
            mDBHelper.open();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        listaLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                mostrarLibro(position);
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busqueda = titulo.getText().toString();
                Context ctx = getApplicationContext();
                rellenaBusqueda(busqueda,ctx);
            }
        });
    }


    private void rellenaBusqueda(String busqueda, Context ctx){

        libros.clear();
        error.setText("");

        if (busqueda == null || busqueda == "" || busqueda.length() == 0) {
            error.setText("Introduce un término para buscar.");

        }else{
            lCursor = mDBHelper.recuperaConTitulo(busqueda);

            for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
                Libro libro = new Libro(lCursor.getInt(0), lCursor.getString(1), lCursor.getString(2), lCursor.getString(3),
                        lCursor.getString(4), lCursor.getString(5), lCursor.getString(6), lCursor.getString(7), lCursor.getInt(8));
                libros.add(lCursor.getPosition(), libro);
            }
            if(libros.isEmpty()){
               error.setText("No se ha encontrado ningún resultado.");
            }else{
                mAdapter = new LibroAdaptador(this, libros);
                listaLibros.setAdapter(mAdapter);
            }

        }
    }

    private void mostrarLibro(int position){
        Intent i;
        i = new Intent(this, LibroActivity.class);
        i.putExtra("id",libros.get(position).get_id()); //
        startActivity(i);
    }
}
