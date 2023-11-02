package com.tistory.aircook.playground.repository;

import com.tistory.aircook.playground.config.database.mapper.BatchMapper;
import com.tistory.aircook.playground.domain.PeopleRequest;

//@Mapper
@BatchMapper
public interface PeopleBatchRepository {

    int insertPeople(PeopleRequest peopleRequest);
}
