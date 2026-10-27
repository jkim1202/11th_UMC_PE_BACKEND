# 3주차 도서 API

## Raw SQL 방식과 달라진 점

Raw SQL 방식에서는 `JdbcTemplate`으로 SQL을 직접 작성하고 조회 결과를 `Map`으로 처리했지만, ORM 방식에서는 `Book`, `Category` 엔티티와 JPA Repository를 통해 데이터를 조회하고 저장합니다.
도서와 카테고리의 관계를 `@ManyToOne`으로 표현하여 `category_id`를 직접 다루는 대신 연관된 `Category` 객체를 사용합니다.
DB 컬럼명을 그대로 응답하던 방식에서 벗어나 Service가 엔티티를 `BookResponse` DTO로 변환하므로 API 응답 필드를 명확하게 관리할 수 있습니다.
신규 도서 등록 시에도 INSERT SQL을 직접 실행하지 않고 카테고리 존재 여부를 확인한 후 `Book` 엔티티를 생성하여 `JpaRepository.save()`로 저장합니다.

## 실행 결과 검증

구현 결과 `GET /books`는 전체 도서를 최신 ID순으로 반환하고, `POST /books`는 유효한 요청을 201로 등록하며 존재하지 않는 카테고리는 404로 처리하도록 요구사항과 일치하게 구성했습니다.
# 11th_UMC_PE_BACKEND
