package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;

   @Column(nullable = false, unique = true)
   private String username;

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String nickname;

   @Column(nullable = false)
   private boolean activated;

   // ✅ isAdmin 필드 추가
   @Column(nullable = false)
   private boolean isAdmin;

   // ✅ Getter 추가 (isAdmin() 메서드 문제 해결)
   public boolean isAdmin() {
      return isAdmin;
   }
}
