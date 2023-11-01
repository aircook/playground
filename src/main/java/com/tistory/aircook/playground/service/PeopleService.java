package com.tistory.aircook.playground.service;


import com.tistory.aircook.playground.domain.PeopleRequest;
import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.repository.PeopleBatchRespository;
import com.tistory.aircook.playground.repository.PeopleSimpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleService {

    private final PeopleSimpleRepository peopleSimpleRepository;

    private final PeopleBatchRespository peopleBatchRespository;

    public List<PeopleResponse> selectPeopleNoraml() {
        return peopleSimpleRepository.selectPeopleNormal();
    }

    public void selectPeopleHandler() {
        peopleSimpleRepository.selectPeopleHandler(resultContext -> {
            PeopleResponse response = resultContext.getResultObject();
            log.debug("response is [{}]", response);
        });
    }

    /**
     * Cursor를 사용하려면 해당 서비스 메서드에 @Transactional 어노테이션을 추가하여 트랜잭션 상태를 유지시켜야 하고,
     * 추가로 해당 메서드를 벗어나기 전에 Cursor를 사용하는 작업을 모두 끝마쳐야 한다.
     */
    @Transactional //--> 이거 안해주면 "A Cursor is already closed." 나온다.
    public void selectPeopleCursor() {
        try (Cursor<PeopleResponse> cursorResponse = peopleSimpleRepository.selectPeopleCursor()) {
            for (PeopleResponse response : cursorResponse) {
                log.debug("response is [{}], current index is [{}]", response, cursorResponse.getCurrentIndex());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(transactionManager = "batchTransactionManager")
    public void insertPeoples() {

        for (int i = 0; i < 500; i++) {

            PeopleRequest peopleRequest = new PeopleRequest();
            peopleRequest.setName(String.valueOf(i));
            peopleRequest.setBirth(String.valueOf(i));
            peopleBatchRespository.insertPeople(peopleRequest);
        }
    }

}
