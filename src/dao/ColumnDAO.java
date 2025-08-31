package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Column;
import model.ColumnType;

public class ColumnDAO {
    private Connection connection;

    public ColumnDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public int insert(Column column) {
        String sql = "INSERT INTO columns (board_id, name, column_order, type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, column.getBoardId());
            stmt.setString(2, column.getName());
            stmt.setInt(3, column.getOrder());
            stmt.setString(4, column.getType().toString());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir coluna: " + e.getMessage());
        }

        return -1;
    }

    public List<Column> findByBoardId(int boardId) {
        List<Column> columns = new ArrayList<>();
        String sql = "SELECT * FROM columns WHERE board_id = ? ORDER BY column_order ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Column column = new Column();
                    column.setId(rs.getInt("id"));
                    column.setBoardId(rs.getInt("board_id"));
                    column.setName(rs.getString("name"));
                    column.setOrder(rs.getInt("column_order"));
                    column.setType(ColumnType.valueOf(rs.getString("type")));

                    columns.add(column);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar colunas: " + e.getMessage());
        }

        return columns;
    }

    public Column findById(int id) {
        String sql = "SELECT * FROM columns WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Column column = new Column();
                    column.setId(rs.getInt("id"));
                    column.setBoardId(rs.getInt("board_id"));
                    column.setName(rs.getString("name"));
                    column.setOrder(rs.getInt("column_order"));
                    column.setType(ColumnType.valueOf(rs.getString("type")));

                    return column;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar coluna: " + e.getMessage());
        }

        return null;
    }

    public Column findByBoardIdAndType(int boardId, ColumnType type) {
        String sql = "SELECT * FROM columns WHERE board_id = ? AND type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            stmt.setString(2, type.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Column column = new Column();
                    column.setId(rs.getInt("id"));
                    column.setBoardId(rs.getInt("board_id"));
                    column.setName(rs.getString("name"));
                    column.setOrder(rs.getInt("column_order"));
                    column.setType(ColumnType.valueOf(rs.getString("type")));

                    return column;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar coluna: " + e.getMessage());
        }

        return null;
    }

    public Column findNextColumn(int boardId, int currentOrder) {
        String sql = "SELECT * FROM columns WHERE board_id = ? AND column_order > ? ORDER BY column_order ASC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            stmt.setInt(2, currentOrder);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Column column = new Column();
                    column.setId(rs.getInt("id"));
                    column.setBoardId(rs.getInt("board_id"));
                    column.setName(rs.getString("name"));
                    column.setOrder(rs.getInt("column_order"));
                    column.setType(ColumnType.valueOf(rs.getString("type")));

                    return column;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar próxima coluna: " + e.getMessage());
        }

        return null;
    }
}