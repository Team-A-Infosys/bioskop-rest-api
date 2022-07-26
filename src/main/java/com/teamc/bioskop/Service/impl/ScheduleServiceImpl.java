package com.teamc.bioskop.Service.impl;

import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.Schedule;
import com.teamc.bioskop.Repository.ScheduleRepository;
import com.teamc.bioskop.Service.ScheduleService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor // kalau udah pakai AllArgsConstructor ngga perlu lagi pakai anotasi @AutoWired :D
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;
    private DataSource dataSource;

    //Get All
    public List<Schedule> getAll() {
        List<Schedule> optionalSchedule = this.scheduleRepository.findAll();
        if (optionalSchedule.isEmpty()) {
            throw new ResourceNotFoundException("Schedule not exist");
        }
        return this.scheduleRepository.findAll();
    }

    //Get By ID
    public Optional<Schedule> getScheduleById(Integer Id) throws ResourceNotFoundException {
        Optional<Schedule> optionalSchedule = this.scheduleRepository.findById(Id);
        if (optionalSchedule == null) {
            throw new ResourceNotFoundException("Schedule not exist with id : " + Id);
        }
        return this.scheduleRepository.findById(Id);
    }

    //Post
    public Schedule createSchedule(Schedule schedule) {
        return this.scheduleRepository.save(schedule);
    }

    //Update
    @Override
    public Schedule updateSchedule(Schedule schedule) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(schedule.getScheduleId());
        if (optionalSchedule == null) {
            throw new ResourceNotFoundException("User not exist with id : " + schedule.getScheduleId());
        }
        return this.scheduleRepository.save(schedule);
    }

    public Schedule getReferenceById(Integer id) {

        return this.scheduleRepository.getById(id);
    }

    //Delete
    public void deleteScheduleById(Integer Id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(Id);
        if (optionalSchedule == null) {
            throw new ResourceNotFoundException("Schedule not exist with id : " + Id);
        }
        Schedule schedule = scheduleRepository.getById(Id);
        this.scheduleRepository.delete(schedule);
    }

    //Custom Select
    @Override
    public List<Schedule> getScheduleByFilmName(String name) {
        List<Schedule> getScheduleByFilmName = this.scheduleRepository.getScheduleByFilmName(name);
        if (getScheduleByFilmName.isEmpty()) {
            throw new ResourceNotFoundException("Schedule with film name " + name + " is not exist!!!!!");
        }
        return this.scheduleRepository.getScheduleByFilmName(name);
    }

    @Override
    public List<Schedule> getScheduleByFilmNameLike(String name) {
        List<Schedule> getScheduleByFilmNameLike = this.scheduleRepository.getScheduleFilmsNameLike(name);
        if (getScheduleByFilmNameLike == null) {
            throw new ResourceNotFoundException("Schedule with film name " + name + " is not exist!!!!!");
        }
        return this.scheduleRepository.getScheduleFilmsNameLike(name);
    }

    @Override
    public Page<Schedule> findPaginated(int pageNumber, int pageSize) {
        Pageable pageable  = PageRequest.of(pageNumber-1, pageSize);

        return this.scheduleRepository.findAll(pageable);
    }

    private Connection getConnection(){
        try {
            return dataSource.getConnection();
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

        public JasperPrint generateJasperPrint() throws Exception{
            InputStream scheduleReport = new ClassPathResource("reports/Schedule-list.jasper").getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(scheduleReport);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource.getConnection());
            return jasperPrint;

        }
}