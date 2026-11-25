
package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;

public class Exemplar {

    private final Long exemplarId;
    private final String exemplarCodigo;
    private final Midia midia;

    private String exemplarEstado;
    private String exemplarObservacao;
    private boolean exemplarAtivo;

    public Exemplar(
        Long id,
        String codigo,
        Midia jogo,
        String estado,
        String obs,
        boolean ativo
    ) {

        if (codigo == null || codigo.isBlank()) {
            throw new RegraNegocioException(
                "Código do exemplar obrigatório."
            );
        }

        if (jogo == null) {
            throw new RegraNegocioException(
                "Exemplar precisa de um jogo."
            );
        }

        exemplarId = id;
        exemplarCodigo = codigo;
        midia = jogo;
        exemplarEstado = estado;
        exemplarObservacao = obs;
        exemplarAtivo = ativo;
    }

    public Long getExemplarId() {

        return exemplarId;
    }

    public String getExemplarCodigo() {

        return exemplarCodigo;
    }

    public Midia getMidia() {

        return midia;
    }

    public String getExemplarEstado() {

        return exemplarEstado;
    }

    public String getExemplarObservacao() {

        return exemplarObservacao;
    }

    public boolean isExemplarAtivo() {

        return exemplarAtivo;
    }
}
