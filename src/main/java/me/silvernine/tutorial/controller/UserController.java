package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.ApiResponse;
import me.silvernine.tutorial.dto.UserDto;
import me.silvernine.tutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
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

    @Operation(summary = "Say hello", description = "Returns a simple hello message")
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @Operation(summary = "Test redirect", description = "Redirects to the user endpoint")
    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    @Operation(summary = "회원 가입", description = "유저 회원 가입 페이지 입니다")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> signup(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.signup(userDto);
        return ResponseEntity.ok(
                ApiResponse.<UserDto>builder()
                        .status(200)
                        .message("회원가입 성공")
                        .data(createdUser)
                        .build()
        );
    }

    @Operation(
            summary = "권한 조회",
            description = "현재 로그인한 사용자의 정보를 가져옵니다",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @Operation(
            summary = "Get user information by username",
            description = "Fetches the information of a specific user by their username. Only accessible to admins",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
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
