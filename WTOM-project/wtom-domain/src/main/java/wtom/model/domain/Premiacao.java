package wtom.model.domain;

import java.time.LocalDate;
import java.util.Objects;
import wtom.model.domain.util.TipoPremio;

public class Premiacao {

    private Long id;                    
    private Olimpiada olimpiada;        
    private TipoPremio tipoPremio;      
    private String nivel;               
    private Integer ano;                
    private double pesoFinal;           

    public Premiacao() {}

    public Premiacao(Long id, Olimpiada olimpiada, TipoPremio tipoPremio, String nivel, Integer ano) {

        this.id = id;
        this.olimpiada = Objects.requireNonNull(olimpiada);
        this.tipoPremio = Objects.requireNonNull(tipoPremio);
        this.nivel = nivel;
        this.ano = ano;

        this.pesoFinal = calcularPeso();
    }

    private double calcularPeso() {
        return olimpiada.getPesoOlimpiada() * tipoPremio.getMultiplicador();
    }

    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { this.id = id; }

    public Olimpiada getOlimpiada() { 
        return olimpiada;
    }
    
    public void setOlimpiada(Olimpiada olimpiada) {
        this.olimpiada = olimpiada;
        this.pesoFinal = calcularPeso();
    }

    public TipoPremio getTipoPremio() { 
        return tipoPremio; 
    }
    
    public void setTipoPremio(TipoPremio tipoPremio) {
        this.tipoPremio = tipoPremio;
        this.pesoFinal = calcularPeso();
    }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public double getPesoFinal() { return pesoFinal; }

    @Override
    public String toString() {
        return olimpiada.getNome() + " - " + tipoPremio + " (" + ano + ")";
    }
}
