### 시작 JPQL vs. Querydsl
- querydsl은 select절에서 문법이 틀려도 틀릴수가 없다.
  - 컴파일링을 통해 오류를 다 잡아내기 때문이다.
- JPAQueryFactory 선언을 필드로 선언을 하면 동시성 문제를 야기할 수 있다고 생각할 것이다.
  - 하지만 동시에 여러 멀티스레드에서 접근을 해도 이전에 문제없게 설계가 되어있기 때문에 심각할 것 없다!

### 기본 Q-Type활용
- QClass를 선언하는 방법은 3가지가 있다.
  1. 별칭 생성 및 선언(QMember m = new QMember("m");)
  2. 바로 선언(QMember m = QMember.member);
  3. static import하기
- querydsl로 실행된 코드는 결과적으로 jpql이 된다.
  - 만일 지금 실행되는 jpql을 보고 싶다면 application.yml에 hibernate 밑에
  use_sql_comments를 삽입하자.
