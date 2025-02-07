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
   private String userId;

   @Column(nullable = false, unique = true)
   private String id;

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String nickname;

   @Column(nullable = false)
   private boolean activated;

   @Column(nullable = false)
   private boolean isAdmin;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
           name = "user_authority",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "authority_name")
   )
   @Builder.Default
   private Set<Authority> authorities = new HashSet<>();

   public Collection<? extends GrantedAuthority> getAuthorities() {
      System.out.println("🔍 [DEBUG] User.getAuthorities() 호출됨");

      if (authorities.isEmpty()) {
         System.out.println("❌ [ERROR] 사용자에게 할당된 권한이 없습니다!");
      } else {
         authorities.forEach(auth ->
                 System.out.println("✅ [DEBUG] 사용자 권한 로드: " + auth.getAuthority())
         );
      }

      return authorities.stream()
              .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
              .collect(Collectors.toList());
   }

   @PrePersist
   public void prePersist() {
      if (this.userId == null || this.userId.isEmpty()) {
         this.userId = UUID.randomUUID().toString();
         System.out.println("✅ [DEBUG] 새 UUID 생성됨: " + this.userId);
      }
   }
}
