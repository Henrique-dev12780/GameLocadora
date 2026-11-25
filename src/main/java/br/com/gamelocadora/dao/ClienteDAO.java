
package br.com.gamelocadora.dao;

import br.com.gamelocadora.model.Cliente;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClienteDAO {

    Cliente salvar(Cliente cliente) throws SQLException;

    List<Cliente> listarTodos() throws SQLException;

    Optional<Cliente> buscarPorId(long id) throws SQLException;

    void excluir(long id) throws SQLException;
}
