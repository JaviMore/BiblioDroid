package com.moreno.javier.bibliodroid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Javas on 19/01/2016.
 */
public class BDAdapter {
    private Context mCtx;
    private SQLiteDatabase mDB;
    private BibliotecaSQLiteHelper mDBHelper;
    public static final String DATABASE_NAME = "MiBiblioteca";
    public static final Integer DATABASE_VERSION = 1;

    //Campos de la Tabla Categorias.
    public static final String DATABASE_TABLE_CATEGORIAS = "tblCategorias";
    public static final String KEY_ROWID_CATEGORIAS = "_id";
    public static final String KEY_CATEGORIA = "categoria";

    //Campos de la tabla Libros.
    public static final String DATABASE_TABLE_LIBROS = "tblLibros";
    public static final String KEY_ROWID_LIBROS = "_id";
    public static final String KEY_TITULO = "titulo";
    public static final String KEY_AUTOR = "autor";
    public static final String KEY_EDITORIAL = "editorial";
    public static final String KEY_ISBN13 = "ISBN13";
    public static final String KEY_ISBN10 = "ISBN10";
    public static final String KEY_DESCRIPCION = "descripcion";
    public static final String KEY_IMAGEN = "imagen";
    public static final String KEY_CATEGORIA_LIBRO = "categoria";


    //Constructor se le pasa el contexto para poder abrir la BD.
    public BDAdapter (Context context){this.mCtx = context;}


    // Abre la BD. Si no puede abrirla la crea.
    // Si no puede abrirla o crearla lanza una excepción.
    public BDAdapter open() throws SQLException {
        mDBHelper = new BibliotecaSQLiteHelper(mCtx);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    //Cierra la base de datos
    public void close(){
        mDBHelper.close();
    }

    public Cursor recuperaCategorias(){
        return mDB.rawQuery("SELECT * FROM tblCategorias", null);
    };

    public Cursor recuperaCategoriassinTodos(){
        return mDB.rawQuery("SELECT * FROM tblCategorias WHERE _id > 0", null);
    };

    //Recupera todos.
    public Cursor recuperaTodos(){
        return mDB.query(DATABASE_TABLE_LIBROS, new String[]{KEY_ROWID_LIBROS, KEY_TITULO, KEY_AUTOR, KEY_EDITORIAL,
                KEY_ISBN13, KEY_ISBN10, KEY_DESCRIPCION, KEY_IMAGEN, KEY_CATEGORIA_LIBRO}, null, null, null, null, null);
    }

    public Cursor recuperaUno(String id) {

        return mDB.rawQuery("SELECT * FROM tblLibros WHERE _id = " + id, null);
    }

    public Cursor recuperaConCategoria(String Categoria){

        return mDB.rawQuery("SELECT * FROM tblLibros WHERE categoria = " + Categoria, null);
    }

    public Cursor recuperaConTitulo(String titulo){

        return mDB.rawQuery("SELECT * FROM tblLibros WHERE titulo LIKE '%" + titulo + "%'", null);
    }

    public void borraUno(String id){
        mDB.execSQL("DELETE FROM tblLibros WHERE _id = " + id);
    }

    public void actualizarUno(String id, String titulo, String autor,
                              String editorial, String isbn13, String isbn10, String descripcion, String imagen, String categoria){
        mDB.execSQL("UPDATE tblLibros " +
                "SET titulo='" + titulo + "', autor='"+ autor +"', editorial='" + editorial + "', " +
                "isbn13='" + isbn13 + "', isbn10 ='" + isbn10 + "', descripcion='"+ descripcion + "', " +
                "imagen='"+ imagen + "', categoria="+ categoria + " WHERE _id = " + id);
    }

    public void crearUno(String titulo, String autor,
                         String editorial, String isbn13, String isbn10, String descripcion, String imagen, String categoria){

        mDB.execSQL("INSERT INTO tblLibros (titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES('" + titulo + "','" + autor + "','" + editorial + "','" + isbn13 + "','" + isbn10 + "','" + descripcion + "','" + imagen + "',"+categoria+")");
    }
}
