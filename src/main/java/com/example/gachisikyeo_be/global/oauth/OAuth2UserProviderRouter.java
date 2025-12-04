package com.example.gachisikyeo_be.global.oauth;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserProviderRouter implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
}
