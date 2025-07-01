package com.tistory.aircook.playground.controller;

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
@RestController
@RequiredArgsConstructor
//@RequestMapping("/")
@Slf4j
public class RootController {

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