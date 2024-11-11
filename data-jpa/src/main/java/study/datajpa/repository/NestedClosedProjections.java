package study.datajpa.repository;

public interface NestedClosedProjections {

    // 중첩 구조에서는 두 번째부터 최적화가 안된다. 팀은 엔티티로 불러오는 것이다.
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
