// 다음 내용 참조하여 설정
// https://minkukjo.github.io/study/docs/spring/jpa/kotlin-jpa-guide/

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.5.21" // JPA 사용을 위한 플러그인 추가 옵션
	id ("org.jetbrains.kotlin.plugin.allopen") version "1.5.21" // allOpen에 지정한 어노테이션으로 만든 클래스에 open 키워드를 적용
	id ("org.jetbrains.kotlin.plugin.noarg") version "1.5.21" //  자동으로 Entity, Embeddable, MappedSuperClass 어노테이션이 붙어있는 클래스에 자동으로 no-arg 생성자를 생성
}

group = "com.tistory.aircook"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	//groupId:artifactId
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-graphql")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	//SQLite JDBC Driver
	//https://github.com/xerial/sqlite-jdbc
    //implementation("org.xerial:sqlite-jdbc:3.32.3.2")
	implementation("org.xerial:sqlite-jdbc")

	//SQLite Dialect
	//다음 dialect는 DEPRECATED 라고 나오네
	//https://github.com/gwenn/sqlite-dialect --> https://github.com/hibernate/hibernate-orm/tree/main/hibernate-community-dialects
	//implementation("com.github.gwenn:sqlite-dialect:0.1.0")
	implementation("org.hibernate.orm:hibernate-community-dialects")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework:spring-webflux")
	testImplementation("org.springframework.graphql:spring-graphql-test")
}

noArg {
	annotation("javax.persistence.Entity") // Entity 애노테이션이 붙은 코틀린 클래스의 NoArgument 생성자 자동 생성을 위한 설정
}

allOpen { // 추가적으로 열어줄 allOpen
	annotation("javax.persistence.Entity") //Entity 애노테이션이 붙은 코틀린의 클래스를 open 클래스로 만들어주는 설정
	annotation("javax.persistence.MappedSuperclass") // MappedSuperclass 애노테이션이 붙은 코틀린의 클래스를 open 클래스로 만들어주는 설정
	annotation("javax.persistence.Embeddable") // Embeddable 애노테이션이 붙은 코틀린 클래스를 open 클래스로 만들어주는 설정
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
