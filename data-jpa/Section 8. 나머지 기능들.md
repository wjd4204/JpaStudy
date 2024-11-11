### 1. Specifications(명세)
- 실무에서는 쓰지 않는 것을 지향
- JPA Criteria의 개념
  - 술어(predicate)
    - 참 또는 거짓으로 반환
    - AND나 OR 같은 연산자로 조합하여 다양한 검색 조건을 쉽게 생성한다.
  - 명세를 정의하기위해서는 'Specification' 인터페이스를 구현하거나 'toPredicate(...)' 메서드만 구현하면 되지만
실무에서는 JPA Criteria를 거의 안쓴다! 대신 QueryDSL을 사용하자.