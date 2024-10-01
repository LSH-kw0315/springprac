package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceV3_3Test {
    public static final String MEMBER_A="memberA";
    public static final String MEMBER_B="memberB";
    public static final String MEMBER_EX="ex";

    @Autowired
    private MemberRepositoryV3 memberRepositoryV1;

    @Autowired
    private MemberServiceV3_3 memberServiceV1;

   @TestConfiguration
   static class TestConfig{
       @Bean
       DataSource dataSource(){
           return new DriverManagerDataSource(URL,USERNAME,PASSWORD);
       }

       @Bean
       PlatformTransactionManager transactionManager(){
           return new DataSourceTransactionManager(dataSource());
       }

       @Bean
       MemberRepositoryV3 memberRepositoryV3(){
           return new MemberRepositoryV3(dataSource());
       }

       @Bean
       MemberServiceV3_3 memberServiceV3_3(){
           return new MemberServiceV3_3(memberRepositoryV3());
       }
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
        memberServiceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member findA=memberRepositoryV1.findById(memberA.getMemberId());
        Member findB=memberRepositoryV1.findById(memberB.getMemberId());
        Assertions.assertThat(findA.getMoney()).isEqualTo(memberA.getMoney()-2000);
        Assertions.assertThat(findB.getMoney()).isEqualTo(memberB.getMoney()+2000);
    }

    @AfterEach
    public void after() throws SQLException {
       memberRepositoryV1.delete(MEMBER_A);
        memberRepositoryV1.delete(MEMBER_B);
        memberRepositoryV1.delete(MEMBER_EX);
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
        Assertions.assertThatThrownBy(() -> memberServiceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000)).isInstanceOf(
                IllegalStateException.class
        );

        //then
        Member findA=memberRepositoryV1.findById(memberA.getMemberId());
        Member findB=memberRepositoryV1.findById(memberB.getMemberId());
        Assertions.assertThat(findA.getMoney()).isEqualTo(memberA.getMoney());
        Assertions.assertThat(findB.getMoney()).isEqualTo(memberB.getMoney());
    }

    @Test
    public void AopCheck(){
       log.info("memberService class={}",memberServiceV1.getClass());
       log.info("memberRepository class={}",memberRepositoryV1.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberServiceV1)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepositoryV1)).isFalse();
    }

}