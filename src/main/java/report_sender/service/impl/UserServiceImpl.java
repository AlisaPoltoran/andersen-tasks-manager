package report_sender.service.impl;

import report_sender.entity.User;
import report_sender.repository.UserRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.impl.UserRepositoryImpl;
import report_sender.service.UserService;
import report_sender.service.exception.ServiceException;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public User validateUser(User user) throws ServiceException {
        try {
            return userRepository.validateUser(user);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void createTable() throws ServiceException {
        try {
            userRepository.createTables();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void createUsers() throws ServiceException {
        try {
            userRepository.createUsers();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


}
