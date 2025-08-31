package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "task_boards";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static boolean connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);

            if (connection != null) {
                System.out.println("Conectado ao banco de dados MySQL");
                return true;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao MySQL: " + e.getMessage());

            try {
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                conn.close();

                connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
                System.out.println("Banco de dados criado e conectado com sucesso");
                return true;
            } catch (SQLException ex) {
                System.out.println("Erro ao criar banco de dados: " + ex.getMessage());
            }
        }
        return false;
    }

    public static void createTables() {
        if (connection == null) return;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS boards (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS columns (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    board_id INT NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    column_order INT NOT NULL,
                    type ENUM('INICIAL', 'PENDENTE', 'FINAL', 'CANCELAMENTO') NOT NULL,
                    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cards (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    column_id INT NOT NULL,
                    title VARCHAR(255) NOT NULL,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    is_blocked BOOLEAN DEFAULT FALSE,
                    block_reason TEXT,
                    FOREIGN KEY (column_id) REFERENCES columns(id) ON DELETE CASCADE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS card_movements (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    card_id INT NOT NULL,
                    from_column_id INT,
                    to_column_id INT NOT NULL,
                    moved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE,
                    FOREIGN KEY (from_column_id) REFERENCES columns(id),
                    FOREIGN KEY (to_column_id) REFERENCES columns(id)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS block_history (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    card_id INT NOT NULL,
                    action ENUM('BLOCK', 'UNBLOCK') NOT NULL,
                    reason TEXT,
                    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
                )
            """);

            System.out.println("Tabelas criadas/verificadas com sucesso");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
