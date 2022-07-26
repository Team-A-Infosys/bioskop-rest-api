package com.teamc.bioskop.Response;

import com.teamc.bioskop.Model.Enum.StatusFilms;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmsResponseDTO {
    private Long code;
    private String title;
    private StatusFilms status;

    @Override
    public String toString() {
        return "FilmsResponseDTO{" +
                "code=" + code +
                ", title='" + title + '\'' +
                ", status=" + status +
                '}';
    }
}