package br.com.joaopedroafluz.timely.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        tokenService.getUserDetailsFromTokenIfValid(authorizationHeader)
                .ifPresent(userDetails -> {
                    AuthorizationUtils.setAuthenticatedUser(userDetails.getUser());
                    var auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                });

        filterChain.doFilter(request, response);
    }

}
