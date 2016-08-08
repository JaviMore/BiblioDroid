package com.moreno.javier.bibliodroid;

/**
 * Created by Javas on 19/01/2016.
 */
public class Libro {
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getISBN13() {
        return ISBN13;
    }

    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }

    public String getISBN10() {
        return ISBN10;
    }

    public void setISBN10(String ISBN10) {
        this.ISBN10 = ISBN10;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    private int _id;
    private String titulo;
    private String autor;
    private String editorial;
    private String ISBN13;
    private String ISBN10;
    private String descripcion;
    private String imagen;
    private int categoria;

    public Libro(int _id, String titulo, String autor, String editorial, String ISBN13, String ISBN10, String descripcion, String imagen, int categoria){
        this._id = _id;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.ISBN13 = ISBN13;
        this.ISBN10 = ISBN10;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.categoria = categoria;
    }



}
