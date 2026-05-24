package isep.psoft.aisafe.api.auth;

import isep.psoft.aisafe.api.auth.dto.JwtResponse;
import isep.psoft.aisafe.api.auth.dto.LoginRequest;
import isep.psoft.aisafe.domain.user.SystemUser;
import isep.psoft.aisafe.infrastructure.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SystemUser user = (SystemUser) auth.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token, user.getUsername(), user.getRole().name()));
    }
}
