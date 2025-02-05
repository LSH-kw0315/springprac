package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@SpringBootTest
class MemberServiceV4Test {
    public static final String MEMBER_A="memberA";
    public static final String MEMBER_B="memberB";
    public static final String MEMBER_EX="ex";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberServiceV4 memberService;

   @TestConfiguration
   static class TestConfig{

       private final DataSource dataSource;

       public TestConfig(DataSource dataSource){
           this.dataSource=dataSource;
       }

       @Bean
       MemberRepository memberRepository(){


           //return new MemberRepositoryV4_1(dataSource);
           //return new MemberRepositoryV4_2(dataSource);
           return new MemberRepositoryV5(dataSource);
       }

       @Bean
       MemberServiceV4 memberServiceV4(){
           return new MemberServiceV4(memberRepository());
       }
   }

    @Test
    @DisplayName("정상 이체")
    public void success(){
        //given
        Member memberA=new Member(MEMBER_A,10000);
        Member memberB=new Member(MEMBER_B,10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        //when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member findA=memberRepository.findById(memberA.getMemberId());
        Member findB=memberRepository.findById(memberB.getMemberId());
        Assertions.assertThat(findA.getMoney()).isEqualTo(memberA.getMoney()-2000);
        Assertions.assertThat(findB.getMoney()).isEqualTo(memberB.getMoney()+2000);
    }

    @AfterEach
    public void after() {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("예외 발생")
    public void fail() {
        //given
        Member memberA=new Member(MEMBER_A,10000);
        Member memberB=new Member(MEMBER_EX,10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        //when
        Assertions.assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000)).isInstanceOf(
                IllegalStateException.class
        );

        //then
        Member findA=memberRepository.findById(memberA.getMemberId());
        Member findB=memberRepository.findById(memberB.getMemberId());
        Assertions.assertThat(findA.getMoney()).isEqualTo(memberA.getMoney());
        Assertions.assertThat(findB.getMoney()).isEqualTo(memberB.getMoney());
    }

    @Test
    public void AopCheck(){
       log.info("memberService class={}",memberService.getClass());
       log.info("memberRepository class={}",memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }

}