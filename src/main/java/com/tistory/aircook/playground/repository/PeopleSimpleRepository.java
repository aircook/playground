package com.tistory.aircook.playground.repository;

import com.tistory.aircook.playground.domain.PeopleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

@Mapper
public interface PeopleSimpleRepository {

    // 그냥 리스트 불러올 때
    List<PeopleResponse> selectPeopleNormal();

    // Mybatis 3.2.4 이전까지 대용량 리스트 불러올 때
    void selectPeopleHandler(ResultHandler<PeopleResponse> handler);

    // Mybatis 3.2.4 이후 대용량 리스트 불러올 때
    Cursor<PeopleResponse> selectPeopleCursor();


}
