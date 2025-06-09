package com.virtual.lab.backend.controller;


import com.virtual.lab.backend.dto.*;
import com.virtual.lab.backend.model.*;
import com.virtual.lab.backend.service.UserService;
import jakarta.validation.Valid;
//import jakarta.validation.constraints;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }




    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseGetAllDTO>> getAllUser(){
        List<UserResponseGetAllDTO> users = userService.findAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admin/users")
    @PreAuthorize("haseRole('ADMIN')")
    public ResponseEntity<List<UserAfficheDTO>> getAllUsers(){
        List<UserAfficheDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/admin/find/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable("id") Long id) throws Throwable {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserCreationRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        User user = switch (request.getDtype().toUpperCase()) {
            case "ADMIN" -> {
                Admin admin = new Admin();
                admin.setRole(Role.ADMIN);
                yield admin;
            }
            case "TECHNICIEN" -> {
                Technician tech = new Technician();
                tech.setRole(Role.TECHNICIEN);
                yield tech;
            }
            case "CLIENT" -> {
                Client client = new Client();
                client.setRole(Role.CLIENT);
                yield client;
            }
            default -> throw new IllegalArgumentException("Type invalide");
        };

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return ResponseEntity.ok(userService.addUser(user));
    }



    @PutMapping("/admin/updateUser")
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser( @RequestBody UserUpdateDTO dto) {
        return userService.updateUser(dto);
    }


    @DeleteMapping("/admin/deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
/*
    @PostMapping("/login")
    public String login(@RequestBody User user){
        //System.out.println(user);
        return userService.verify(user);
    }

 */
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    System.out.println("=== LOGIN REQUEST ===");
    System.out.println("Username reçu: " + request.getUsername());
    System.out.println("Password reçu: " + (request.getPassword() != null && !request.getPassword().isEmpty() ? "***" : "VIDE/NULL"));

    try {
        Map<String, Object> result = userService.verify(request.getUsername(), request.getPassword());
        System.out.println("Login réussi pour: " + request.getUsername());
        return ResponseEntity.ok(result);

    } catch (BadCredentialsException | UsernameNotFoundException e) {
        System.out.println("Login échoué - Identifiants incorrects pour: " + request.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Identifiants incorrects");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    } catch (Exception e) {
        System.out.println("Erreur inattendue lors du login: " + e.getMessage());
        e.printStackTrace();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Une erreur est survenue");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


    @GetMapping("/admin/getAllTechnicians")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllTechnicians() {
        List<User> technicians = userService.findByRole(Role.TECHNICIEN);
        return ResponseEntity.ok(technicians);
    }


    @GetMapping("/id/{username}")
    public ResponseEntity<Long> getUserIdByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user.getId());
    }


}
