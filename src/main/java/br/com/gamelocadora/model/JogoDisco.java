package br.com.gamelocadora.model;

import java.math.BigDecimal;

public class JogoDisco extends Midia implements Resumivel {
    public JogoDisco(String titulo, String plataforma, String genero, BigDecimal diaria) {
        super(titulo, plataforma, genero, diaria);
    }

    public JogoDisco(Long id, String titulo, String plataforma, String genero,
                     BigDecimal diaria, boolean ativo) {
        super(id, titulo, plataforma, genero, diaria, ativo);
    }

    @Override
    public String descricaoFormato() { return "Mídia em disco"; }

    @Override
    public String resumo() {
        return getJogoId() + " | " + getJogoTitulo() + " | " + getJogoPlataforma() + " | DISCO";
    }
}
