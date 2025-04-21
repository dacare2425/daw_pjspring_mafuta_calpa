package edu.fje.daw2.sm72.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    private String contrasena;
    private boolean esAdmin;

    private String provider;
    private String providerId;

    public Usuario() {}

    public Usuario(String nombre, String correo, String contrasena, boolean esAdmin) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.esAdmin = esAdmin;
    }

    public Usuario(String nombre, String correo, String provider, String providerId) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = null;
        this.esAdmin = false;
        this.provider = provider;
        this.providerId = providerId;
    }

    @ElementCollection
    @CollectionTable(name = "usuario_peliculas", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "pelicula_id")
    private List<String> peliculasSeleccionadas = new ArrayList<>();

    public List<String> getPeliculasSeleccionadas() {
        if (peliculasSeleccionadas == null) {
            peliculasSeleccionadas = new ArrayList<>();
        }
        return peliculasSeleccionadas;
    }

    public void setPeliculasSeleccionadas(List<String> peliculasSeleccionadas) {
        this.peliculasSeleccionadas = peliculasSeleccionadas;
    }

    public void agregarPelicula(String peliculaId) {
        if (peliculasSeleccionadas == null) {
            peliculasSeleccionadas = new ArrayList<>();
        }
        if (!peliculasSeleccionadas.contains(peliculaId)) {
            peliculasSeleccionadas.add(peliculaId);
        }
    }

    public void eliminarPelicula(String peliculaId) {
        if (peliculasSeleccionadas != null) {
            peliculasSeleccionadas.remove(peliculaId);
        }
    }
    // Add getters and setters for new fields
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Long.valueOf(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }
}
