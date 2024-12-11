복잡한 실무 환경에서 사용하기에는 부족하지만, 스프링 데이터에서 제공하는 기능이므로 특징과 부족한 이유를 설명하겠다.
### 1. 인터페이스 지원 - QuerydslPredicateExecutor
- MemberRepository에 "querydslPredicateExecutor"를 작성해보았다.
- QuerydslPredicateExecutor의 단점은 조인이 불가능하다는 것이다.
  - 데이터 관계에서 조인은 대부분의 프로젝트에 들어가기에 적합하지 않다.
- 클라이언트가 Querydsl에 의존해야 한다. 서비스 클래스가 Querydsl이라는 구현 기술에 의존해야한다.
- 따라서 복잡한 실무환경에서 사용하기에는 한계가 명확하다!

### 2. Querydsl Web 지원
- QuerydslPredicate의 단점은 다음과 같다.
  1. 단순한 조건만 가능하다.
  2. 조건을 커스텀하는 기능이 복잡하고 명시적이지 않다.
  3. 컨트롤러가 Querydsl에 의존한다.
  4. 복잡한 실무환경에서 사용하기에 한계가 명확하다.

### 3. 리포지토리 지원 - QuerydslRepositorySupport
추상화 클래스로 Querydsl 라이브러리를 쓰기 위해 Repository 구현체가 받으면 편리한 클래스이다.
해당 클래스는 Querydsl이 3버전일 때 만들어졌기에 from절부터 시작된다.
- 장점은 다음과 같다.
  1. "getQuerydsl().applyPagination()" 스프링 데이터가 제공하는 페이징을 Querydsl로 편리하게 변환 가능하다.(하지만 Sort는 오류가 발생함)
  2. "from()"으로 시작이 가능하다.
  3. EntityManager를 기본적으로 제공해준다.
- 단점은 다음과 같다.
  1. Querydsl 3.x 버전을 대상으로 만들었기에 Querydsl 4.x에 나온 JPAQueryFactory로 시작할 수가 없다.
  2. "QueryFactory"를 제공하지 않는다.
  3. 스프링 데이터 Sort 기능이 정상 동작하지 않는다. 정 사용하고 싶다면 직접 처리하여야 한다.
결론! -> 그냥 원래꺼 쓰자.