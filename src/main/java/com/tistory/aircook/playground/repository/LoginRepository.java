package com.tistory.aircook.playground.repository;

import com.tistory.aircook.playground.domain.LoginResponse;
import com.tistory.aircook.playground.domain.PeopleRequest;
import com.tistory.aircook.playground.domain.PeopleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;

@Mapper
public interface LoginRepository {

    LoginResponse selectLogin(String userId);


}
