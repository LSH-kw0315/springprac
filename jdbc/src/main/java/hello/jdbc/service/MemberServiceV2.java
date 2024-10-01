package hello.jdbc.service;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepositoryV1;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            businessLogic(fromId, toId, money, con);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }

    }

    private void businessLogic(String fromId, String toId, int money, Connection con) throws SQLException {
        //시작
        Member fromMember = memberRepositoryV1.findById(fromId, con);
        Member toMember = memberRepositoryV1.findById(toId, con);

        memberRepositoryV1.update(fromId, fromMember.getMoney() - money, con);
        validation(toMember);
        memberRepositoryV1.update(toId, toMember.getMoney() + money, con);
        //끝
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                //위 선언을 안해주면 커넥션 풀에 돌아간 놈은 오토 커밋이 false인 상태로 돌아간다.
                con.close();
            } catch (Exception e) {
                log.info("error",e);
            }
        }
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외");
        }
    }

}
