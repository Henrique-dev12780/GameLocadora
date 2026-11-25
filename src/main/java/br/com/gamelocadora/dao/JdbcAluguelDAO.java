package br.com.gamelocadora.dao;

import br.com.gamelocadora.config.Conexao;
import br.com.gamelocadora.exception.RegraNegocioException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class JdbcAluguelDAO implements AluguelDAO {
    @Override
    public long registrarAluguel(long clienteId, long exemplarId, LocalDate retirada,
                                 LocalDate prevista, BigDecimal valorTotal) throws SQLException {
        String bloquearExemplar = "SELECT exemplar_id FROM exemplar "
                                + "WHERE exemplar_id = ? AND exemplar_ativo = TRUE FOR UPDATE";
        String verificarAberto = "SELECT 1 FROM aluguel "
                               + "WHERE aluguel_exemplar_id = ? AND aluguel_data_devolucao_real IS NULL";
        String inserir = "INSERT INTO aluguel (aluguel_cliente_id, aluguel_exemplar_id, "
                       + "aluguel_data_retirada, aluguel_data_prevista_devolucao, aluguel_valor_total) "
                       + "VALUES (?, ?, ?, ?, ?) RETURNING aluguel_id";

        try (Connection c = Conexao.obterConexao()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement ps = c.prepareStatement(bloquearExemplar)) {
                    ps.setLong(1, exemplarId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new RegraNegocioException("Exemplar não encontrado ou inativo.");
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(verificarAberto)) {
                    ps.setLong(1, exemplarId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) throw new RegraNegocioException("Esta cópia já possui um aluguel em aberto.");
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(inserir)) {
                    ps.setLong(1, clienteId);
                    ps.setLong(2, exemplarId);
                    ps.setDate(3, Date.valueOf(retirada));
                    ps.setDate(4, Date.valueOf(prevista));
                    ps.setBigDecimal(5, valorTotal);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        long id = rs.getLong("aluguel_id");
                        c.commit();
                        return id;
                    }
                }
            } catch (Exception e) {
                c.rollback();
                if (e instanceof SQLException sqlException) throw sqlException;
                if (e instanceof RuntimeException runtimeException) throw runtimeException;
                throw new SQLException("Falha ao registrar aluguel.", e);
            }
        }
    }

    @Override
    public BigDecimal registrarDevolucao(long aluguelId, LocalDate devolucao) throws SQLException {
        String buscar = "SELECT aluguel_data_prevista_devolucao, aluguel_data_retirada, aluguel_valor_total "
                      + "FROM aluguel WHERE aluguel_id = ? AND aluguel_data_devolucao_real IS NULL FOR UPDATE";
        String atualizar = "UPDATE aluguel SET aluguel_data_devolucao_real = ?, aluguel_valor_multa = ? "
                         + "WHERE aluguel_id = ?";
        try (Connection c = Conexao.obterConexao()) {
            c.setAutoCommit(false);
            try {
                LocalDate prevista;
                LocalDate retirada;
                BigDecimal total;
                try (PreparedStatement ps = c.prepareStatement(buscar)) {
                    ps.setLong(1, aluguelId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new RegraNegocioException("Aluguel não encontrado ou já devolvido.");
                        prevista = rs.getDate("aluguel_data_prevista_devolucao").toLocalDate();
                        retirada = rs.getDate("aluguel_data_retirada").toLocalDate();
                        total = rs.getBigDecimal("aluguel_valor_total");
                    }
                }
                if (devolucao.isBefore(retirada))
                    throw new RegraNegocioException("A devolução não pode ser anterior à retirada.");

                long diasAtraso = Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(prevista, devolucao));
                BigDecimal multa = total.multiply(new BigDecimal("0.02"))
                    .multiply(BigDecimal.valueOf(diasAtraso))
                    .setScale(2, java.math.RoundingMode.HALF_UP);

                try (PreparedStatement ps = c.prepareStatement(atualizar)) {
                    ps.setDate(1, Date.valueOf(devolucao));
                    ps.setBigDecimal(2, multa);
                    ps.setLong(3, aluguelId);
                    ps.executeUpdate();
                }
                c.commit();
                return multa;
            } catch (Exception e) {
                c.rollback();
                if (e instanceof SQLException sqlException) throw sqlException;
                if (e instanceof RuntimeException runtimeException) throw runtimeException;
                throw new SQLException("Falha ao registrar devolução.", e);
            }
        }
    }
}
