package edu.fje.daw2.sm72;

import edu.fje.daw2.sm72.models.Pelicula;
import edu.fje.daw2.sm72.repositoris.PeliculaRepositori;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class Sm72Application {
	public static void main(String[] args) {
		SpringApplication.run(Sm72Application.class, args);
	}
//SI ES EL PRIMER COP I NO TENS LA BASE DE DADES INICIALITZADA.
//	@Bean
//	CommandLineRunner commandLineRunner(PeliculaRepositori peliculaRepositori) {
//		return args -> {
//			// Eliminar datos existentes para evitar duplicados
//			peliculaRepositori.deleteAll();
//
//			// Crear la lista de películas
//			List<Pelicula> peliculas = Arrays.asList(
//					new Pelicula("The Godfather", "Francis Ford Coppola", 1972, "Drama"),
//					new Pelicula("Pulp Fiction", "Quentin Tarantino", 1994, "Acción"),
//					new Pelicula("The Dark Knight", "Christopher Nolan", 2008, "Acción"),
//					new Pelicula("La Lista de Schindler", "Steven Spielberg", 1993, "Drama"),
//					new Pelicula("El Señor de los Anillos: El Retorno del Rey", "Peter Jackson", 2003, "Fantasía"),
//					new Pelicula("Forrest Gump", "Robert Zemeckis", 1994, "Drama"),
//					new Pelicula("Inception", "Christopher Nolan", 2010, "Ciencia Ficción"),
//					new Pelicula("Matrix", "Lana y Lilly Wachowski", 1999, "Ciencia Ficción"),
//					new Pelicula("Ciudad de Dios", "Fernando Meirelles", 2002, "Crimen"),
//					new Pelicula("Seven", "David Fincher", 1995, "Thriller"),
//					new Pelicula("El Silencio de los Corderos", "Jonathan Demme", 1991, "Thriller"),
//					new Pelicula("Interestelar", "Christopher Nolan", 2014, "Ciencia Ficción"),
//					new Pelicula("Gladiador", "Ridley Scott", 2000, "Acción"),
//					new Pelicula("El Rey León", "Roger Allers, Rob Minkoff", 1994, "Animación"),
//					new Pelicula("El Club de la Lucha", "David Fincher", 1999, "Drama")
//			);
//
//			// Guardar todas las películas en la base de datos
//			peliculaRepositori.saveAll(peliculas);
//
//			System.out.println("Se han insertado " + peliculas.size() + " películas en la base de datos.");
//
//			// Mostrar todas las películas insertadas
//			System.out.println("\nPelículas en la base de datos:");
//			peliculaRepositori.findAll().forEach(pelicula -> {
//				System.out.println(pelicula);
//			});
//		};
//	}


}