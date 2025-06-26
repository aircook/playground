package com.tistory.aircook.playground.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/async")
@Slf4j
public class AsyncController {

    @GetMapping("/run-async")
    public void runAsync() throws ExecutionException, InterruptedException {

        //runAsync, 반환값이 없는 경우, 비동기로 작업 실행
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.debug("Thread: [{}]", Thread.currentThread().getName());
        });

        future.get();
        log.debug("Thread: [{}]", Thread.currentThread().getName());

    }

    @GetMapping("/supply-async")
    public void supplyAsync() throws ExecutionException, InterruptedException {

        //supplyAsync, 반환값이 있는 경우, 비동기로 작업 실행
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.debug("Thread: [{}]", Thread.currentThread().getName());
            return "Async call is completed";
        });

        String asyncResult = future.get();
        log.debug("return result is [{}]", asyncResult);

        log.debug("Thread: [{}]", Thread.currentThread().getName());

    }

}
