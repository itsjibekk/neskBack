package org.example.config;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {
    private final UserAuthProvider userAuthProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Skip filtering for public endpoints
        if (!isPublicEndpoint(request)) {
            if (header != null) {
                String[] authElements = header.split(" ");
                if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                    try {
                        Authentication auth = "GET".equals(request.getMethod())
                                ? userAuthProvider.validateToken(authElements[1])
                                : userAuthProvider.validateTokenStrongly(authElements[1]);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } catch (TokenExpiredException ex) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                        return;
                    } catch (RuntimeException ex) {
                        SecurityContextHolder.clearContext();
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/login") || path.startsWith("/register");
    }
}