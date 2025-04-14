package edu.fje.daw2.sm72.controladors;

import edu.fje.daw2.sm72.repositoris.MongoRepositori;
import edu.fje.daw2.sm72.models.ClientMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de clients
 * Verifica el funcionament de curl
 * @author sergi.grau@fje.edu
 * @version 1.0 21.3.19
 * @version 2.0 25.3.21
 */
@Controller
@SessionAttributes("clients")
public class ClientMongoDBController {

    @Autowired
    private MongoRepositori repositori;

    @ModelAttribute("clients")
    public List<ClientMongo> inicialitzar() {

        List<ClientMongo> clients = new ArrayList<>();
        for (ClientMongo c : repositori.findAll()) {
            clients.add(c);
        }
        return clients;
    }

    @RequestMapping(value={"/client", "/usuari"})
    String mostrarFormulari() {
        return("formulari");
    }

    @RequestMapping(value="/desarClient", method = RequestMethod.POST)
    String desarClient(@SessionAttribute("clients") List<ClientMongo> clients,
                       @RequestParam (defaultValue = "") String nom,
                       @RequestParam (defaultValue = "") String cognom,
                       @RequestParam (defaultValue = "") int volumCompres,
                       ModelMap model){
        ClientMongo c = new ClientMongo(nom, cognom, volumCompres);
        repositori.save(c);

        if(!model.containsAttribute("clients")) {
            model.addAttribute("clients", clients);
        }
        clients.add(c);
        return("llistarClients");
    }

    @RequestMapping(value="/esborrarClient", method = RequestMethod.GET)
    String esborrarClient(@SessionAttribute("clients") List<ClientMongo> clients,
                          @RequestParam (defaultValue = "") String id){

        System.out.println(id);

        repositori.deleteById(id);
        ClientMongo t = new ClientMongo();
        t.setId(id);
        clients.remove(t);

        return("llistarClients");
    }

}