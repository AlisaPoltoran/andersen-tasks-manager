package report_sender.service.impl;

import report_sender.entity.User;
import report_sender.repository.DBPreparationRepository;
import report_sender.repository.RepositoryProvider;
import report_sender.repository.TaskRepository;
import report_sender.repository.UserRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.impl.UserRepositoryImpl;
import report_sender.service.UserService;
import report_sender.service.exception.ServiceException;

public class UserServiceImpl implements UserService {
    private RepositoryProvider repositoryProvider = RepositoryProvider.getInstance();
    private UserRepository userRepository = repositoryProvider.getUserRepository();
    private DBPreparationRepository dbPreparationRepository = repositoryProvider.getDBPreparationRepository();
    private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    private UserServiceImpl() {
    }
    public static UserServiceImpl getInstance() {
        return INSTANCE;
    }

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
            dbPreparationRepository.createTables();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void createUsers() throws ServiceException {
        try {
            dbPreparationRepository.createUsers();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


}
