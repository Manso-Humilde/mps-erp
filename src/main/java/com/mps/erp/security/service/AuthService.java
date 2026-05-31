package com.mps.erp.security.service;

import com.mps.erp.dto.LoginRequest;
import com.mps.erp.dto.LoginResponse;
import com.mps.erp.exception.AuthenticationException;
import com.mps.erp.model.User;
import com.mps.erp.repository.UserRepository;
import com.mps.erp.security.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public LoginResponse login(LoginRequest request) {

        System.out.println("Email recibido: " + request.getEmail());
        System.out.println("Password recibido: " + request.getPassword());

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            System.out.println("Usuario no encontrado");
            throw new AuthenticationException("Credenciales inválidas");
        }

        User user = userOptional.get();
        System.out.println("Password hash en BD: " + user.getPasswordHash());
        System.out.println("¿Match? " + passwordEncoder.matches(request.getPassword(), user.getPasswordHash()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthenticationException("Credenciales inválidas");
        }

        String loginId = request.getEmail();
        if (loginId == null || loginId.isEmpty()) {
            throw new AuthenticationException("Credenciales incompletas");
        }

//        Optional<User> userOptional = userRepository.findByEmail(loginId);
//        if (userOptional.isEmpty()) {
//            throw new AuthenticationException("Credenciales inválidas");
//        }
//
//        User user = userOptional.get();
//        if (!user.getActivo()) {
//            throw new AuthenticationException("Usuario inactivo");
//        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId(), user.getTenantId());

        return new LoginResponse(
                token,
                user.getId(),
                user.getNombreCompleto(),
                user.getEmail(),
                user.getRole(),
                user.getDoctorId()
        );
    }
}
