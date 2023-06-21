package com.domain.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.domain.services.PenggunaService;
import com.domain.helpers.password.PasswordEncoderExample;
import com.domain.models.entities.Pengguna;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    // Secret key untuk menandatangani JWT
    private static final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    @Autowired
    private PenggunaService penggunaService;

    // Metode untuk membuat JWT Token
    public String generateToken(String username) {
        // Konfigurasi tanggal kedaluwarsa token
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // Token kedaluwarsa dalam 1 jam

        // Membuat JWT Token
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    // Metode untuk memvalidasi dan mendapatkan informasi dari JWT Token
    public String getUsernameFromToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);

        return claimsJws.getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            // Jika tidak ada exception yang terjadi, maka token valid
            return true;
        } catch (Exception e) {
            // Exception terjadi saat validasi token gagal
            return false;
        }
    }

    public boolean validateUsernameAndPassword(String username, String password) {
        // Mengambil pengguna berdasarkan username (misalnya dari repositori)
        Pengguna pengguna = penggunaService.findByUsername(username);

        if (pengguna != null) {
            // Membandingkan password yang diberikan dengan password pengguna yang tersimpan
            return PasswordEncoderExample.matches(password, pengguna.getPassword());
        }

        return false;
    }
}
