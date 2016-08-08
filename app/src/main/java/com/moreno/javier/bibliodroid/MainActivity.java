package com.moreno.javier.bibliodroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BDAdapter mDBHelper; //Adaptador BD
    private Cursor lCursor;
    private Cursor cCursor;//Cursor
    private ArrayList<Libro> libros = new ArrayList<Libro>(); //Array de libros
    private static ListView listaLibros;
    private Spinner categorias;
    private String[] spinnerValues;
    private static LibroAdaptador mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        listaLibros = (ListView) findViewById(R.id.lista_libros);
        categorias = (Spinner) findViewById(R.id.spinner_toolbar);

        //Abrimos conexion BD
        mDBHelper = new BDAdapter(this);
        try {
            mDBHelper.open();
        } catch (SQLException e) {
            e.getErrorCode();
        }

        cCursor = mDBHelper.recuperaCategorias();
        spinnerValues = new String[cCursor.getCount()];
        for(cCursor.moveToFirst(); !cCursor.isAfterLast(); cCursor.moveToNext()){
               spinnerValues[cCursor.getPosition()] = cCursor.getString(1);
        }
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, spinnerValues);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categorias.setAdapter(new SpinnerAdapter(this, R.layout.spinner_layout, spinnerValues));

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    fillData();
                } else {
                    fillDataCategoria(id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                fillData();
            }

        });



    }

    @Override
    protected void onResume(){
        super.onResume();
        //Rellenamos datos
        fillData();
        //Llenamos el ListView con nuestor adaptador

        listaLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                mostrarLibro(position);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.anyadir:
                crearLibro();
                return true;
            case R.id.acerca:
                crearAcerca();
                return true;
            case R.id.buscar:
                crearBuscar();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Llena el cursor y el array de libros
    private void fillData() {
        libros.clear();
        // Recupera todas las tareas de la DB y las guarda en el cursor
        lCursor = mDBHelper.recuperaTodos();

        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Libro libro = new Libro(lCursor.getInt(0), lCursor.getString(1), lCursor.getString(2), lCursor.getString(3),
                    lCursor.getString(4), lCursor.getString(5), lCursor.getString(6), lCursor.getString(7), lCursor.getInt(8));
            libros.add(lCursor.getPosition(), libro);
        }

        mAdapter = new LibroAdaptador(this, libros);
        listaLibros.setAdapter(mAdapter);
    }

    private void fillDataCategoria(long id){
        libros.clear();
        // Recupera todas las tareas de la DB y las guarda en el cursor
        lCursor = mDBHelper.recuperaConCategoria(Long.toString(id));

        for(lCursor.moveToFirst(); !lCursor.isAfterLast(); lCursor.moveToNext()){
            Libro libro = new Libro(lCursor.getInt(0), lCursor.getString(1), lCursor.getString(2), lCursor.getString(3),
                    lCursor.getString(4), lCursor.getString(5), lCursor.getString(6), lCursor.getString(7), lCursor.getInt(8));
            libros.add(lCursor.getPosition(), libro);
        }
        mAdapter = new LibroAdaptador(this, libros);
        listaLibros.setAdapter(mAdapter);

    }



    private void mostrarLibro(int position){
        Intent i;
        i = new Intent(this, LibroActivity.class);
        i.putExtra("id",libros.get(position).get_id()); //
        startActivityForResult(i, 5);
    }

    private void crearLibro(){
        Intent i;
        i = new Intent(this,AddActivity.class);
        startActivityForResult(i, 7);
    }

    private void crearAcerca(){
        Intent i;
        i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }
    private void crearBuscar(){
        Intent i;
        i = new Intent(this, BuscarActivity.class);
        startActivity(i);
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {

        public SpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.spinner_layout, parent,
                    false);
            TextView main_text = (TextView) mySpinner
                    .findViewById(R.id.texto_spinner);
            main_text.setText(spinnerValues[position]);


            return mySpinner;
        }


    }
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 5) {
            mDBHelper.borraUno(data.getExtras().getString("id"));
            fillData();
        }else if (resultCode == RESULT_OK && requestCode == 7){
            Bundle extras = data.getExtras();
            mDBHelper.crearUno(extras.getString("titulo"),extras.getString("autor"),
                    extras.getString("editorial"), extras.getString("isbn13"), extras.getString("isbn10"),
                    extras.getString("descripcion"), extras.getString("imagen"), extras.getString("categoria"));
            fillData();
        }
    }
}


