package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Card;

public class CardDAO {
    private Connection connection;

    public CardDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public int insert(Card card) {
        String sql = "INSERT INTO cards (column_id, title, description) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, card.getColumnId());
            stmt.setString(2, card.getTitle());
            stmt.setString(3, card.getDescription());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir card: " + e.getMessage());
        }

        return -1;
    }

    public List<Card> findByColumnId(int columnId) {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM cards WHERE column_id = ? ORDER BY created_at ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, columnId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card();
                    card.setId(rs.getInt("id"));
                    card.setColumnId(rs.getInt("column_id"));
                    card.setTitle(rs.getString("title"));
                    card.setDescription(rs.getString("description"));
                    card.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    card.setBlocked(rs.getBoolean("is_blocked"));
                    card.setBlockReason(rs.getString("block_reason"));

                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cards: " + e.getMessage());
        }

        return cards;
    }

    public boolean updateColumn(int cardId, int columnId) {
        String sql = "UPDATE cards SET column_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, columnId);
            stmt.setInt(2, cardId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar card: " + e.getMessage());
        }

        return false;
    }

    public boolean updateBlockStatus(int cardId, boolean isBlocked, String reason) {
        String sql = "UPDATE cards SET is_blocked = ?, block_reason = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isBlocked);
            stmt.setString(2, isBlocked ? reason : null);
            stmt.setInt(3, cardId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status de bloqueio: " + e.getMessage());
        }

        return false;
    }

    public Card findById(int id) {
        String sql = "SELECT * FROM cards WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Card card = new Card();
                    card.setId(rs.getInt("id"));
                    card.setColumnId(rs.getInt("column_id"));
                    card.setTitle(rs.getString("title"));
                    card.setDescription(rs.getString("description"));
                    card.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    card.setBlocked(rs.getBoolean("is_blocked"));
                    card.setBlockReason(rs.getString("block_reason"));

                    return card;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar card: " + e.getMessage());
        }

        return null;
    }

    public List<Card> findByBoardId(int boardId) {
        List<Card> cards = new ArrayList<>();
        String sql = """
            SELECT c.* FROM cards c 
            JOIN columns col ON c.column_id = col.id 
            WHERE col.board_id = ? 
            ORDER BY col.column_order ASC, c.created_at ASC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card();
                    card.setId(rs.getInt("id"));
                    card.setColumnId(rs.getInt("column_id"));
                    card.setTitle(rs.getString("title"));
                    card.setDescription(rs.getString("description"));
                    card.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    card.setBlocked(rs.getBoolean("is_blocked"));
                    card.setBlockReason(rs.getString("block_reason"));

                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar cards do board: " + e.getMessage());
        }

        return cards;
    }
}