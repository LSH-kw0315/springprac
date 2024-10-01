package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.catalina.User;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void testMember(){
        Member member=new Member("userA");
        Member savedMember = memberRepository.save(member);

        Member byId = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(byId.getId()).isEqualTo(member.getId());
        Assertions.assertThat(byId.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(byId).isEqualTo(member);
    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1= memberRepository.findById(member1.getId()).get();
        Member findMember2=memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);


        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount=memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result =memberRepository.findByUsernameAndAgeGreaterThan("AAA",15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findByUsername(){
        Member m1 = new Member("AAA",10);
        Member m2 =new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result=memberRepository.findByUsername("AAA");
        Member findMember=result.get(0);
        Assertions.assertThat(findMember).isEqualTo(m1);
    }

    @Test
    void testQuery(){
        Member m1 = new Member("AAA",10);
        Member m2 =new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA",10);
        Assertions.assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    void testUsernameList(){
        Member m1 = new Member("AAA",10);
        Member m2 =new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        result.forEach(
                str-> System.out.println("name:"+str)
        );
    }

    @Test
    void testMemberDTO(){
        Team team=new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDTO> memberDTOS=memberRepository.findMemberDTO();
        memberDTOS.forEach(
                dto-> System.out.println("dto: "+dto)
        );
    }

    @Test
    void testFindNames(){
        Member m1 = new Member("AAA",10);
        Member m2 =new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        result.forEach(
                member -> System.out.println("member:"+member)
        );
    }

    @Test
    void returnType(){
        Member m1 = new Member("AAA",10);
        Member m2 =new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> memberList=memberRepository.findListByUsername("BBC");
        Member findMember = memberRepository.findMemberByUsername("BBC");
        Optional<Member> memberOptional=memberRepository.findOptionalByUsername("BBC");

        System.out.println("member obj:" + findMember);
        System.out.println("memberList size:"+memberList.size());
        System.out.println("member optional:"+memberOptional);

    }

    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age=10;
        PageRequest pageRequest=PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age,pageRequest);


        Page<MemberDTO> toMap=
        page.map(
                member -> new MemberDTO(member.getId(), member.getUsername() ,member.getTeam().getName())
        );
        //then : 페이지 계산 공식 적용

        List<Member> content=page.getContent();
       // long totalElements=page.getTotalElements();

//        content.forEach(
//                member-> System.out.println("member = "+member)
//        );
//
//        System.out.println("totalElements = "+totalElements);
        Assertions.assertThat(content.size()).isEqualTo(3);
       // Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
       // Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }

    @Test
    void bulkUpdate(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",30));
        memberRepository.save(new Member("member4",40));
        memberRepository.save(new Member("member5",50));

        int resultCount = memberRepository.bulkAgePlus(20);

        Assertions.assertThat(resultCount).isEqualTo(3);


    }

    @Test
    void findMemberLazy(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member m1=new Member("member1",10,teamA);
        Member m2=new Member("member2", 20, teamB);
        memberRepository.save(m1);
        memberRepository.save(m2);

        em.flush();
        em.clear();

        List<Member> members=memberRepository.findAll();

        members.forEach(
                member-> {
                    System.out.println("name:"+member.getUsername());
                    System.out.println("team name:"+member.getTeam().getName());
                }
        );



    }


    @Test
    public void queryHint(){
        //given
        Member member1=new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();

    }

    @Test
    void lock(){
        //given
        Member member1=new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");

    }

    @Test
    void callCustom(){
        List<Member> members=memberRepository.findMemberCustom();
    }

    @Test
    void JpaBaseEntity() throws InterruptedException {
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(1000);
        member.setUsername("memberA");

        em.flush();
        em.clear();

        Member findMember=memberRepository.findById(member.getId()).get();

        System.out.println("createdDate:" + findMember.getCreatedDate());
        System.out.println("updatedDate:" + findMember.getLastModifiedDate());
        System.out.println("createBy:" + findMember.getCreatedBy());
        System.out.println("updateBy:"+findMember.getLastModifiedBy());
    }

    @Test
    void specBasic(){
        Team team = new Team("teamA");
        em.persist(team);

        Member m1=new Member("m1",0,team);
        Member m2 = new Member("m2", 0, team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result=memberRepository.findAll(spec);

        Assertions.assertThat(result.size()).isEqualTo(1);




    }

    @Test
    void queryByExample(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1=new Member("m1",0,teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();
        Member member=new Member("m1");
        Team team=new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher =
                ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("m1");
    }

    @Test
    void projections(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1=new Member("m1",0,teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1",NestedClosedProjections.class);

        result.forEach(
                obj-> System.out.println("usernameOnly="+obj)
        );


    }

    @Test
    void nativeQuery() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member result=memberRepository.findByNativeQuery("m1");
        System.out.println("result:"+result);
    }

    @Test
    void nativeQueryProjection() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Page<MemberProjection> result=memberRepository.findByNativeProjection(PageRequest.of(0,10));
        System.out.println("result:"+result);
        result.forEach(
                obj ->{
                    System.out.println("teamName:"+obj.getTeamName());
                    System.out.println("userName:"+obj.getUsername());
                }
        );
    }
}
