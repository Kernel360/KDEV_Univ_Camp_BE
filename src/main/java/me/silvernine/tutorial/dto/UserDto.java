package me.silvernine.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonPropertyOrder({"id", "nickname"}) // ✅ JSON 응답 순서 지정 (비밀번호 제거)
public class UserDto {

   @NotNull
   @Size(min = 3, max = 50)
   @JsonProperty("id")
   private String id;

   @JsonIgnore // ✅ JSON 응답에서 제외
   @NotNull
   @Size(min = 3, max = 100)
   private String password;

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   public static UserDto from(User user) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId())
              .nickname(user.getNickname())
              .build();
   }
}
