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
   @JsonProperty("id") // ✅ JSON에서 id로 표시
   private String id;

   @NotNull
   @Size(min = 3, max = 100)
   private String password;

   @NotNull
   @Size(min = 3, max = 50)
   private String nickname;

   public static UserDto from(User user) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId())  // ✅ 변경된 필드 반영
              .password(user.getPassword())
              .nickname(user.getNickname())
              .build();
   }
}
