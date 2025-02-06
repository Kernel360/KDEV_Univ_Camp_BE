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
   private String userId; // ✅ String 타입으로 변경

   @Column(nullable = false, unique = true)
   private String id;  // ✅ 사용자가 입력하는 ID (예: username)

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
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "authority_name")
   )
   private Set<Authority> authorities = new HashSet<>();

   // ✅ userId를 자동 생성하는 로직 추가
   @PrePersist
   public void prePersist() {
      this.userId = UUID.randomUUID().toString();
   }
}
