package edu.fje.daw2.sm72.repositoris;

import edu.fje.daw2.sm72.models.ClientMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

/**
 * Interficie de persistència amb Spring Data i MongoDB.
 * @author sergi.grau@fje.edu
 * @version  1.0 4.4.2019
 */

public interface MongoRepositori extends MongoRepository<ClientMongo, String> {

    ClientMongo findByNom(String nom);
    List<ClientMongo> findByCognom(String cognom);
    List<ClientMongo> findByVolumCompres(int volumCompres);

}

