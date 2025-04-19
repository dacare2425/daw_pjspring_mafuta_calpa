package edu.fje.daw2.sm72.controladors;

import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.serveis.UsuarioServei;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Ensure only admins can access these endpoints
public class AdminController {

    @Autowired
    private UsuarioServei usuarioServei;

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