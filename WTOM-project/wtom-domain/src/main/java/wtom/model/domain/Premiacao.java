package wtom.model.domain;

import java.util.Objects;
import wtom.model.domain.util.TipoPremio;

public class Premiacao {

    private Long id;                    
    private Olimpiada olimpiada;        
    private TipoPremio tipoPremio;      
    private String nivel;               
    private Integer ano;                
    private double pesoFinal;           

    public Premiacao() {
        // construtor vazio obrigatório para DAO
    }

    public Premiacao(Long id, Olimpiada olimpiada, TipoPremio tipoPremio, String nivel, Integer ano) {

        this.id = id;
        this.olimpiada = Objects.requireNonNull(olimpiada, "Olimpíada obrigatória");
        this.tipoPremio = Objects.requireNonNull(tipoPremio, "Tipo de prêmio obrigatório");
        this.nivel = nivel;
        this.ano = ano;

        // só calcula quando já tem os dados completos
        recalcularPesoFinal();
    }

    /**
     * Método seguro de cálculo.
     * Só calcula quando olimpiada e tipoPremio não são nulos.
     */
    public void recalcularPesoFinal() {
        if (olimpiada != null && tipoPremio != null) {
            this.pesoFinal = olimpiada.getPesoOlimpiada() * tipoPremio.getMultiplicador();
        }
    }

    // GETTERS E SETTERS -------------------------------------

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Olimpiada getOlimpiada() { 
        return olimpiada; 
    }

    public void setOlimpiada(Olimpiada olimpiada) {
        this.olimpiada = olimpiada;
        // não recalcula aqui (para evitar NPE durante o carregamento do BD)
    }

    public TipoPremio getTipoPremio() { 
        return tipoPremio; 
    }

    public void setTipoPremio(TipoPremio tipoPremio) {
        this.tipoPremio = tipoPremio;
        // não recalcula aqui
    }

    public String getNivel() { 
        return nivel; 
    }

    public void setNivel(String nivel) { 
        this.nivel = nivel; 
    }

    public Integer getAno() { 
        return ano; 
    }

    public void setAno(Integer ano) { 
        this.ano = ano; 
    }

    public double getPesoFinal() { 
        return pesoFinal; 
    }

    /**
     * Usado somente pelo DAO para carregar do banco.
     */
    public void setPesoFinalForDao(double peso) {
        this.pesoFinal = peso;
    }

    @Override
    public String toString() {
        return (olimpiada != null ? olimpiada.getNome() : "???")
                + " - " + tipoPremio + " (" + ano + ")";
    }
}
