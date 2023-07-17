package org.web.admin.security.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

// @Configuration
public class UserPasswdAuthenticationProvider implements AuthenticationProvider {
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());
        
        String _password = "123123";
        if (StringUtils.equals(authentication.getName(), username) && StringUtils.equals(password, _password)) {
            return UsernamePasswordAuthenticationToken.authenticated(username, password, authentication.getAuthorities()); // (2)
        } else {
            throw new RuntimeException("could not login");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}

