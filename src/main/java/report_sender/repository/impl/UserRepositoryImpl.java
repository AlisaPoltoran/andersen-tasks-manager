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
            statement.executeQuery("CREATE TABLE IF NOT EXISTS users (\n" +
                    "  id serial PRIMARY KEY,\n" +
                    "  name VARCHAR(55) NOT NULL,\n" +
                    "  password VARCHAR (55)\n" +
                    ")\n" +
                    "\n" +
                    "CREATE TABLE IF NOT EXISTS tasks (\n" +
                    "  id serial PRIMARY KEY,\n" +
                    "  toDo VARCHAR(1000) NOT NULL,\n" +
                    "  timeBegin TIMESTAMP NOT NULL,\n" +
                    "  timeEnd TIMESTAMP NOT NULL,\n" +
                    "  user_id INT NOT NULL,\n" +
                    "  FOREGN KEY (user_id),\n" +
                    "  REFERENCES countries (id)\n" +
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
            statement.executeQuery("INSERT into users (id, name, password) \n" +
                    "VALUES (1, 'daniil', 'daniil') \n" +
                    "ON CONFLICT (id, name, password) DO NOTHING;\n" +
                    "\n" +
                    "INSERT into users (id, name, password) \n" +
                    "VALUES (2, 'alisa', 'alisa')\n" +
                    "ON CONFLICT (id, name, password) DO NOTHING;\n" +
                    "\n" +
                    "INSERT into users (id, name, password) \n" +
                    "VALUES (3, 'maksim', 'maksim')\n" +
                    "ON CONFLICT (id, name, password) DO NOTHING;");
            transactionalRepository.commitTransaction(connection);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        } catch (TransactionalException e) {
            throw new RepositoryException(e);
        }
    }
}
