package com.teamc.bioskop.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamc.bioskop.Exception.ResourceNotFoundException;
import com.teamc.bioskop.Model.Role;
import com.teamc.bioskop.Model.User;
import com.teamc.bioskop.Repository.UserRepository;
import com.teamc.bioskop.Response.ResponseHandler;
import com.teamc.bioskop.Service.impl.UserServiceImplements;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "2. User Controller")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserServiceImplements userServiceImplements;
    private final UserRepository userRepository;

    /***
     * Get All Users, Logger And Response DONE
     * @return
     */
    @GetMapping("/dashboard/users")
    @Operation(summary = "Get All Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success get All users",
                    content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema (implementation = User.class))) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    public ResponseEntity<Object> getAllUser() {
        try {
            List<User> result = userServiceImplements.getAll();
            List<Map<String, Object>> maps = new ArrayList<>();
            logger.info("==================== Logger Start Get All Users     ====================");

            for (User userData : result) {
                Map<String, Object> user = new HashMap<>();

                logger.info("-------------------------");
                logger.info("ID       : " + userData.getUserId());
                logger.info("Username : " + userData.getUsername());
                logger.info("Email    : " + userData.getEmailId());
                logger.info("Password : " + userData.getPassword());

                user.put("ID            ", userData.getUserId());
                user.put("Username      ", userData.getUsername());
                user.put("Email         ", userData.getEmailId());
                user.put("Password      ", userData.getPassword());
                maps.add(user);
            }
            logger.info("==================== Logger End Get All Users     ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse("Successfully Get All User!", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get All Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table has no value"));
            logger.info("==================== Logger End Get All Users     ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, "Table Has No Value!");

        }
    }

    /***
     * Get User By Id, Logger and Response DONE
     * @param users_Id
     * @return
     */
    @GetMapping("/users/{users_Id}")
    @Operation(summary = "Get One User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success get token",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    public ResponseEntity<Object> getUserById(@PathVariable Long users_Id) {
        try {
//            User userResult = userRepository.getReferenceById(users_Id);
            User userResult = userServiceImplements.getUserById(users_Id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not exist with user_Id :" + users_Id));
            Map<String, Object> user = new HashMap<>();
            List<Map<String, Object>> maps = new ArrayList<>();

            logger.info("==================== Logger Start Find By ID Users ====================");
            logger.info("ID       : " + userResult.getUserId());
            logger.info("Username : " + userResult.getUsername());
            logger.info("Email    : " + userResult.getEmailId());
            logger.info("Password : " + userResult.getPassword());

            user.put("ID             ", userResult.getUserId());
            user.put("Username       ", userResult.getUsername());
            user.put("Email          ", userResult.getEmailId());
            user.put("Password       ", userResult.getPassword());
            maps.add(user);

            logger.info("==================== Logger End Find By ID Users   ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse("Successfully Get User By ID!", HttpStatus.OK, maps);
        } catch (Exception e) {
            logger.info("==================== Logger Start Get By ID Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!"));
            logger.info("==================== Logger End Get By ID Users     ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }

    }

    /***
     * Create User, Logger and Response DONE
     * @param user
     * @return
     */

    @PostMapping("/dashboard/role")
    public ResponseEntity<Object> createRole(@RequestBody Role role){
        return ResponseHandler.generateResponse("role created",
                HttpStatus.CREATED, this.userServiceImplements.createRole(role));
    }

    @PostMapping("/dashboard/role/addtouser")
    public ResponseEntity<Object> addRoleToUser(@RequestBody FormAddRoleToUser form){
        this.userServiceImplements.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseHandler.generateResponse("",
                HttpStatus.OK,
                "success add role" + form.getRoleName() +" to user " + form.getUsername());
    }

    /***
     * Update User, Logger and Response DONE
     * @param users_Id
     * @param userDetails
     * @return
     */
    @PutMapping("/update/user")
    public ResponseEntity<Object> updateUser(Authentication authentication, @RequestBody User userDetails) {


            logger.info("==================== Logger Start Update Users ====================");
            logger.info("User Data Successfully Updated !");
            logger.info("ID       : " + userDetails.getUserId());
            logger.info("Username : " + userDetails.getUsername());
            logger.info("Email    : " + userDetails.getEmailId());
            logger.info("Password : " + userDetails.getPassword());
            logger.info("==================== Logger End Update Users   ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse("Successfully Updated User!", HttpStatus.OK, this.userServiceImplements.updateUser(userDetails, authentication));


    }

    /***
     * Delete User,Logger and Response DONE
     * @param users_Id
     * @return
     */
    @DeleteMapping("/delete/user/{users_Id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long users_Id) {
        try {
            userServiceImplements.deleteUserById(users_Id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            logger.info("==================== Logger Start Delete Users ====================");
            logger.info("User Data Successfully Deleted! :" + response.put("deleted", Boolean.TRUE));
            logger.info("==================== Logger End Delete Users   ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse("Successfully Delete User! ", HttpStatus.OK, response);
        } catch (ResourceNotFoundException e) {
            logger.info("==================== Logger Start Delete Users     ====================");
            logger.error(ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!"));
            logger.info("==================== Logger End Delete Users     ====================");
            logger.info(" ");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!");
        }
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userServiceImplements.getUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
    @GetMapping("/print-user-login")
    public List<String> userLogin(Authentication authentication){
        List<String> loginInfo = new ArrayList<>();

        loginInfo.add("Name :" + authentication.getName());
        loginInfo.add("Authorities : " + authentication.getAuthorities());
        loginInfo.add("Principal : " + authentication.getPrincipal());

        return loginInfo;
    }
}

@Data
class FormAddRoleToUser{
    private String username;
    private String roleName;
}