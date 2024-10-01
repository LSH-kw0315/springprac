package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    public void driverManager() throws SQLException {
        Connection c1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection c2=DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection={}, class={}",c1,c1.getClass());
        log.info("connection={}, class={}",c2,c2.getClass());
    }

    @Test
    public void dataSourceDriverManager() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    private void useDataSource(DataSource dataSource) throws  SQLException{
        Connection c1= dataSource.getConnection();
        Connection c2=dataSource.getConnection();
        log.info("connection={}, class={}",c1,c1.getClass());
        log.info("connection={}, class={}",c2,c2.getClass());
    }

    @Test
    public void dataSourceConnectionPool() throws SQLException, InterruptedException {
        HikariDataSource dataSource=new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000);
    }
}