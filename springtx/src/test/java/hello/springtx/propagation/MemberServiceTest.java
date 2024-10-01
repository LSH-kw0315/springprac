package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import java.rmi.UnexpectedException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;


    /**
     * 멤버서비스 트랜잭션 x
     * 멤버리포지토리 트랜잭션 ㅇ
     * 로그리포지토리 트랜잭션 ㅇ
     */
    @Test
    public  void outerTxOff_success(){
        //given
        String username="outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    @Test
    public  void outerTxOff_fail(){
        //given
        String username="로그예외_outerTxOff_fail";

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(
                ()->memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    @Test
    public void singleTx(){
        //given
        String username="outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    @Test
    public void outerTxOn_success(){
        //given
        String username="outerTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());
    }

    @Test
    public void outerTxOn_fail(){
        //given
        String username="로그예외_outerTxOff_fail";

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(
                ()->memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        //then
        //모든 데이터 롤백
        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    @Test
    public void recoverException_fail(){
        //given
        String username="로그예외_outerTxOff_fail";

        //when
        org.assertj.core.api.Assertions.assertThatThrownBy(
                ()->memberService.joinV2(username)).isInstanceOf(UnexpectedRollbackException.class);

        //then
        //기대하는 상황은 회원 정보가 저장되는 것
        //허나 둘 다 롤백!
        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

    @Test
    public void recoverException_success(){
        //given
        String username="로그예외_outerTxOff_fail";

        //when

        memberService.joinV2(username);

        //then
        //기대하는 상황은 회원 정보가 저장되는 것
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }

}