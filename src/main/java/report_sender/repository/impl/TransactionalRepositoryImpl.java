package report_sender.repository.impl;

import org.apache.commons.dbcp2.BasicDataSource;
import report_sender.repository.TransactionalRepository;
import report_sender.repository.exception.TransactionalException;
import static report_sender.ConfigService.*;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionalRepositoryImpl implements TransactionalRepository {

    private static final TransactionalRepositoryImpl INSTANCE = new TransactionalRepositoryImpl();
    private TransactionalRepositoryImpl() {
    }
    public static TransactionalRepository getInstance() {
        return INSTANCE;
    }

    private static final BasicDataSource ds = new BasicDataSource();

    static   {
        try {
            Class.forName(getProperty("database.driver"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver is not found. Include it in your library path ");
        }
        ds.setUrl(getProperty("database.url"));
        ds.setUsername(getProperty("database.username"));
        ds.setPassword(getProperty("database.password"));
        ds.setMinIdle(Integer.parseInt(getProperty("database.minIdl")));
        ds.setMaxIdle(Integer.parseInt(getProperty("database.maxIdle")));
        ds.setMaxOpenPreparedStatements(Integer.parseInt(getProperty("database.maxOpenPreparedStatements")));

//        ds.setUrl("jdbc:postgresql://192.168.1.74:5432/andersen_trainee");
//        ds.setUsername("maksim");
//        ds.setPassword("02042010");
//        ds.setMinIdle(5);
//        ds.setMaxIdle(10);
//        ds.setMaxOpenPreparedStatements(100);
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public Connection startTransaction() throws TransactionalException {
        Connection connection = null;
        try {
            connection = TransactionalRepositoryImpl.getConnection();
        } catch (SQLException e) {
            throw new TransactionalException(e); //"Error while trying to take connection from Connection Pool"
        }
        return connection;
    }

    @Override
    public void commitTransaction(Connection connection) throws TransactionalException {
        try {
            connection.setAutoCommit(false);
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionalException("Error while trying to commit connection: " + e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new TransactionalException("Error while trying to close connection: " + e);
        }
    }

    @Override
    public void rollbackTransaction(Connection connection) throws TransactionalException {
        try {
            connection.setAutoCommit(false);
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionalException("Error while trying to rollback connection: " + e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
