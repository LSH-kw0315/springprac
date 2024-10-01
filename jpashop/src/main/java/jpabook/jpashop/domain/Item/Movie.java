package jpabook.jpashop.domain.Item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("M")
//DBMS에서의 상속 구현 시 구분방법
public class Movie extends Item{

    private String director;
    private String actor;
}
