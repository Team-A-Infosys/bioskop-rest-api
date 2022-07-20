package com.teamc.bioskop;

import com.teamc.bioskop.Model.User;
import com.teamc.bioskop.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class BioskopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BioskopApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserService userService){
        return args ->{
        userService.createUser(new User(null, "harist", "harist@gmail.com", "1234", new ArrayList<>(), ZonedDateTime.now(), ZonedDateTime.now()));
        userService.addRoleToUser("harist", "ROLE_ADMIN");

        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}