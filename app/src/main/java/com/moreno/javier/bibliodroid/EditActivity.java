package com.moreno.javier.bibliodroid;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by Javas on 22/01/2016.
 */
public class EditActivity extends AppCompatActivity {
    private BDAdapter mDBHelper; //Adaptador BD
    private Cursor mCursor;
    private EditText titulo;
    private EditText autor;
    private EditText editorial;
    private EditText isbn13;
    private EditText isbn10;
    private EditText descripcion;
    private ImageView portada;
    private String idlibro;
    private Bitmap bitmap;
    private int REQUEST_CODE = 6;
    private boolean editar = false;
    private String imagepath;
    private Spinner categorias;
    private String[] spinnerValues;
    private Cursor cCursor;
    private String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit_libro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Abrimos conexion BD
        mDBHelper = new BDAdapter(this);
        try {
            mDBHelper.open();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        Bundle extras = getIntent().getExtras();
        idlibro = extras.getString("id");
        mCursor = mDBHelper.recuperaUno(idlibro);

        if (mCursor.moveToFirst()) {
            titulo = (EditText) findViewById(R.id.titulo_comp_edit);
            titulo.setText(mCursor.getString(1));

            autor = (EditText) findViewById(R.id.autor_comp_edit);
            autor.setText(mCursor.getString(2));

            editorial = (EditText) findViewById(R.id.editorial_edit);
            editorial.setText(mCursor.getString(3));

            isbn13 = (EditText) findViewById(R.id.isbn13_edit);
            isbn13.setText(mCursor.getString(4));

            isbn10 = (EditText) findViewById(R.id.isbn10_edit);
            isbn10.setText(mCursor.getString(5));

            descripcion = (EditText) findViewById(R.id.descripcion_edit);
            descripcion.setText(mCursor.getString(6));

           imagepath = mCursor.getString(7);
            if (imagepath.contains("@")) {
                int imageResource = this.getResources().getIdentifier(imagepath, null, this.getPackageName());
                Drawable res = ContextCompat.getDrawable(this, imageResource);
                portada = (ImageView) findViewById(R.id.portada_comp_edit);
                portada.setImageDrawable(res);
            }else{
                Uri uriImage = Uri.parse(imagepath);
                portada = (ImageView) findViewById(R.id.portada_comp_edit);
                portada.setImageURI(uriImage);
            }
            categoria = mCursor.getString(8);

        }

        categorias = (Spinner) findViewById(R.id.spinner_toolbar_edit);
        cCursor = mDBHelper.recuperaCategoriassinTodos();
        spinnerValues = new String[cCursor.getCount()];
        for(cCursor.moveToFirst(); !cCursor.isAfterLast(); cCursor.moveToNext()){
            spinnerValues[cCursor.getPosition()] = cCursor.getString(1);
        }
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, spinnerValues);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categorias.setAdapter(new SpinnerAdapter(this, R.layout.spinner_layout, spinnerValues));
        categorias.setSelection(Integer.parseInt(categoria) - 1);

        categorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                categoria = Long.toString(id + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });




        portada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, REQUEST_CODE);
            }
        });

        Button aceptar = (Button) findViewById(R.id.Aceptar);
        Button cancelar = (Button)findViewById(R.id.Cancelar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editar = true;
                finish();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar = false;
                finish();
            }
        });

    }

    @Override
    public void finish(){
       if(editar){
           Intent data = new Intent();
           data.putExtra("id", idlibro);
           data.putExtra("titulo", titulo.getText().toString());
           data.putExtra("autor", autor.getText().toString());
           data.putExtra("editorial",editorial.getText().toString());
           data.putExtra("isbn13", isbn13.getText().toString());
           data.putExtra("isbn10", isbn10.getText().toString());
           data.putExtra("descripcion", descripcion.getText().toString());
           data.putExtra("imagen", imagepath);
           data.putExtra("categoria", categoria);

        // Si la actividad acaba ok, devuelve datos
        setResult(RESULT_OK, data);
       }else{
            setResult(RESULT_CANCELED);
       }
        super.finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if(data.getData()!=null)
            {
                try
                {
                    if (bitmap != null)
                    {
                        bitmap.recycle();
                    }

                    InputStream stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);

                    stream.close();

                }

                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            else
            {
                bitmap=(Bitmap) data.getExtras().get("data");
                saveToInternalSorage(bitmap);
                portada.setImageBitmap(bitmap);
                imagepath = "/data/data/com.moreno.javier.bibliodroid/app_imageDir/"+ "portada" + idlibro + ".jpg";
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private String saveToInternalSorage(Bitmap bitmapImage){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"portada"+ idlibro +".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try{
            fos.close();
            }catch (IOException e){}
        }
        return directory.getAbsolutePath();
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
}

