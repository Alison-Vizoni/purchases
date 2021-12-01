package br.com.alison.purchases.resources;

import br.com.alison.purchases.dto.EmailDTO;
import br.com.alison.purchases.security.JWTUtil;
import br.com.alison.purchases.security.UserSpringSecurity;
import br.com.alison.purchases.service.AuthService;
import br.com.alison.purchases.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthService service;

    @PostMapping(value = "/refresh_token")
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UserSpringSecurity user = UserService.getUserAuthenticated();
        String token = jwtUtil.generateToken(user.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/forgot")
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO emailDTO) {
        service.sendNewPassword(emailDTO.getEmail());
        return ResponseEntity.noContent().build();
    }
}
