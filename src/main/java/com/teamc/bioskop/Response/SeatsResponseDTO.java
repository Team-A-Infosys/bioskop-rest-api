package com.teamc.bioskop.Response;


import com.teamc.bioskop.Model.StatusSeats;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatsResponseDTO {
    private Long code;
    private StatusSeats status;
    private String studio;
    private Long seat;

    public String toString() {
        return "SeatsResponseDTO{" +
                "code=" + code +
                ", status=" + status +
                ", studio=" + studio +
                ", seat=" + seat +
                '}';
    }
}