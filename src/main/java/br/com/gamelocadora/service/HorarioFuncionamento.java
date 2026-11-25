
package br.com.gamelocadora.service;

import br.com.gamelocadora.exception.RegraNegocioException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class HorarioFuncionamento {

    private HorarioFuncionamento() {
    }

    public static void validar(LocalDateTime m) {

        DayOfWeek d = m.getDayOfWeek();

        LocalTime h = m.toLocalTime();

        boolean ok =
            (
                d.getValue() <= 5 &&
                !h.isBefore(LocalTime.of(8, 0)) &&
                h.isBefore(LocalTime.of(18, 0))
            )
            ||
            (
                d == DayOfWeek.SATURDAY &&
                !h.isBefore(LocalTime.of(8, 0)) &&
                h.isBefore(LocalTime.NOON)
            );

        if (!ok) {
            throw new RegraNegocioException(
                "Funcionamento: segunda a sexta, 08:00–18:00; " +
                "sábado, 08:00–12:00; domingo fechado."
            );
        }
    }

    public static LocalDate ajustarPrazo(LocalDate prevista) {

        while (prevista.getDayOfWeek() == DayOfWeek.SUNDAY) {
            prevista = prevista.plusDays(1);
        }

        return prevista;
    }
}
