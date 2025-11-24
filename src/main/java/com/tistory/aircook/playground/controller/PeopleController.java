package com.tistory.aircook.playground.controller;

import com.tistory.aircook.playground.domain.PeopleResponse;
import com.tistory.aircook.playground.service.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "People", description = "사람 정보 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Operation(summary = "일반 조회 (Normal)", description = "이름으로 검색하여 사람 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PeopleResponse.class))),
    })
    @GetMapping("/normal")
    public List<PeopleResponse> selectPeopleNormal(
            @Parameter(description = "검색할 이름 (선택사항)") @RequestParam(required = false) String searchName) {
        return peopleService.selectPeopleNormal(searchName);
    }

    @Operation(summary = "핸들러로 조회 (Handler)", description = "ResultHandler를 사용하여 사람 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PeopleResponse.class))),
    })
    @GetMapping("/handler")
    public List<PeopleResponse> selectPeopleHandler() {
        return peopleService.selectPeopleHandler();
    }

    @Operation(summary = "커서로 조회 (Cursor)", description = "Cursor를 사용하여 사람 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PeopleResponse.class))),
    })
    @GetMapping("/cursor")
    public List<PeopleResponse> selectPeopleCursor() {
        return peopleService.selectPeopleCursor();
    }

    @Operation(summary = "단순 일괄 삽입", description = "간단한 방식으로 사람 정보를 일괄 삽입합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/simple")
    public void insertSimplePeoples() {
        peopleService.insertSimplePeoples();
    }

    @Operation(summary = "배치 모드 삽입", description = "배치 모드를 사용하여 사람 정보를 일괄 삽입합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/batch")
    public void insertBatchPeoples() {
        peopleService.insertBatchPeoples();
    }

    @Operation(summary = "단위별 배치 삽입", description = "단위별로 배치 모드를 사용하여 사람 정보를 일괄 삽입합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/batch-by-unit")
    public void insertBatchPeoplesByUnit() {
        peopleService.insertBatchPeoplesByUnit();
    }
}

