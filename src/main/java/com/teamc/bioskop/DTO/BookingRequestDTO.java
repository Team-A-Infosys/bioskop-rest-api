package com.teamc.bioskop.DTO;

import com.teamc.bioskop.Model.Booking;
import com.teamc.bioskop.Model.Schedule;
import com.teamc.bioskop.Model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDTO {
    private Long userId;
    private Integer scheduleId;

}