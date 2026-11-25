package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;

public class Exemplar {
    private final Long exemplarId;
    private final String exemplarCodigo;
    private final Midia midia;
    private final String exemplarEstado;
    private final boolean exemplarAtivo;

    public Exemplar(Long id, String codigo, Midia midia, String estado, boolean ativo) {
        if (codigo == null || codigo.isBlank())
            throw new RegraNegocioException("O código do exemplar é obrigatório.");
        if (midia == null)
            throw new RegraNegocioException("O exemplar precisa estar associado a um jogo.");
        this.exemplarId = id;
        this.exemplarCodigo = codigo;
        this.midia = midia;
        this.exemplarEstado = estado;
        this.exemplarAtivo = ativo;
    }

    public Long getExemplarId() { return exemplarId; }
    public String getExemplarCodigo() { return exemplarCodigo; }
    public Midia getMidia() { return midia; }
    public String getExemplarEstado() { return exemplarEstado; }
    public boolean isExemplarAtivo() { return exemplarAtivo; }
}
