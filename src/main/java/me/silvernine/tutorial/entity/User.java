package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

   @Id
   @Column(name = "user_id", nullable = false, unique = true)
   private String userId; // ✅ String 타입의 UUID 자동 생성 ID

   @Column(nullable = false, unique = true)
   private String id;  // ✅ 사용자가 입력하는 ID (예: username)

   @Column(nullable = false)
   private String password;  // ✅ 암호화된 비밀번호 저장

   @Column(nullable = false)
   private String nickname;  // ✅ 닉네임 저장

   @Column(nullable = false)
   private boolean activated;  // ✅ 계정 활성화 여부

   @Column(nullable = false)
   private boolean isAdmin;  // ✅ 관리자 여부

   @ManyToMany(fetch = FetchType.EAGER) // ⚠️ 지연 로딩(LAZY) → 즉시 로딩(EAGER) 변경
   @JoinTable(
           name = "user_authority",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "authority_name")
   )
   @Builder.Default
   private Set<Authority> authorities = new HashSet<>();

   // ✅ Spring Security가 인식할 수 있도록 GrantedAuthority로 변환 (디버깅 추가)
   public Collection<? extends GrantedAuthority> getAuthorities() {
      System.out.println("🔍 [DEBUG] User.getAuthorities() 호출됨");

      authorities.forEach(auth ->
              System.out.println("✅ 사용자 권한 로드: " + auth.getAuthority())
      );

      return authorities.stream()
              .map(auth -> new SimpleGrantedAuthority(auth.getAuthority())) // 변경된 부분
              .collect(Collectors.toList());
   }

   // ✅ userId가 없을 경우 UUID 자동 생성 (디버깅 추가)
   @PrePersist
   public void prePersist() {
      if (this.userId == null || this.userId.isEmpty()) {
         this.userId = UUID.randomUUID().toString();
         System.out.println("✅ [DEBUG] 새 UUID 생성됨: " + this.userId);
      }
   }
}
