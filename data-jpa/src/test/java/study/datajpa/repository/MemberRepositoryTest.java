package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @DisplayName("")
    @Test
    void test(){
     Member member = new Member("memberA");
     Member savedMember = memberRepository.save(member);

     Member findMember = memberRepository.findById(savedMember.getId()).get();
     assertThat(findMember.getId()).isEqualTo(savedMember.getId());
     assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
     assertThat(findMember).isEqualTo(savedMember);

    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단일 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @DisplayName("입력된 나이보다 많고 입력된 사용자 이름과 같은 인원들을 출력한다.")
    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @DisplayName("query 어노테이션을 사용하여 username과 age가 일치하는 멤버를 찾는다.")
    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @DisplayName("query 어노테이션을 사용하여 username을 리스트로 보여줍니다.")
    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for(String s : usernameList){
            System.out.println("s = " + s);
        }
    }

    @DisplayName("쿼리문을 작성하여 member와 조인되는 team의 값도 같이 조회한다.")
    @Test
    public void findMemberDto(){

        Team team = new Team("TeamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for(MemberDto dto : memberDto){
            System.out.println("dto =" + dto);
        }
    }

    @DisplayName("query 어노테이션을 사용하여 username을 리스트로 보여줍니다.")
    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for(Member member : result){
            System.out.println("member = " + member);
        }
    }

    @DisplayName("query 어노테이션을 사용하여 username을 리스트로 보여줍니다.")
    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findByUsername("AAA"); // 아무것도 가져오지 않았을 경우, 오류가 아닌 빈 컬렉션을 반환한다!!
        Member findMember = memberRepository.findMemberByUsername("AAA"); // 단일 객체는 빈 값이 아닌 null을 반환한다!!
        System.out.println("findMember = " + findMember);

        Optional<Member> findOptional = memberRepository.findOptionalByUsername("AAA"); // optional은 옵션사항이라는 의미로, 없으면 빈 객체 반환!!
        System.out.println("findOptional = " + findOptional);
    }

    @DisplayName("페이징 기술을 활용하여 원하는 범위의 값을 가지고 올 수 있다.")
    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        // page는 0부터 시작이다.
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @DisplayName("20살 이상인 멤버들에게 원래 나이의 +1 값으로 수정한다.")
    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member( "member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
//        em.clear();

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @DisplayName("EntityGraph를 사용하여 페치 조인을 최적화한다.")
    @Test
    public void findMemberLazy(){
     //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

      //when N+1
        //findMemberFetchJoin의 join을 통해 한 번에 엔티티를 조회할 수 있다! N + 1 해결
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        /* member에 있는 teamId는 프록시 객체만 가져오지만 teamEntity 내의
         값을 이용하고 싶으면 실제 객체를 가져온다. 이 과정을 LAZY 로딩이라 한다.*/
        for(Member member : members){
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam().getName());
        }

    }

    @Test
    public void queryHint(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush(); // 강제 플러시는 db에 쿼리가 나간다.
        em.clear(); // 영속성 컨텍스트의 결과를 db에 동기화한다. 그리고 영속성 컨텍스트는 비워진다.

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2"); // QueryHint를 썻기 때문에 허용되는 이외의 것은 모두 무시해버린다.

        em.flush();
    }

    @Test
    public void lock(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        // 트랜잭션끼리 충돌이 발생한다고 가정하고 락을 건다. DB에서 제공하는 락기능을 사용한다.
        // LockMode가 PESSIMISTIC_WRITE를 사용하면 다른 트랜잭션에서 읽거나 쓰지를 못한다. 이를 '배타적 잠금'이라고 한다.
        List<Member> result = memberRepository.findLockByUsername("member1");
    }

    @DisplayName("MemberRepositoryCustom에 작성한 메서드를 MemberRepository에 상속")
    @Test
    void callCustom(){
     //given
        List<Member> result = memberRepository.findMemberCustom();

        //when

     //then
    }
}
