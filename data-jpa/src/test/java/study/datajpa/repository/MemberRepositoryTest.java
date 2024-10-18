package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

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
}
