package com.tistory.aircook.playground.service;


import com.google.common.collect.Lists;
import com.tistory.aircook.playground.domain.PeopleRequest;
import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.repository.PeopleBatchRepository;
import com.tistory.aircook.playground.repository.PeopleSimpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleService {

    private final PeopleSimpleRepository peopleSimpleRepository;

    private final PeopleBatchRepository peopleBatchRepository;

    private final SqlSessionFactory batchSqlSessionFactory;

    private final SqlSessionTemplate batchSqlSessionTemplate;

    public List<PeopleResponse> selectPeopleNormal(String searchName) {
        return peopleSimpleRepository.selectPeopleNormal(searchName);
    }

    public List<PeopleResponse> selectPeopleHandler() {
        //List<PeopleResponse> resultList = java.util.Collections.emptyList();
        List<PeopleResponse> resultList = com.google.common.collect.Lists.newArrayList();
        peopleSimpleRepository.selectPeopleHandler(resultContext -> {
            PeopleResponse response = resultContext.getResultObject();
            log.debug("response is [{}]", response);
            response.setName(response.getName() + " [handler]");
            resultList.add(response);
        });
        return resultList;
    }

    /**
     * Cursor를 사용하려면 해당 서비스 메서드에 @Transactional 어노테이션을 추가하여 트랜잭션 상태를 유지시켜야 하고,
     * 추가로 해당 메서드를 벗어나기 전에 Cursor를 사용하는 작업을 모두 끝마쳐야 한다.
     */
    @Transactional(transactionManager = "transactionManager") //--> 이거 안해주면 "A Cursor is already closed." 나온다.
    public List<PeopleResponse> selectPeopleCursor() {
        List<PeopleResponse> resultList = Lists.newArrayList();
        try (Cursor<PeopleResponse> cursorResponse = peopleSimpleRepository.selectPeopleCursor()) {
            for (PeopleResponse response : cursorResponse) {
                log.debug("response is [{}], current index is [{}]", response, cursorResponse.getCurrentIndex());
                response.setName(response.getName() + " [cursor]");
                resultList.add(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    /**
     * 결과 로그, 50000건 입력시 4188 ms
     * <pre>
     * Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@51d263ec] from current transaction
     * ==>  Preparing: INSERT INTO PEOPLES (NAME, BIRTH) VALUES (?, ?)
     * ==> Parameters: 사용자 498(String), 2025-03-14(String)
     * <==    Updates: 1
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@51d263ec]
     *
     * Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@51d263ec] from current transaction
     * ==>  Preparing: INSERT INTO PEOPLES (NAME, BIRTH) VALUES (?, ?)
     * ==> Parameters: 사용자 499(String), 2025-03-15(String)
     * <==    Updates: 1
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@51d263ec]
     * </pre>
     */
    @Transactional(transactionManager = "transactionManager")
    public void insertSimplePeoples() {

        int loopCount = 50_000;

        StopWatch stopWatch = new StopWatch("People Simple 대량입력 [" + loopCount + "]");
        stopWatch.start("Simple 대량입력 시작");
        LocalDate nowDate = LocalDate.now();

        for (int i = 0; i < loopCount; i++) {

            PeopleRequest peopleRequest = new PeopleRequest();
            peopleRequest.setName("사용자 " + i); //--> 이게 왜 되는거야?, 덧셈 연사자를 진행할때 연산자중 한 쪽이 String 형이면 나머지 쪽을 String 형태로 변환 한 다음 두 String형 문자열을 결합하는 방식
            peopleRequest.setBirth(String.valueOf(nowDate.plusDays(i)));

            peopleSimpleRepository.insertPeople(peopleRequest);
        }

        stopWatch.stop();
        //text block, java 15
        String stopWatchLogFormat = """
                
                ===========================================================
                Second           : {} s
                Millisecond      : {} ms
                Nanosecond       : {} ns
                ---------------------------------------------
                {}
                ===========================================================""";
        log.info(stopWatchLogFormat, stopWatch.getTotalTimeSeconds(), stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeNanos(), stopWatch.prettyPrint());
    }


    /**
     * 결과 로그, 50000건 입력시 2116 ms
     * <pre>
     * Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2eaa74b1] from current transaction
     * ==> Parameters: 사용자 498(String), 2025-03-14(String)
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2eaa74b1]
     *
     * Fetched SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2eaa74b1] from current transaction
     * ==> Parameters: 사용자 499(String), 2025-03-15(String)
     * Releasing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2eaa74b1]
     * </pre>
     */
    @Transactional(transactionManager = "batchTransactionManager")
    public void insertBatchPeoples() {

        int loopCount = 50_000;

        StopWatch stopWatch = new StopWatch("People Batch 대량입력 [" + loopCount + "]");
        stopWatch.start("Batch 대량입력 시작");
        LocalDate nowDate = LocalDate.now();

        for (int i = 0; i < loopCount; i++) {

            PeopleRequest peopleRequest = new PeopleRequest();
            peopleRequest.setName("사용자 " + i); //--> 이게 왜 되는거야?, 덧셈 연사자를 진행할때 연산자중 한 쪽이 String 형이면 나머지 쪽을 String 형태로 변환 한 다음 두 String형 문자열을 결합하는 방식
            peopleRequest.setBirth(String.valueOf(nowDate.plusDays(i)));

            peopleBatchRepository.insertPeople(peopleRequest);
        }

        stopWatch.stop();
        //text block, java 15
        String stopWatchLogFormat = """
                
                ===========================================================
                Second           : {} s
                Millisecond      : {} ms
                Nanosecond       : {} ns
                ---------------------------------------------
                {}
                ===========================================================""";
        log.info(stopWatchLogFormat, stopWatch.getTotalTimeSeconds(), stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeNanos(), stopWatch.prettyPrint());
    }

    /**
     * 결과 로그,
     * <pre>
     JDBC Connection [HikariProxyConnection@666663809 wrapping org.sqlite.jdbc4.JDBC4Connection@28fbe66b] will not be managed by Spring
     ==>  Preparing: INSERT INTO PEOPLES (NAME, BIRTH) VALUES (?, ?)
     ==> Parameters: 사용자 0(String), 2023-12-11(String)
     ==> Parameters: 사용자 1(String), 2023-12-12(String)
     ==> Parameters: 사용자 2(String), 2023-12-13(String)
     ==> Parameters: 사용자 3(String), 2023-12-14(String)
     ==> Parameters: 사용자 4(String), 2023-12-15(String)
     ==> Parameters: 사용자 5(String), 2023-12-16(String)
     ==> Parameters: 사용자 6(String), 2023-12-17(String)
     ==> Parameters: 사용자 7(String), 2023-12-18(String)
     ==> Parameters: 사용자 8(String), 2023-12-19(String)
     ==> Parameters: 사용자 9(String), 2023-12-20(String)
     *
     * 중요: @Transactional 설정하면 스프링에 의해 관리되고 flushStatements 가 동작되지 않음
     * 중요: sqlSessionTemplate으로 으로 실행해도 flushStatements 가 동작되지 않음
     * </pre>
     */
    //@Transactional(transactionManager = "batchTransactionManager")
    public void insertBatchPeoplesByUnit() {

        SqlSession sqlSession = batchSqlSessionFactory.openSession(ExecutorType.BATCH);

        int loopCount = 50;

        StopWatch stopWatch = new StopWatch("People Batch 대량입력(중간 commit) [" + loopCount + "]");
        stopWatch.start("Batch 대량입력(중간 commit) 시작");
        LocalDate nowDate = LocalDate.now();

        for (int i = 0; i < loopCount; i++) {

            PeopleRequest peopleRequest = new PeopleRequest();
            peopleRequest.setName("사용자 " + i);
            peopleRequest.setBirth(String.valueOf(nowDate.plusDays(i)));

            //sqlSessionTemplate으로 호출, flushStatements() 동작하지 않음
            //batchSqlSessionTemplate.insert("insertPeople", peopleRequest);

            //아래처럼  mapper를 찾아서 호출 가능하다.
            sqlSession.insert("insertPeople", peopleRequest);

            PeopleBatchRepository mapper = sqlSession.getMapper(PeopleBatchRepository.class);
            mapper.insertPeople(peopleRequest);

            if ((i + 1) % 10 == 0) {
                log.debug("[{}]번째, commit 실행", (i + 1));
                //List<BatchResult> batchResults = batchSqlSessionTemplate.flushStatements();
                List<BatchResult> batchResults = sqlSession.flushStatements();
                log.debug("[{}]번째, commit 실행 결과 [{}]", (i + 1), batchResults);
            }

        }

        stopWatch.stop();
        //text block, java 15
        String stopWatchLogFormat = """
                                
                ===========================================================
                Second           : {} s
                Millisecond      : {} ms
                Nanosecond       : {} ns
                ---------------------------------------------
                {}
                ===========================================================""";
        log.info(stopWatchLogFormat, stopWatch.getTotalTimeSeconds(), stopWatch.getTotalTimeMillis(), stopWatch.getTotalTimeNanos(), stopWatch.prettyPrint());

    }

}
