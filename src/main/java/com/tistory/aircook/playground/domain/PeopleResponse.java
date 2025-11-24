package com.tistory.aircook.playground.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "사람 정보 응답 DTO")
public class PeopleResponse {

    @Schema(description = "사람 ID", example = "1")
    private Integer id;

    @Schema(description = "사람 이름", example = "알베르트 아인슈타인")
    private String name;

    @Schema(description = "사람 생년월일", example = "1879-03-14")
    private String birth;

}

