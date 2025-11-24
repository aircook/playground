package com.tistory.aircook.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Tag(name = "Async", description = "비동기 작업 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/async")
@Slf4j
public class AsyncController {

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private TaskExecutor executor;

    public AsyncController(@Qualifier("applicationTaskExecutor") TaskExecutor executor) {
        this.executor = executor;
    }

    @Operation(summary = "비동기 runAsync 테스트", description = "반환값이 없는 비동기 작업을 수행합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/run-async")
    public String runAsync() throws ExecutionException, InterruptedException {

        log.info("Available Processors is [{}]", Runtime.getRuntime().availableProcessors());

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        this.delayTask(3_000);

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.debug("Thread 2: [{}]", Thread.currentThread().getName());
            this.delayTask(3_000);
            log.debug("Thread 3: [{}]", Thread.currentThread().getName());
        }, executor);

        log.debug("Thread 4: [{}]", Thread.currentThread().getName());

        return "completed";

    }

    @Operation(summary = "비동기 supplyAsync 테스트", description = "반환값이 있는 비동기 작업을 수행합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/supply-async")
    public String supplyAsync() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        this.delayTask(3_000);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.debug("Thread 2: [{}]", Thread.currentThread().getName());
            this.delayTask(3_000);
            log.debug("Thread 3: [{}]", Thread.currentThread().getName());
            return "Async Call is Completed";
        });

        log.debug("Thread 4: [{}]", Thread.currentThread().getName());

        return "completed";

    }

    @Operation(summary = "supplyAsync + thenApply 테스트", description = "비동기 작업 결과를 변환합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/supply-async-then-apply")
    public String supplyAsyncThenApply() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        this.delayTask(3_000);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    log.debug("Thread 2: [{}]", Thread.currentThread().getName());
                    this.delayTask(3_000);
                    log.debug("Thread 3: [{}]", Thread.currentThread().getName());
                    return "Async Call is Completed";
                })
                .thenApply(s -> {
                    log.debug("Thread 4: [{}]", Thread.currentThread().getName());
                    return s.toUpperCase();
                });

        log.debug("Thread 5: [{}]", Thread.currentThread().getName());

        return "completed";
    }

    @Operation(summary = "supplyAsync + thenApplyAsync 테스트", description = "비동기 작업 결과를 비동기로 변환합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/supply-async-then-apply-async")
    public String supplyAsyncThenApplyAsync() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        this.delayTask(3_000);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    log.debug("Thread 2: [{}]", Thread.currentThread().getName());
                    this.delayTask(3_000);
                    log.debug("Thread 3: [{}]", Thread.currentThread().getName());
                    return "Async Call is Completed";
                })
                .thenApplyAsync(s -> {
                    log.debug("Thread 4: [{}]", Thread.currentThread().getName());
                    return s.toUpperCase();
                });

        log.debug("Thread 5: [{}]", Thread.currentThread().getName());

        return "completed";

    }

    @Operation(summary = "supplyAsync + exceptionally 테스트", description = "비동기 작업 중 예외 처리를 합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/supply-async-then-apply-exceptionally")
    public String supplyAsyncThenApplyExceptionally() throws ExecutionException, InterruptedException {

        log.debug("Thread 1: [{}]", Thread.currentThread().getName());
        this.delayTask(3_000);

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    log.debug("Thread 2: [{}]", Thread.currentThread().getName());
                    this.delayTask(3_000);
                    log.debug("Thread 3: [{}]", Thread.currentThread().getName());
                    return "Async Call is Completed";
                })
                .thenApply(s -> {
                    log.debug("Thread 4: [{}]", Thread.currentThread().getName());
                    if (s.contains("Async")) {
                        throw new RuntimeException(MessageFormat.format("Consumer 내부에서 예외 [{0}]", s.toUpperCase()));
                    }
                    return s.toUpperCase();
                })
                .exceptionally(t -> {
                    log.debug("Thread 5: [{}]", Thread.currentThread().getName());
                    t.printStackTrace();
                    return t.getMessage().toUpperCase();
                });

        log.debug("Thread 6: [{}]", Thread.currentThread().getName());

        return "completed";
    }

    @Operation(summary = "복합 비동기 워크플로우", description = "회원가입 프로세스를 비동기로 처리합니다. (입력검증 -> 회원가입 -> 이메일/푸시 -> 혜택부여)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패")
    })
    @GetMapping("/complex")
    public String complex(
            @Parameter(description = "사용자명") @RequestParam String user) throws ExecutionException, InterruptedException {
        CompletableFuture
                .completedFuture(user)
                .thenApply(u -> {
                    validate(u);
                    return u;
                })
                .thenComposeAsync(u ->
                                CompletableFuture.supplyAsync(() -> register(u), executor)
                        , executor)
                .thenApplyAsync(u -> {
                    log(user, "회원가입시작");
                    return u;
                }, executor)
                .thenComposeAsync(u -> {
                    CompletableFuture<Void> emailTask = CompletableFuture
                            .runAsync(() -> email(u), executor);

                    CompletableFuture<Void> pushTask = CompletableFuture
                            .runAsync(() -> push(u), executor);

                    return CompletableFuture.allOf(emailTask, pushTask)
                            .thenApply(v -> u);
                }, executor)
                .thenApplyAsync(u -> {
                    trace(u);
                    return u;
                }, executor)
                .thenApplyAsync(u -> {
                    if (u.equals("VIP")) {
                        benefit(u);
                    }
                    return u;
                }, executor)
                .thenApply(u -> Map.of("id", "1", "user", u)
                )
                .orTimeout(3, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    log(user, MessageFormat.format("회원가입실패 {0}", ex.getMessage()));
                    return Map.of("id", "1", "user", user);
                })
                .whenComplete((response, throwable) -> {
                    if (throwable == null) {
                        log(user, MessageFormat.format("최종 응답 완료: {0}", response));
                    } else {
                        log(user, MessageFormat.format("최종 처리 중 오류: {0}", throwable.getMessage()));
                    }
                });

        return "completed";
    }

    private void delayTask(int delaySecond) {
        try {
            Thread.sleep(delaySecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String validate(String user) {
        log.debug("[{}]에 대한 검증 ", user);
        return user;
    }

    private String register(String user) {
        log.debug("[{}]에 대한 회원가입", user);
        return user;
    }

    private String email(String user) {
        log.debug("[{}]에 대한 이메일전송", user);
        return user;
    }

    private String push(String user) {
        log.debug("[{}]에 대한 푸시전송", user);
        return user;
    }

    private String trace(String user) {
        log.debug("[{}]에 대한 데이터분석", user);
        return user;
    }

    private String benefit(String user) {
        log.debug("[{}]에 대한 혜택부여", user);
        return user;
    }

    private String log(String user, String message) {
        log.debug("[{}]에 대한 기록: [{}]", user, message);
        return user;
    }
}

