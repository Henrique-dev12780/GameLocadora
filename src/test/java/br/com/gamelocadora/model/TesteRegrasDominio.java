
package br.com.gamelocadora.model;

import br.com.gamelocadora.exception.RegraNegocioException;
import br.com.gamelocadora.service.HorarioFuncionamento;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TesteRegrasDominio {

    @Test
    void calculaMultaDeDoisPorCentoPorDia() {

        Cliente c = new Cliente(
            "Ana Souza",
            "ana@email.com"
        );

        Midia jogo = new JogoDisco(
            "Jogo",
            "PS5",
            "Ação",
            new BigDecimal("8.00")
        );

        Exemplar e = new Exemplar(
            1L,
            "PS5-001",
            jogo,
            "BOM",
            null,
            true
        );

        Aluguel a = new Aluguel(
            c,
            e,
            LocalDate.of(2026, 11, 2)
        );

        assertEquals(
            new BigDecimal("3.36"),
            a.calcularMulta(
                LocalDate.of(2026, 11, 12)
            )
        );
    }

    @Test
    void rejeitaClienteComNomeInvalido() {

        assertThrows(
            RegraNegocioException.class,
            () -> new Cliente(
                "A",
                "a@email.com"
            )
        );
    }

    @Test
    void rejeitaOperacaoNoDomingo() {

        assertThrows(
            RegraNegocioException.class,
            () -> HorarioFuncionamento.validar(
                LocalDateTime.of(
                    2026,
                    11,
                    29,
                    10,
                    0
                )
            )
        );
    }

    @Test
    void aceitaSabadoDentroDoHorario() {

        assertDoesNotThrow(
            () -> HorarioFuncionamento.validar(
                LocalDateTime.of(
                    2026,
                    11,
                    28,
                    11,
                    30
                )
            )
        );
    }

    @Test
    void demonstraPolimorfismoComListaDaSuperclasse() {

        List<String> descricoes =
            DemonstracaoPolimorfismo.exemplos();

        assertEquals(
            2,
            descricoes.size()
        );

        assertTrue(
            descricoes.get(0).contains("DISCO")
        );

        assertTrue(
            descricoes.get(1).contains("CARTUCHO")
        );
    }
}
