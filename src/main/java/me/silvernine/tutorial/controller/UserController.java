package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "User Management", description = "APIs for managing users and retrieving user information")
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "권한 조회",
            description = "현재 로그인한 사용자의 정보를 가져옵니다",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @Operation(
            summary = "Get user information by username",
            description = "Fetches the information of a specific user by their username. Only accessible to admins",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }

    @Operation(
            summary = "승인 헤더 확인",
            description = "테스트 목적으로 승인 헤더의 값을 반환합니다"
    )
    @GetMapping("/auth-header-check")
    public ResponseEntity<Object> authHeaderChecker(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>() {{
            put("Authorization", request.getHeader("Authorization"));
        }};
        return ResponseEntity.ok(response);
    }
}
