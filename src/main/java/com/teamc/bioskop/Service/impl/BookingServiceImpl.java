package com.teamc.bioskop.Service.impl;

import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.Booking;
import com.teamc.bioskop.Model.Role;
import com.teamc.bioskop.Model.User;
import com.teamc.bioskop.Repository.BookingRepository;
import com.teamc.bioskop.Repository.RoleRepository;
import com.teamc.bioskop.Repository.UserRepository;
import com.teamc.bioskop.Service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;

    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Override
    public List<Booking> getAll() {
        List<Booking> optionalBooking = bookingRepository.findAll();
        if (optionalBooking.isEmpty()) {
            throw new ResourceNotFoundException("Data Booking not exist");
        }
        return this.bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getBookingById(Long Id) throws ResourceNotFoundException {
        Optional<Booking> optionalBooking = bookingRepository.findById(Id);
        if (optionalBooking.isPresent()) {
            return this.bookingRepository.findById(Id);
        } else {
            throw new ResourceNotFoundException("Booking not exist with id " + Id);
        }
    }

    @Override
    public Booking updateBooking(Booking booking) throws ResourceNotFoundException {
        Optional<Booking> optionalBooking = bookingRepository.findById(booking.getBookingId());
        if (optionalBooking.isPresent()) {
            return this.bookingRepository.save(booking);
        } else {
            throw new ResourceNotFoundException("Booking not exist with id " + booking.getBookingId());
        }
    }

    @Override
    public Booking getReferenceById(Long Id) {
        return this.bookingRepository.getById(Id);
    }

    @Override
    public Booking createBooking(Booking booking, Principal principal) {
        // Can Use Authentication on parameter, then String username = authentication.getName();
        // If using authentication, so we can get authorities from the user

        String username = principal.getName();
        User user = this.userRepository.findByUsername(username);

//        booking.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Tokyo")));
//        booking.setUpdatedAt(ZonedDateTime.now(ZoneId.of("Asia/Tokyo")));

        if (principal == null){
            throw new ResourceNotFoundException("Login First");
        }
        if (booking.getUser() == null){
            booking.setUser(user);
        } else if(!(user.getRoles().contains(this.roleRepository.findByName("ROLE_ADMIN")))) {
            throw new ResourceNotFoundException("Not authenticated as an admin");
        }
        return this.bookingRepository.save(booking);
    }

    @Override
    public void deleteSBookingById(Long Id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(Id);
        this.bookingRepository.delete(optionalBooking.get());
    }

    //custom select
    @Override
    public List<Booking> getBookingByFilmName(String name) {
        List<Booking> optionalBooking = bookingRepository.getBookingByFilmName(name);
        if (optionalBooking.isEmpty()) {
            throw new ResourceNotFoundException("Booking not exist with Filmname " + name);
        }
        return this.bookingRepository.getBookingByFilmName(name);
    }

    @Override
    public Page<Booking> findPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);

        return this.bookingRepository.findAll(pageable);
    }

}