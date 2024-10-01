package hello.jdbc.service;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceV1Test {

    public static final String MEMBER_A="memberA";
    public static final String MEMBER_B="memberB";
    public static final String MEMBER_EX="ex";

    private MemberRepositoryV1 memberRepositoryV1;
    private MemberServiceV1 memberServiceV1;

    @BeforeEach
    public void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepositoryV1=new MemberRepositoryV1(dataSource);
        memberServiceV1=new MemberServiceV1(memberRepositoryV1);
    }

    @AfterEach
    public void after() throws SQLException {
        memberRepositoryV1.delete(MEMBER_A);
        memberRepositoryV1.delete(MEMBER_B);
        memberRepositoryV1.delete(MEMBER_EX);
    }


    @Test
    @DisplayName("정상 이체")
    public void success() throws SQLException {
        //given
        Member memberA=new Member(MEMBER_A,10000);
        Member memberB=new Member(MEMBER_B,10000);
        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(memberB);
        //when
        memberServiceV1.accountTranster(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member findA=memberRepositoryV1.findById(memberA.getMemberId());
        Member findB=memberRepositoryV1.findById(memberB.getMemberId());
        Assertions.assertThat(findA.getMoney()).isEqualTo(memberA.getMoney()-2000);
        Assertions.assertThat(findB.getMoney()).isEqualTo(memberB.getMoney()+2000);
    }

    @Test
    @DisplayName("예외 발생")
    public void fail() throws SQLException {
        //given
        Member memberA=new Member(MEMBER_A,10000);
        Member memberB=new Member(MEMBER_EX,10000);
        memberRepositoryV1.save(memberA);
        memberRepositoryV1.save(memberB);
        //when
        Assertions.assertThatThrownBy(() -> memberServiceV1.accountTranster(memberA.getMemberId(), memberB.getMemberId(), 2000)).isInstanceOf(
                IllegalStateException.class
        );

        //then
        Member findA=memberRepositoryV1.findById(memberA.getMemberId());
        Member findB=memberRepositoryV1.findById(memberB.getMemberId());
        Assertions.assertThat(findA.getMoney()).isEqualTo(memberA.getMoney()-2000);
        Assertions.assertThat(findB.getMoney()).isEqualTo(memberB.getMoney());
    }


}