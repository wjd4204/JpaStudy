package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;

// @Repository 어노테이션이 없어도 Spring이 JPA를 인식한다! 즉, 해줄 필요가 없다!
// 어노테이션의 역할은 컴포넌트 스캔하고 예외를 Spring에서 공통적으로 처리할 수 있는 예외로 변환하는 역할이 있다.
public interface TeamRepository extends JpaRepository<Team, Long> {
}
