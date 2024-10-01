package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import java.util.List;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory query;

    @BeforeEach
    void beforeEach(){
        query=new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);


    }

    @Test
    void startJPQL(){

        Member member = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        Assertions.assertThat(member.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQueryDsl(){
        QMember m=QMember.member;//new QMember("m");

        Member member = query
                .select(m)
                .from(m)
                .where(m.username.eq("member1")) //파라미터 바인딩
                .fetchOne();

        Assertions.assertThat(member.getUsername()).isEqualTo("member1");


    }

    @Test
    void search(){
        Member member =
                query
                        .selectFrom(QMember.member)
                        .where(QMember.member.username.eq("member1").and(QMember.member.age.eq(10)))
                        .fetchOne();
        Assertions.assertThat(member.getUsername()).isEqualTo("member1");
    }

    @Test
    void searchAndParam(){
        Member member =
                query
                        .selectFrom(QMember.member)
                        .where(
                                QMember.member.username.eq("member1"), QMember.member.age.eq(10)
                        )
                        .fetchOne();
        Assertions.assertThat(member.getUsername()).isEqualTo("member1");
    }

    @Test
    void resultFetch(){
        List<Member> fetch = query.selectFrom(QMember.member)
                .fetch();

        try {
            Member fetchOne = query.selectFrom(QMember.member).fetchOne();

        }catch (Exception e){
            System.out.println("예외 발생");
        }

        Member fetchFirst = query.selectFrom(QMember.member).fetchFirst();

        QueryResults<Member> results = query.selectFrom(QMember.member).fetchResults();

        results.getTotal();
        List<Member> content=results.getResults();

        long count
                = query.selectFrom(QMember.member).fetchCount();

    }

    @Test
    void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result=
        query.selectFrom(QMember.member)
                .where(QMember.member.age.eq(100))
                .orderBy(QMember.member.age.desc(), QMember.member.username.asc().nullsLast())
                .fetch();

        Member member5=result.get(0);
        Member member6=result.get(1);
        Member memberNull=result.get(2);
        Assertions.assertThat(member5.getUsername()).isEqualTo("member5");
        Assertions.assertThat(member6.getUsername()).isEqualTo("member6");
        Assertions.assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    void paging(){

        List<Member> result = query
                .selectFrom(QMember.member)
                .orderBy(QMember.member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void paging2(){

        QueryResults<Member> result = query
                .selectFrom(QMember.member)
                .orderBy(QMember.member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        Assertions.assertThat(result.getTotal()).isEqualTo(4);
        Assertions.assertThat(result.getLimit()).isEqualTo(2);
        Assertions.assertThat(result.getOffset()).isEqualTo(1);
        Assertions.assertThat(result.getResults().size()).isEqualTo(2);
    }

    @Test
    void aggregation(){
        List<Tuple> result=
        query
                .select(
                        QMember.member.count(),
                        QMember.member.age.sum(),
                        QMember.member.age.avg(),
                        QMember.member.age.max(),
                        QMember.member.age.min()
                )
                .from(QMember.member)
                .fetch();

        Tuple tuple = result.get(0);
        Assertions.assertThat(tuple.get(QMember.member.count())).isEqualTo(4);
        Assertions.assertThat(tuple.get(QMember.member.age.sum())).isEqualTo(100);
        Assertions.assertThat(tuple.get(QMember.member.age.avg())).isEqualTo(25);
        Assertions.assertThat(tuple.get(QMember.member.age.max())).isEqualTo(40);
        Assertions.assertThat(tuple.get(QMember.member.age.min())).isEqualTo(10);
    }

    @Test
    void groupBy() throws Exception{
        List<Tuple> result=
        query
                .select(
                QTeam.team.name,
                QMember.member.age.avg())
                .from(
                        QMember.member
                )
                .join(QMember.member.team,QTeam.team)
                .groupBy(QTeam.team.name)
                .fetch();

        Tuple teamA=result.get(0);
        Tuple teamB=result.get(1);

        Assertions.assertThat(teamA.get(QTeam.team.name)).isEqualTo("teamA");
        Assertions.assertThat(teamA.get(QMember.member.age.avg())).isEqualTo(15);

        Assertions.assertThat(teamB.get(QTeam.team.name)).isEqualTo("teamB");
        Assertions.assertThat(teamB.get(QMember.member.age.avg())).isEqualTo(35);
    }

    @Test
    void join(){
        List<Member> result
                = query
                .selectFrom(QMember.member)
                .join(QMember.member.team, QTeam.team)
                .where(QTeam.team.name.eq("teamA"))
                .fetch();
        Assertions.assertThat(result)
                .extracting("username")
                .containsExactly("member1","member2");



    }

    @Test
    void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result=
        query
                .select(QMember.member)
                .from(QMember.member, QTeam.team)
                .where(QMember.member.username.eq(QTeam.team.name))
                .fetch();

        Assertions
                .assertThat(result)
                .extracting("username")
                .containsExactly("teamA","teamB")
                ;
    }

    //회원과 팀을 조인하되, teamA인 팀만을 조인. 다만 회원은 모두 나오도록. 팀 정보는 teamA인 애들만 채워진다는 말이다. (join 결과 매칭된 게 없는 경우 보여줄 게 없으므로)
    @Test
    void join_on_filtering() {
        List<Tuple> result=
        query
                .select(QMember.member, QTeam.team)
                .from(QMember.member)
                .leftJoin(QMember.member.team, QTeam.team)
                .on(QTeam.team.name.eq("teamA"))
                .fetch();

        result.forEach(
                tuple -> System.out.println("tuple="+tuple)
        );
    }

    @Test
    void join_on_no_relation(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result=
                query
                        .select(QMember.member, QTeam.team)
                        .from(QMember.member)
                        .leftJoin(QTeam.team)
                        .on(QMember.member.username.eq(QTeam.team.name))
                        .fetch();

        result.forEach(
                tuple -> System.out.println("tuple="+tuple)
        );
    }

    @PersistenceUnit
    EntityManagerFactory emf; //지연로딩 여부를 알 수 있게 함.

    @Test
    void noFetchJoin(){
        em.flush();
        em.clear();

        Member member = query
                        .selectFrom(QMember.member)
                        .where(QMember.member.username.eq("member1"))
                        .fetchOne();

        boolean isloaded=emf.getPersistenceUnitUtil().isLoaded(member.getTeam());
        Assertions.assertThat(isloaded).isFalse();

    }

    @Test
    void fetchJoin() {
        em.flush();
        em.clear();

        Member member = query
                .selectFrom(QMember.member)
                .join(QMember.member.team, QTeam.team).fetchJoin()
                .where(QMember.member.username.eq("member1"))
                .fetchOne();

        boolean isloaded = emf.getPersistenceUnitUtil().isLoaded(member.getTeam());
        Assertions.assertThat(isloaded).isTrue();
    }

    @Test
    void subQuery(){
        QMember memberSub=new QMember("memberSub");

        List<Member> result = query
                .selectFrom(QMember.member)
                .where(QMember.member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();
        //max인 값을 딸랑 구하는 게 아니라 max인 값을 가진 row를 가져와야 하므로 이렇게 짜야함.

        Assertions.assertThat(result).extracting("age")
                .containsExactly(40);
    }

    @Test
    void subQueryGoe(){
        QMember memberSub=new QMember("memberSub");

        List<Member> result = query
                .selectFrom(QMember.member)
                .where(QMember.member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();
        //평균보다 큰 row를 가져와야 하므로

        Assertions.assertThat(result).extracting("age")
                .containsExactly(30,40);
    }

    @Test
    void subQueryIn(){
        QMember memberSub=new QMember("memberSub");

        List<Member> result = query
                .selectFrom(QMember.member)
                .where(QMember.member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();
        //사실 그냥 바로 gt를 써도 된다. 동일한 테이블에서 추출한 값으로 in을 쓰는 건 딱히 의미가 없기 때문

        Assertions.assertThat(result).extracting("age")
                .containsExactly(20,30,40);
    }

    @Test
    void selectSubQuery(){
        QMember memberSub=new QMember("memberSub");

        List<Tuple> result=
        query
                .select(
                        QMember.member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                )
                .from(QMember.member)
                .fetch();

        result.forEach(
                tuple -> System.out.println("tuple="+tuple)
        );
    }

    @Test
    void basicCase(){
        List<String> result = query
                .select(QMember.member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(QMember.member)
                .fetch();

        result.forEach(
                str-> System.out.println("s="+str)
        );
    }

    @Test
    void complexCase(){
        List<String> result=
        query
                .select(new CaseBuilder()
                        .when(QMember.member.age.between(0,20)).then("0~20살")
                        .when(QMember.member.age.between(21,30)).then("21~30살")
                        .otherwise("기타")
                )
                .from(QMember.member)
                .fetch();

        result.forEach(
                s-> System.out.println("str="+s)
        );
    }

    @Test
    void constant(){
        List<Tuple> result =
                query
                        .select(QMember.member.username, Expressions.constant("A"))
                        .from(QMember.member)
                        .fetch();

        result.forEach(
                tuple -> System.out.println("tuple="+tuple)
        );
    }

    @Test
    void concat(){
        List<String> result =
                query
                        .select(QMember.member.username.concat("_").concat(QMember.member.age.stringValue()))
                        .from(QMember.member)
                        .where(QMember.member.username.eq("member1"))
                        .fetch();
        result.forEach(
                s-> System.out.println("str="+s)
        );

    }

    @Test
    void singleProjection(){
        List<String> result =
                query
                        .select(QMember.member.username)
                        .from(QMember.member)
                        .fetch();
        result.forEach(
                str-> System.out.println("s="+str)
        );
    }

    @Test
    void tupleProjection(){
        List<Tuple> result =
                query
                        .select(QMember.member.username, QMember.member.username)
                        .from(QMember.member)
                        .fetch();

        result
                .forEach(
                        tuple -> {
                            String username=tuple.get(QMember.member.username);
                            Integer age=tuple.get(QMember.member.age);
                            System.out.println("username=" + username);
                            System.out.println("age="+age);
                        }
                );
    }

    @Test
    void findDtoByJPQL(){
        List<MemberDto> result=
        em.createQuery("select new study.querydsl.dto.MemberDto(m.username,m.age) from Member m")
                .getResultList();

        result.forEach(
                memberDto -> System.out.println("memberDto="+memberDto)
        );
    }

    @Test
    void findDtoBySetter() {
        List<MemberDto> result =
                query
                        .select(
                                Projections.bean(
                                        MemberDto.class,
                                        QMember.member.username,
                                        QMember.member.age
                                ))
                        .from(QMember.member)
                        .fetch();

        result.forEach(
                memberDto -> System.out.println("memberDto="+memberDto)
        );
    }

    @Test
    void findDtoByField() {
        List<MemberDto> result =
                query
                        .select(
                                Projections.fields(
                                        MemberDto.class,
                                        QMember.member.username,
                                        QMember.member.age
                                ))
                        .from(QMember.member)
                        .fetch();

        result.forEach(
                memberDto -> System.out.println("memberDto="+memberDto)
        );
    }

    @Test
    void findDtoByConstructor() {
        List<MemberDto> result =
                query
                        .select(
                                Projections.constructor(
                                        MemberDto.class,
                                        QMember.member.username,
                                        QMember.member.age
                                ))
                        .from(QMember.member)
                        .fetch();

        result.forEach(
                memberDto -> System.out.println("memberDto="+memberDto)
        );
    }

    @Test
    void findUserDto() {
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result =
                query
                        .select(
                                Projections.fields(
                                        UserDto.class,
                                        QMember.member.username.as("name"),

                                        ExpressionUtils.as(
                                                JPAExpressions.select(memberSub.age.max()).from(memberSub),
                                                "age"
                                        )
                                ))
                        .from(QMember.member)
                        .fetch();

        result.forEach(
                memberDto -> System.out.println("memberDto="+memberDto)
        );
    }

    @Test
    void findDtoByQueryProjection() {
        List<MemberDto> result =
                query
                        .select(new QMemberDto(QMember.member.username, QMember.member.age))
                        .from(QMember.member)
                        .fetch();

        result.forEach(
                memberDto -> System.out.println("memberDto="+memberDto)
        );
    }

    @Test
    void dynamicQuery_BooleanBuilder(){
        String usernameParam="member1";
        Integer ageParam=null;

        List<Member> result=searchMember1(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameParam, Integer ageParam) {
        BooleanBuilder builder=new BooleanBuilder();
        if(usernameParam !=null){
            builder.and(QMember.member.username.eq(usernameParam));
        }

        if(ageParam != null){
            builder.and(QMember.member.age.eq(ageParam));
        }

        return query
                .selectFrom(QMember.member)
                .where(builder)
                .fetch();
    }

    @Test
    void dynamicQuery_whereParam(){
        String usernameParam="member1";
        Integer ageParam=10;

        List<Member> result= searchMember2(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameParam, Integer ageParam) {
        return query
                .selectFrom(QMember.member)
                .where(usernameEq(usernameParam), ageEq(ageParam))
                .fetch();
    }

    private BooleanExpression  ageEq(Integer ageParam) {
        if(ageParam==null) {
            return null;
        }

        return QMember.member.age.eq(ageParam);
    }

    private BooleanExpression usernameEq(String usernameParam) {
        if(usernameParam==null) {
            return null;
        }

        return QMember.member.username.eq(usernameParam);
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    @Test
    void bulkUpdate(){
        long count =
                query
                        .update(QMember.member)
                        .set(QMember.member.username, "비회원")
                        .where(QMember.member.age.lt(28))
                        .execute();


    }

    @Test
    void bulkAdd(){
        long count =
                query
                        .update(QMember.member)
                        .set(QMember.member.age, QMember.member.age.add(1))
                        .execute();
    }

    @Test
    void bulkMultiply(){
        long count =
                query
                        .update(QMember.member)
                        .set(QMember.member.age, QMember.member.age.multiply(2))
                        .execute();
    }

    @Test
    void bulkDelete(){
        long count=
        query
                .delete(QMember.member)
                .where(QMember.member.age.gt(18))
                .execute();
    }

    @Test
    void sqlFunction(){

        List<String> result =
                query.select(
                                Expressions.stringTemplate("function('replace',{0},{1},{2})",
                                        QMember.member.username, "member", "M")
                        )
                        .from(QMember.member)
                        .fetch();
        result.forEach(
                str-> System.out.println("str="+str)
        );

    }

    @Test
    void sqlFunction2(){
        List<String> result =
                query.
                        select(QMember.member.username)
                        .from(QMember.member)
                        .where(QMember.member.username.eq(
                               // Expressions.stringTemplate("function('lower',{0})", QMember.member.username)
                                QMember.member.username.lower()
                        )).fetch();

        result.forEach(
                str-> System.out.println("str="+str)
        );
    }


}
