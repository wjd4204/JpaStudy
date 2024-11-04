package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import java.util.List;

// 내가 커스터마이징하고 싶은 메서드에 대해 구현하는 MemberRepositoryImple 클래스 생성
// Impl이라는 이름 대신 다른 이름으로 변경하고 싶다면, xml을 설정하거나 javaConfig를 설정한다.
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
