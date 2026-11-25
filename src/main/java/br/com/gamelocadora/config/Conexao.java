
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

    private static final Properties P = carregar();

    private Conexao() {
    }

    private static Properties carregar() {
        Path arquivo = Path.of("config.properties");

        if (!Files.exists(arquivo)) {
            throw new IllegalStateException(
                "Crie config.properties a partir de " +
                "config.exemplo.properties."
            );
        }

        Properties p = new Properties();

        try (InputStream in = Files.newInputStream(arquivo)) {
            p.load(in);
            return p;
        } catch (IOException e) {
            throw new IllegalStateException(
                "Não foi possível ler config.properties.",
                e
            );
        }
    }

    public static Connection obter() throws SQLException {
        return DriverManager.getConnection(
            P.getProperty("db.url"),
            P.getProperty("db.usuario"),
            P.getProperty("db.senha")
        );
    }

    public static Connection obterConexao() throws SQLException {
        return obter();
    }
}
