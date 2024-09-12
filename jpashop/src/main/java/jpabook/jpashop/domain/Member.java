package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @JsonIgnore // 프레젠테이션 layer 로직이 추가되기 시작
    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>(); // best!
}
