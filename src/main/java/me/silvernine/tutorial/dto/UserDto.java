package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import me.silvernine.tutorial.entity.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "password", "nickname"}) // ✅ JSON 응답 순서 지정
public class UserDto {

   @NotNull
   @Size(min = 3, max = 50)
   @JsonProperty("id")
   private String id;

   @NotNull
   @Size(min = 3, max = 100)
   @JsonInclude(JsonInclude.Include.NON_NULL)  // ✅ null 값이 아닐 때만 JSON 포함
   @Schema(description = "사용자의 비밀번호", example = "mysecurepassword")  // ✅ Swagger 문서화
   private String password;

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   // ✅ 비밀번호를 제외한 응답용 DTO 생성
   public static UserDto from(User user) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId())
              .nickname(user.getNickname())
              .build();
   }
}
