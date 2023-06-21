package com.domain.services;

import com.domain.util.InvalidTokenException;

public interface UserAuthenticationService {
    String authenticate(String username, String password);
    void validateToken(String token) throws InvalidTokenException;
}
