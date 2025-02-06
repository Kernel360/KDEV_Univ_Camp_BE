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

   @NotNull(message = "사용자 ID는 필수 입력값입니다.") // ✅ `id`가 `null`이면 예외 발생
   @Size(min = 3, max = 50, message = "ID는 3자 이상 50자 이하로 입력하세요.")
   @JsonProperty("id") // ✅ JSON 키가 "id"일 때 매핑을 보장
   private String id;

   @NotNull(message = "비밀번호는 필수 입력값입니다.") // ✅ `password`가 `null`이면 예외 발생
   @Size(min = 3, max = 100, message = "비밀번호는 3자 이상 100자 이하로 입력하세요.")
   private String password;

   @NotNull(message = "닉네임은 필수 입력값입니다.") // ✅ `nickname`이 `null`이면 예외 발생
   @Size(min = 3, max = 50, message = "닉네임은 3자 이상 50자 이하로 입력하세요.")
   private String nickname;

   // ✅ 기존 메서드 (password 포함)
   public static UserDto from(User user, String rawPassword) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId()) // ✅ `user` 엔터티의 `id`를 DTO로 변환
              .password(rawPassword) // ✅ 원본 비밀번호 포함
              .nickname(user.getNickname())
              .build();
   }

   // ✅ 새로 추가된 메서드 (Optional.map() 사용 가능)
   public static UserDto from(User user) {
      if (user == null) return null;

      return UserDto.builder()
              .id(user.getId()) // ✅ `user` 엔터티의 `id`를 DTO로 변환
              .password(null) // ✅ 보안상 비밀번호 숨김 처리
              .nickname(user.getNickname())
              .build();
   }
}
