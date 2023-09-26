package report_sender.service;

import report_sender.entity.User;
import report_sender.service.exception.ServiceException;

public interface UserService {
    User validateUser(User user) throws ServiceException;
}
