package com.tistory.aircook.playground.controller;

import com.tistory.aircook.playground.repository.PeopleSimpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
@Slf4j
public class SecurityController {

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/api1")
    public ResponseEntity<String> api1() {
        return new ResponseEntity<>("api1 입니다.", HttpStatus.OK);
    }

    @GetMapping("/api2")
    public ResponseEntity<String> api2() {
        return new ResponseEntity<>("api2 입니다.", HttpStatus.OK);
    }

    /**
     * 로그인한 사용자 정보 가져오기
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<String> user(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userDetails.getUsername(), HttpStatus.OK);
    }


    @GetMapping("/password")
    public ResponseEntity<Map<String, String>> password() {

        log.debug("{}", passwordEncoder.encode("password01"));
        log.debug("{}", passwordEncoder.encode("password02"));
        log.debug("{}", passwordEncoder.encode("password03"));
        log.debug("{}", passwordEncoder.encode("password04"));
        log.debug("{}", passwordEncoder.encode("password05"));

//        String encodingId = "{bcrypt}";
//        PasswordEncoder encoder = new BCryptPasswordEncoder();

        return new ResponseEntity<>(Map.of(
                "user01", passwordEncoder.encode("password01"),
                "user02", passwordEncoder.encode("password02"),
                "user03", passwordEncoder.encode("password03"),
                "user04", passwordEncoder.encode("password04"),
                "user05", passwordEncoder.encode("password05")
        ), HttpStatus.OK);
    }
}

