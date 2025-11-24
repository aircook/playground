package com.tistory.aircook.playground.controller;

import com.tistory.aircook.playground.repository.PeopleSimpleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Tag(name = "Security", description = "보안 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/security")
@Slf4j
public class SecurityController {

    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "USER 역할 API", description = "USER 역할이 필요한 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "미인증"),
            @ApiResponse(responseCode = "403", description = "권한 부족")
    })
    @GetMapping("/api1")
    public ResponseEntity<String> api1() {
        return new ResponseEntity<>("api1 입니다.", HttpStatus.OK);
    }

    @Operation(summary = "ADMIN 역할 API", description = "ADMIN 역할이 필요한 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "미인증"),
            @ApiResponse(responseCode = "403", description = "권한 부족")
    })
    @GetMapping("/api2")
    public ResponseEntity<String> api2() {
        return new ResponseEntity<>("api2 입니다.", HttpStatus.OK);
    }

    @Operation(summary = "로그인한 사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "미인증")
    })
    @GetMapping("/user")
    public ResponseEntity<String> user(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userDetails.getUsername(), HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 암호화 (관리용)", description = "입력한 비밀번호를 bcrypt로 암호화합니다. (관리자만 접근 가능)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요")
    })
    @GetMapping("/password")
    public ResponseEntity<Map<String, String>> password() {

        log.debug("{}", passwordEncoder.encode("password01"));
        log.debug("{}", passwordEncoder.encode("password02"));
        log.debug("{}", passwordEncoder.encode("password03"));
        log.debug("{}", passwordEncoder.encode("password04"));
        log.debug("{}", passwordEncoder.encode("password05"));

        return new ResponseEntity<>(Map.of(
                "user01", passwordEncoder.encode("password01"),
                "user02", passwordEncoder.encode("password02"),
                "user03", passwordEncoder.encode("password03"),
                "user04", passwordEncoder.encode("password04"),
                "user05", passwordEncoder.encode("password05")
        ), HttpStatus.OK);
    }
}
