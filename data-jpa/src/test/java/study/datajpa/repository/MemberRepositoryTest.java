package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        List<Member> aaa = memberRepository.findListByUsername("AAA"); // 아무것도 가져오지 않았을 경우, 오류가 아닌 빈 컬렉션을 반환한다!!
        Member findMember = memberRepository.findMemberByUsername("AAA"); // 단일 객체는 빈 값이 아닌 null을 반환한다!!
        System.out.println("findMember = " + findMember);

        Optional<Member> findOptional = memberRepository.findOptionalByUsername("AAA"); // optional은 옵션사항이라는 의미로, 없으면 빈 객체 반환!!
        System.out.println("findOptional = " + findOptional);
    }
}
