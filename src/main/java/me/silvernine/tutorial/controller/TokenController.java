package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/getToken")
    public ResponseEntity<TokenResponseDto> getToken(@RequestBody TokenRequestDto request) {
        TokenResponseDto response = tokenService.generateToken(request);
        return ResponseEntity.ok(response);
    }
}
