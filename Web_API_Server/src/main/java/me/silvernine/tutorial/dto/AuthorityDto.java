package me.silvernine.tutorial.dto;

//Dto 클래스는 외부와의 통신에 사용할 클래스임
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {
   private String authorityName;
}