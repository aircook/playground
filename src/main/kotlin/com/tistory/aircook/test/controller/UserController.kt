package com.tistory.aircook.test.controller

import org.springframework.web.bind.annotation.*
import java.util.Optional;

@RestController
@RequestMapping("/user")
class UserController {

    @GetMapping
    fun getUser(@RequestParam(required = false) name: Optional<String>): String {
        return "Hello ${name.orElse("World")}"
    }

    @GetMapping("/{idx}")
    fun getUserDetail(@PathVariable("idx") idx: Int): String {
        return "Hello User Detail $idx"
    }

}