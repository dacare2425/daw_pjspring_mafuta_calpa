package edu.fje.daw2.sm72.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model per películas, desadas en MongoDB. Conté Pelicula.posterUrl per trobar el poster a l'api de OMDB.
 */
@Document(collection = "peliculas")
public class Pelicula {

    @Id
    private String id;

    private String titulo;
    private String director;
    private Integer anyo;
    private String genero;
    @Transient
    private String posterUrl;



    public Pelicula() {}

    public Pelicula(String titulo, String director, int anyo, String genero) {
        this.titulo = titulo;
        this.director = director;
        this.anyo = anyo;
        this.genero = genero;
    }

    // Getters y Setters
    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}