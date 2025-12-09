package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.service.TokenService;
import me.silvernine.tutorial.util.TokenValidator;
import me.silvernine.tutorial.util.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Token Management", description = "APIs for managing and validating tokens")
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final TokenValidator tokenValidator;

    @Operation(summary = "Generate a new token", description = "Receives client credentials and returns a generated token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/getToken")
    public ResponseEntity<TokenResponseDto> getToken(@RequestBody TokenRequestDto request) {
        TokenResponseDto response = tokenService.generateToken(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Validate token", description = "Checks if the provided token is valid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired token")
    })
    @GetMapping("/validate")
    public ResponseEntity<TokenResponseDto> validateToken(@RequestParam String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean isValid = tokenValidator.validate(token);

        if (isValid) {
            return ResponseEntity.ok().body(
                    TokenResponseDto.builder()
                            .rstCd(ResponseCode.SUCCESS)
                            .rstMsg("Token is valid")
                            .token(token)
                            .build()
            );
        } else {
            return ResponseEntity.ok().body(
                    TokenResponseDto.builder()
                            .rstCd(ResponseCode.INVALID_TOKEN)
                            .rstMsg("Invalid or expired token")
                            .build()
            );
        }
    }
}
