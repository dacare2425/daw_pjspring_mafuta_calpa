package edu.fje.daw2.sm72.repositoris;

import edu.fje.daw2.sm72.models.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepositori extends CrudRepository<Usuario, String> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByProviderAndProviderId(String provider, String providerId);
    boolean existsByCorreo(String correo);
}