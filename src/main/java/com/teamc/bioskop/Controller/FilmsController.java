package com.teamc.bioskop.Controller;

import com.teamc.bioskop.Model.Films;
import com.teamc.bioskop.Response.ResponseHandler;
import com.teamc.bioskop.Service.FilmsService;
import com.teamc.bioskop.Service.ReportPDFService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


@RestController
@AllArgsConstructor
public class FilmsController {

    private static final Logger logger = LogManager.getLogger(FilmsController.class);
    private static final String Line = "====================";

    private final FilmsService filmsService;

    private ReportPDFService reportPDFService;

    private HttpServletResponse response;

    /**
     * GET ALL FILM
     */
    @GetMapping("/films")
    public ResponseEntity<Object> getAllFilm() {
        try {
            List<Films> result = filmsService.findAllFilms();
            List<Map<String, Object>> maps = new ArrayList<>();
            logger.info(Line + " Logger Start Get All Films " + Line);
            for (Films filmsResult : result) {
                Map<String, Object> film = new HashMap<>();
                logger.info(Line);
                film.put("Judul", filmsResult.getName());
                logger.info("Judul: " + filmsResult.getName());
                film.put("Status Film", filmsResult.getIsPlaying());
                logger.info("Status Film: " + filmsResult.getIsPlaying());
                logger.info(Line);
                maps.add(film);
            }
            logger.info(Line + " Logger End Get All Films " + Line);
            return ResponseHandler.generateResponse("Sukses Get All Films", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table Has No Value!"));
            logger.info("==================== Logger End Get All Users     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table Has No Value!");
        }
    }

    /**
     * GET FILM BY ID
     */
    @GetMapping("/film/{filmId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long filmId) {
        try {
            Optional<Films> result = filmsService.findbyId(filmId);
            logger.info(Line + " Logger Start SearchById " + Line);
            logger.info(result);
            logger.info(Line + " Logger End SearchById " + Line);
            return ResponseHandler.generateResponse("Sukses GetSearchById", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!"));
            logger.info("==================== Logger End Get All Users     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    /**
     * CREATE FILM
     */
    @PostMapping("/dashboard/create/film")
    public ResponseEntity<Object> createFilm(@RequestBody Films films) {
        try {
            Films result = filmsService.createFilm(films);
            logger.info(Line + " Logger Start Create " + Line);
            logger.info(result);
            logger.info(Line + " Logger End Create " + Line);
            return ResponseHandler.generateResponse("Sukses Create", HttpStatus.CREATED, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Film Already Exist!"));
            logger.info("==================== Logger End Get All Users     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Film Already Exist!");
        }
    }

    /**
     * UPDATE FILM by ID
     */
    @PutMapping("/dashboard/update/film/{filmId}")
    public ResponseEntity<Object> updateFilms(@PathVariable(value = "filmId") Long filmId, @RequestBody Films filmsdetails) {
        try {
            Films films = filmsdetails;
            films.setFilmId(filmId);
            Films filmsUpdate = filmsService.updateFilm(films, filmId);
            logger.info(Line + " Logger Start Update " + Line);
            logger.info(filmsUpdate);
            logger.info(Line + " Logger End Update " + Line);
            return ResponseHandler.generateResponse("Sukses Update", HttpStatus.CREATED, filmsUpdate);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!"));
            logger.info("==================== Logger End Get All Users     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    /**
     * DELETE FILM by ID
     */
    @DeleteMapping("/dashboard/delete/film/{filmId}")
    public ResponseEntity<Object> deleteFilms(@PathVariable Long filmId) {
        try {
            filmsService.deleteFilmById(filmId);
            Map<String, Boolean> respone = new HashMap<>();
            respone.put("deleted", Boolean.TRUE);
            ResponseEntity<Map<String, Boolean>> delete = ResponseEntity.ok(respone);
            logger.info(Line + " Logger Delete " + Line);
            logger.info(delete);
            logger.info(Line + " Logger Delete " + Line);
            return ResponseHandler.generateResponse("Sukses Delete", HttpStatus.OK, delete);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!"));
            logger.info("==================== Logger End Get All Users     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    /**
     * FIND FILM
     * custom Challange 4 slide 8 nomor 1
     */
    @PostMapping("/search/films/status")
    public ResponseEntity<Object> ShowMovie(@RequestBody Films films) {
        try {
            List<Films> result = filmsService.getByIsPlaying(films.getIsPlaying());
            List<Map<String, Object>> maps = new ArrayList<>();
            logger.info(Line + " Logger Start Get All Films " + Line);
            for (Films filmsResult : result) {
                Map<String, Object> film = new HashMap<>();
                logger.info(Line);
                film.put("Judul", filmsResult.getName());
                logger.info("Judul: " + filmsResult.getName());
                film.put("Status Film", filmsResult.getIsPlaying());
                logger.info("Status Film: " + filmsResult.getIsPlaying());
                logger.info(Line);
                maps.add(film);
            }
            logger.info(Line + " Logger End Get All Films " + Line);
            return ResponseHandler.generateResponse("Sukses Search Show Movie", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!"));
            logger.info("==================== Logger End Get All Users     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }

    @GetMapping("/dashboard/print/film")
    public void printReport() throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"films_list.pdf\"");
        JasperPrint jasperPrint = this.reportPDFService.generateJasperPrint();
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();

    }
}