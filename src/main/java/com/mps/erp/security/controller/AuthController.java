package com.mps.erp.security.controller;

import com.mps.erp.dto.LoginRequest;
import com.mps.erp.dto.LoginResponse;
import com.mps.erp.exception.AuthenticationException;
import com.mps.erp.security.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authService.login(request);

            String token = loginResponse.getToken();

            // Crear cookie httpOnly
            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);  // true en producción con HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(86400);  // 24 horas
            cookie.setAttribute("SameSite", "Lax");  // ← Agrega esto
            response.addCookie(cookie);

            // Retornar respuesta sin token (para no exponerlo)
            return ResponseEntity.ok(new LoginResponse(
                    null,
                    loginResponse.getUserId(),
                    loginResponse.getNombreCompleto(),
                    loginResponse.getEmail(),
                    loginResponse.getRole(),
                    loginResponse.getDoctorId()
            ));
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();  // ← Agrega esto para ver el error
            throw new AuthenticationException("Credenciales inválidas");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hash")
    public String hash(@RequestParam String pass) {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(pass);
    }
}