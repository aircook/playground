package com.tistory.aircook.playground.controller;

import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @GetMapping("/normal")
    public List<PeopleResponse> selectPeopleNormal(@RequestParam(required = false) String searchName) {
        return peopleService.selectPeopleNormal(searchName);
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
