package edu.fje.daw2.sm72.serveis;

import edu.fje.daw2.sm72.models.Pelicula;
import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.repositoris.UsuarioRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
