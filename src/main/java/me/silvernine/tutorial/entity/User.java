package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

   @Id
   private String id;  // ✅ id가 PK이므로 유지

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
           joinColumns = @JoinColumn(name = "id"),
           inverseJoinColumns = @JoinColumn(name = "authority_name")
   )
   @Builder.Default
   private Set<Authority> authorities = new HashSet<>();
}
