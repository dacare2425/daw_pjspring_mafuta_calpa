package edu.fje.daw2.sm72.controladors;

import edu.fje.daw2.sm72.models.Pelicula;
import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.serveis.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Ensure only admins can access these endpoints
public class AdminController {

    @Autowired
    private UsuarioServei usuarioServei;
    @Autowired
    private PeliculaServei peliculaServei;

    // Listar todas las películas
    @GetMapping("/peliculas")
    public String listarPeliculas(Model model) {
        List<Pelicula> peliculas = peliculaServei.obtenerTodas();
        model.addAttribute("peliculas", peliculas);
        return "admin/peliculas"; // Vista para listar películas
    }

    // Mostrar formulario para crear nueva película
    @GetMapping("/peliculas/nueva")
    public String nuevaPelicula(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "admin/form-pelicula"; // Vista para crear/editar película
    }

    // Mostrar formulario de edición de película (ruta con ID)
    @GetMapping("/peliculas/editar")
    public String editarPelicula(@RequestParam String id, Model model) {
        Pelicula pelicula = peliculaServei.obtenirPerId(id);
        if (pelicula != null) {
            model.addAttribute("pelicula", pelicula);
            return "admin/form-pelicula"; // Vista para editar película
        } else {
            return "redirect:/admin/peliculas"; // Si no se encuentra la película, redirigir a la lista
        }
    }

    // Guardar los cambios de la película
    @PostMapping("/peliculas/guardar")
    public String guardarPelicula(@ModelAttribute Pelicula pelicula) {
        peliculaServei.guardar(pelicula); // Guardar la película con el nuevo nombre
        return "redirect:/admin/peliculas"; // Redirigir a la lista de películas
    }

    // Eliminar película
    @GetMapping("/peliculas/eliminar")
    public String eliminarPelicula(@RequestParam String id) {
        peliculaServei.eliminar(id);
        return "redirect:/admin/peliculas"; // Redirigir a la lista de películas
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        ArrayList<Usuario> usuarios = usuarioServei.obtenerTodos();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String verUsuario(@PathVariable String id, Model model) {
        Optional<Usuario> usuario = usuarioServei.obtenerPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "admin/detalle-usuario";
        } else {
            return "redirect:/admin/usuarios";
        }
    }

    @GetMapping("/usuarios/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/form-usuario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioServei.guardar(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable String id, Model model) {
        Optional<Usuario> usuario = usuarioServei.obtenerPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "admin/form-usuario";
        } else {
            return "redirect:/admin/usuarios";
        }
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        usuarioServei.eliminarPorId(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/toggle-admin/{id}")
    public String toggleAdminStatus(@PathVariable String id) {
        Optional<Usuario> usuarioOpt = usuarioServei.obtenerPorId(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setEsAdmin(!usuario.isEsAdmin());
            usuarioServei.guardar(usuario);
        }
        return "redirect:/admin/usuarios";
    }
}