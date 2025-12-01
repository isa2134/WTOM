package wtom.model.service.exception;

public class ReuniaoException extends RuntimeException {
    public ReuniaoException(String msg) { super(msg); }
    public ReuniaoException(String msg, Throwable t) { super(msg, t); }
}
