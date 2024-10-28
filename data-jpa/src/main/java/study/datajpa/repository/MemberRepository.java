package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    // jpa에 jpql 정적 쿼리를 작성하는 방법
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String name, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findByUsername(String username); // collection
    Member findMemberByUsername(String username); // single
    Optional<Member> findOptionalByUsername(String username); // optional

    Page<Member> findByAge(int age, Pageable pageable);

    // 벌크 연산은 영속성 컨텍스트에 저장되는 것이 아닌 db에 직접적으로 접근하기 때문에 유의해야 한다.
    // 자동으로 clear 수행
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age =  m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
