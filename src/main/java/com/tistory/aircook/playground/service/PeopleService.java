package com.tistory.aircook.playground.service;


import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.repository.PeopleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleService {

    private final PeopleMapper peopleMapper;

    public List<PeopleResponse> selectPeople() {
        return peopleMapper.selectPeople();
    }

    public void selectStreamPeople() {
        peopleMapper.selectStreamPeople(new ResultHandler<PeopleResponse>() {
            @Override
            public void handleResult(ResultContext<? extends PeopleResponse> resultContext) {
                PeopleResponse response = resultContext.getResultObject();
                log.debug("response is [{}]", response);
            }
        });
    }

}
