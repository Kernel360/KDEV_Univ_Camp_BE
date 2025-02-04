package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.silvernine.tutorial.entity.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

   @NotNull
   @Size(min = 3, max = 50)
   @JsonProperty("id")  // ✅ JSON 응답에서 "id"로 표시
   private String username;

   @NotNull
   @Size(min = 3, max = 100)
   private String password;

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   // ✅ getId() 추가 (username 필드를 id처럼 사용)
   public String getId() {
      return this.username;
   }

   public static UserDto from(User user) {
      if (user == null) return null;

      return UserDto.builder()
              .username(user.getUsername())  // ✅ id로 변경됨
              .password(user.getPassword())  // ✅ 응답에 비밀번호 포함
              .nickname(user.getNickname())
              .build();
   }
}
