//다음 링크 참조하여 entity와 dto 처리
//https://velog.io/@freddiey/DTO-%EB%A7%A4%ED%95%91kotlin
//sealed class
//https://devs0n.tistory.com/66
package com.tistory.aircook.playground.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class People(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var birth: String
) {
    constructor(name: String, birth: String) : this(null, name, birth)
}
