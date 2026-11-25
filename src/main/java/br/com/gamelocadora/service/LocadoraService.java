
package br.com.gamelocadora.service;

import br.com.gamelocadora.dao.LocadoraDAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LocadoraService {

    private final LocadoraDAO dao;

    public LocadoraService(LocadoraDAO dao) {
        this.dao = dao;
    }

    public long alugar(
            long cliente,
            long exemplar,
            LocalDateTime momento
    ) throws SQLException {

        HorarioFuncionamento.validar(momento);

        return dao.inserirAluguel(
                cliente,
                exemplar,
                momento.toLocalDate()
        );
    }

    public long alugar(
            long cliente,
            long exemplar
    ) throws SQLException {

        return alugar(
                cliente,
                exemplar,
                LocalDateTime.now()
        );
    }

    public BigDecimal devolver(
            long aluguel,
            LocalDateTime momento
    ) throws SQLException {

        HorarioFuncionamento.validar(momento);

        return dao.devolver(
                aluguel,
                momento.toLocalDate()
        );
    }

    public BigDecimal devolver(
            long aluguel
    ) throws SQLException {

        return devolver(
                aluguel,
                LocalDateTime.now()
        );
    }
}
