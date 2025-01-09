package me.silvernine.tutorial.dto;

//Dto 클래스는 외부와의 통신에 사용할 클래스임
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT 토큰 반환 객체")
public class TokenDto {

    @Schema(description = "발급된 JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
