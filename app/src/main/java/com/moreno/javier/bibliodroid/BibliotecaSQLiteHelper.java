package com.moreno.javier.bibliodroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Javas on 19/01/2016.
 */
public class BibliotecaSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BDbiblioteca";
    private static final int DATABASE_VERSION = 1;

    public BibliotecaSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase bd) {
        String cadSQL;
        cadSQL = "create table tblCategorias (" +
                "_id integer primary key, " +
                "categoria text not null);";
        bd.execSQL(cadSQL);
        cadSQL = "create table tblLibros (" +
                "_id integer primary key AUTOINCREMENT, " +
                "titulo text not null, " +
                "autor text not null, " +
                "editorial text, " +
                "ISBN13 text, " +
                "ISBN10 text, " +
                "descripcion text, " +
                "imagen text not null, " +
                "categoria integer not null);";
        bd.execSQL(cadSQL);
        //Rellenando Libros
        bd.execSQL("INSERT INTO tblLibros (_id, titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES (1, 'LaBiblia', 'Dios Todopoderoso', 'Belen Editorial', '9783161484100','9783161484', " +
                "'Libro que contiene toda la sabiduria divina de Dios', '@drawable/labiblia', 1)");
        bd.execSQL("INSERT INTO tblLibros (_id, titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES (2, 'La Naranja Mecanica', 'Anthony Burgues','Minotauro', '9783161484100','9783161484', " +
                "'La naranja mecánica cuenta la historia del nadsat-adolescente Alex y sus tres drugos-amigos en un mundo de crueldad y destrucción', '@drawable/naranja', 1)");
        bd.execSQL("INSERT INTO tblLibros (_id, titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES (3, 'La Metamorfosis', 'Franz Kafka','AKAL', '9783161484100','9783161484', " +
                "'La peripecia subterránea y literal de Gregor Samsa, un viajante de comercio que al despertarse una mañana «de un sueño lleno de pesadillas se encontró en su cama convertido en un bicho enorme».'," +
                " '@drawable/metamorfosis', 3)");
        bd.execSQL("INSERT INTO tblLibros (_id, titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES (4, 'Criptonomicon: El codigo Enigma', 'Neal Stephenson','Zeta Bolsillo', '9783161484100','9783161484', " +
                "'En 1942, L.P.Waterhouse, genio matemático, colaboró con otros especialistas en descifrar los códigos secretos de las potencias del Eje.', '@drawable/criptonomicon', 1)");
        bd.execSQL("INSERT INTO tblLibros (_id, titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES (5, 'Busca Trabajo para dummies', 'Maite Piera','DUMMIES', '9783161484100','9783161484', " +
                "'El mercado laboral está complicado y la competencia es feroz. Por eso, este libro te ofrece toda la ayuda y apoyo que necesitas para dar lo mejor de ti mismo en cualquier proceso de búsqueda.', '@drawable/trabajo', 4)");
        bd.execSQL("INSERT INTO tblLibros (_id, titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES (6, 'Meditación para dummies', 'Stephan Bodian','DUMMIES', '9783161484100','9783161484', " +
                "'Las investigaciones científicas han demostrado que la meditación relaja el cuerpo, serena la mente, reduce el estrés y mejora la salud y el bienestar general.', '@drawable/meditacion', 4)");
        bd.execSQL("INSERT INTO tblLibros (_id, titulo, autor, editorial, ISBN13, ISBN10, descripcion, imagen, categoria) " +
                "VALUES (7, 'Tom Sawyer', 'Mark Twain','American Publishing', '9783161484100','9783161484', " +
                "'Las aventuras de Tom Sawyer es una novela del autor estadounidense Mark Twain publicada en 1876, actualmente considerada una obra maestra de la literatura.', '@drawable/sawyer', 2)");


        //Rellenando Categorias
        bd.execSQL("INSERT INTO tblCategorias (_id,categoria) " +
                "VALUES (0,'Todos')");
        bd.execSQL("INSERT INTO tblCategorias (_id,categoria) " +
                "VALUES (1,'Ficcion')");
        bd.execSQL("INSERT INTO tblCategorias (_id,categoria) " +
                "VALUES (2,'Aventuras')");
        bd.execSQL("INSERT INTO tblCategorias (_id, categoria) " +
                "VALUES (3, 'Terror')");
        bd.execSQL("INSERT INTO tblCategorias (_id, categoria) " +
                "VALUES (4, 'Autoayuda')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        Log.w(BibliotecaSQLiteHelper.class.getName(),
                "Actualizando " + oldVersion + " a " + newVersion +
                        ". Se destruirán todos los datos almacenados.");

        bd.execSQL("DROP TABLE IF EXISTS tblCategorias");
        bd.execSQL("DROP TABLE IF EXISTS tblLibros");
        onCreate(bd);
    }

}
