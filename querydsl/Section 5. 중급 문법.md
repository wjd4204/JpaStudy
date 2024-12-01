### 1. 프로젝션과 결과 반환 - 기본
- 프로젝션: "select"에서 대상을 지정하는 기능(=entity 전체를 가져오는 것이 아닌 조회 대상을 지정해 원하는 값만 조회하는 기능)
- 프로젝션 대상이 하나일 경우,
  - **프로젝션 대상이 하나면** 타입을 명확히 지정할 수 있으며,
  - **프로젝션 대상이 둘 이상이면** 튜플이나 DTO로 조회를 한다.

### 2. 프로젝션과 결과 반환 - DTO 조회
1. 순수 JPA에서 DTO 조회
   - QuerydslBasicTest에서 구현한 "findDtoByJPQL"은 순수 JPA에서 DTO를 조회할 때 new 명령어를 사용하는 것을 보여준다.
     - DTO의 패키지 이름을 다 적어줘야해서 지저분하다는 단점이 있다.
     - 생성자 방식만 지원한다.
2. Querydsl 빈 생성
   - "querydsl"은 3가지 방법을 지원한다.
   1. 프로퍼티 접근
   2. 필드 직접 접근
   3. 생성자 사용
   - 프로퍼티나 필드 접근 생성 방식에서 이름이 다를 때의 해결 방안은 다음과 같다.
   1. 'ExpressionUtils.as(source.alias)'로 필드나 서브 쿼리에 별칭을 적용한다.
   2. 'username.as("memberName")' 같은 방식을 사용하자!

### 3. 프로젝션과 결과 반환 - @QueryProjection
- DTO에 프로젝션을 통해 결과를 반환하는 것은 안전한 방법이다. 하지만 단점도 존재한다.
1. Q파일을 생성해야한다.(@QueryProjection을 사용해야함)
2. 아키텍쳐적 문제로 querydsl는 DTO를 기존에 몰랐다. 그렇기에 라이브러리 의존성이 없었는데,
이렇게 @QueryProjection을 달게 되면 DTO 자체가 queryDSL의 의존성을 가지게 된다.
3. 결론은 DTO가 순수하지 않은 상태로 존재하게 되는 것이다.
   1. 실용적인 관점에서 가져가고 싶다면 그대로 사용하고, 그렇지 않다면 생성자나 필드 접근 방식을 사용하자!

### 4. 동적 쿼리 - BooleanBuilder 사용
- 동적 쿼리를 해결하는 방법에는 두 가지 방식이 있다.
1. BooleanBuilder
   1. "dynamicQuery_BooleanBuilder"의 searchMember1을 보면 BooleanBuilder를 통해 입력된 값에 대한 조건을 생성하여 추가해서
   최종적인 파라미터들을 queryFactory에 반영하는 것을 볼 수 있다.
2. Where 다중 파라미터 사용