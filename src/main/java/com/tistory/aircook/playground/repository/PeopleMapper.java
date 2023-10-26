package com.tistory.aircook.playground.repository;

import com.tistory.aircook.playground.domain.PeopleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

@Mapper
public interface PeopleMapper {

    List<PeopleResponse> selectPeople();

    void selectStreamPeople(ResultHandler<PeopleResponse> handler);

}
