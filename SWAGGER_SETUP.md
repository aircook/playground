# Swagger/OpenAPI 설정 가이드

## 📋 추가된 기능

Spring Boot 3.2.5 프로젝트에 **Swagger(SpringDoc OpenAPI)** 문서화 기능을 추가했습니다.

---

## 🔧 변경 사항

### 1. **pom.xml** - 의존성 추가
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 2. **OpenApiConfig.java** - Swagger 설정 클래스 생성
- 프로젝트 메타데이터 설정
- JWT Bearer Token 보안 스킴 정의
- API 문서 정보 구성

### 3. **application.yml** - Swagger UI 설정 추가
```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
  api-docs:
    path: /v3/api-docs
```

### 4. **WebSecurityConfig.java** - Swagger 경로 인증 제외
```java
.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
```

### 5. **컨트롤러 및 DTO에 Swagger 애노테이션 추가**

#### 📌 컨트롤러 레벨
- `@Tag`: 컨트롤러 그룹 정의
  ```java
  @Tag(name = "Security", description = "보안 관련 API")
  ```

#### 📌 메서드 레벨
- `@Operation`: 엔드포인트 설명
  ```java
  @Operation(summary = "로그인한 사용자 정보", description = "현재 로그인한 사용자의 정보를 반환합니다.")
  ```

- `@Parameter`: 요청 파라미터 설명
  ```java
  @Parameter(description = "검색할 이름 (선택사항)")
  ```

- `@ApiResponse` / `@ApiResponses`: 응답 코드 설명
  ```java
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "성공"),
      @ApiResponse(responseCode = "403", description = "권한 부족")
  })
  ```

#### 📌 DTO/Domain 레벨
- `@Schema`: 필드 설명
  ```java
  @Schema(description = "사람 ID", example = "1")
  private Integer id;
  ```

---

## 📖 적용된 컨트롤러

### ✅ 1. **Root** (`RootController`)
- `GET /` - 홈 화면

### ✅ 2. **Security** (`SecurityController`)
- `GET /security/api1` - USER 역할 필요 API
- `GET /security/api2` - ADMIN 역할 필요 API
- `GET /security/user` - 로그인한 사용자 정보
- `GET /security/password` - 비밀번호 암호화 (관리용)

### ✅ 3. **People** (`PeopleController`)
- `GET /people/normal` - 일반 조회
- `GET /people/handler` - Handler 조회
- `GET /people/cursor` - Cursor 조회
- `GET /people/simple` - 단순 일괄 삽입
- `GET /people/batch` - 배치 삽입
- `GET /people/batch-by-unit` - 단위별 배치 삽입

### ✅ 4. **Async** (`AsyncController`)
- `GET /async/run-async` - runAsync 테스트
- `GET /async/supply-async` - supplyAsync 테스트
- `GET /async/supply-async-then-apply` - thenApply 테스트
- `GET /async/supply-async-then-apply-async` - thenApplyAsync 테스트
- `GET /async/supply-async-then-apply-exceptionally` - 예외 처리 테스트
- `GET /async/complex` - 복합 비동기 워크플로우

### ✅ 5. **Chat** (`ChatController`)
- `WebSocket /ws/chat` - 채팅 메시지 전송

---

## 🌐 Swagger UI 접근

### 시작 방법
1. 애플리케이션 실행
2. 브라우저에서 접속:
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **API JSON**: http://localhost:8080/v3/api-docs

### 주요 기능
- 📚 모든 엔드포인트 문서화
- 🧪 직접 API 테스트 가능
- 🔐 JWT 토큰 입력 지원
- 📊 요청/응답 스키마 시각화
- 🔍 파라미터 자동 검증

---

## 🔐 보안 설정 참고

다음 경로는 인증 없이 접근 가능합니다:
- `/swagger-ui.html` - Swagger UI 페이지
- `/swagger-ui/**` - Swagger UI 리소스
- `/v3/api-docs` - OpenAPI JSON 문서
- `/v3/api-docs/**` - OpenAPI 추가 문서
- `/` - 홈 페이지
- `/login` - 로그인 페이지
- `/async/**` - 모든 비동기 API (개발용)
- `/ws/**` - WebSocket (개발용)

---

## 📝 예제

### 요청 예제 (Swagger UI에서 테스트)

```bash
# 사람 정보 조회
curl -X GET "http://localhost:8080/people/normal" -H "accept: application/json"

# 로그인 (기본 인증)
# username: user01
# password: password01
curl -X GET "http://localhost:8080/security/user" \
  -u user01:password01
```

---

## ✨ 주의사항

1. **운영 환경에서는** `/swagger-ui.html` 경로를 보호해야 합니다.
2. Swagger 문서는 프로덕션 배포 시 비활성화하는 것이 좋습니다:
   ```yaml
   springdoc:
     swagger-ui:
       enabled: false
   ```

3. 민감한 정보(비밀번호 등)는 문서에 노출되지 않도록 주의합니다.

---

## 📚 참고 문서

- [SpringDoc OpenAPI 공식 문서](https://springdoc.org/)
- [OpenAPI 3.0 명세](https://spec.openapis.org/oas/v3.0.3)
