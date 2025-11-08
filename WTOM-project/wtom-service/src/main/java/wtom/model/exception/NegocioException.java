package wtom.model.exception;

import java.util.ArrayList;
import java.util.List;

public class NegocioException extends RuntimeException {

    private List<String> msgs;
    
    public NegocioException() {
        msgs = new ArrayList();
    }
    
    public NegocioException(String msg, Exception exception) {
        super(msg, exception);
    }

    public NegocioException(String msg) {
        super(msg);
    }
    
    public NegocioException(List<String> msgs) {
        this.msgs = msgs;
    }
    
    public List<String> getMessages() {
        return this.msgs;
    }
    
}
