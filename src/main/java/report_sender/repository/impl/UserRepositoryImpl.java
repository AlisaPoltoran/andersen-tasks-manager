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
}
