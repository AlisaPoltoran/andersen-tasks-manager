package report_sender.repository.exception;

public class TransactionalException extends Exception {
    public TransactionalException() {
    }

    public TransactionalException(String message) {
        super(message);
    }

    public TransactionalException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionalException(Throwable cause) {
        super(cause);
    }
}
