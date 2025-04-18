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
    @PostMapping("/afEWNOSegirAlumne")
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
    public String esborrarAlumne(@SessionAttribute("llistaAlumnes") ArrayList<Alumne> alumnes,
                                 @RequestParam(required = true) int id, Model model) {
        // Crear un nuevo Alumne solo con el id para buscarlo en la lista
        Alumne alumneToRemove = new Alumne(id);

        // Eliminar el alumno si existe
        alumnes.removeIf(alumne -> alumne.getId() == id);

        // Actualizar el modelo con la lista de alumnos
        model.addAttribute("llistaAlumnes", alumnes);
        return "alumnes/consultarAlumnes";  // Redirigir a la vista de consulta de alumnos
    }
    @PostMapping("/modificarAlumne")
    public String modificarAlumne(@SessionAttribute("llistaAlumnes") ArrayList<Alumne> alumnes,
                                  @RequestParam(required = true) int id,
                                  @RequestParam(required = true) String nom,
                                  @RequestParam(required = true) int nota,
                                  Model model) {
        // Buscar el alumno por su id
        Alumne alumneToModify = alumnes.stream()
                .filter(alumne -> alumne.getId() == id)
                .findFirst()
                .orElse(null);

        if (alumneToModify != null) {
            // Modificar los datos del alumno
            alumneToModify.setNom(nom);
            alumneToModify.setNota(nota);
        }

        // Actualizar el modelo con la lista de alumnos
        model.addAttribute("llistaAlumnes", alumnes);
        return "alumnes/consultarAlumnes";  // Redirigir a la vista de consulta de alumnos
    }

}
