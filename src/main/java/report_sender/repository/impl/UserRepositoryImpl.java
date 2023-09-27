package report_sender.repository.impl;

import report_sender.entity.User;
import report_sender.repository.TransactionalRepository;
import report_sender.repository.UserRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.exception.TransactionalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private TransactionalRepository transactionalRepository = new TransactionalRepositoryImpl();
    private final String SELECT_USER = "SELECT * FROM users WHERE name=? AND password=?";

    @Override
    public User validateUser(User user) throws RepositoryException {
        Connection connection = null;
        try {
            connection = transactionalRepository.startTransaction();
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                user = new User(userId, name, password);
            }
            return user;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
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
