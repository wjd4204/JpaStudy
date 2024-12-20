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
   - 세타조인은 from 절에 여러 엔티티를 선택해서 조인할 수 있다.
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

### 조인 - 페치 조인
- 페치 조인은 SQL에서 제공하는 기능은 아니지만 SQL조인을 활용하여 연관된 엔티티를 SQL 한번에 조회하는 기능이다.
- 주로 성능 최적화에 사용한다.

### 서브 쿼리
- 'com.querydsl.jpa.JPAExpressions' 사용
- from 절의 서브쿼리에는 한계가 있다.
  - JPQL의 한계점으로 from 절의 서브쿼리는 지원하지 않는다.
  - Querydsl도 지원하지 않으며, 하이버네이트 구현체를 사용하는 경우에는 select 절의 서브쿼리를 지원한다.
  - 마찬가지로 Querydsl도 하이버네이트 구현체를 사용하면 select 절의 서브쿼리를 지원한다.
- from 서브쿼리를 해결하려면?
  - 이를 해결하려면 3가지의 방법이 있다.
  1. 서브쿼리를 join으로 변경한다.
  2. 어플리케이션에서 쿼리를 2번 분리해서 실행한다.
  3. nativeSQL을 사용한다.

### CASE 문
- 조건 절로 select, where 절에서 사용할 수 있다!
- 전환하고 바꾸고 보여주는 것은 DB에서 보여주면 안된다.
  - 어플리케이션에서 해결하는 것을 추천!

### 상수, 문자 더하기
- 상수가 필요하면 "Expresstions.constant()"를 사용한다.
- member.age.stringValue()는 같은 문자가 아닌 타입들은 'stringValue()'로 문자로 변환할 수가 있다!
  - 이는 ENUM 타입을 처리할 때도 자주 쓰이니 주의하자!
