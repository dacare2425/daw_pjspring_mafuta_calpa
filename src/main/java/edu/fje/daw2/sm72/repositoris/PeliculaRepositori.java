package edu.fje.daw2.sm72.repositoris;

import edu.fje.daw2.sm72.models.Pelicula;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeliculaRepositori extends MongoRepository<Pelicula, String> {
    // Métodos adicionales si los necesitas
}