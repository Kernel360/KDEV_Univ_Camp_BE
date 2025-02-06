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
   private String id;

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String nickname;

   @Column(nullable = false)
   private boolean activated;

   @Column(nullable = false)
   private boolean admin; // ✅ 필드명 유지 (Getter 자동 생성됨)
}
