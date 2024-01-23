package com.example.cloudstorage.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.cloudstorage.dto.JwtRequest;
import com.example.cloudstorage.exceptions.StorageException;
import com.example.cloudstorage.repository.CloudRepository;
import com.example.cloudstorage.security.JwtTokenUtil;

@Service
public class JwtAuthService {
    private final CloudRepository cloudRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthService(CloudRepository cloudRepository, AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil) {
        this.cloudRepository = cloudRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String login(JwtRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userPrincipal);
            cloudRepository.login(jwt, userPrincipal);
            return jwt;
        } catch (BadCredentialsException e) {
            throw new StorageException("Bad credentials");
        }
    }

    public void logout(String authToken) {
        cloudRepository.logout(authToken).orElseThrow(() -> new Error("No valid logout"));
    }
}