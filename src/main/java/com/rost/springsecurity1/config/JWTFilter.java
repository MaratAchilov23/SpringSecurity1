package com.rost.springsecurity1.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rost.springsecurity1.security.JWTUtil;
import com.rost.springsecurity1.service.PersonDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final PersonDetailService service;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonDetailService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorisation");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);
            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Jwt-token in header Bearer ");
            } else {
                try {

                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    UserDetails userDetails = service.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken passwordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(), userDetails.getAuthorities());


                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
                    }
                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Что то не так с Токеном");
                }
            }
        }
        
        filterChain.doFilter(request,response);
    }
}