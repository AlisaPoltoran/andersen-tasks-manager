package report_sender.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import report_sender.entity.Report;
import report_sender.repository.TaskRepository;
import report_sender.repository.exception.RepositoryException;
import report_sender.service.exception.ServiceException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SavingReportServiceImplTest {
    @InjectMocks
    private SavingReportServiceImpl service;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveReportAsTasks() throws ServiceException, RepositoryException {
        Report mockReport = mock(Report.class);
        when(mockReport.getTaskList()).thenReturn(Collections.emptyList());

        service.saveReportAsTasks(mockReport);

        verify(taskRepository).saveTasks(Collections.emptyList());
    }

    @Test
    public void testSaveReportAsTasksWithRepositoryException() throws RepositoryException {
        Report mockReport = mock(Report.class);
        when(mockReport.getTaskList()).thenReturn(Collections.emptyList());
        doThrow(RepositoryException.class).when(taskRepository).saveTasks(Collections.emptyList());

        assertThrows(ServiceException.class, () -> service.saveReportAsTasks(mockReport));
    }

}