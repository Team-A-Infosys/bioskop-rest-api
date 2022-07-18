package com.teamc.bioskop.DTO;

import com.teamc.bioskop.Model.Films;
import com.teamc.bioskop.Model.Schedule;
import com.teamc.bioskop.Model.Seats;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequestDTO {

    private Integer scheduleId;
    private Films films;
    private Seats seats;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateShow;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime showStart;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime showEnd;
    private Integer price;

    public Schedule convertToEntity() {
        return Schedule.builder().scheduleId(this.scheduleId).films(this.films).seats(this.seats)
                .dateShow(this.dateShow).showStart(this.showStart)
                .showEnd(this.showEnd).price(this.price)
                .build();
    }


}