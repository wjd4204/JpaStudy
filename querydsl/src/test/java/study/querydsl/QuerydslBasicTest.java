package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.querydsl.entity.QMember.member;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @DisplayName("username과 일치하는 Member를 찾는다.")
    @Test
    void startJPQL(){
     //given
        // member1 찾기
        String qlString = "select m from Member m where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        //when

     //then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("querydsl로 member1인 member 엔티티를 찾는다.")
    @Test
    void startQuerydsl(){
     //given
        //QMember m = new QMember("m");
        //QMember m = QMember.member; // or static import하기

     //when
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) // 파라미터 바인딩 처리
                .fetchOne();

        //then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("검색 조건을 활용하여 원하는 객체를 조회한다.")
    @Test
    void search(){
     //given
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1").and(member.age.eq(10)))
                .fetchOne();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("체인을 사용하여 검색 조건을 추가할 수 있다.")
    @Test
    void searchAndParam(){
        //given
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"),
                        member.age.eq(10)
                )
                .fetchOne();

        // then
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @DisplayName("여러가지 결과 조회를 테스트해본다.")
    @Test
    void resultFetch(){
     //given
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();// ==limit(1).fetchOne();

        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        results.getTotal(); // total count를 가지고 와야하므로 select를 두 번 실행한다.
        List<Member> content = results.getResults();

        long total = queryFactory
                .selectFrom(member)
                .fetchCount();

    }

    /*
    회원 정렬 순서
    1. 회원 나이 내림차순
    2. 회원 이름 올림차순
    2에서 회원 이름이 없으면 마지막에 null 출력
     */
    @DisplayName("회원들을 특정 속성을 베이스로 정렬한다.")
    @Test
    void sort(){
     //given
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        //when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

     //then
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @DisplayName("페이징 기술을 구현한다.")
    @Test
    void paging1(){
     //given
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();
    }

    @DisplayName("페이징 기술을 구현한다.")
    @Test
    void paging2(){
        QueryResults<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(result.getTotal()).isEqualTo(4);
        assertThat(result.getLimit()).isEqualTo(2);
        assertThat(result.getOffset()).isEqualTo(1);
        assertThat(result.getResults().size()   ).isEqualTo(2);
    }
}
