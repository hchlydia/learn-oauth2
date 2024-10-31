package com.example.learn_oauth2.security;

import com.example.learn_oauth2.entity.OAuth2Member;
import com.example.learn_oauth2.repository.OAuth2MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

/**
 * OIDC: Open ID Connect
 * 針對支援OIDC的網站，Spring Security就會從OAuth2UserService，改為使用這個OidcUserService的loadUser()方法
 * ex: Google
 **/
@Component
public class MyOidcUserService extends OidcUserService {

    @Autowired
    private OAuth2MemberRepository oAuth2MemberRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        //取得OidcUser和OidcUserRequest中的資訊
        String email = Objects.toString(oidcUser.getAttributes().get("email"), null);
        String name = Objects.toString(oidcUser.getAttributes().get("name"), null);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oidcUser.getName();

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
        return oidcUser;
    }
}
