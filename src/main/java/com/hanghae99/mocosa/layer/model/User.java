package com.hanghae99.mocosa.layer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_email", columnNames = { "email" })})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    private UserRoleEnum role;

    public User(String email, String password, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        if (role.equals("셀러")) {
            this.role = UserRoleEnum.ADMIN;
        }else {
            this.role = UserRoleEnum.USER;
        }
    }
}