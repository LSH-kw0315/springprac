package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass //상속 시 DB에서 해당 속성을 삽입하도록 함.
//DB에 객체지향의 상속을 구현하는 방법을 상속관계 매핑이라고 하는 것이다.
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @PrePersist //영속성 컨텍스트에 저장하기 전 호출
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createDate=now;
        updateDate=now;
    }

    @PreUpdate //업데이트 전에 호출
    public void preUpdate(){
        updateDate=LocalDateTime.now();
    }
}
