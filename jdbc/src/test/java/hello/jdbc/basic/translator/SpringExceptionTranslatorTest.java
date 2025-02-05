package hello.jdbc.basic.translator;

import hello.jdbc.connection.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class SpringExceptionTranslatorTest {

    DataSource dataSource;

    @BeforeEach
    void init(){
        dataSource=new DriverManagerDataSource(URL,USERNAME,PASSWORD);
    }

    @Test
    void sqlExceptionErrorCode(){
        String sql="select sex!!!";
        try{
            Connection con=dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        }catch (SQLException e){
            Assertions.assertThat(e.getErrorCode()).isEqualTo(42000);
            int errorCode = e.getErrorCode();
            log.info("errorCode={}",errorCode);
            log.info("error",e);
        }
    }

    @Test
    public void exceptionTranslator(){
        String sql="select bad grammer";

        try{
            Connection con = dataSource.getConnection();
            PreparedStatement stmt=con.prepareStatement(sql);
            stmt.executeQuery();
        }catch (SQLException e){
            Assertions.assertThat(e.getErrorCode()).isEqualTo(42122);
            //org.springframework.jdbc.support.sql-error-codes.xml
            SQLErrorCodeSQLExceptionTranslator translator=new SQLErrorCodeSQLExceptionTranslator();
            DataAccessException resultEx = translator.translate("select", sql, e);
            log.info("resultEx",resultEx);

            Assertions.assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }
}
