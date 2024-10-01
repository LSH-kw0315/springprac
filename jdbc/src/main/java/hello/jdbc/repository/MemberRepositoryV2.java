package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV2 {

    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource){
        this.dataSource=dataSource;
    }

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

    public Member findById(String memberId,Connection connection) throws SQLException {
        String sql="select * from member where member_id =?";


        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;

        try{

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
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(preparedStatement);
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

    public void update(String memberId, int money,Connection con) throws SQLException {
        String sql="update member set money=? where member_id=?";

        PreparedStatement preparedStatement=null;

        try{
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,money);
            preparedStatement.setString(2,memberId);
            int resultSize=preparedStatement.executeUpdate();
            log.info("resultSize={}",resultSize);
        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            JdbcUtils.closeStatement(preparedStatement);
        }
    }
    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);

    }

    private Connection getConnection() throws SQLException {
        Connection conn=dataSource.getConnection();
        log.info("get connection={}, class={}",dataSource,dataSource.getClass());
        return conn;
    }
}
