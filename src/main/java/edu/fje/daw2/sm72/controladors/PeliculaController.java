package edu.fje.daw2.sm72.controladors;

import edu.fje.daw2.sm72.models.Pelicula;
import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.serveis.PeliculaServei;
import edu.fje.daw2.sm72.serveis.UsuarioServei;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaServei peliculaServei;

    @Autowired
    private UsuarioServei usuarioServei;

    /**
     * Muestra el catálogo completo de películas
     */
    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        // Obtener todas las películas
        List<Pelicula> todasPeliculas = peliculaServei.obtenerTodas();
        model.addAttribute("peliculas", todasPeliculas);

        // Obtener el usuario actual y sus películas seleccionadas
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = auth.getName();

        Optional<Usuario> usuarioOpt = usuarioServei.obtenerPorCorreo(correoUsuario);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            model.addAttribute("usuario", usuario);
            model.addAttribute("peliculasSeleccionadas", usuario.getPeliculasSeleccionadas());
        } else {
            model.addAttribute("peliculasSeleccionadas", List.of());
        }

        return "peliculas/catalogo";
    }

    /**
     * Muestra las películas seleccionadas por el usuario
     */
    @GetMapping("/mis-peliculas")
    public String misPeliculas(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // Extraer el correo electrónico del OAuth2 Principal
            String correoUsuario;

            if (auth.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
                correoUsuario = oauth2User.getAttribute("email");
            } else if (auth.getPrincipal() instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) auth.getPrincipal();
                correoUsuario = oidcUser.getEmail();
            } else {
                correoUsuario = auth.getName();
            }

            Optional<Usuario> usuarioOpt = usuarioServei.obtenerPorCorreo(correoUsuario);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // Obtener los IDs de películas seleccionadas por el usuario
                List<String> peliculasIds = usuario.getPeliculasSeleccionadas();

                // Obtener las películas completas desde MongoDB usando los IDs
                List<Pelicula> peliculasUsuario = new ArrayList<>();

                if (peliculasIds != null && !peliculasIds.isEmpty()) {
                    for (String peliculaId : peliculasIds) {
                        Pelicula pelicula = peliculaServei.obtenirPerId(peliculaId);
                        if (pelicula != null) {
                            peliculasUsuario.add(pelicula);
                        }
                    }
                }

                model.addAttribute("peliculas", peliculasUsuario);
                model.addAttribute("usuario", usuario);
            } else {
                model.addAttribute("peliculas", new ArrayList<>());
                model.addAttribute("mensaje", "Usuario no encontrado");
                model.addAttribute("tipoMensaje", "warning");
            }

        } catch (Exception e) {
            model.addAttribute("peliculas", new ArrayList<>());
            model.addAttribute("mensaje", "Error al cargar películas: " + e.getMessage());
            model.addAttribute("tipoMensaje", "danger");
            e.printStackTrace();
        }

        return "peliculas/mis-peliculas";
    }

    /**
     * Añade una película a la lista personal del usuario
     */
    @PostMapping("/agregar/{id}")
    public String agregarPelicula(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            // Extraer el correo electrónico del OAuth2 Principal
            String correoUsuario;

            if (auth.getPrincipal() instanceof Map) {
                // Si es un mapa de atributos (común en algunos flujos OAuth2)
                @SuppressWarnings("unchecked")
                Map<String, Object> attributes = (Map<String, Object>) auth.getPrincipal();
                correoUsuario = (String) attributes.get("email");
            } else if (auth.getPrincipal() instanceof OAuth2User) {
                // Si es un OAuth2User (más común)
                OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
                correoUsuario = oauth2User.getAttribute("email");
            } else if (auth.getPrincipal() instanceof DefaultOidcUser) {
                // Si es un DefaultOidcUser (específico para OpenID Connect)
                DefaultOidcUser oidcUser = (DefaultOidcUser) auth.getPrincipal();
                correoUsuario = oidcUser.getEmail();
            } else {
                // Fallback al nombre de usuario (probablemente el ID de Google)
                correoUsuario = auth.getName();
            }

            System.out.println("Correo extraído: " + correoUsuario);

            // Buscar usuario por correo
            Optional<Usuario> usuarioOpt = usuarioServei.obtenerPorCorreo(correoUsuario);

            if (!usuarioOpt.isPresent()) {
                // Si no se encuentra por correo, intentar crear un nuevo usuario
                System.out.println("Usuario no encontrado, intentando crear uno nuevo");

                // Extraer más información del usuario
                String nombre = null;
                String providerId = auth.getName(); // ID de Google
                String provider = "google";

                if (auth.getPrincipal() instanceof OAuth2User) {
                    OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
                    nombre = oauth2User.getAttribute("name");
                } else if (auth.getPrincipal() instanceof DefaultOidcUser) {
                    DefaultOidcUser oidcUser = (DefaultOidcUser) auth.getPrincipal();
                    nombre = oidcUser.getFullName();
                }

                // Crear nuevo usuario
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setCorreo(correoUsuario);
                nuevoUsuario.setNombre(nombre != null ? nombre : "Usuario");
                nuevoUsuario.setProvider(provider);
                nuevoUsuario.setProviderId(providerId);
                nuevoUsuario.setEsAdmin(false); // Por defecto no es admin

                // Guardar el nuevo usuario
                usuarioServei.guardar(nuevoUsuario);

                // Volver a buscar el usuario recién creado
                usuarioOpt = usuarioServei.obtenerPorCorreo(correoUsuario);

                if (!usuarioOpt.isPresent()) {
                    redirectAttributes.addFlashAttribute("mensaje", "No se pudo crear el usuario");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                    return "redirect:/peliculas/catalogo";
                }
            }

            Usuario usuario = usuarioOpt.get();
            System.out.println("Usuario encontrado/creado: " + usuario.getId() + ", Correo: " + usuario.getCorreo());

            // Validar si la película existe antes de añadirla
            Pelicula pelicula = peliculaServei.obtenirPerId(id);
            if (pelicula == null) {
                redirectAttributes.addFlashAttribute("mensaje", "La película no existe");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/peliculas/catalogo";
            }

            if (usuario.getPeliculasSeleccionadas().contains(id)) {
                redirectAttributes.addFlashAttribute("mensaje", "Esta película ya está en tu lista");
                redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            } else {
                usuario.agregarPelicula(id);
                usuarioServei.guardar(usuario);
                redirectAttributes.addFlashAttribute("mensaje", pelicula.getTitulo() + " añadida a tu lista");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error interno: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            // Log del error
            System.err.println("Error al añadir película: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/peliculas/catalogo";
    }
    /**
     * Elimina una película de la lista personal del usuario
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarPelicula(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = auth.getName();

        Optional<Usuario> usuarioOpt = usuarioServei.obtenerPorCorreo(correoUsuario);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuario.getPeliculasSeleccionadas().contains(id)) {
                usuario.eliminarPelicula(id);
                usuarioServei.guardar(usuario);

                Pelicula pelicula = peliculaServei.obtenirPerId(id);
                String titulo = pelicula != null ? pelicula.getTitulo() : "Película";

                redirectAttributes.addFlashAttribute("mensaje", titulo + " eliminada de tu lista");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la película");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }

        return "redirect:/peliculas/mis-peliculas";
    }

}