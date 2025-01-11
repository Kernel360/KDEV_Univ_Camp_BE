package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Token Management", description = "APIs for managing and generating tokens")
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @Operation(
            summary = "Generate a new token",
            description = "Receives client credentials, processes the request, and returns a generated token"
    )
    @PostMapping("/getToken")
    public ResponseEntity<TokenResponseDto> getToken(@RequestBody TokenRequestDto request) {
        TokenResponseDto response = tokenService.generateToken(request);
        return ResponseEntity.ok(response);
    }
}
