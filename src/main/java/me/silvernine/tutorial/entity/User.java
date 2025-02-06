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
   @Column(name = "user_id", unique = true, nullable = false)
   private String userId;  // ✅ user_id를 PK로 유지

   @Column(nullable = false, unique = true)
   private String id;  // ✅ 사용자 입력 ID (로그인 시 사용)

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String nickname;

   @Column(nullable = false)
   private boolean activated;

   @Column(nullable = false)
   private boolean isAdmin;

   @ManyToMany
   @JoinTable(
           name = "user_authority",
           joinColumns = @JoinColumn(name = "user_id"), // ✅ FK는 user_id 사용
           inverseJoinColumns = @JoinColumn(name = "authority_name")
   )
   @Builder.Default
   private Set<Authority> authorities = new HashSet<>();

   @PrePersist
   public void prePersist() {
      this.userId = UUID.randomUUID().toString(); // ✅ userId 자동 생성
   }
}
