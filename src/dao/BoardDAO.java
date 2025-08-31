package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Board;

public class BoardDAO {
    private Connection connection;

    public BoardDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public int insert(Board board) {
        String sql = "INSERT INTO boards (name) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, board.getName());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir board: " + e.getMessage());
        }

        return -1;
    }

    public List<Board> findAll() {
        List<Board> boards = new ArrayList<>();
        String sql = "SELECT * FROM boards ORDER BY created_at DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("id"));
                board.setName(rs.getString("name"));
                board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                boards.add(board);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar boards: " + e.getMessage());
        }

        return boards;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM boards WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir board: " + e.getMessage());
        }

        return false;
    }

    public Board findById(int id) {
        String sql = "SELECT * FROM boards WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Board board = new Board();
                    board.setId(rs.getInt("id"));
                    board.setName(rs.getString("name"));
                    board.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                    return board;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar board: " + e.getMessage());
        }

        return null;
    }
}