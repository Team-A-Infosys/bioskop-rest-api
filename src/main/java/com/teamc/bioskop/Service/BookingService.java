package com.teamc.bioskop.Service;

import com.teamc.bioskop.Model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    List<Booking> getAll();

    Optional<Booking> getBookingById(Long Id);

    Booking createBooking(Booking booking, Principal principal);

    void deleteSBookingById(Long Id);

    Booking updateBooking(Booking booking);

    Booking getReferenceById(Long Id);

    List<Booking> getBookingByFilmName(String name);

    Page<Booking> findPaginated(int pageNumber, int pageSize);

}