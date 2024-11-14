package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    @Value("#{target.username + ' ' + target.age}") // Open Projection
    String getUsername(); // 정확히 매칭이 될 경우에는 위의 Value를 쓰지 않아도 최적와된다.
}
