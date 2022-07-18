package com.teamc.bioskop.Repository;

import com.teamc.bioskop.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("Select b from Booking b where b.schedule.films.name like %:name%")
    List<Booking> getBookingByFilmName(@Param("name") String name);

}