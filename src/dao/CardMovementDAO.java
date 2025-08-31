package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.CardMovement;

public class CardMovementDAO {
    private Connection connection;

    public CardMovementDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean insert(CardMovement movement) {
        String sql = "INSERT INTO card_movements (card_id, from_column_id, to_column_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, movement.getCardId());

            if (movement.getFromColumnId() > 0) {
                stmt.setInt(2, movement.getFromColumnId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            stmt.setInt(3, movement.getToColumnId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir movimentação: " + e.getMessage());
        }

        return false;
    }

    public List<CardMovement> findByCardId(int cardId) {
        List<CardMovement> movements = new ArrayList<>();
        String sql = "SELECT * FROM card_movements WHERE card_id = ? ORDER BY moved_at ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CardMovement movement = new CardMovement();
                    movement.setId(rs.getInt("id"));
                    movement.setCardId(rs.getInt("card_id"));

                    int fromColumnId = rs.getInt("from_column_id");
                    movement.setFromColumnId(rs.wasNull() ? 0 : fromColumnId);

                    movement.setToColumnId(rs.getInt("to_column_id"));
                    movement.setMovedAt(rs.getTimestamp("moved_at").toLocalDateTime());

                    movements.add(movement);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar movimentações: " + e.getMessage());
        }

        return movements;
    }

    public LocalDateTime getTimeInColumn(int cardId, int columnId) {
        String sql = """
            SELECT moved_at FROM card_movements 
            WHERE card_id = ? AND to_column_id = ? 
            ORDER BY moved_at ASC 
            LIMIT 1
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardId);
            stmt.setInt(2, columnId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("moved_at").toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar tempo na coluna: " + e.getMessage());
        }

        return null;
    }
}