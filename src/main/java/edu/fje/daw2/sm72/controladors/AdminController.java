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

/**
 * Controlador que gestiona las operaciones de administración sobre películas y usuarios.
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Ensure only admins can access these endpoints
public class AdminController {

    @Autowired
    private UsuarioServei usuarioServei;
    @Autowired
    private PeliculaServei peliculaServei;

    /**
     * Lista todas las películas disponibles en el sistema.
     */
    @GetMapping("/peliculas")
    public String listarPeliculas(Model model) {
        List<Pelicula> peliculas = peliculaServei.obtenerTodas();
        model.addAttribute("peliculas", peliculas);
        return "admin/peliculas";
    }

    /**
     * Muestra el formulario para crear una nueva película.
     */
    @GetMapping("/peliculas/nueva")
    public String nuevaPelicula(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "admin/form-pelicula";
    }

    /**
     * Muestra el formulario para editar una película existente.
     * @param id Identificador de la película a editar
     */
    @GetMapping("/peliculas/editar")
    public String editarPelicula(@RequestParam String id, Model model) {
        Pelicula pelicula = peliculaServei.obtenirPerId(id);
        if (pelicula != null) {
            model.addAttribute("pelicula", pelicula);
            return "admin/form-pelicula";
        } else {
            return "redirect:/admin/peliculas";
        }
    }

    /**
     * Guarda los cambios realizados a una película (creación o edición).
     * @param pelicula Objeto película con los datos a guardar
     */
    @PostMapping("/peliculas/guardar")
    public String guardarPelicula(@ModelAttribute Pelicula pelicula) {
        peliculaServei.guardar(pelicula);
        return "redirect:/admin/peliculas";
    }

    /**
     * Elimina una película del sistema.
     * @param id Identificador de la película a eliminar
     */
    @GetMapping("/peliculas/eliminar")
    public String eliminarPelicula(@RequestParam String id) {
        peliculaServei.eliminar(id);
        return "redirect:/admin/peliculas";
    }

    /**
     * Lista todos los usuarios registrados.
     */
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        ArrayList<Usuario> usuarios = usuarioServei.obtenerTodos();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    /**
     * Muestra los detalles de un usuario específico.
     * @param id Identificador del usuario
     */
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

    /**
     * Muestra el formulario para crear un nuevo usuario.
     */
    @GetMapping("/usuarios/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/form-usuario";
    }

    /**
     * Guarda los datos de un usuario (nuevo o editado).
     * @param usuario Objeto usuario con los datos a guardar
     */
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioServei.guardar(usuario);
        return "redirect:/admin/usuarios";
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     * @param id Identificador del usuario a editar
     */
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

    /**
     * Elimina un usuario del sistema.
     * @param id Identificador del usuario a eliminar
     */
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id) {
        usuarioServei.eliminarPorId(id);
        return "redirect:/admin/usuarios";
    }

    /**
     * Alterna el estado de administrador de un usuario (activación/desactivación).
     * @param id Identificador del usuario
     */
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
