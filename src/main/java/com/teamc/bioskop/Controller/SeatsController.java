package com.teamc.bioskop.Controller;

import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.Seats;
import com.teamc.bioskop.Response.ResponseHandler;
import com.teamc.bioskop.Service.SeatsService;
import com.teamc.bioskop.Service.SeatsServiceImpl;
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
import javax.validation.Valid;
import java.util.*;

@RestController
@AllArgsConstructor
public class SeatsController {

    private static final Logger logger = LogManager.getLogger(SeatsController.class);
    private HttpServletResponse httpServletResponse;
    private static final String Line = "============================================================";
    private SeatsServiceImpl seatsService;

    /**
     * GET ALL
     */
    @GetMapping("/seats")
    public ResponseEntity<Object> getSeats() {
        try {
            List<Seats> result = seatsService.findAllseats();
            List<Map<String, Object>> maps = new ArrayList<>();
            logger.info(Line + " Logger Start Get All Films " + Line);
            for (Seats SeatAvailable : result) {
                Map<String, Object> seat = new HashMap<>();
                logger.info(Line);
                seat.put("No Kursi", SeatAvailable.getSeatNumber());
                logger.info("No Kursi: " + SeatAvailable.getSeatNumber());
                seat.put("Studio", SeatAvailable.getStudioName());
                logger.info("Studio: " + SeatAvailable.getStudioName());
                seat.put("Available", SeatAvailable.getIsAvailable());
                logger.info("Available: " + SeatAvailable.getIsAvailable());
                logger.info(Line);
                maps.add(seat);
            }
            logger.info(Line + " Logger END Get All Films " + Line);
            return ResponseHandler.generateResponse("Succesfully Read All Data Seats !", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table Has No Value!"));
            logger.info("==================== Logger End Get All Users     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table has no value");
        }

    }

    /**
     * GET SEAT BY ID
     */
    @GetMapping("/seats/{seatId}")
    public ResponseEntity<Object> getseatsById(@PathVariable Long seatId) {
        try {
            Optional<Seats> seats = seatsService.findbyid(seatId);
            logger.info(Line + " Logger Start Create " + Line);
            logger.info(seats);
            logger.info(Line + " Logger END Create " + Line);
            return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, seats);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get By Id    ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table Has No Value!"));
            logger.info("==================== Logger End Get By Id      ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found");
        }
        //Seats seat = seatsService.findbyid(seatId).orElseThrow(() ->
        //new ResourceNotFoundException("seats not exit with Seats_number:" + seatId));
    }

    /**
     * CREATE BY ID
     */
    @PostMapping("/dashboard/create/seats")
    public ResponseEntity<Object> createseats(@RequestBody Seats seat) {
        try {
            Seats result = seatsService.createseat(seat);
            logger.info(Line + " Logger Start Create " + Line);
            logger.info(result);
            logger.info(Line + " Logger END GCreate " + Line);
            return ResponseHandler.generateResponse("Succesfully Add Data Seats !", HttpStatus.CREATED, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Create     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table Has No Value!"));
            logger.info("==================== Logger End Create     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    /**
     * UPDATE BY ID
     */
    @PutMapping("/dashboard/update/seats/{seatId}")
    public ResponseEntity<Object> updateSeats(@PathVariable(value = "seatId") Long seatId, @Valid @RequestBody Seats seatsDetails) {
        try {
            Seats seat = seatsService.findbyid(seatId).orElseThrow(() ->
                    new ResourceNotFoundException("seats not exit with Seats_number:" + seatsDetails));

            //seat.setSeatId(seatsDetails.getSeatId());
            seat.setStudioName(seatsDetails.getStudioName());
            seat.setSeatNumber(seatsDetails.getSeatNumber());
            seat.setIsAvailable(seatsDetails.getIsAvailable());
            //Optional<Seats> updatedseats = seatsService.updateseat(seat, seatId);
            Seats updatedseats = seatsService.updateseat(seat, seatId);
            logger.info(Line + " Logger Start Update " + Line);
            logger.info(seatsService.updateseat(seat, seatId));
            logger.info(Line + " Logger END Update " + Line);
            return ResponseHandler.generateResponse("Success Update Seats", HttpStatus.CREATED, updatedseats);
        } catch (Exception e) {
            logger.info("==================== Logger Start Update     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table Has No Value!"));
            logger.info("==================== Logger End Update     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "data not found");
        }

    }

    /**
     * DELETE BY ID
     */
    @DeleteMapping("/dashboard/delete/seats/{seatId}")
    public ResponseEntity<Object> deleteseats(@PathVariable Long seatId) {
        try {
            seatsService.deleteseat(seatId);
            Map<String, Boolean> respone = new HashMap<>();
            respone.put("deleted", Boolean.TRUE);
            ResponseEntity<Map<String, Boolean>> delete = ResponseEntity.ok(respone);
            logger.info(Line + " Logger Start Delete " + Line);
            logger.info(delete);
            logger.info(Line + " Logger END Delete " + Line);
            return ResponseHandler.generateResponse("Deleted! ", HttpStatus.OK, delete);
        } catch (Exception e) {
            logger.info("==================== Logger Start Delete     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!"));
            logger.info("==================== Logger End Delete     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found");
        }

//        Seats seat = seatsService.findbyid(seatId).orElseThrow(() ->
//                new ResourceNotFoundException("seats not exit with seatId:" + seatId));

    }

    /**
     * Seat Available
     * custom challange 4 slide 8 nomor 3 update map
     */
    @PostMapping("/search/seats/isAvailable")
    public ResponseEntity<Object> findSeats(@RequestBody Seats seats) {
        try {
            List<Seats> result = seatsService.getSeatAvailable(seats.getIsAvailable());
            List<Map<String, Object>> maps = new ArrayList<>();
            logger.info(Line + " Logger Start Get Available Seat " + Line);
            for (Seats SeatAvailable : result) {
                Map<String, Object> seat = new HashMap<>();
                logger.info(Line);
                seat.put("No Kursi", SeatAvailable.getSeatNumber());
                logger.info("No Kursi: " + SeatAvailable.getSeatNumber());
                seat.put("Studio", SeatAvailable.getStudioName());
                logger.info("Studio: " + SeatAvailable.getStudioName());
                seat.put("Available", SeatAvailable.getIsAvailable());
                logger.info("Available: " + SeatAvailable.getIsAvailable());
                logger.info(Line);
                maps.add(seat);
            }
            logger.info(Line + " Logger End Get Available Seat " + Line);
            return ResponseHandler.generateResponse("Successfully seats is Available ! ", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get Available Seat     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table Has No Value!"));
            logger.info("==================== Logger End Get Available Seat     ====================");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found");
        }

    }
    @GetMapping("/dashboard/print/seats")
    public void printReportSeat() throws Exception{
        httpServletResponse.setContentType("application/pdf");
        httpServletResponse.setHeader("Content-Disposition","attachment; filename=\"Seats Report.pdf\"");

        JasperPrint jasperPrint = this.seatsService.generateJReport();
        JasperExportManager.exportReportToPdfStream(jasperPrint,httpServletResponse.getOutputStream());
        httpServletResponse.getOutputStream().flush();
        httpServletResponse.getOutputStream().close();
    }

}