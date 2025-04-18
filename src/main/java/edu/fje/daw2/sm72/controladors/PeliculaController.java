package edu.fje.daw2.sm72.controladors;

import edu.fje.daw2.sm72.models.Pelicula;
import edu.fje.daw2.sm72.serveis.PeliculaServei;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaServei peliculaServei;

    @GetMapping
    public String listarPeliculas(Model model) {
        List<Pelicula> peliculas = peliculaServei.obtenerTodas();
        model.addAttribute("peliculas", peliculas);
        return "peliculas/listar";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaPelicula(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "peliculas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPelicula(@ModelAttribute Pelicula pelicula) {
        System.out.println("Recibiendo película para guardar: " + pelicula.getTitulo() + ", ID: " + pelicula.getId());

        // Guardar y obtener la película con ID asignado
        Pelicula peliculaGuardada = peliculaServei.guardar(pelicula);

        System.out.println("Película guardada con ID: " + peliculaGuardada.getId());
        return "redirect:/peliculas";
    }

    @GetMapping("/editar/{id}")
    public String editarPelicula(@PathVariable String id, Model model) {
        Pelicula pelicula = peliculaServei.obtenirPerId(id);
        if (pelicula == null) {
            return "redirect:/peliculas";
        }
        model.addAttribute("pelicula", pelicula);
        return "peliculas/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPelicula(@PathVariable String id) {
        peliculaServei.eliminar(id);
        return "redirect:/peliculas";
    }
}