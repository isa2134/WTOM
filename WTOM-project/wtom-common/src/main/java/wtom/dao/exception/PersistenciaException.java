package wtom.dao.exception;

import java.sql.SQLException;


public class PersistenciaException extends Exception {
    
    public PersistenciaException(String msg){
        super(msg);
    }

    public PersistenciaException(String string, SQLException e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
