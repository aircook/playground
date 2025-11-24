package com.tistory.aircook.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : francis.lee
 * @since : 2025-07-01
 */
@Tag(name = "Root", description = "루트 API")
@RestController
@RequiredArgsConstructor
//@RequestMapping("/")
@Slf4j
public class RootController {

    @Operation(summary = "홈 화면", description = "API 문서로 이동할 수 있는 링크를 제공합니다.")
    @GetMapping("/")
    public ResponseEntity<String> root() {
        String content = """
            <div>
            root입니다.
            <ul>
                <li><a href = "/login">login</li>
                <li><a href = "/security/user">user (Role: USER, ADMIN)</li>
                <li><a href = "/security/api1">api1 (Role: USER)</li>
                <li><a href = "/security/api2">api2 (Role: ADMIN)</li>
            </ul>
            </div>
        """;

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

}