package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;


@Slf4j
public class MemberServiceV4 {
    //private final DataSource dataSource;
    //private final PlatformTransactionManager transactionManager;
    //private final TransactionTemplate transactionTemplate;
    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository repository){
        this.memberRepository=repository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money){
        businessLogic(fromId,toId,money);
    }

    private void businessLogic(String fromId, String toId, int money) {
        //시작
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
        //끝
    }


    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외");
        }
    }

}
