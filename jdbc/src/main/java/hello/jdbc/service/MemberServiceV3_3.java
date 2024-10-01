package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;


@Slf4j
public class MemberServiceV3_3 {
    //private final DataSource dataSource;
    //private final PlatformTransactionManager transactionManager;
    //private final TransactionTemplate transactionTemplate;
    private final MemberRepositoryV3 memberRepositoryV1;

    public MemberServiceV3_3( MemberRepositoryV3 repositoryV3){
        this.memberRepositoryV1=repositoryV3;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        businessLogic(fromId,toId,money);
    }

    private void businessLogic(String fromId, String toId, int money) throws SQLException {
        //시작
        Member fromMember = memberRepositoryV1.findById(fromId);
        Member toMember = memberRepositoryV1.findById(toId);

        memberRepositoryV1.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepositoryV1.update(toId, toMember.getMoney() + money);
        //끝
    }


    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외");
        }
    }

}
