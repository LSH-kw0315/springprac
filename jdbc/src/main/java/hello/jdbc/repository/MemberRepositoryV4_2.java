package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{

    private final DataSource dataSource;
    private final SQLExceptionTranslator translator;

    public MemberRepositoryV4_2(DataSource dataSource)
    {
        this.dataSource = dataSource;
        this.translator=new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Override
    public Member save(Member member){
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
            throw translator.translate("save",sql,e);
        }finally {
            close(con,pstmt,null);
        }
    }


    @Override
    public Member findById(String memberId) {
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
            throw translator.translate("findById",sql,e);
        }finally {
            close(connection,preparedStatement,resultSet);
        }
    }

    @Override
    public void update(String memberId, int money) {
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
            throw translator.translate("update",sql,e);
        }finally {
            close(con,preparedStatement,null);
        }
    }

    @Override
    public void delete(String memberId) {
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
            throw translator.translate("delete",sql,e);
        }finally {
            close(con,preparedStatement,null);
        }

    }


    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //JdbcUtils.closeConnection(con);
        DataSourceUtils.releaseConnection(con,dataSource);

    }

    private Connection getConnection() throws SQLException {
        Connection conn=DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}",dataSource,dataSource.getClass());
        return conn;
    }
}
