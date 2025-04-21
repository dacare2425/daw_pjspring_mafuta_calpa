package edu.fje.daw2.sm72.serveis;

import edu.fje.daw2.sm72.models.Pelicula;
import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.repositoris.PeliculaRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PeliculaServei {

    @Autowired
    private PeliculaRepositori peliculaRepositori;

    public Pelicula guardar(Pelicula pelicula) {
        // Establecer ID a null si está vacío para que MongoDB genere uno automáticamente
        if (pelicula.getId() != null && pelicula.getId().isEmpty()) {
            System.out.println("ID vacío detectado, estableciendo a null para generación automática");
            pelicula.setId(null);
        }

        System.out.println("Guardando película: " + pelicula.getTitulo() + ", ID antes: " + pelicula.getId());
        Pelicula guardada = peliculaRepositori.save(pelicula);
        System.out.println("Película guardada, ID después: " + guardada.getId());
        return guardada;
    }

    // Resto de métodos sin cambios...
    public List<Pelicula> obtenerTodas() {
        return peliculaRepositori.findAll();
    }

    public Pelicula obtenirPerId(String id) {
        return peliculaRepositori.findById(id).orElse(null);
    }

    public void eliminar(String id) {
        peliculaRepositori.deleteById(id);
    }

    /**
     * Obtiene las películas seleccionadas por un usuario
     */
    public List<Pelicula> obtenerPeliculasDeUsuario(Usuario usuario) {
        if (usuario == null || usuario.getPeliculasSeleccionadas() == null || usuario.getPeliculasSeleccionadas().isEmpty()) {
            return new ArrayList<>();
        }

        return peliculaRepositori.findAllById(usuario.getPeliculasSeleccionadas());
    }
}