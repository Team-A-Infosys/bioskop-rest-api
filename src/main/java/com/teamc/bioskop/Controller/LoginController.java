package com.teamc.bioskop.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamc.bioskop.Model.User;
import com.teamc.bioskop.Response.ResponseHandler;
import com.teamc.bioskop.Service.impl.UserServiceImplements;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@Tag(name = "1. Sign Up/Login Controller")
@OpenAPIDefinition(info = @Info(title = "Team A Bioskop Rest API - Docs",
        description = "Silahkan dilihat-lihat, kalau ada yg kurang diam aja ga usah dikasih tau... Karena kami lelah"))
public class LoginController {


    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserServiceImplements userServiceImplements;

    @Operation(summary = "Login for get token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success get token",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", description = "Unauthorized, failed login",
                    content = @Content) })
    @PostMapping("/login")
    public void getToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

    }
    @Operation(summary = "Sign Up User")
    @PostMapping("/signup/user")
    public ResponseEntity<Object> createUser(@RequestBody User user) {

        try {
//            userServiceImplements.createUser(user);
            User userResult = userServiceImplements.createUser(user);
            Map<String, Object> userMap = new HashMap<>();
            List<Map<String, Object>> maps = new ArrayList<>();

            logger.info("==================== Logger Start Create Users ====================");
            logger.info("User Successfully Created !");
            logger.info("ID       : " + userResult.getUserId());
            logger.info("Username : " + userResult.getUsername());
            logger.info("Email    : " + userResult.getEmailId());
            logger.info("Password : " + userResult.getPassword());

            userMap.put("ID             ", userResult.getUserId());
            userMap.put("Username       ", userResult.getUsername());
            userMap.put("Email          ", userResult.getEmailId());
            userMap.put("Password       ", userResult.getPassword());
            maps.add(userMap);
            logger.info("==================== Logger End Create Users   ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse("Successfully Created User!", HttpStatus.CREATED, maps);
        } catch (Exception e) {
            logger.info("==================== Logger Start Create Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "User Already Exist!"));
            logger.info("==================== Logger End Create Users     ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "User Already Exist!");
        }

    }


}