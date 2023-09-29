package report_sender.repository;

import report_sender.repository.exception.RepositoryException;

public interface DBPreparationRepository {
        void createTables() throws RepositoryException;
        void createUsers() throws  RepositoryException;
}
