package edu.fje.daw2.sm72.serveis;

import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.repositoris.UsuarioRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioServei {

    @Autowired
    private UsuarioRepositori repositori;

    public ArrayList<Usuario> obtenerTodos() {
        //return repositori.findAll();
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
    /**
     * Process a user authenticated via OAuth
     * @param provider The OAuth provider (google, github, etc)
     * @param attributes User attributes from OAuth provider
     * @return The saved or updated Usuario
     */
    public Usuario processOAuthUser(String provider, Map<String, Object> attributes) {
        // Extract user details from OAuth attributes
        String providerId = (String) attributes.get("sub"); // or "id" depending on provider
        String correo = (String) attributes.get("email");
        String nombre = (String) attributes.get("name");

        // Check if user exists by provider and providerId
        Optional<Usuario> existingUserByProvider =
                repositori.findByProviderAndProviderId(provider, providerId);

        if (existingUserByProvider.isPresent()) {
            return existingUserByProvider.get();
        }

        // Check if user exists by email
        Optional<Usuario> existingUserByEmail = repositori.findByCorreo(correo);

        if (existingUserByEmail.isPresent()) {
            // Link existing account with OAuth provider
            Usuario user = existingUserByEmail.get();
            user.setProvider(provider);
            user.setProviderId(providerId);
            return repositori.save(user);
        }

        // Create new user
        Usuario newUser = new Usuario(nombre, correo, provider, providerId);
        return repositori.save(newUser);
    }
}
