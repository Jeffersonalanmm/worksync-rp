package com.example.worksync.controller;

import com.example.worksync.dto.requests.AuthDTO;
import com.example.worksync.dto.responses.LoginResponseDTO;
import com.example.worksync.dto.requests.UserDTO;
import com.example.worksync.model.User;
import com.example.worksync.service.AuthService;
import com.example.worksync.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                authDTO.getEmail(), authDTO.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        String token = this.tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserDTO userDTO) {
        User registeredUser = this.authService.register(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
