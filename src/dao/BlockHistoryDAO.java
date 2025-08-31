package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.BlockHistory;

public class BlockHistoryDAO {
    private Connection connection;

    public BlockHistoryDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean insert(BlockHistory history) {
        String sql = "INSERT INTO block_history (card_id, action, reason) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, history.getCardId());
            stmt.setString(2, history.getAction());
            stmt.setString(3, history.getReason());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir histórico: " + e.getMessage());
        }

        return false;
    }

    public List<BlockHistory> findByCardId(int cardId) {
        List<BlockHistory> history = new ArrayList<>();
        String sql = "SELECT * FROM block_history WHERE card_id = ? ORDER BY action_time ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BlockHistory item = new BlockHistory();
                    item.setId(rs.getInt("id"));
                    item.setCardId(rs.getInt("card_id"));
                    item.setAction(rs.getString("action"));
                    item.setReason(rs.getString("reason"));
                    item.setActionTime(rs.getTimestamp("action_time").toLocalDateTime());

                    history.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar histórico: " + e.getMessage());
        }

        return history;
    }

    public List<BlockHistory> findByBoardId(int boardId) {
        List<BlockHistory> history = new ArrayList<>();
        String sql = """
            SELECT bh.* FROM block_history bh 
            JOIN cards c ON bh.card_id = c.id 
            JOIN columns col ON c.column_id = col.id 
            WHERE col.board_id = ? 
            ORDER BY bh.action_time DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BlockHistory item = new BlockHistory();
                    item.setId(rs.getInt("id"));
                    item.setCardId(rs.getInt("card_id"));
                    item.setAction(rs.getString("action"));
                    item.setReason(rs.getString("reason"));
                    item.setActionTime(rs.getTimestamp("action_time").toLocalDateTime());

                    history.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar histórico do board: " + e.getMessage());
        }

        return history;
    }
}