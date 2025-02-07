package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

   @ManyToMany
   @JoinTable(
           name = "user_authority",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "authority_name")
   )
   @Builder.Default
   private Set<Authority> authorities = new HashSet<>();

   // ✅ userId가 없을 경우 UUID 자동 생성
   @PrePersist
   public void prePersist() {
      if (this.userId == null || this.userId.isEmpty()) {
         this.userId = UUID.randomUUID().toString();
      }
   }
}
