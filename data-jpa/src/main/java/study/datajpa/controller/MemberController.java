package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // HTTP 파라미터로 넘어온 엔티티의 아이디로 엔티티 객체를 찾아서 바인딩한다.
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 아래의 HTTP 요청은 회원 id를 받지만 도메인 클래스 컨버터가 중간에 동작하여 회원 엔티티 객체를 반환한다.
    // 주의할 점으로는 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 조회용으로만 사용해야만 한다.(DB에 반영이 안됨)
    @GetMapping("/members2/{id}")
    public String findMembers2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }
}
