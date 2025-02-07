package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

   @Id
   @Column(name = "authority_name", length = 50)
   private String authority;

   public String getAuthority() {
      return authority;
   }
}
