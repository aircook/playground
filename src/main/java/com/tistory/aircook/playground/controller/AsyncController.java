package com.tistory.aircook.playground.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
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
    public String runAsync() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        //메인쓰레드 3초지연
        this.delayTask(3_000);

        //runAsync, 반환값이 없는 경우, 비동기로 작업 실행
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.debug("Thread 2: [{}]", Thread.currentThread().getName());
            //비동기 쓰레드 3초 지연
            this.delayTask(3_000);
            log.debug("Thread 3: [{}]", Thread.currentThread().getName());
        });

        //반환값이 없음으로 필요없음
        //future.get();
        log.debug("Thread 4: [{}]", Thread.currentThread().getName());

        return "completed";

    }

    @GetMapping("/supply-async")
    public String supplyAsync() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        //메인쓰레드 3초지연
        this.delayTask(3_000);

        //supplyAsync, 반환값이 있는 경우, 비동기로 작업 실행
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.debug("Thread 2: [{}]", Thread.currentThread().getName());
            //비동기 쓰레드 3초 지연
            this.delayTask(3_000);
            log.debug("Thread 3: [{}]", Thread.currentThread().getName());
            return "Async Call is Completed";
        });

        //메인쓰레드에서 값이 필요하면 get()사용, 하지만 blocking이 된다.
        //String asyncResult = future.get();
        //log.debug("Return result is [{}]", asyncResult);

        log.debug("Thread 4: [{}]", Thread.currentThread().getName());

        return "completed";

    }

    //1 --> 2 --> 5 --> 3 --> 4
    //http://localhost:8080/async/supply-async-then-apply
    @GetMapping("/supply-async-then-apply")
    public String supplyAsyncThenApply() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        //메인쓰레드 3초지연
        this.delayTask(3_000);

        //supplyAsync, 반환값이 있는 경우, 비동기로 작업 실행
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.debug("Thread 2: [{}]", Thread.currentThread().getName());
            //비동기 쓰레드 3초 지연
            this.delayTask(3_000);
            log.debug("Thread 3: [{}]", Thread.currentThread().getName());
            return "Async Call is Completed";
        })
        //thenApply는 값을 받아서 다른 값을 반환
        .thenApply(s -> {
            log.debug("Thread 4: [{}]", Thread.currentThread().getName());
            //여기에서 콜백을 받아 완료시 다른 작업을 진행할 수 있다.
            //대문자로 변경
            return s.toUpperCase();
        });

        //메인쓰레드에서 값이 필요하면 get()사용, 하지만 blocking이 된다.
        //String asyncResult = future.get();
        //log.debug("Return result is [{}]", asyncResult);
        log.debug("Thread 5: [{}]", Thread.currentThread().getName());

        return "completed";

    }

    //1 --> 5 --> 2 --> 3 --> 4
    //http://localhost:8080/async/supply-async-then-apply-async
    @GetMapping("/supply-async-then-apply-async")
    public String supplyAsyncThenApplyAsync() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        //메인쓰레드 3초지연
        this.delayTask(3_000);

        //supplyAsync, 반환값이 있는 경우, 비동기로 작업 실행
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.debug("Thread 2: [{}]", Thread.currentThread().getName());
            //비동기 쓰레드 3초 지연
            this.delayTask(3_000);
            log.debug("Thread 3: [{}]", Thread.currentThread().getName());
            return "Async Call is Completed";
        })
        //thenApply는 값을 받아서 다른 값을 반환
        .thenApplyAsync(s -> {
            log.debug("Thread 4: [{}]", Thread.currentThread().getName());
            //여기에서 콜백을 받아 완료시 다른 작업을 진행할 수 있다.
            //대문자로 변경
            return s.toUpperCase();
        });

        //메인쓰레드에서 값이 필요하면 get()사용, 하지만 blocking이 된다.
        //String asyncResult = future.get();
        //log.debug("Return result is [{}]", asyncResult);
        log.debug("Thread 5: [{}]", Thread.currentThread().getName());

        return "completed";

    }


    /**
     *
     * 쓰레드를 지연시키는 메소드
     * @param delaySecond 밀리세컨드
     */
    private void delayTask(int delaySecond) {
        try {
            Thread.sleep(delaySecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
