package com.tistory.aircook.playground.repository

import com.tistory.aircook.playground.entity.People
import org.springframework.data.repository.CrudRepository

interface PeopleRepository : CrudRepository<People, Long> {
}