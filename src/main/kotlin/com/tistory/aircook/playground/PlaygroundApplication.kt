package com.tistory.aircook.playground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlaygroundApplication

//entry point
fun main(args: Array<String>) {
	runApplication<PlaygroundApplication>(*args)
}
