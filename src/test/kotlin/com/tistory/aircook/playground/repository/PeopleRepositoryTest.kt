package com.tistory.aircook.playground.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PeopleRepositoryTest(
    val peopleRepository: PeopleRepository
) {

    @Test
    @DisplayName("테이블 갯수 테스트")
    //fun `테이블 갯수 테스트`() {
    fun countTest() {
        //when
        val count = peopleRepository.count()
        //then
        Assertions.assertEquals(1, count)
    }

}