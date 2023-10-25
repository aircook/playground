package com.tistory.aircook.playground.service;


import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.repository.PeopleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PeopleMapper peopleMapper;

    public List<PeopleResponse> selectPeople() {
        return peopleMapper.selectPeople();
    }

}
