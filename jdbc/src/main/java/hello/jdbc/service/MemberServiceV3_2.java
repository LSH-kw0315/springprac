package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;


@Slf4j
public class MemberServiceV3_2  {
    //private final DataSource dataSource;
    //private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate transactionTemplate;
    private final MemberRepositoryV3 memberRepositoryV1;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager,MemberRepositoryV3 repositoryV3){
        this.transactionTemplate=new TransactionTemplate(transactionManager);
        this.memberRepositoryV1=repositoryV3;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        transactionTemplate.executeWithoutResult((status)->{
            try {
                businessLogic(fromId,toId,money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });

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
