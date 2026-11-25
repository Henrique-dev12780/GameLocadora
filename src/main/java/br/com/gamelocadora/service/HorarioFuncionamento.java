package br.com.gamelocadora.service;

import br.com.gamelocadora.exception.RegraNegocioException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class HorarioFuncionamento {
    private HorarioFuncionamento() { }

    public static void validar(LocalDateTime momento) {
        DayOfWeek dia = momento.getDayOfWeek();
        LocalTime hora = momento.toLocalTime();

        boolean diaUtil = dia.getValue() >= DayOfWeek.MONDAY.getValue()
                       && dia.getValue() <= DayOfWeek.FRIDAY.getValue();
        boolean sabado = dia == DayOfWeek.SATURDAY;

        boolean dentroDoHorario = diaUtil
            ? !hora.isBefore(LocalTime.of(8, 0)) && hora.isBefore(LocalTime.of(18, 0))
            : sabado && !hora.isBefore(LocalTime.of(8, 0)) && hora.isBefore(LocalTime.NOON);

        if (!dentroDoHorario) {
            throw new RegraNegocioException(
                "Atendimento: segunda a sexta, das 08:00 às 18:00; sábado, das 08:00 às 12:00. Domingo fechado."
            );
        }
    }
}
