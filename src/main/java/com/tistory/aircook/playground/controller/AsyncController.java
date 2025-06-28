package com.tistory.aircook.playground.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * thenApply() 데이터 변형, map
 * thenAccept() 데이터 소비 (출력/저장), forEach
 * thenRun() 결과와 무관한 후속 실행
 * thenCompose() 앞의 결과로 다음 비동기 작업 연동, flatMap
 * thenCombine() 두 작업 결과를 병렬로 조합
 *
 * @author : francis.lee
 * @since : 2025-06-26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/async")
@Slf4j
public class AsyncController {

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final Executor executor = Executors.newFixedThreadPool(10, r -> {
        Thread t = new Thread(r);
        //t.setName("async-thread-" + t.getId());
        t.setName("async-thread-" + threadNumber.getAndIncrement());
        t.setDaemon(true); // 프로그램 종료를 방해하지 않는 데몬 스레드를 사용한다
        return t;
    });

    @GetMapping("/run-async")
    public String runAsync() throws ExecutionException, InterruptedException {

        log.info("Available Processors is [{}]", Runtime.getRuntime().availableProcessors());

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        //메인쓰레드 3초지연
        this.delayTask(3_000);

        //runAsync, 반환값이 없는 경우, 비동기로 작업 실행
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.debug("Thread 2: [{}]", Thread.currentThread().getName());
            //비동기 쓰레드 3초 지연
            this.delayTask(3_000);
            log.debug("Thread 3: [{}]", Thread.currentThread().getName());
        }, executor);

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

    //http://localhost:8080/async/supply-async-then-apply-exceptionally
    @GetMapping("/supply-async-then-apply-exceptionally")
    public String supplyAsyncThenApplyExceptionally() throws ExecutionException, InterruptedException {

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
            if (StringUtils.countOccurrencesOf(s, "Async") > 0) {
                throw new RuntimeException(MessageFormat.format("Consumer 내부에서 예외 [{0}]", s.toUpperCase()));
            }
            //대문자로 변경
            return s.toUpperCase();
        })
        //예외처리
        .exceptionally(t -> {
            log.debug("Thread 5: [{}]", Thread.currentThread().getName());
            t.printStackTrace();
            return t.getMessage().toUpperCase();
        });

        //메인쓰레드에서 값이 필요하면 get()사용, 하지만 blocking이 된다.
        //String asyncResult = future.get();
        //log.debug("Return result is [{}]", asyncResult);
        log.debug("Thread 6: [{}]", Thread.currentThread().getName());

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
