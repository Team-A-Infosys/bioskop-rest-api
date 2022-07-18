package com.teamc.bioskop.Repository;

import com.teamc.bioskop.Model.Seats;
import com.teamc.bioskop.Model.StatusSeats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatsRepository extends JpaRepository<Seats, Long> {
    @Query(value = "select * from seats s where is_available =?1", nativeQuery = true)
    List<Seats> getSeatAvailable(StatusSeats isAvailable);

//    @Query(value = "select * from seats s where studio_name =?1", nativeQuery=true)
    public List<Seats> findByStudioName(String studioName);

    Page<Seats> findSeatsByIsAvailable(StatusSeats isAvailable, Pageable pageable);

}