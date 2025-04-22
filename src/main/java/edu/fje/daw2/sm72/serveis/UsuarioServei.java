package edu.fje.daw2.sm72.serveis;

import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.repositoris.UsuarioRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * Servei per conectar amb la base de dades d'Usuaris, en MariaDB
 */
@Service
public class UsuarioServei {

    @Autowired
    private UsuarioRepositori repositori;

    public ArrayList<Usuario> obtenerTodos() {
        var it = repositori.findAll();
        var users = new ArrayList<Usuario>();
        it.forEach(users::add);
        return users;
    }

    public Optional<Usuario> obtenerPorId(String id) {
        return repositori.findById(id);
    }

    public Usuario guardar(Usuario usuario) {
        return repositori.save(usuario);
    }

    public void eliminarPorId(String id) {
        repositori.deleteById(id);
    }

    public Optional<Usuario> obtenerPorCorreo(String correo) {
        return repositori.findByCorreo(correo);
    }

    public Usuario processOAuthUser(String provider, Map<String, Object> attributes) {
        String providerId = (String) attributes.get("sub");
        String correo = (String) attributes.get("email");
        String nombre = (String) attributes.get("name");

        Optional<Usuario> existingUserByProvider =
                repositori.findByProviderAndProviderId(provider, providerId);

        if (existingUserByProvider.isPresent()) {
            return existingUserByProvider.get();
        }

        Optional<Usuario> existingUserByEmail = repositori.findByCorreo(correo);

        if (existingUserByEmail.isPresent()) {
            Usuario user = existingUserByEmail.get();
            user.setProvider(provider);
            user.setProviderId(providerId);
            return repositori.save(user);
        }

        Usuario newUser = new Usuario(nombre, correo, provider, providerId);
        return repositori.save(newUser);
    }
}
