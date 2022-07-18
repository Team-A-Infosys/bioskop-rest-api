package com.teamc.bioskop.Service;

import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.Seats;
import com.teamc.bioskop.Model.StatusSeats;
import com.teamc.bioskop.Repository.SeatsRepository;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SeatsServiceImpl implements SeatsService {

    private final SeatsRepository seatRepository;

    private DataSource dataSource;


    @Override
    public List<Seats> findAllseats() {

        List<Seats> optionalSeats = seatRepository.findAll();
        if (optionalSeats.isEmpty()) {
            throw new ResourceNotFoundException("table seats have not value");
        }
        return seatRepository.findAll();
    }

    @Override
    public Optional<Seats> findbyid(Long id) {

        Optional<Seats> optionalSeats = seatRepository.findById(id);
        if (optionalSeats == null) {
            throw new ResourceNotFoundException(" Seats not Exist with id :" + id);
        }
        return seatRepository.findById(id);
    }

    @Override
    public Seats createseat(Seats seat) {
        return seatRepository.save(seat);
    }

    @Override
    public Seats updateseat(Seats seat, Long seatId) {
        Optional<Seats> optionalSeats = seatRepository.findById(seatId);
        if (optionalSeats == null) {
            throw new ResourceNotFoundException("Films not exist with id" + seatId);
        }
        return seatRepository.save(seat);
    }

    @Override
    public void deleteseat(Long seatId) {
        Optional<Seats> optionalSeats = seatRepository.findById(seatId);
        if (optionalSeats == null) {
            throw new ResourceNotFoundException("Seats not exist with id :" + seatId);
        }
        Seats seats = seatRepository.getReferenceById(seatId);
        this.seatRepository.delete(seats);

    }


    public Seats getReferenceById(Long id) {
        return this.seatRepository.getReferenceById(id);
    }

    @Override
    public List<Seats> getSeatAvailable(StatusSeats isAvailable) {

        List<Seats> optionalSeats = seatRepository.getSeatAvailable(isAvailable);
        if (optionalSeats == null) {
            throw new ResourceNotFoundException("Seats not exist with id : " + isAvailable);
        }
        return this.seatRepository.getSeatAvailable(isAvailable);
    }

    @Override
    public List<Seats> getSeatsByStudioName(String studioName){
        List<Seats> optionStudio = seatRepository.findByStudioName(studioName);

        return optionStudio;
    }

    @Override
    public Page<Seats> findPaginated(int pageNumber, int pageSize, String sortStudio, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortStudio).ascending() :
                Sort.by(sortStudio).descending();

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);

        return this.seatRepository.findAll(pageable);
    }

    @Override
    public Page<Seats> findPaginatedByStatus(StatusSeats isAvailable, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.seatRepository.findSeatsByIsAvailable(isAvailable, pageable);
    }

    private Connection getConnection(){
        try{
            return dataSource.getConnection();
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public JasperPrint generateJReport() throws Exception{
        InputStream seatsReport = new ClassPathResource("reports/Daftar Seats.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(seatsReport);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource.getConnection());

        return jasperPrint;
    }
}