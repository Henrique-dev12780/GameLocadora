package br.com.gamelocadora.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Conexao {
    private static final Properties PROPERTIES = carregar();

    private Conexao() { }

    private static Properties carregar() {
        Properties properties = new Properties();
        Path arquivo = Path.of("config.properties");
        if (!Files.exists(arquivo)) {
            throw new IllegalStateException(
                "Arquivo config.properties não encontrado. Copie config.exemplo.properties e configure o acesso."
            );
        }
        try (InputStream input = Files.newInputStream(arquivo)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler config.properties.", e);
        }
    }

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(
            PROPERTIES.getProperty("db.url"),
            PROPERTIES.getProperty("db.usuario"),
            PROPERTIES.getProperty("db.senha")
        );
    }

    public static void testarConexao() throws SQLException {
        try (Connection ignored = obterConexao()) {
            System.out.println("Conexão com PostgreSQL estabelecida.");
        }
    }
}
