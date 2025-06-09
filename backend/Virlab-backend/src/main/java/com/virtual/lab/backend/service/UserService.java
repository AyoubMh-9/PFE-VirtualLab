package com.virtual.lab.backend.service;

import com.virtual.lab.backend.dto.UserAfficheDTO;
import com.virtual.lab.backend.dto.UserResponseGetAllDTO;
import com.virtual.lab.backend.dto.UserUpdateDTO;
import com.virtual.lab.backend.exception.ResourceNotFoundException;
import com.virtual.lab.backend.exception.UserNotFoundException;
import com.virtual.lab.backend.model.Role;
import com.virtual.lab.backend.model.User;
import com.virtual.lab.backend.model.UserPrincipal;
import com.virtual.lab.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository utilisateurRepo;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    public UserService(UserRepository utilisateurRepo, PasswordEncoder passwordEncoder){
        this.utilisateurRepo = utilisateurRepo;

        this.passwordEncoder = passwordEncoder;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User addUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return utilisateurRepo.save(user);
    }

    public List<UserResponseGetAllDTO> findAllUser(){

        List<User> users =  utilisateurRepo.findAll();

        return users.stream()
                .map(u -> new UserResponseGetAllDTO(
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole()
                ))
                .collect(Collectors.toList());
    }

    public User updateUser(UserUpdateDTO dto) {
        User existingUser = utilisateurRepo.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPassword(dto.getPassword());

        return utilisateurRepo.save(existingUser);
    }


    public User findUserById(long id) throws Throwable {
        return (User) utilisateurRepo.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id "+ id + " Not Found"));
    }
    @Transactional
    public void deleteUser(long id){
        utilisateurRepo.deleteById(id);
    }

    // 1. Méthode verify() corrigée
    public Map<String, Object> verify(String username, String rawPassword) {
        System.out.println("Tentative de login pour: " + username);
        System.out.println("Mot de passe reçu: " + (rawPassword != null ? "***" : "NULL"));

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, rawPassword)
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User authenticatedUser = userPrincipal.getUser();

            String token = jwtService.generateToken(userPrincipal);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", authenticatedUser.getId());
            userMap.put("username", authenticatedUser.getUsername());
            userMap.put("role", authenticatedUser.getRole());

            response.put("user", userMap);

            return response;

        } catch (BadCredentialsException e) {
            System.out.println("Échec de l'authentification: credentials incorrects");
            throw e;
        } catch (Exception e) {
            System.out.println("Erreur lors de l'authentification: " + e.getMessage());
            throw new RuntimeException("Erreur d'authentification", e);
        }
    }


    public Long findIdByUsername(String currentUsername) {
        User user = utilisateurRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + currentUsername));

        Long userId = user.getId();
        return userId;
    }

    public List<UserAfficheDTO> findAllUsers() {
        List<User> users = utilisateurRepo.findAll();
        return users.stream()
                .map(u -> new UserAfficheDTO(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getPassword(),
                        u.getRole()
                ))
                .collect(Collectors.toList());
    }

    public List<User> findByRole(Role role) {
        return utilisateurRepo.findByRole(role);
    }

    public Optional<User> findByUsername(String username) {
        return utilisateurRepo.findByUsername(username);
    }

    public Optional<User> getUserById(Long id){
        return utilisateurRepo.findUserById(id);
    }
    /*
    public User connecte(String email, String motDePasse){
        // logic authentification
    }

    public void deconnecte(){
        // logic for deconnecte
    }

    public User updateProfile(Long userId, ProfileUpdateDto updateDto){
        // we can update the profile (user can update her profile)
    }


     */





}
