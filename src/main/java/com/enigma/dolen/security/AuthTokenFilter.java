package com.enigma.dolen.security;

import com.enigma.dolen.service.UserCredentialService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserCredentialService userCredentialService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String headerAuth = request.getHeader("Authorization");
            String token = null;
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                token = headerAuth.substring(7);
            }
            if (token != null && jwtUtil.verifyJwtToken(token)) {
                Map<String, String> userInfo = jwtUtil.getUserInfoByToken(token);
                UserDetails user = userCredentialService.loadUserById(userInfo.get("credentialId"));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        filterChain.doFilter(request, response);
    }
}
