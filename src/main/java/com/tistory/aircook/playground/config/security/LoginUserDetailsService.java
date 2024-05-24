package com.tistory.aircook.playground.config.security;

import com.tistory.aircook.playground.domain.LoginResponse;
import com.tistory.aircook.playground.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginResponse response = loginService.selectLogin(username);
        log.debug("response is [{}]", response);

        if (response == null){
            throw new UsernameNotFoundException("Could not found user" + username);
        }

        //login service를 만들고 di를 통해서 여기서 해당되는 login 객체를 가져와서 매핑한다.
        return User.builder()
                .username(response.getUserid())
                .password(response.getPassword())
                .roles(response.getRole())
                .build();
    }
}
