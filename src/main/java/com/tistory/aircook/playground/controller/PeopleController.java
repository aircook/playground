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
    public List<PeopleResponse> selectPeopleNormal() {
        return peopleService.selectPeopleNormal();
    }

    @GetMapping("/handler")
    public List<PeopleResponse> selectPeopleHandler() {
        return peopleService.selectPeopleHandler();
    }

    @GetMapping("/cursor")
    public List<PeopleResponse> selectPeopleCursor() {
        return peopleService.selectPeopleCursor();
    }

    @GetMapping("/simple")
    public void insertSimplePeoples() {
        peopleService.insertSimplePeoples();
    }

    @GetMapping("/batch")
    public void insertBatchPeoples() {
        peopleService.insertBatchPeoples();
    }
    @GetMapping("/batch-by-unit")
    public void insertBatchPeoplesByUnit() {
        peopleService.insertBatchPeoplesByUnit();
    }
}
