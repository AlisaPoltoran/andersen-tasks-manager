package report_sender.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import report_sender.repository.RepositoryProvider;
import report_sender.repository.TransactionalRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.exception.TransactionalException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

class DBPreparationRepositoryImplTest {
    @InjectMocks
    private DBPreparationRepositoryImpl dbPreparationRepository;

    @Mock
    private RepositoryProvider repositoryProvider;

    @Mock
    private TransactionalRepository transactionalRepository;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException, TransactionalException {
        MockitoAnnotations.openMocks(this);

        when(repositoryProvider.getTransactionalRepository()).thenReturn(transactionalRepository);
        when(transactionalRepository.startTransaction()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
    }

    @Test
    public void testCreateTables() throws RepositoryException, SQLException, TransactionalException {
        dbPreparationRepository.createTables();

        verify(connection).createStatement();
        verify(statement).executeUpdate(anyString());
        verify(transactionalRepository).commitTransaction(connection);
    }

    @Test
    public void testCreateUsers() throws RepositoryException, SQLException, TransactionalException {
        when(resultSet.next()).thenReturn(false);

        dbPreparationRepository.createUsers();

        verify(connection, times(2)).createStatement();
        verify(statement).executeQuery(anyString());
        verify(statement, times(1)).executeUpdate(anyString());
        verify(transactionalRepository).commitTransaction(connection);
    }


}