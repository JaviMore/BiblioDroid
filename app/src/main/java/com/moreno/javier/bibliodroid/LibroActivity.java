package com.moreno.javier.bibliodroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;


/**
 * Created by Javas on 19/01/2016.
 *
 */
public class LibroActivity extends AppCompatActivity {
    private BDAdapter mDBHelper; //Adaptador BD
    private TextView titulo;
    private TextView autor;
    private TextView editorial;
    private TextView isbn13;
    private TextView isbn10;
    private TextView descripcion;
    private ImageView portada;
    private int idlibro;
    private boolean borrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libro_completo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_libro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        borrar = false;

        //Abrimos conexion BD
        mDBHelper = new BDAdapter(this);
        try {
            mDBHelper.open();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        fillData();
        portada.invalidate();

    }

    @Override
    protected void onResume(){
        super.onResume();
        fillData();
        portada.invalidate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_libro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.borrar:
                borraLibro();
                return true;
            case R.id.editar:
                editaLibro();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish(){
        if(borrar){
            Intent data = new Intent();
            data.putExtra("id", Integer.toString(idlibro));
            // Si la actividad acaba ok, devuelve datos
            setResult(RESULT_OK, data)
            ;}
        else{
            setResult(RESULT_CANCELED);
        }
        super.finish();
    }

    private void borraLibro(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("¿Esta seguro que desea borrar?");

        adb.setIcon(android.R.drawable.ic_dialog_alert);

        adb.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                borrar = true;
                finish();
            }
        });


        adb.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            } });
        adb.show();
    }

    private void editaLibro(){
        Intent data = new Intent(this, EditActivity.class);
        data.putExtra("id", Integer.toString(idlibro));
        startActivityForResult(data, 6);
    }

    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 6) {
            Bundle extras = data.getExtras();
            mDBHelper.actualizarUno(extras.getString("id"), extras.getString("titulo"), extras.getString("autor"),
                    extras.getString("editorial"), extras.getString("isbn13"), extras.getString("isbn10"),
                    extras.getString("descripcion"), extras.getString("imagen"), extras.getString("categoria"));
            fillData();
            portada.invalidate();
        }
    }

    private void fillData(){
        Bundle extras = getIntent().getExtras();
        idlibro = extras.getInt("id");
        Cursor mCursor = mDBHelper.recuperaUno(Integer.toString(idlibro));

        if(mCursor.moveToFirst()) {
            titulo = (TextView) findViewById(R.id.titulo_comp);
            titulo.setText(mCursor.getString(1));

            autor = (TextView) findViewById(R.id.autor_comp);
            autor.setText(mCursor.getString(2));

            editorial = (TextView) findViewById(R.id.editorial);
            editorial.setText(mCursor.getString(3));

            isbn13 = (TextView) findViewById(R.id.isbn13);
            isbn13.setText(mCursor.getString(4));

            isbn10 = (TextView) findViewById(R.id.isbn10);
            isbn10.setText(mCursor.getString(5));

            descripcion = (TextView) findViewById(R.id.descripcion);
            descripcion.setText(mCursor.getString(6));

            String imagepath =  mCursor.getString(7);
            if (imagepath.contains("@")) {
                int imageResource = this.getResources().getIdentifier(imagepath, null, this.getPackageName());
                Drawable res = ContextCompat.getDrawable(this, imageResource);
                portada = (ImageView) findViewById(R.id.portada_comp);
                portada.setImageDrawable(res);
            }else{
                Uri uriImage = Uri.parse(imagepath);
                portada = (ImageView) findViewById(R.id.portada_comp);
                portada.setImageURI(uriImage);
            }


        }
    }
}
