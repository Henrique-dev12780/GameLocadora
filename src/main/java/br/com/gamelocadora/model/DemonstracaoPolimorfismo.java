package br.com.gamelocadora.model;

import java.math.BigDecimal;
import java.util.List;

/** Demonstra A4: uma lista da superclasse guarda objetos de subclasses diferentes. */
public final class DemonstracaoPolimorfismo {
    private DemonstracaoPolimorfismo() { }

    public static void executar() {
        List<Midia> catalogo = List.of(
            new JogoDisco("The Last of Us Part II", "PlayStation 4", "Ação", new BigDecimal("8.00")),
            new JogoCartucho("Mario Kart 8 Deluxe", "Nintendo Switch", "Corrida", new BigDecimal("7.00"))
        );

        for (Midia midia : catalogo) {
            // A JVM escolhe a implementação sobrescrita conforme o tipo real do objeto.
            System.out.println(midia.getJogoTitulo() + " - " + midia.descricaoFormato());
        }
    }
}
