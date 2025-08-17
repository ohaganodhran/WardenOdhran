package com.warden.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Credential")
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "User_id")
    private User user;

    @Column(name  = "site_name", unique = false)
    private String siteName;

    @Column(name = "site_username")
    private String username;

    @Column(name  = "site_password_hashed")
    private String passwordHash;

    @Column(name = "notes")
    private String notes;
}
