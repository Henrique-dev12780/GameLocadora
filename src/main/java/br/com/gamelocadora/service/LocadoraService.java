package br.com.gamelocadora.service;

import br.com.gamelocadora.dao.AluguelDAO;
import br.com.gamelocadora.exception.RegraNegocioException;
import br.com.gamelocadora.model.Aluguel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocadoraService {
    private final AluguelDAO aluguelDAO;

    public LocadoraService(AluguelDAO aluguelDAO) {
        this.aluguelDAO = aluguelDAO;
    }

    public long alugar(long clienteId, long exemplarId, BigDecimal valorDiaria,
                       LocalDateTime momento) throws SQLException {
        HorarioFuncionamento.validar(momento);
        if (clienteId <= 0 || exemplarId <= 0)
            throw new RegraNegocioException("Cliente e exemplar devem ser válidos.");
        if (valorDiaria == null || valorDiaria.compareTo(BigDecimal.ZERO) <= 0)
            throw new RegraNegocioException("O valor da diária deve ser positivo.");

        LocalDate retirada = momento.toLocalDate();
        LocalDate prevista = retirada.plusDays(Aluguel.DIAS_ALUGUEL);
        BigDecimal valorTotal = valorDiaria.multiply(BigDecimal.valueOf(Aluguel.DIAS_ALUGUEL));
        return aluguelDAO.registrarAluguel(clienteId, exemplarId, retirada, prevista, valorTotal);
    }

    // Sobrecarga: permite informar a data/hora atual pelo relógio do sistema.
    public long alugar(long clienteId, long exemplarId, BigDecimal valorDiaria) throws SQLException {
        return alugar(clienteId, exemplarId, valorDiaria, LocalDateTime.now());
    }

    public BigDecimal devolver(long aluguelId, LocalDateTime momento) throws SQLException {
        HorarioFuncionamento.validar(momento);
        return aluguelDAO.registrarDevolucao(aluguelId, momento.toLocalDate());
    }

    public BigDecimal devolver(long aluguelId) throws SQLException {
        return devolver(aluguelId, LocalDateTime.now());
    }
}
