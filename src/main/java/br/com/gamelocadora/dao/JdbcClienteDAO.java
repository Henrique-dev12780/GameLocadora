
package br.com.gamelocadora.dao;

import br.com.gamelocadora.config.Conexao;
import br.com.gamelocadora.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcClienteDAO implements ClienteDAO {

    @Override
    public Cliente salvar(Cliente cliente) throws SQLException {

        String sql =
            "INSERT INTO cliente (cliente_nome, cliente_email, cliente_telefone, cliente_ativo) " +
            "VALUES (?, ?, ?, ?) RETURNING cliente_id";

        try (
            Connection conexao = Conexao.obterConexao();
            PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setString(1, cliente.getClienteNome());
            comando.setString(2, cliente.getClienteEmail());
            comando.setString(3, cliente.getClienteTelefone());
            comando.setBoolean(4, cliente.isClienteAtivo());

            try (ResultSet resultado = comando.executeQuery()) {

                resultado.next();

                return new Cliente(
                    resultado.getLong("cliente_id"),
                    cliente.getClienteNome(),
                    cliente.getClienteEmail(),
                    cliente.getClienteTelefone(),
                    cliente.isClienteAtivo()
                );
            }
        }
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {

        String sql =
            "SELECT cliente_id, cliente_nome, cliente_email, cliente_telefone, cliente_ativo " +
            "FROM cliente ORDER BY cliente_nome";

        List<Cliente> clientes = new ArrayList<>();

        try (
            Connection conexao = Conexao.obterConexao();
            PreparedStatement comando = conexao.prepareStatement(sql);
            ResultSet resultado = comando.executeQuery()
        ) {

            while (resultado.next()) {
                clientes.add(mapear(resultado));
            }
        }

        return clientes;
    }

    @Override
    public Optional<Cliente> buscarPorId(long id) throws SQLException {

        String sql =
            "SELECT cliente_id, cliente_nome, cliente_email, cliente_telefone, cliente_ativo " +
            "FROM cliente WHERE cliente_id = ?";

        try (
            Connection conexao = Conexao.obterConexao();
            PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setLong(1, id);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {
                    return Optional.of(mapear(resultado));
                }

                return Optional.empty();
            }
        }
    }

    @Override
    public void excluir(long id) throws SQLException {

        String sql =
            "UPDATE cliente SET cliente_ativo = FALSE WHERE cliente_id = ?";

        try (
            Connection conexao = Conexao.obterConexao();
            PreparedStatement comando = conexao.prepareStatement(sql)
        ) {

            comando.setLong(1, id);
            comando.executeUpdate();
        }
    }

    private Cliente mapear(ResultSet resultado) throws SQLException {

        return new Cliente(
            resultado.getLong("cliente_id"),
            resultado.getString("cliente_nome"),
            resultado.getString("cliente_email"),
            resultado.getString("cliente_telefone"),
            resultado.getBoolean("cliente_ativo")
        );
    }
}
