package report_sender.repository;

import report_sender.repository.exception.TransactionalException;

import java.sql.Connection;

public interface TransactionalRepository {
    Connection startTransaction() throws TransactionalException;
    void commitTransaction(Connection connection) throws TransactionalException;
    void rollbackTransaction(Connection connection) throws TransactionalException;



}
