package com.teamc.bioskop.Service;

import com.teamc.bioskop.Model.Seats;
import com.teamc.bioskop.Model.Enum.StatusSeats;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SeatsService {

    List<Seats> findAllseats();

    Optional<Seats> findbyid(Long id);

    Seats createseat(Seats seat);

    Seats updateseat(Seats seat, Long seatId);

    void deleteseat(Long seatId);

    Seats getReferenceById(Long id);

    List<Seats> getSeatAvailable(StatusSeats isAvailable);
    List<Seats> getSeatsByStudioName(String studioName);
    Page<Seats> findPaginated(int pageNumber, int pageSize, String sortStudio, String sortDir);
    Page<Seats> findPaginatedByStatus(StatusSeats isAvailable, int pageNo, int pageSize);

}