package com.example.cloudstorage.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import com.example.cloudstorage.dto.JwtRequest;
import com.example.cloudstorage.dto.ResponseError;
import com.example.cloudstorage.service.auth.JwtAuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
public class JwtAuthenticationController {
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";

    private final JwtAuthService jwtAuthService;

    public JwtAuthenticationController(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    @PostMapping(LOGIN)
    public ResponseEntity<JwtRequest> login(@Valid @RequestBody JwtRequest authorization) {
        String authToken = jwtAuthService.login(authorization);
        return authToken != null ? new ResponseEntity<>(new JwtRequest(authToken), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(LOGOUT)
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        jwtAuthService.logout(authToken);
        return HttpStatus.OK;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(e.getMessage(), 400));
    }
}