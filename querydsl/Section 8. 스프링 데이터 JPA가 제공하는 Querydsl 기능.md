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