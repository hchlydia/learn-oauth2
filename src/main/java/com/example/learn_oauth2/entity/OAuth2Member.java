package com.example.learn_oauth2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "oauth2_member")
@Getter
@Setter
public class OAuth2Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth2_member_id")
    private Integer oauth2MemberId;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
