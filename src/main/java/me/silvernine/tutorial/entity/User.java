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
   private String id;  // ✅ username → id 변경

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String nickname;

   @Column(nullable = false)
   private boolean activated;

   @Column(nullable = false)
   private boolean isAdmin; // ✅ isAdmin 필드 추가

   // ✅ Getter 추가 (CustomUserDetailsService에서 사용 가능하도록)
   public boolean isAdmin() {
      return isAdmin;
   }
}
