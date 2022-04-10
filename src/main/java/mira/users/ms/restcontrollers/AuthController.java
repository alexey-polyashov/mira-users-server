package mira.users.ms.restcontrollers;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mira.users.ms.config.JwtTokenUtil;
import mira.users.ms.dto.CredentialsDto;
import mira.users.ms.dto.JwtRequestDTO;
import mira.users.ms.dto.JwtResponseDTO;
import mira.users.ms.exceptions.BadRequestException;
import mira.users.ms.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Api(value = "AuthController", tags = "Контролер для авторизации")
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("")
    public ResponseEntity<JwtResponseDTO> createAuthToken(@RequestBody JwtRequestDTO authRequest) {
        log.info("AuthController, createAuthToken, {}", authRequest.getUsername());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            log.info("AuthController, createAuthToken, Incorrect username or password for user {}", authRequest.getUsername());
            throw new BadRequestException( "Incorrect username or password");
        }
        UserDetails userDetails = userService.getUserDetail(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        log.info("AuthController, createAuthToken, succes - {}", authRequest.getUsername());
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @PostMapping("/checkcredentials")
    public ResponseEntity<CredentialsDto> checkToken(@RequestBody JwtResponseDTO token) {

        String username;
        String jwt = token.getToken();

        List<String> credentials = new ArrayList<>();

        try {
            username = jwtTokenUtil.getUsernameFromToken(jwt);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(new CredentialsDto(credentials), HttpStatus.UNAUTHORIZED);
        }

        if (username != null) {
            credentials = jwtTokenUtil.getRoles(jwt).stream().map(SimpleGrantedAuthority::new)
                    .map(SimpleGrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(new CredentialsDto(credentials), HttpStatus.OK);

    }



}