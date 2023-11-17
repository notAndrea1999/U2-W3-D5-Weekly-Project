package andreademasi.security;

import andreademasi.entities.User;
import andreademasi.exceptions.UnauthorizedException;
import andreademasi.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Verifico se c'è l'header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Inserire token in Authorization Header");
        } else {
            // Estraggo il token da Auth Header
            String token = authHeader.substring(7);
            System.out.println("IL TOKEN -->" + token);
            jwtTools.verifyToken(token);

            //Estraggo user id dal Token
            String id = jwtTools.idFromToken(token);

            //Cerco User in db
            User foundUser = userService.findUserById(Integer.parseInt(id));

            //Autorizzo lo user a procedere
            Authentication authentication = new UsernamePasswordAuthenticationToken(foundUser, null, foundUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //Procedere verso il prossimo blocco della filter chain
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
