
package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;

import java.math.BigDecimal;

public abstract class Midia implements Validavel, Resumivel {

    private final Long jogoId;

    private String jogoTitulo;
    private String jogoPlataforma;
    private String jogoGenero;
    private BigDecimal jogoValorDiaria;
    private boolean jogoAtivo;

    public Midia(
        String t,
        String p,
        String g,
        BigDecimal v
    ) {
        this(
            null,
            t,
            p,
            g,
            v,
            true
        );
    }

    public Midia(
        Long id,
        String t,
        String p,
        String g,
        BigDecimal v,
        boolean a
    ) {

        jogoId = id;
        jogoTitulo = t;
        jogoPlataforma = p;
        jogoGenero = g;
        jogoValorDiaria = v;
        jogoAtivo = a;

        validar();
    }

    @Override
    public void validar() {

        if (jogoTitulo == null || jogoTitulo.isBlank()) {
            throw new RegraNegocioException(
                "Título obrigatório."
            );
        }

        if (jogoPlataforma == null || jogoPlataforma.isBlank()) {
            throw new RegraNegocioException(
                "Plataforma obrigatória."
            );
        }

        if (jogoGenero == null || jogoGenero.isBlank()) {
            throw new RegraNegocioException(
                "Gênero obrigatório."
            );
        }

        if (
            jogoValorDiaria == null ||
            jogoValorDiaria.signum() <= 0
        ) {
            throw new RegraNegocioException(
                "Diária deve ser positiva."
            );
        }
    }

    public abstract String descricaoFormato();

    public Long getJogoId() {

        return jogoId;
    }

    public String getJogoTitulo() {

        return jogoTitulo;
    }

    public String getJogoPlataforma() {

        return jogoPlataforma;
    }

    public String getJogoGenero() {

        return jogoGenero;
    }

    public BigDecimal getJogoValorDiaria() {

        return jogoValorDiaria;
    }

    public boolean isJogoAtivo() {

        return jogoAtivo;
    }

    public void setJogoTitulo(String v) {

        jogoTitulo = v;
        validar();
    }

    public void setJogoValorDiaria(BigDecimal v) {

        jogoValorDiaria = v;
        validar();
    }

    public void setJogoAtivo(boolean v) {

        jogoAtivo = v;
    }

    @Override
    public String resumo() {

        return jogoId +
               " | " + jogoTitulo +
               " | " + jogoPlataforma +
               " | " + descricaoFormato();
    }
}
