
package br.com.gamelocadora.dao;

import br.com.gamelocadora.model.Cliente;
import br.com.gamelocadora.model.Midia;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface LocadoraDAO {

    long inserirCliente(Cliente c) throws SQLException;

    void atualizarCliente(
        long id,
        Cliente c
    ) throws SQLException;

    void excluirCliente(long id) throws SQLException;

    List<String> listarClientes() throws SQLException;

    long inserirJogo(Midia j) throws SQLException;

    void atualizarJogo(
        long id,
        Midia j
    ) throws SQLException;

    void excluirJogo(long id) throws SQLException;

    List<String> listarJogos() throws SQLException;

    long inserirExemplar(
        long jogoId,
        String codigo,
        String estado,
        String obs
    ) throws SQLException;

    void atualizarExemplar(
        long id,
        long jogoId,
        String codigo,
        String estado,
        String obs,
        boolean ativo
    ) throws SQLException;

    void excluirExemplar(long id) throws SQLException;

    List<String> listarExemplares() throws SQLException;

    List<String> listarDisponiveis() throws SQLException;

    long inserirAluguel(
        long clienteId,
        long exemplarId,
        LocalDate retirada
    ) throws SQLException;

    void atualizarObservacaoAluguel(
        long id,
        String observacao
    ) throws SQLException;

    void excluirAluguel(long id) throws SQLException;

    BigDecimal devolver(
        long aluguelId,
        LocalDate data
    ) throws SQLException;

    List<String> listarAlugueis() throws SQLException;

    List<String> listarAbertos() throws SQLException;

    List<String> listarAtrasados(
        LocalDate hoje
    ) throws SQLException;

    List<String> consultaJoin() throws SQLException;

    long inserirJogoComExemplar(
        Midia jogo,
        String codigo,
        String estado,
        String obs
    ) throws SQLException;
}
