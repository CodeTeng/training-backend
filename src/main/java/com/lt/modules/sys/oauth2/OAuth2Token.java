package com.lt.modules.sys.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description: token
 * @author: ~Teng~
 * @date: 2022/11/17 11:10
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
