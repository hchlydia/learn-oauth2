package com.example.learn_oauth2.repository;

import com.example.learn_oauth2.entity.OAuth2Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2MemberRepository extends JpaRepository<OAuth2Member, Integer> {

    Optional<OAuth2Member> findByProviderAndProviderId(String provider, String providerId);
}
