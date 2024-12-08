package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

// 반드시 사용자 정의 리포지토리가 필요한 것은 아니다. 그냥 임의로 리포지토리를 만들어 직접 사용해도 된다.(반드시 상속할 필요는 없다는 말)
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

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

    // 페치 조인 시 무조건 JPQL의 쿼리문을 작성해야 하기 때문에 EntityGraph를 사용한다.
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // member를 조회하는 데 team을 살짝 섞고싶다면?
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team"))
    //@EntityGraph("Member.all") // Member 상단에 적어놓은 NamedEntityGraph를 JPA에서 지원한다.
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // 다른 엔티티를 가져와 사용할 거면 JPQL 페치 조인을 추천한다!

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    /* 다양한 이유로 인터페이스의 메서드를 직접 구현하고 싶다면, EntityManager, JDBC Template, MyBatis, Querydsl 등을 사용하자! */

    <T> List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);

    @Query(value = "select * from member where username =?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName" +
            " from member m left join team t ",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
