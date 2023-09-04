package com.tistory.aircook.test.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional;

@RestController
@RequestMapping("/user")
class UserController {

    @GetMapping
    fun getUser(@RequestParam(required = false) name: Optional<String>): String {
        return "Hello ${name.orElse("World")}"
    }

}