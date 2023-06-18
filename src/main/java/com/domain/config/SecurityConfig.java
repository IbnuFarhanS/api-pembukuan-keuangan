package com.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/kategori").permitAll()
                .antMatchers("/api/kategori/{id}").permitAll()

                .antMatchers("/api/daftar-keuangan").permitAll()
                .antMatchers("/api/daftar-keuangan/{id}").permitAll()
                .antMatchers("/api/daftar-keuangan/kategori/{kategoriId}").permitAll()
                .antMatchers("/api/daftar-keuangan/amount-greater-than/{amount}").permitAll()
                .antMatchers("/api/daftar-keuangan/amount-less-than/{amount}").permitAll()
                .antMatchers("/api/daftar-keuangan/date-between/{startDate}/{endDate}").permitAll()

                .antMatchers("/api/pengguna").permitAll()
                .antMatchers("/api/pengguna/{id}").permitAll()
                .anyRequest().authenticated()
                .and()
                // Tambahkan konfigurasi autentikasi jika diperlukan
                // ...
                .csrf().disable(); // Matikan fitur CSRF jika tidak digunakan

        return http.build();
    }

}
