package edu.fje.daw2.sm72.config;

import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.repositoris.UsuarioRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepositori usuarioRepositori;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepositori.findByCorreo(correo);

        if (!usuarioOpt.isPresent()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + correo);
        }

        Usuario usuario = usuarioOpt.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add ROLE_USER for all authenticated users
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Add ROLE_ADMIN for admin users
        if (usuario.isEsAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(
                usuario.getCorreo(),
                usuario.getContrasena() != null ? usuario.getContrasena() : "", // OAuth users might not have password
                true, true, true, true,
                authorities
        );
    }
}