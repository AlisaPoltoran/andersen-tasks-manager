package report_sender.repository;

import report_sender.entity.User;
import report_sender.repository.exception.RepositoryException;

public interface UserRepository {
    User validateUser(User user) throws RepositoryException;


}
