package report_sender.repository;

import report_sender.repository.impl.DBPreparationRepositoryImpl;
import report_sender.repository.impl.TaskRepositoryImpl;
import report_sender.repository.impl.TransactionalRepositoryImpl;
import report_sender.repository.impl.UserRepositoryImpl;

public class RepositoryProvider {
    private final static RepositoryProvider INSTANCE= new RepositoryProvider();
    private RepositoryProvider() {
    }

    public static RepositoryProvider getInstance() {
        return INSTANCE;
    }

    public DBPreparationRepository getDBPreparationRepository() {
        return DBPreparationRepositoryImpl.getInstance();
    }

    public TransactionalRepository getTransactionalRepository() {
        return TransactionalRepositoryImpl.getInstance();
    }

    public UserRepository getUserRepository() {
        return UserRepositoryImpl.getInstance();
    }

    public TaskRepository getTaskRepository() {
        return TaskRepositoryImpl.getInstance();
    }


}
