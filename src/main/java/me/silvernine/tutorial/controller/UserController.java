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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @Operation(
            summary = "Get user information by ID",
            description = "Fetches the information of a specific user by their ID. Only accessible to admins",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(id));
    }
}
