package hellojpa;

import hellojpa.mapping_domain.Team;
import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

//            //find(em);
//
//            insert(em,null,"abcd");
//            insert(em,null,"1234");
//            insert(em,null,"5555");
//
//            //update(em);
//
//            //remove(em);
//
//            List<Member> result=
//                    em.createQuery("select m from Member as m", Member.class)
//                            .setFirstResult(5)
//                            .setMaxResults(8)
//                            .getResultList();
//            for(Member member :result){
//                System.out.println("member.name:"+member.getName());
//            }

//            Team team=new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            hellojpa.mapping_domain.Member member=new hellojpa.mapping_domain.Member();
//            member.setUsername("member1");
//            //member.setTeam(team.getId());
//            member.setTeam(team);
//            em.persist(member);
//
//            em.flush();
//            em.clear();
//
//            hellojpa.mapping_domain.Member findMember=em.find(hellojpa.mapping_domain.Member.class,member.getId());
//            System.out.println("member Name:"+findMember.getUsername());
//
//            for (hellojpa.mapping_domain.Member m : findMember.getTeam().getMembers()) {
//                System.out.println("team Member Name:"+m.getUsername());
//            }
//
//            Team findTeam=findMember.getTeam();
//            System.out.println("team Name:"+findTeam.getName());
//            //Long findTeamId= findMember.getTeamId();
//            //Team findTeam=em.find(Team.class,findTeamId);
//
//
//            Team teamB=new Team();
//            teamB.setName("TeamB");
//            em.persist(teamB);
//
//            member.setTeam(teamB);

//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//            hellojpa.mapping_domain.Member member = new hellojpa.mapping_domain.Member();
//            member.setUsername("member1");
//            member.setTeam(team);
//            //역방향(주인이 아닌 방향)만 연관관계 설정
//            //team.getMembers().add(member);
//            em.persist(member);
//
////            em.flush();
////            em.clear();
//
//            Team findTeam=em.find(Team.class,team.getId());
//            List<hellojpa.mapping_domain.Member> members=findTeam.getMembers();
//            System.out.println("for-each!");
//            for (hellojpa.mapping_domain.Member m : members) {
//                System.out.println("m="+m.getUsername());
//            }

//            Team t1=new Team();
//            t1.setName("t1");
//
//            em.persist(t1);
//
//            hellojpa.mapping_domain.Member m1=new hellojpa.mapping_domain.Member();
//            m1.setUsername("member1");
//
//            m1.setTeam(t1);
//            em.persist(m1);
//
//            em.flush();
//            em.clear();
//
//            hellojpa.mapping_domain.Member fm=em.find(hellojpa.mapping_domain.Member.class,m1.getId());
//
//            System.out.println("fm team = " + fm.getTeam().getClass());
//
//            System.out.println("fm.getTeam().getName() = " + fm.getTeam().getName());

            Child child1=new Child();
            Child child2=new Child();

            Parent parent=new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);

            em.flush();
            em.clear();

            Parent fp = em.find(Parent.class, parent.getId());
            fp.getChildren().remove(0);

            tx.commit();

        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

    private static void update(EntityManager em,Long id, String name) {
        Member member= em.find(Member.class,id);
        member.setName(name);
    }

    private static void remove(EntityManager em) {
        Member member= em.find(Member.class,1L);
        em.remove(member);
    }

    private static void insert(EntityManager em,Long id, String name) {
        Member member=new Member();
        member.setName(name);
        //member.setId(id);
        System.out.println("===BEFORE===");
        em.persist(member);
        System.out.println("===AFTER===");
    }

    private static void find(EntityManager em) {
        Member member= em.find(Member.class,1L);
        System.out.println("MemberId:"+member.getId());
        System.out.println("MemberName:"+member.getName());
    }
}
