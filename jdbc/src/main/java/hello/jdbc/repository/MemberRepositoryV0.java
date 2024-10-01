package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql="insert into member(member_id,money) values (?,?)";

        Connection con=null;
        PreparedStatement pstmt=null;
        try {
            con = getConnection();
            pstmt=con.prepareStatement(sql);
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2,member.getMoney());
            pstmt.executeUpdate();
            return member;
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql="select * from member where member_id =?";

        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try{
            connection=getConnection();

            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,memberId);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Member member=new Member();
                member.setMemberId(resultSet.getString("member_id"));
                member.setMoney(resultSet.getInt("money"));

                return member;
            }else{
                throw new NoSuchElementException("member not found memberId="+memberId);
            }
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(connection,preparedStatement,resultSet);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql="update member set money=? where member_id=?";

        Connection con=null;
        PreparedStatement preparedStatement=null;

        try{
            con = getConnection();
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,money);
            preparedStatement.setString(2,memberId);
            int resultSize=preparedStatement.executeUpdate();
            log.info("resultSize={}",resultSize);
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,preparedStatement,null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con=null;
        PreparedStatement preparedStatement=null;

        try{
            con = getConnection();
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,memberId);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,preparedStatement,null);
        }

    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        if(rs!=null){
            try{
                rs.close();
            }catch (SQLException e){
                log.info("error",e);
            }
        }

        if(stmt!=null) {
            try {
                stmt.close();
            }catch (SQLException e){
                log.info("error",e);
            }
        }

        if(con!=null) {
            try {
                con.close();
            }catch (SQLException e){
                log.info("error",e);
            }
        }
    }

    private Connection getConnection(){
        return DBConnectionUtil.getConnection();
    }
}
