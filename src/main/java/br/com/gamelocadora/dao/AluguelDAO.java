
package br.com.gamelocadora.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public interface AluguelDAO {

    long registrarAluguel(
        long clienteId,
        long exemplarId,
        LocalDate retirada,
        LocalDate prevista,
        BigDecimal valorTotal
    ) throws SQLException;

    BigDecimal registrarDevolucao(
        long aluguelId,
        LocalDate devolucao
    ) throws SQLException;
}
