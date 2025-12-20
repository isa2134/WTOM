package wtom.model.domain.util;

public enum TipoPremio {
    OURO(3.0),
    PRATA(2.0),
    BRONZE(1.5),
    MENCAO_HONROSA(1.0);

    private final double multiplicador;

    TipoPremio(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }
}
