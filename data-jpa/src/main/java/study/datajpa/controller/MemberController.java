package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
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

    // 특별한 개별 설정을 원한다면 "@PageableDefault(size = 5, sort = "username")을  Pageable 앞에 구현하자.
    // Pageable, Page를 파라미터와 응답 값으로 사용하지 않고, 직접 클래스를 만들어서 처리한다. 그리고 직접 PageRequest를 생성하여 리포지토리에 넘긴다.
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
//        PageRequest request = PageRequest.of(1,2);
//
//        Page<MemberDto> map = memberRepository.findAll(request)
//                .map(MemberDto::new);

        return memberRepository.findAll(pageable).map(MemberDto::new);
    }

//    @PostConstruct
//    public void init() {
//        for(int i=0;i<100;i++){
//            memberRepository.save(new Member("user" + i, i));
//        }
//    }
}
