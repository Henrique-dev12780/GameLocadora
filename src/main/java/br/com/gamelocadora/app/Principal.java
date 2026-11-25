package br.com.gamelocadora.app;

import br.com.gamelocadora.config.Conexao;
import br.com.gamelocadora.dao.ClienteDAO;
import br.com.gamelocadora.dao.JdbcClienteDAO;

public class Principal {
    public static void main(String[] args) {
        try {
            Conexao.testarConexao();
            ClienteDAO clienteDAO = new JdbcClienteDAO();
            System.out.println("Clientes cadastrados:");
            clienteDAO.listarTodos().forEach(cliente -> System.out.println(cliente.resumo()));
        } catch (Exception e) {
            System.err.println("Não foi possível iniciar o backend: " + e.getMessage());
        }
    }
}
