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
   private boolean admin; // ✅ 필드명 변경 (isAdmin → admin)

   // ✅ Getter 추가 (CustomUserDetailsService에서 사용 가능하도록)
   public boolean getAdmin() { // ✅ getter 이름을 수정하여 JPA 매핑 문제 방지
      return admin;
   }
}
