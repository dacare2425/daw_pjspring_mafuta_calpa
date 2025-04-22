package edu.fje.daw2.sm72.serveis;

import edu.fje.daw2.sm72.models.Pelicula;
import edu.fje.daw2.sm72.models.Usuario;
import edu.fje.daw2.sm72.repositoris.PeliculaRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Servei per conectar amb la base de dades de Películas, en MongoDB
 */
@Service
public class PeliculaServei {

    @Autowired
    private PeliculaRepositori peliculaRepositori;
    private final String OMDB_API_KEY = "8ce64598";
    private final String OMDB_URL = "https://www.omdbapi.com/";
    private final String DEFAULT_IMAGE = "https://dummyimage.com/400x600/000/fff.png&text=No+Poster";

    public Pelicula guardar(Pelicula pelicula) {
        // Establecer ID a null si está vacío para que MongoDB genere uno automáticamente
        if (pelicula.getId() != null && pelicula.getId().isEmpty()) {
            System.out.println("ID vacío detectado, estableciendo a null para generación automática");
            pelicula.setId(null);
        }

//        System.out.println("Guardando película: " + pelicula.getTitulo() + ", ID antes: " + pelicula.getId());
        Pelicula guardada = peliculaRepositori.save(pelicula);
//        System.out.println("Película guardada, ID después: " + guardada.getId());
        return guardada;
    }

    public List<Pelicula> obtenerTodas() {
        List<Pelicula> peliculas = peliculaRepositori.findAll();
        for (Pelicula pelicula : peliculas) {
            buscarPosterParaPelicula(pelicula);
        }
        return peliculas;
    }

    public Pelicula obtenirPerId(String id) {
        Pelicula pelicula = peliculaRepositori.findById(id).orElse(null);
        if (pelicula != null) {
            buscarPosterParaPelicula(pelicula);
        }
        return pelicula;
    }

    /**
     * Método para buscar el póster de una película usando la API de OMDB
     * Utiliza el mismo enfoque que el script JS proporcionado
     */
    private void buscarPosterParaPelicula(Pelicula pelicula) {
        try {
            // Usar directamente la búsqueda por título exacto primero
            String tituloEncoded = UriUtils.encode(pelicula.getTitulo(), "UTF-8");
            String titleUrl = OMDB_URL + "?t=" + tituloEncoded + "&apikey=" + OMDB_API_KEY;
            if (pelicula.getAnyo() != null) {
                titleUrl += "&y=" + pelicula.getAnyo();
            }

            RestTemplate restTemplate = new RestTemplate();
            String titleResponse = restTemplate.getForObject(titleUrl, String.class);
            JSONObject titleJson = new JSONObject(titleResponse);

            // Verificar si la búsqueda por título exacto fue exitosa
            if (titleJson.has("Response") && titleJson.getString("Response").equals("True") &&
                    titleJson.has("Poster") && !titleJson.getString("Poster").equalsIgnoreCase("N/A")) {
                pelicula.setPosterUrl(titleJson.getString("Poster"));
                System.out.println("Póster encontrado por título exacto para " + pelicula.getTitulo() + ": " + pelicula.getPosterUrl());
                return;
            }

            // Si no se encontró por título exacto, intentar búsqueda general
            String searchUrl = OMDB_URL + "?s=" + tituloEncoded + "&apikey=" + OMDB_API_KEY;
            if (pelicula.getAnyo() != null) {
                searchUrl += "&y=" + pelicula.getAnyo();
            }

            String searchResponse = restTemplate.getForObject(searchUrl, String.class);
            JSONObject searchJson = new JSONObject(searchResponse);

            // Verificar si la búsqueda general fue exitosa
            if (searchJson.has("Search") && searchJson.getJSONArray("Search").length() > 0) {
                // Obtener el primer resultado
                JSONArray results = searchJson.getJSONArray("Search");
                JSONObject firstResult = results.getJSONObject(0);

                if (firstResult.has("Poster") && !firstResult.getString("Poster").equalsIgnoreCase("N/A")) {
                    pelicula.setPosterUrl(firstResult.getString("Poster"));
                    System.out.println("Póster encontrado por búsqueda general para " + pelicula.getTitulo() + ": " + pelicula.getPosterUrl());
                } else if (firstResult.has("imdbID")) {
                    // Si no tiene póster pero tiene ID de IMDB, intentar obtener detalles
                    String imdbId = firstResult.getString("imdbID");
                    String detailUrl = OMDB_URL + "?i=" + imdbId + "&apikey=" + OMDB_API_KEY;
                    String detailResponse = restTemplate.getForObject(detailUrl, String.class);
                    JSONObject detailJson = new JSONObject(detailResponse);

                    if (detailJson.has("Poster") && !detailJson.getString("Poster").equalsIgnoreCase("N/A")) {
                        pelicula.setPosterUrl(detailJson.getString("Poster"));
                        System.out.println("Póster encontrado por ID de IMDB para " + pelicula.getTitulo() + ": " + pelicula.getPosterUrl());
                    } else {
                        pelicula.setPosterUrl(DEFAULT_IMAGE);
                        System.out.println("No se encontró póster por ID de IMDB para " + pelicula.getTitulo());
                    }
                } else {
                    pelicula.setPosterUrl(DEFAULT_IMAGE);
                    System.out.println("No se encontró póster en el primer resultado para " + pelicula.getTitulo());
                }
            } else {
                pelicula.setPosterUrl(DEFAULT_IMAGE);
                System.out.println("No se encontraron resultados de búsqueda para " + pelicula.getTitulo());
            }
        } catch (Exception e) {
            pelicula.setPosterUrl(DEFAULT_IMAGE);
            System.out.println("Error buscando póster para " + pelicula.getTitulo() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminar(String id) {
        peliculaRepositori.deleteById(id);
    }

    /**
     * Obtiene las películas seleccionadas por un usuario
     */
    public List<Pelicula> obtenerPeliculasDeUsuario(Usuario usuario) {
        if (usuario == null || usuario.getPeliculasSeleccionadas() == null || usuario.getPeliculasSeleccionadas().isEmpty()) {
            return new ArrayList<>();
        }

        List<Pelicula> peliculas = peliculaRepositori.findAllById(usuario.getPeliculasSeleccionadas());
        for (Pelicula pelicula : peliculas) {
            buscarPosterParaPelicula(pelicula);
        }
        return peliculas;
    }
}