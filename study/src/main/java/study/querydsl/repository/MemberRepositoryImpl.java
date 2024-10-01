package study.querydsl.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.dto.QMemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;

import java.util.List;

public class MemberRepositoryImpl /* extends QuerydslRepositorySupport */implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    public MemberRepositoryImpl(){
//        super(Member.class);
//        this.queryFactory=new JPAQueryFactory(getEntityManager());
//    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
//        return
//                from(QMember.member)
//                        .leftJoin(QMember.member.team, QTeam.team)
//                        .where(
//                                usernameEq(condition.getUsername()),
//                                teamNameEq(condition.getTeamName()),
//                                ageGoe(condition.getAgeGoe()),
//                                ageLoe(condition.getAgeLoe())
//                        ).select(new QMemberTeamDto(
//                                QMember.member.id.as("memberId"),
//                                QMember.member.username,
//                                QMember.member.age,
//                                QTeam.team.id.as("teamId"),
//                                QTeam.team.name
//                        ))
//                        .fetch();
        return queryFactory
                .select(new QMemberTeamDto(
                        QMember.member.id.as("memberId"),
                        QMember.member.username,
                        QMember.member.age,
                        QTeam.team.id.as("teamId"),
                        QTeam.team.name
                ))
                .from(QMember.member)
                .leftJoin(QMember.member.team, QTeam.team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? QMember.member.age.loe(ageLoe) : null;
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? QMember.member.age.goe(ageGoe) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? QTeam.team.name.eq(teamName) : null;
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.hasText(username) ? QMember.member.username.eq(username) : null;
    }

    @Override
    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
//        JPQLQuery<MemberTeamDto> jpaQuery = from(QMember.member)
//                .leftJoin(QMember.member.team, QTeam.team)
//                .where(
//                        usernameEq(condition.getUsername()),
//                        teamNameEq(condition.getTeamName()),
//                        ageGoe(condition.getAgeGoe()),
//                        ageLoe(condition.getAgeLoe())
//                ).select(new QMemberTeamDto(
//                        QMember.member.id.as("memberId"),
//                        QMember.member.username,
//                        QMember.member.age,
//                        QTeam.team.id.as("teamId"),
//                        QTeam.team.name
//                ));
//
//        JPQLQuery<MemberTeamDto> result = getQuerydsl().applyPagination(pageable, jpaQuery);
//        List<MemberTeamDto> content=result.fetchResults().getResults();
//        long total = result.fetchResults().getTotal();
//        return new PageImpl<>(content,pageable,total);
        QueryResults<MemberTeamDto> results= queryFactory
                .select(new QMemberTeamDto(
                        QMember.member.id.as("memberId"),
                        QMember.member.username,
                        QMember.member.age,
                        QTeam.team.id.as("teamId"),
                        QTeam.team.name
                ))
                .from(QMember.member)
                .leftJoin(QMember.member.team, QTeam.team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MemberTeamDto> content=results.getResults();
        long total=results.getTotal();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDto> content= queryFactory
                .select(new QMemberTeamDto(
                        QMember.member.id.as("memberId"),
                        QMember.member.username,
                        QMember.member.age,
                        QTeam.team.id.as("teamId"),
                        QTeam.team.name
                ))
                .from(QMember.member)
                .leftJoin(QMember.member.team, QTeam.team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //long total =
        JPAQuery<Member> countQuery=
                queryFactory
                        .select(QMember.member)
                        .from(QMember.member)
                        .leftJoin(QMember.member.team, QTeam.team)
                        .where(
                                usernameEq(condition.getUsername()),
                                teamNameEq(condition.getTeamName()),
                                ageGoe(condition.getAgeGoe()),
                                ageLoe(condition.getAgeLoe())
                        );
        //                .fetchCount();

        //return new PageImpl<>(content,pageable,total);
        return PageableExecutionUtils.getPage(content,pageable, ()-> countQuery.fetchCount());
    }
}

