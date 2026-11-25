package br.com.gamelocadora.model;

import java.math.BigDecimal;

public class JogoCartucho extends Midia implements Resumivel {
    public JogoCartucho(String titulo, String plataforma, String genero, BigDecimal diaria) {
        super(titulo, plataforma, genero, diaria);
    }

    public JogoCartucho(Long id, String titulo, String plataforma, String genero,
                        BigDecimal diaria, boolean ativo) {
        super(id, titulo, plataforma, genero, diaria, ativo);
    }

    @Override
    public String descricaoFormato() { return "Mídia em cartucho"; }

    @Override
    public String resumo() {
        return getJogoId() + " | " + getJogoTitulo() + " | " + getJogoPlataforma() + " | CARTUCHO";
    }
}
