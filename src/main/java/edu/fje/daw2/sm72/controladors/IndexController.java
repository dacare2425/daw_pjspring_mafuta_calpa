package edu.fje.daw2.sm72.controladors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class IndexController {
    private ArrayList<Integer> llistadeNombres = new ArrayList<>();
    @GetMapping("/salutacio")
    public String salutar(
            @RequestParam(defaultValue = "sergi", required = false) String nom,
            Model model) {
        System.out.println("NOM: " + nom);
        model.addAttribute("usuari", nom);
        return "salutacio";
    }

    @GetMapping("/sumar")
    public String sumar(
            @RequestParam(defaultValue = "0", required = false) int n1,
            @RequestParam(defaultValue = "0", required = false) int n2,
            Model model) {
        llistadeNombres.add(n1);
        llistadeNombres.add(n2);
        model.addAttribute("llista", llistadeNombres);
        model.addAttribute("n1", n1);
        model.addAttribute("n2", n2);
        return "suma";
    }

}