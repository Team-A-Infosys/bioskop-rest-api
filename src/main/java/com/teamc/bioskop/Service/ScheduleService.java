package com.teamc.bioskop.Service;

import com.teamc.bioskop.Model.Films;
import com.teamc.bioskop.Model.Schedule;
import com.teamc.bioskop.Model.StatusFilms;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;


public interface ScheduleService {

    List<Schedule> getAll();

    Optional<Schedule> getScheduleById(Integer Id);

    Schedule createSchedule(Schedule schedule);

    void deleteScheduleById(Integer Id);

    Schedule updateSchedule(Schedule schedule);

    Schedule getReferenceById(Integer id);

    List<Schedule> getScheduleByFilmName(String name);

    List<Schedule> getScheduleByFilmNameLike(String name);

    Page<Schedule> findPaginated(int pageNumber, int pageSize);


}