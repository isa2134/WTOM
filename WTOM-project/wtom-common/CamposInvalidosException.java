public class CamposInvalidosException extends RuntimeException {
    public CamposInvalidosException(){
        super("Senha ou usuário inválidos");
    }
}
