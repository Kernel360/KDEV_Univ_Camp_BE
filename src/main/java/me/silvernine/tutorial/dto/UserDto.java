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
   @JsonProperty("id")
   private String id;

   @NotNull
   @Size(min = 3, max = 100)
   private String password;  // ✅ 비밀번호 응답에 포함

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   public static UserDto from(User user, String rawPassword) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId())
              .password(rawPassword) // ✅ 원본 비밀번호 유지
              .nickname(user.getNickname())
              .build();
   }
}
