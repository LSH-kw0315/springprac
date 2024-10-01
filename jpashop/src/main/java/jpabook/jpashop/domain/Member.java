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

    @Embedded
    private Address Address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") //해당 필드가 상대편 테이블에 대응하는 필드가 어디인가?
    private List<Order> orders = new ArrayList<>();

}
