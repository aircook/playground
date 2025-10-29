package com.tistory.aircook.playground.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
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

    /*private final Executor executor = Executors.newFixedThreadPool(10, r -> {
        Thread t = new Thread(r);
        //t.setName("async-thread-" + t.getId());
        t.setName("async-thread-" + threadNumber.getAndIncrement());
        //t.setDaemon(true); // 프로그램 종료를 방해하지 않는 데몬 스레드를 사용한다
        //데몬 스레드는 메인 애플리케이션이 종료될 때 즉시 종료됨
        //장점: 종료를 막지 않음, /단점: 실행 중이던 비동기 작업이 중간에 끊길 수 있음
        //applicationTaskExecutor의 daemon값은 false이다.
        return t;
    });*/

    private TaskExecutor executor; // Spring Boot 기본 Bean

    public AsyncController(@Qualifier("applicationTaskExecutor") TaskExecutor executor) {
        this.executor = executor;
    }

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

    @GetMapping("/complex")
    public String complex(@RequestParam String user) throws ExecutionException, InterruptedException {
        CompletableFuture

                // 1️⃣ 입력 검증 (동기)
                .completedFuture(user)
                // thenApply, 결과 변환동기 처리, 이전 결과를 받아 새 결과 반환
                .thenApply(u -> {
                    validate(u);
                    return u;
                })

                // 2️⃣ 비동기 회원가입 처리
                // thenCompose 중첩 Future 평탄화 Future를 반환하는 작업 체이닝
                .thenComposeAsync(u ->
                                CompletableFuture.supplyAsync(() -> register(u), executor)
                        , executor)

                // 3️⃣ 회원가입 결과를 다음 단계로 전달
                // thenApplyAsync, 비동기 결과 변환, 지정된 Executor에서 실행
                .thenApplyAsync(u -> {
                    log(user, "회원가입시작");
                    return u;
                }, executor)

                // 4️⃣ 병렬로 이메일 발송 & 알림 발송 (둘 다 완료 대기)
                // thenCompose 중첩 Future 평탄화Future를 반환하는 작업 체이닝
                .thenComposeAsync(u -> {
                    CompletableFuture<Void> emailTask = CompletableFuture
                            .runAsync(() -> email(u), executor);

                    CompletableFuture<Void> pushTask = CompletableFuture
                            .runAsync(() -> push(u), executor);

                    // 두 작업 모두 완료 후 registeredUser 반환
                    //allOf 병렬 작업 대기 여러 Future 모두 완료 대기
                    return CompletableFuture.allOf(emailTask, pushTask)
                            .thenApply(v -> u);
                }, executor)

                // 5️⃣ 분석 데이터 수집 (결과에 영향 없음)
                // thenApplyAsync, 비동기 결과 변환, 지정된 Executor에서 실행
                .thenApplyAsync(u -> {
                    trace(u);
                    return u;
                }, executor)

                // 6️⃣ 조건부 처리: VIP 사용자에게 특별 혜택
                // thenApplyAsync, 비동기 결과 변환, 지정된 Executor에서 실행
                .thenApplyAsync(u -> {
                    if (u.equals("VIP")) {
                        benefit(u);
                    }
                    return u;
                }, executor)

                // 7️⃣ 응답 생성
                // thenApply결과 변환, 동기 처리, 이전 결과를 받아 새 결과 반환
                .thenApply(u -> Map.of("id", "1", "user", u)
                )

                // 8️⃣ 타임아웃 설정 (3초)
                // orTimeout 타임아웃 설정, 지정 시간 초과 시 예외 발생
                .orTimeout(3, TimeUnit.SECONDS)

                // 9️⃣ 예외 처리 (특정 예외별로)
                // exceptionally, 예외 처리, 에러 발생 시 대체 값 반환
                .exceptionally(ex -> {
                    //실패로 저장
                    log(user, MessageFormat.format("회원가입실패 {0}", ex.getMessage()));
                    return Map.of("id", "1", "user", user);
                })

                // 🔟 완료 후 후처리 (성공/실패 상관없이)
                // whenComplete, 최종 후처리, 성공/실패 상관없이 실행
                .whenComplete((response, throwable) -> {
                    if (throwable == null) {
                        log(user, MessageFormat.format("최종 응답 완료: {0}", response));
                    } else {
                        log(user, MessageFormat.format("최종 처리 중 오류: {0}", throwable.getMessage()));
                    }
                });

        return "completed";
    }

    /**
     * 쓰레드를 지연시키는 메소드
     *
     * @param delaySecond 밀리세컨드
     */
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
