package edu.fje.daw2.sm72.controladors;

import edu.fje.daw2.sm72.models.Alumne;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Controlador de la gestió d'Aumnes
@Controller
@SessionAttributes("llistaAlumnes")
public class AlumnesController {

    @ModelAttribute("llistaAlumnes")
    public List<Alumne> inicializarLlistaAlumnes() {
        return new ArrayList<>();
    }
    //private ArrayList<Alumne> alumnes = new ArrayList<Alumne>();
    private int numId=1;
    @PostMapping("/afegirAlumne")
    public String afegirAlumne(
            @SessionAttribute("llistaAlumnes") ArrayList<Alumne> alumnes,
            @RequestParam(required = true) String nom,
            @RequestParam(required = true) int nota,
            Model model) {
        Alumne alumne = new Alumne(numId++, nom, nota);
        alumnes.add(alumne);
        model.addAttribute("alumneAfegit", alumne);
        model.addAttribute("llistaAlumnes", alumnes);
        return "alumnes/afegirAlumne";
    }
    @GetMapping("/consultarAlumnes")
    public String consultarAlumnes(Model model) {
        //model.addAttribute("llistaAlumnes", alumnes);
        return "alumnes/consultarAlumnes";
    }
    @PostMapping("/esborrarAlumne")
    public String esborrarAlumne(@SessionAttribute("llistaAlumnes") ArrayList<Alumne> alumnes, @RequestParam(required = true) int id, Model model) {
        alumnes.remove(new Alumne(id));
//        model.addAttribute("llistaAlumnes", alumnes);
        return "alumnes/consultarAlumnes";
    }
    @PostMapping("/modificarAlumne")
    public String modificarAlumne(@SessionAttribute("llistaAlumnes") ArrayList<Alumne> alumnes,
                                  @RequestParam(required = true) int id,
                                  @RequestParam(required = true) String nom,
                                  @RequestParam(required = true) int nota,
                                  Model model) {
        Alumne alumne = alumnes.get(alumnes.indexOf(new Alumne(id)));
        alumne.setNom(nom);
        alumne.setNota(nota);
//        model.addAttribute("llistaAlumnes", alumnes);
        return "alumnes/consultarAlumnes";
    }

}
