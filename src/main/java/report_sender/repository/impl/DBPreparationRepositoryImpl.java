package report_sender.repository.impl;

import report_sender.repository.DBPreparationRepository;
import report_sender.repository.RepositoryProvider;
import report_sender.repository.TransactionalRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.exception.TransactionalException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBPreparationRepositoryImpl implements DBPreparationRepository {
    private RepositoryProvider repositoryProvider= RepositoryProvider.getInstance();
    private TransactionalRepository transactionalRepository = repositoryProvider.getTransactionalRepository();
    private static final DBPreparationRepositoryImpl INSTANCE = new DBPreparationRepositoryImpl();
    private DBPreparationRepositoryImpl() {
    }
    public static DBPreparationRepositoryImpl getInstance() {
        return INSTANCE;
    }
    @Override
    public void createTables() throws RepositoryException {
        Connection connection = null;
        try {
            connection = transactionalRepository.startTransaction();
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n" +
                    "  id INT GENERATED ALWAYS AS IDENTITY,\n" +
                    "  name VARCHAR(55) NOT NULL,\n" +
                    "  password VARCHAR (55),\n" +
                    "  PRIMARY KEY(id)" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS tasks (\n" +
                    "  id INT GENERATED ALWAYS AS IDENTITY,\n" +
                    "  job VARCHAR(1000) NOT NULL,\n" +
                    "  timeBegin TIMESTAMP NOT NULL,\n" +
                    "  timeEnd TIMESTAMP NOT NULL,\n" +
                    "  user_id INT NOT NULL,\n" +
                    "  PRIMARY KEY(id),\n" +
                    "  CONSTRAINT fk_user\n"+
                    "  FOREIGN KEY(user_id)\n"+
                    "  REFERENCES users(id)\n"+
                    "  ON DELETE NO ACTION\n" +
                    "  ON UPDATE NO ACTION\n" +
                    ");");

            transactionalRepository.commitTransaction(connection);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }

    }

    @Override
    public void createUsers() throws RepositoryException {
        Connection connection = null;
        try {
            connection = transactionalRepository.startTransaction();
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
        try {
            Statement statement = connection.createStatement();
            Statement statement2 = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");
            if (!resultSet.next()) {
                statement2.executeUpdate("INSERT into users (name, password) \n" +
                        "VALUES ('daniil', 'daniil'); \n" +
                        "\n" +
                        "INSERT into users (name, password) \n" +
                        "VALUES ('alisa', 'alisa');\n" +
                        "\n" +
                        "INSERT into users (name, password) \n" +
                        "VALUES ('maksim', 'maksim');");
            }
            transactionalRepository.commitTransaction(connection);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
    }
}
