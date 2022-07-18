package com.teamc.bioskop.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamc.bioskop.Response.ScheduleResponseDTO;
import com.teamc.bioskop.Response.ScheduleResponseFilmSeatDTO;
import com.teamc.bioskop.Response.ScheduleResponseNameLikeDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "schedules")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer scheduleId;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Films films;

    @Column(name = "date_show")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateShow;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seats seats;

    @Column(name = "show_start")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime showStart;

    @Column(name = "show_end")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime showEnd;

    @Column(name = "price")
    private Integer price;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    public ScheduleResponseDTO convertToResponse() {
        return ScheduleResponseDTO.builder()
                .scheduleId(this.scheduleId).films(this.films)
                .seats(this.seats).dateShow(this.dateShow)
                .showStart(this.showStart).showEnd(this.showEnd).price(this.price).build();
//                .createdAt(this.createdAt).updatedAt(this.updatedAt).build();
    }

    public ScheduleResponseFilmSeatDTO convertToResponseFilmsSeat() {
        return ScheduleResponseFilmSeatDTO.builder()
                .scheduleId(this.scheduleId)
                .filmId(this.films.getFilmId())
                .seatId(this.seats.getSeatId())
                .dateShow(this.dateShow)
                .showStart(this.showStart)
                .showEnd(this.showEnd)
                .price(this.price).build();
//                .updatedAt(this.updatedAt)
//                .createdAt(this.createdAt)

    }

    public ScheduleResponseNameLikeDTO convertToResponseNameLike() {
        return ScheduleResponseNameLikeDTO.builder()
                .filmName(this.getFilms().getName())
                .studioName(this.getSeats().getStudioName())
                .price(this.price)
                .build();
    }

    @Override
    public String toString() {
        return "\n Schedule{" +
                "scheduleId=" + scheduleId +
                ", films=" + films +
                ", dateShow=" + dateShow +
                ", seats=" + seats +
                ", showStart=" + showStart +
                ", showEnd=" + showEnd +
                ", price=" + price +
                '}';
    }
}


//bikin constructor kosong (bisa diganti dengan penggunaan anotasi @NoArgsConstructor)
//bikin constructor method untuk semua property (bisa diganti dengan penggunaan anotasi @AllArgsConstructor)
//bikin setter getter (bisa diganti dengan penggunaan anotasi @SetterGetter)