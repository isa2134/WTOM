package wtom.model.service.exception;

public class AvisoException extends RuntimeException {
    public AvisoException(String msg) { super(msg); }
    public AvisoException(String msg, Throwable t) { super(msg, t); }
}
