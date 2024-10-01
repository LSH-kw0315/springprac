package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void join() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
        //영속성 컨텍스트에서 꺼내오기 때문에 이런 비교가 가능

        //해당 테스트를 보면 insert 쿼리가 존재하지 않는데,
        //영속성 컨텍스트에 저장하는 것은 insert하는 것이 아니고,
        //테스트의 트랜잭션은 기본적으로 롤백하기 때문에 실제 DB에 저장되는 일이 없는 것임.
        //@Rollback(false)를 위에 작성 하거나 em.flush()를 쓰면 된다.
    }

    @Test
    public void duplicate_exception() throws Exception{
        //given
        Member member1=new Member();
        member1.setName("kim");

        Member member2=new Member();
        member2.setName("kim");
        //when


        assertThrows(IllegalStateException.class,()->{
            memberService.join(member1);
            memberService.join(member2);
        });
    }

}