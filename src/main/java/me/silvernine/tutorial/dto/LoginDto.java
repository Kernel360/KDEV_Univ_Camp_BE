package me.silvernine.tutorial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "로그인 요청을 위한 데이터 전송 객체") // Swagger 설명 추가
public class LoginDto {

   @NotNull
   @Size(min = 3, max = 50)
   @Schema(description = "사용자 이름", example = "admin", required = true) // Swagger 필드 설명
   private String username;

   @NotNull
   @Size(min = 3, max = 100)
   @Schema(description = "비밀번호", example = "admin", required = true) // Swagger 필드 설명
   private String password;
}
