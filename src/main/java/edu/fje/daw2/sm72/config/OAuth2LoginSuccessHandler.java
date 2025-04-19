package edu.fje.daw2.sm72.config;

import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.repositoris.UsuarioRepositori;
import edu.fje.daw2.sm72.serveis.UsuarioServei;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UsuarioServei usuarioServei;

    @Autowired
    private UsuarioRepositori usuarioRepositori;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String provider = oauthToken.getAuthorizedClientRegistrationId();

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Check if this is the first user before processing
        boolean isFirstUser = usuarioRepositori.count() == 0;

        // Extract user details from OAuth attributes
        String providerId = attributes.get("sub") != null ?
                (String) attributes.get("sub") : (String) attributes.get("id");
        String correo = (String) attributes.get("email");
        String nombre = attributes.get("name") != null ?
                (String) attributes.get("name") : correo;

        // Create or update user
        Usuario usuario;

        // Check if user exists by provider and providerId
        var existingUserByProvider = usuarioRepositori.findByProviderAndProviderId(provider, providerId);

        if (existingUserByProvider.isPresent()) {
            usuario = existingUserByProvider.get();
        } else {
            // Check if user exists by email
            var existingUserByEmail = usuarioRepositori.findByCorreo(correo);

            if (existingUserByEmail.isPresent()) {
                // Link existing account with OAuth provider
                usuario = existingUserByEmail.get();
                usuario.setProvider(provider);
                usuario.setProviderId(providerId);
            } else {
                // Create new user
                usuario = new Usuario();
                usuario.setNombre(nombre);
                usuario.setCorreo(correo);
                usuario.setProvider(provider);
                usuario.setProviderId(providerId);

                // If this is the first user, make them an admin
                if (isFirstUser) {
                    usuario.setEsAdmin(true);
                    System.out.println("First user created as admin: " + correo);
                }
            }
        }

        // Save the user
        usuario = usuarioServei.guardar(usuario);

        // Create authorities based on user roles
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (usuario.isEsAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // Create a new authentication with the correct authorities
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                oAuth2User,
                null,
                authorities
        );

        // Update the security context with the new authentication
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // Store user in session if needed
        request.getSession().setAttribute("currentUser", usuario);

        // Continue with default success handling
        super.onAuthenticationSuccess(request, response, authentication);
    }
}