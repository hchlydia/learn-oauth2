package com.example.learn_oauth2.security;

import com.example.learn_oauth2.entity.OAuth2Member;
import com.example.learn_oauth2.repository.OAuth2MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@Component
public class MyOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private OAuth2MemberRepository oAuth2MemberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //取得OAuth2User和OAuth2UserRequest中的資訊
        String email = Objects.toString(oAuth2User.getAttributes().get("email"), null);
        String name = Objects.toString(oAuth2User.getAttributes().get("name"), null);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getName();

        String accessToken = userRequest.getAccessToken().getTokenValue();
        LocalDateTime expiresAt = LocalDateTime.ofInstant(userRequest.getAccessToken().getExpiresAt(), ZoneId.systemDefault());

        //查詢provider+providerId在資料庫有無紀錄
        Optional<OAuth2Member> optional = oAuth2MemberRepository.findByProviderAndProviderId(provider, providerId);
        if (optional.isEmpty()) {
            //創建新的OAuth2Member
            OAuth2Member oAuth2Member = new OAuth2Member();
            oAuth2Member.setProvider(provider);
            oAuth2Member.setProviderId(providerId);
            oAuth2Member.setName(name);
            oAuth2Member.setEmail(email);
            oAuth2Member.setAccessToken(accessToken);
            oAuth2Member.setExpiresAt(expiresAt);

            oAuth2MemberRepository.save(oAuth2Member);
        }

        //處理好後返回原本的OAuth2User
        return oAuth2User;
    }
}
