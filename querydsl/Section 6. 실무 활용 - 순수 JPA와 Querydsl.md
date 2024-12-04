### 1. 순수 JPA 리포지토리와 Querydsl
- JPAQueryFactory는 편한 방식으로 생성하자.
- EntityManager와 JPAQueryFactory에 대해 동시성에 대한 문제가 제기될 수도 있다.
  - 이는 모든 멀티스레드에서 사용하기 때문인데, 사실 JPAQueryFactory에 대한 동시성 문제는 모두 EntityManager에 의존한다.
  - EntityManager를 스프링과 엮어서 쓰면 동시성 문제랑 전혀 관계없이 트랜잭션 단위로 분리되어 동작을 하게 된다.
  - MemberJpaRepository에는 진짜 영속성 컨텍스트 EntityManager가 아닌 프록시용을 주입해 주기에 트랜잭션 단위로 실제 EntityManager를 할당해준다.

### 2. 동적 쿼리와 성능 최적화 조회 - Builder 사용
1. MemberTeamDto - 조회 최적화용 DTO 추가

### 3. 동적 쿼리와 성능 최적화 조회 - Where절 파라미터 사용