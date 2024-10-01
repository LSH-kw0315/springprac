package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJPARepositoryTest {

    @Autowired MemberJPARepository memberJPARepository;

    @Test
    void testMember(){
        Member member=new Member("userA");
        Member savedMember = memberJPARepository.save(member);

        Member findMember = memberJPARepository.find(savedMember.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);

    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJPARepository.save(member1);
        memberJPARepository.save(member2);

        Member findMember1= memberJPARepository.findById(member1.getId()).get();
        Member findMember2=memberJPARepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);


        List<Member> all = memberJPARepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        long count = memberJPARepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        memberJPARepository.delete(member1);
        memberJPARepository.delete(member2);

        long deletedCount=memberJPARepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberJPARepository.save(m1);
        memberJPARepository.save(m2);

        List<Member> result =memberJPARepository.findByUserNameAndAgeGreaterThan("AAA",15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findByUsername(){
        Member m1 = new Member("AAA",10);
        Member m2 =new Member("BBB",20);
        memberJPARepository.save(m1);
        memberJPARepository.save(m2);

        List<Member> result=memberJPARepository.findByUsername("AAA");
        Member findMember=result.get(0);
        Assertions.assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void paging(){
        //given
        memberJPARepository.save(new Member("member1", 10));
        memberJPARepository.save(new Member("member2",10));
        memberJPARepository.save(new Member("member3",10));
        memberJPARepository.save(new Member("member4",10));
        memberJPARepository.save(new Member("member5",10));

        int age=10;
        int offset=0;
        int limit=3;
        //when
        List<Member> members = memberJPARepository.findByPage(age,offset,limit);
        long totalCount=memberJPARepository.totalCount(age);

        //then : 페이지 계산 공식 적용

        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5);


    }

    @Test
    void bulkUpdate(){
        memberJPARepository.save(new Member("member1", 10));
        memberJPARepository.save(new Member("member2",19));
        memberJPARepository.save(new Member("member3",30));
        memberJPARepository.save(new Member("member4",40));
        memberJPARepository.save(new Member("member5",50));

        int resultCount = memberJPARepository.bulkAgePlus(20);

        Assertions.assertThat(resultCount).isEqualTo(3);


    }


}