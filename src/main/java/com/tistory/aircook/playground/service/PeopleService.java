package com.tistory.aircook.playground.service;


import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.repository.PeopleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleService {

    private final PeopleMapper peopleMapper;

    public List<PeopleResponse> selectPeopleNoraml() {
        return peopleMapper.selectPeopleNormal();
    }

    public void selectPeopleHandler() {
        peopleMapper.selectPeopleHandler(resultContext -> {
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
        try (Cursor<PeopleResponse> cursorResponse = peopleMapper.selectPeopleCursor()) {
            for (PeopleResponse response : cursorResponse) {
                log.debug("response is [{}], current index is [{}]", response, cursorResponse.getCurrentIndex());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
