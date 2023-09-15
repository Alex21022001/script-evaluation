package com.alexsitiy.script.evaluation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * This is an implementation of {@link OAuth2UserService} that is used for
 * creating Authentication Principal that will be stored in HttpSession.
 * It utilizes {@link KeycloakRoleConverter} to appropriately retrieve roles from Access token.
 */
@Component
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final JwtDecoder jwtDecoder;
    private final KeycloakRoleConverter keycloakRoleConverter;

    @Autowired
    public CustomOidcUserService(JwtDecoder jwtDecoder, KeycloakRoleConverter keycloakRoleConverter) {
        this.jwtDecoder = jwtDecoder;
        this.keycloakRoleConverter = keycloakRoleConverter;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        String token = userRequest.getAccessToken().getTokenValue();
        Jwt jwt = jwtDecoder.decode(token);
        Collection<GrantedAuthority> authorities = keycloakRoleConverter.convert(jwt);
        return new DefaultOidcUser(authorities, userRequest.getIdToken(), "sub");
    }
}
