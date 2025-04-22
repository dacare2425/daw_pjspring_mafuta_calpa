package edu.fje.daw2.sm72.controladors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador per manegar la ruta index
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
