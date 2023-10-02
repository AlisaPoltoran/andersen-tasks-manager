package report_sender.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import report_sender.entity.User;
import report_sender.repository.DBPreparationRepository;
import report_sender.repository.UserRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.service.exception.ServiceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DBPreparationRepository dbPreparationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateUser() throws ServiceException, RepositoryException {
        User mockUser = mock(User.class);
        when(userRepository.validateUser(mockUser)).thenReturn(mockUser);

        userService.validateUser(mockUser);

        verify(userRepository).validateUser(mockUser);
    }

    @Test
    public void testValidateUserWithRepositoryException() throws RepositoryException {
        User mockUser = mock(User.class);
        when(userRepository.validateUser(mockUser)).thenThrow(RepositoryException.class);

        assertThrows(ServiceException.class, () -> userService.validateUser(mockUser));
    }

    @Test
    public void testCreateTable() throws ServiceException, RepositoryException {
        doNothing().when(dbPreparationRepository).createTables();

        userService.createTable();

        verify(dbPreparationRepository).createTables();
    }

    @Test
    public void testCreateTableWithRepositoryException() throws RepositoryException {
        doThrow(RepositoryException.class).when(dbPreparationRepository).createTables();

        assertThrows(ServiceException.class, userService::createTable);
    }

    @Test
    public void testCreateUsers() throws ServiceException, RepositoryException {
        doNothing().when(dbPreparationRepository).createUsers();

        userService.createUsers();

        verify(dbPreparationRepository).createUsers();
    }

    @Test
    public void testCreateUsersWithRepositoryException() throws RepositoryException {
        doThrow(RepositoryException.class).when(dbPreparationRepository).createUsers();

        assertThrows(ServiceException.class, userService::createUsers);
    }

}