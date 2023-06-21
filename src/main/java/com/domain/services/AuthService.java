package com.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import com.domain.helpers.password.PasswordEncoderExample;
import org.springframework.stereotype.Service;

import com.domain.models.entities.Pengguna;
import com.domain.models.repos.PenggunaRepo;
import com.domain.util.InvalidTokenException;
import com.domain.util.JwtTokenProvider;

import org.springframework.security.authentication.BadCredentialsException;

@Service
public class AuthService implements UserAuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PenggunaRepo penggunaRepo;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, PenggunaRepo penggunaRepo) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.penggunaRepo = penggunaRepo;
    }


    @Override
    public String authenticate(String username, String password) {
        Pengguna pengguna = penggunaRepo.findByUsername(username);
        if (pengguna == null || !PasswordEncoderExample.matches(password, pengguna.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return jwtTokenProvider.generateToken(username);
    }

    @Override
    public void validateToken(String token) throws InvalidTokenException {
        boolean isValid = jwtTokenProvider.validateToken(token);
        if (!isValid) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}
