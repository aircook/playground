package com.tistory.aircook.playground.controller;

import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @GetMapping("/normal")
    public List<PeopleResponse> selectPeopleNoraml() {
        return peopleService.selectPeopleNoraml();
    }
    @GetMapping("/handler")
    public void selectPeopleHandler() {
        peopleService.selectPeopleHandler();
    }
    @GetMapping("/cursor")
    public void selectPeopleCursor() {
        peopleService.selectPeopleCursor();
    }

}
