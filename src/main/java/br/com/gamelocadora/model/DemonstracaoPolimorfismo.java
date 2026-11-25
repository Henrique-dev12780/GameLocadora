
package br.com.gamelocadora.model;

import java.math.BigDecimal;
import java.util.List;

public final class DemonstracaoPolimorfismo {

    private DemonstracaoPolimorfismo() {
    }

    public static List<String> exemplos() {

        List<Midia> catalogo = List.of(
            new JogoDisco(
                "The Last of Us Part II",
                "PS4",
                "Ação",
                new BigDecimal("8.00")
            ),
            new JogoCartucho(
                "Mario Kart 8 Deluxe",
                "Switch",
                "Corrida",
                new BigDecimal("7.00")
            )
        );

        return catalogo.stream()
            .map(midia -> {

                String tipo = midia instanceof JogoDisco
                    ? "DISCO"
                    : midia instanceof JogoCartucho
                        ? "CARTUCHO"
                        : "MIDIA";

                return tipo + " - " + midia.resumo();
            })
            .toList();
    }
}
