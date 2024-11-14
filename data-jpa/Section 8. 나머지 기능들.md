### 1. Specifications(명세)
- 실무에서는 쓰지 않는 것을 지향
- JPA Criteria의 개념
  - 술어(predicate)
    - 참 또는 거짓으로 반환
    - AND나 OR 같은 연산자로 조합하여 다양한 검색 조건을 쉽게 생성한다.
  - 명세를 정의하기위해서는 'Specification' 인터페이스를 구현하거나 'toPredicate(...)' 메서드만 구현하면 되지만
실무에서는 JPA Criteria를 거의 안쓴다! 대신 QueryDSL을 사용하자.

### 2. Query by example
1. example 사용 시 장점
  1. 동적 쿼리를 편리하게 처리
  2. 도메인 객체를 그대로 사용
  3. 데이터 저장소를 RDB에서 NoSQL로 변경해도 코드 변경이 없게 추상화 되어 있음
  4. 스프링 데이터 JPARepository 인터페이스에 이미 포함되어 있음.
- 단점
  1. 조인은 가능하지만 Inner 조인만 가능하고, Left 조인은 안된다.
  2. 'firstname = ?0 or (firstname = ?1 and lastname = ?2' 같은 중첩 제약조건은 안된다.
  3. 매칭 조건이 매우 단순하다.
- 결론
  - 실무에서 사용하기에는 매칭 조건이 단순하고, LEFT 조인이 안된다.
  - QueryDSL을 지향한다.

### 3. Projection
   - 주의할 점
     - **프로젝션 대상이 root 엔티티면**, JPQL SELECT 절 최적화 가능
     - **프로젝션 대상이 root가 아니라면**, LEFT OUTER JOIN으로 처리하고, 모든 필드를 SELECT해서 엔티티로 조회한 다음에 계산한다.
   - 정리
     - 프로젝션 대상이 root 엔티티면 유용하다.
     - 프로젝션 대상이 root 엔티티를 넘어가면 JPQL SELECT 최적화가 안된다.
     - 실무의 복잡한 쿼리를 해결하기에는 한계가 있으므로 단순할 때만 사용하는 것을 지향.(QueryDSL을 사용하자 그냥))

### 4. 네이티브 쿼리
- 가급적 네이티브 쿼리는 사용하지 않는 것이 좋다.
- 이를 쓰지 않기 위해 Projections을 활용하는 것이 좋다!
- 스프링 데이터 JPA 기반 네이티브 쿼리
  - 페이징 지원
  - 반환 타입 | Object[], Tuple, DTO
  - 제약 사항
    - Sort 파라미터를 통한 정렬이 정상 동작하지 않을 수도 있다.
    - JPQL처럼 어플리케이션 로딩 시점에 문법이 확인 불가하다.
    - 동적 쿼리가 불가하다.

1. JPA 네이티브 SQL 지원
   - JPQL은 위치 기반 파라미터를 1부터 시작하지만, 네이티브는 0부터 시작한다.
   - 네이티브 SQL을 DTO로 변환을 하기 위해서는
     1. DTO 대신 JPA TUPLE 조회
     2. DTO 대신 MAP 조회
     3. @SqlResultSetMapping(복잡함)
     4. Hibernate ResultTransformer(복잡함)
   - 위와 같은 방법이 있지만 최종적으로는 JdbcTemplate나 MyBatis를 권장한다.
   - Projections를 활용하는 방법도 있다!
2. 동적 네이티브 쿼리
   - 하이버네이트를 직접 활용한다.
   - 스프링 JdbcTemplate, MyBatis, jooq 같은 외부 라이브러리를 사용한다.