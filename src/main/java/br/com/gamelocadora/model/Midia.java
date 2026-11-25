package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;
import java.math.BigDecimal;

public abstract class Midia implements Validavel {
    private final Long jogoId;
    private String jogoTitulo;
    private String jogoPlataforma;
    private String jogoGenero;
    private BigDecimal jogoValorDiaria;
    private boolean jogoAtivo;

    public Midia(String titulo, String plataforma, String genero, BigDecimal valorDiaria) {
        this(null, titulo, plataforma, genero, valorDiaria, true);
    }

    public Midia(Long id, String titulo, String plataforma, String genero,
                 BigDecimal valorDiaria, boolean ativo) {
        this.jogoId = id;
        this.jogoTitulo = titulo;
        this.jogoPlataforma = plataforma;
        this.jogoGenero = genero;
        this.jogoValorDiaria = valorDiaria;
        this.jogoAtivo = ativo;
        validar();
    }

    @Override
    public void validar() {
        if (jogoTitulo == null || jogoTitulo.isBlank())
            throw new RegraNegocioException("O título do jogo é obrigatório.");
        if (jogoPlataforma == null || jogoPlataforma.isBlank())
            throw new RegraNegocioException("A plataforma é obrigatória.");
        if (jogoGenero == null || jogoGenero.isBlank())
            throw new RegraNegocioException("O gênero é obrigatório.");
        if (jogoValorDiaria == null || jogoValorDiaria.compareTo(BigDecimal.ZERO) <= 0)
            throw new RegraNegocioException("O valor da diária deve ser maior que zero.");
    }

    public abstract String descricaoFormato();

    public Long getJogoId() { return jogoId; }
    public String getJogoTitulo() { return jogoTitulo; }
    public String getJogoPlataforma() { return jogoPlataforma; }
    public String getJogoGenero() { return jogoGenero; }
    public BigDecimal getJogoValorDiaria() { return jogoValorDiaria; }
    public boolean isJogoAtivo() { return jogoAtivo; }

    public void setJogoTitulo(String titulo) { this.jogoTitulo = titulo; validar(); }
    public void setJogoValorDiaria(BigDecimal valor) { this.jogoValorDiaria = valor; validar(); }
    public void setJogoAtivo(boolean ativo) { this.jogoAtivo = ativo; }
}
