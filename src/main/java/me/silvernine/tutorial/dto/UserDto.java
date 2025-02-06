package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
   private String password;

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   // ✅ 기존 메서드 (password 포함)
   public static UserDto from(User user, String rawPassword) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId())
              .password(rawPassword) // ✅ 원본 비밀번호 유지
              .nickname(user.getNickname())
              .build();
   }

   // ✅ 새로 추가된 메서드 (Optional.map() 사용 가능)
   public static UserDto from(User user) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId())
              .password(null) // ✅ 패스워드 숨김
              .nickname(user.getNickname())
              .build();
   }
}
