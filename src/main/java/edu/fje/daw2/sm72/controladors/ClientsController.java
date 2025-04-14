package edu.fje.daw2.sm72.controladors;
import edu.fje.daw2.sm72.serveis.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de clients
 * Verifica el funcionament de curl
 * @author sergi.grau@fje.edu
 * @version 1.0 21.3.19
 * @version 2.0 25.3.24
 * @version 3.0 Corregido error de nombre de parámetro
 */
@Controller
@SessionAttributes("clients")
public class ClientsController {
    @Autowired
    private ClientService cs;

    @RequestMapping(value={"/client", "/usuari"})
    String mostrarFormulari() {
        return("clients/formulari");
    }

    @GetMapping(value = {"/esborrarClient"})
    String mostrarFormulariEsborrat() {
        return ("clients/formulariEsborrar");
    }

    @RequestMapping(value = {"/canviarClient"})
    String mostrarFormulariModificar() {
        return ("clients/formulariModificar");
    }

    @GetMapping("/llistarClients")
    public String llistarUsuaris(Model model) {
        model.addAttribute("clients", cs.trobarTots());
        return "clients/llistarClients";
    }

    @PostMapping("/esborrarClient")
    public String esborrarClient(
            @RequestParam String id,
            Model model) {

        Long idClient = Long.parseLong(id);
        cs.eliminarClient(idClient);
        model.addAttribute("clients", cs.trobarTots());
        return "clients/llistarClients";
    }

    @PostMapping("/afegirClient")
    public String afegirClient(
            @RequestParam String nom,
            @RequestParam String cognom,
            @RequestParam(required = false, defaultValue = "0") Integer volumCompres,
            Model model) {

        cs.afegirClient(nom, cognom, volumCompres);
        model.addAttribute("clients", cs.trobarTots());
        return "clients/llistarClients";
    }

    @PostMapping("/modificarClient")
    public String modificarClient(
            @RequestParam String id,
            @RequestParam String nom,
            @RequestParam String cognom,
            @RequestParam(required = false, defaultValue = "0") Integer volumCompres,
            Model model) {

        Long idClient = Long.parseLong(id);
        cs.modificarClient(idClient, nom, cognom, volumCompres);
        model.addAttribute("clients", cs.trobarTots());
        return "clients/llistarClients";
    }
}
