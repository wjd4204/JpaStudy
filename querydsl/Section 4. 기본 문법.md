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

### 검색 조건 쿼리
### 결과 조회
- fetch()
  - 리스트 조회, 데이터 없으면 빈 리스트 반환
- fetchOne()
  - 하나의 결과 조회
  - 결과가 없으면 null이고, 둘 이상이면 NonUniqueResultException을 발행
- fetchFirst()
  - limit(1).fetchOne()
- fetchResults()
  - 페이징 정보를 포함하고, total count 쿼리를 추가 실행한다.
- fetchCount()
  - count 쿼리로 변경해서 count 수를 조회한다.

### 페이징
### 집합
- groupBy에 의한 그룹화결과를 제한하려면 'having'을 사용하자.

### 조인
1. 기본 조인
   - 조인의 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고, 두 번째 파라미터에 별칭으로 사용할
   Q타입을 지정하면 된다!
2. 세타 조인
   - 세타조인은 from절에 여러 엔티티를 선택해서 조인할 수 있다.
   - 하지만 한 가지 제약이 있는데, 이는 외부 조인이 불가능하다는 것이다.(하지만 조인 on을 사용하면 외부 조인이 가능하다!)

### 조인 - on절
1. ON 절을 활용한 조인에는 두 가지가 있다.
   - 조인 대상을 필터링 할 때
   - 연관관계 없는 엔티티를 외부 조인할 때
   1. 조인 대상을 필터링
      - on 절을 활용해 조인 대상을 필터링 할 때는 외부조인이 아니라 내부조인을 사용하면, where 절에서 필터링 하는 것과 기능이 동일하다.
      따라서 on 절을 활용한 조인 대상 필터링을 사용할 때, 내부조인이면 익숙한 where 절로 해결하고, **정말 외부조인이 필요한 경우에만 이 기능을 사용하자!**
   2. 연관관계 없는 엔티티를 외부 조인
      - 'leftJoin'을 사용 시 해당 엔티티의 pk가 from 절에 들어있든지 말지 신경쓰지 않고 표시하기 때문에 없어도 null로 표기된다.
      - 문법을 잘 봐야할 것을 주의하자. 'leftJoin' 부분에 일반 조인과 다르게 엔티티 하나만 들어간다!
        - 일반 조인 : leftJoin(member.team, team)
        - on 조인 : from(member).leftJoin(team).on(xxx)
