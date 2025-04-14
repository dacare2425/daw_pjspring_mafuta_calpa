package edu.fje.daw2.sm72.serveis;

import edu.fje.daw2.sm72.models.Client;
import edu.fje.daw2.sm72.repositoris.ClientRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servei JPA, Aquesta és la classe de servei.
 * La classe proporciona mètodes per gestionar clients a la base de dades.
 *
 * @author sergi.grau@fje.edu
 * @version 1.0 08.04.21
 * @version 2.0 actualitzat amb nous mètodes
 */

@Service
public class ClientService {

    @Autowired
    private ClientRepositori cr;

    public List<Client> trobarTots() {
        var it = cr.findAll();
        var clients = new ArrayList<Client>();
        it.forEach(clients::add);
        return clients;
    }

    public Long comptar() {
        return cr.count();
    }

    public void eliminarClient(Long clientId) {
        cr.deleteById(clientId);
    }

    public Client trobarPerId(Long id) {
        return cr.findById(id).get();
    }

    public void afegirClient(String nom, String cognom, Integer volumCompres) {
        var client = new Client(nom, cognom, volumCompres);
        cr.save(client);
    }

    public void afegirClient(Client client) {
        cr.save(client);
    }

    /**
     * Mètode per modificar un client existent
     */
    public void modificarClient(Long id, String nom, String cognom, Integer volumCompres) {
        Client client = trobarPerId(id);
        client.setNom(nom);
        client.setCognom(cognom);
        client.setVolumCompres(volumCompres);
        cr.save(client);
    }
}
