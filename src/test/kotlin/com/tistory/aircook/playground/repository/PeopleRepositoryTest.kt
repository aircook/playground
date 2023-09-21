package com.tistory.aircook.playground.repository

import com.tistory.aircook.playground.entity.People
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import java.util.function.Consumer

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PeopleRepositoryTest(
    val peopleRepository: PeopleRepository
) {
    val logger: Logger = LoggerFactory.getLogger(PeopleRepositoryTest::class.java)

    @Test
    @DisplayName("테이블 갯수 테스트")
    //fun `테이블 갯수 테스트`() {
    fun countTest() {
        //when
        val count = peopleRepository.count()
        //then
        Assertions.assertEquals(3, count)
    }

    @Test
    @DisplayName("저장 테스트")
    fun saveTest() {
        val person = People("프랜시스", "2000년 10월 10일")
        val savedPerson = peopleRepository.save(person)
        assertEquals(person, savedPerson)
    }

    @Test
    @DisplayName("목록 테스트")
    fun listTest() {
        //val keyword --> immutable type
        peopleRepository.findAll()
        val peoples: Iterable<People> = peopleRepository.findAll()
        peoples.forEach(Consumer<People> { p: People ->
            logger.info("people name is [{}], birth is [{}]", p.name, p.birth)
        })
    }

}