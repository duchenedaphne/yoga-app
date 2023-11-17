package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.assertj.core.api.Assertions;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {
    
    @InjectMocks
    private JwtUtils jwtUtils;

    String jwtSecret = "openclassrooms";
    int jwtExpirationMs = 86400000;
    UserDetails userDetails;
    Authentication authentication;
    String token;
    String userName;

    @BeforeEach
    public void init() {

        userDetails = new UserDetailsImpl(1L, "test@email.com", "User", "Basic", false, "password");

        authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        token = Jwts.builder()
            .setSubject((userDetails.getUsername()))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();

        userName = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    }

    @Test
    public void tokenValidate() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        request.setParameter("authToken", token);

        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

        Boolean tokenValidate = jwtUtils.validateJwtToken(token);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
        
        Assertions.assertThat(userName).isEqualTo("test@email.com");
        Assertions.assertThat(tokenValidate).isFalse();
    }
}
