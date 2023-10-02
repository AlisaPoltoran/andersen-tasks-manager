package report_sender.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;

import report_sender.entity.Task;
import report_sender.repository.RepositoryProvider;
import report_sender.repository.TransactionalRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.repository.exception.TransactionalException;

import static org.mockito.Mockito.*;

class TaskRepositoryImplTest {

    @InjectMocks
    private TaskRepositoryImpl taskRepository;

    @Mock
    private RepositoryProvider repositoryProvider;

    @Mock
    private TransactionalRepository transactionalRepository;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException, TransactionalException {
        MockitoAnnotations.openMocks(this);

        when(repositoryProvider.getTransactionalRepository()).thenReturn(transactionalRepository);
        when(transactionalRepository.startTransaction()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    public void testSaveTasks() throws SQLException, RepositoryException, TransactionalException {
        Task task = new Task(1, "testToDo", LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1);
        taskRepository.saveTasks(Arrays.asList(task));

        verify(connection, times(1)).prepareStatement(anyString());
        verify(preparedStatement, times(2)).setTimestamp(anyInt(), any());
        verify(preparedStatement, times(1)).setString(anyInt(), anyString());
        verify(preparedStatement, times(1)).setLong(anyInt(), anyLong());
        verify(preparedStatement, times(1)).executeUpdate();
        verify(transactionalRepository, times(1)).commitTransaction(connection);
    }

    @Test
    public void testGetTasksForPeriodAsReport() throws SQLException, RepositoryException, TransactionalException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("testName");

        // Mock behavior for getTasksForPeriodAsReport method
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getInt(anyString())).thenReturn(1);
        when(resultSet.getString("job")).thenReturn("testJob");
        when(resultSet.getTimestamp("timeBegin")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getTimestamp("timeEnd")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        taskRepository.getTasksForPeriodAsReport(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        verify(connection, times(2)).prepareStatement(anyString());
        verify(preparedStatement, times(2)).setTimestamp(anyInt(), any());
        verify(preparedStatement, times(1)).setInt(anyInt(), anyInt());
        verify(resultSet, times(1)).getInt(anyString());
        verify(resultSet, times(1)).getString(anyString());
        verify(transactionalRepository, times(1)).commitTransaction(connection);
    }


}