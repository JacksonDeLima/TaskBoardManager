package service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import dao.CardDAO;
import dao.CardMovementDAO;
import dao.ColumnDAO;
import dao.BlockHistoryDAO;
import model.Card;
import model.CardMovement;
import model.BlockHistory;

public class ReportService {
    private CardDAO cardDAO = new CardDAO();
    private CardMovementDAO movementDAO = new CardMovementDAO();
    private ColumnDAO columnDAO = new ColumnDAO();
    private BlockHistoryDAO historyDAO = new BlockHistoryDAO();

    public void generateTimeReport(int boardId) {
        List<Card> cards = cardDAO.findByBoardId(boardId);

        System.out.println("\n=== RELATÓRIO DE TEMPOS ===");
        System.out.printf("%-30s %-20s %-20s %-15s%n", "Card", "Criação", "Conclusão", "Tempo Total");
        System.out.println("-".repeat(85));

        for (Card card : cards) {
            LocalDateTime createdTime = movementDAO.getTimeInColumn(card.getId(), card.getColumnId());
            LocalDateTime finishedTime = getFinishedTime(card);

            if (createdTime != null && finishedTime != null) {
                Duration duration = Duration.between(createdTime, finishedTime);
                String timeStr = formatDuration(duration);

                System.out.printf("%-30s %-20s %-20s %-15s%n",
                        truncateString(card.getTitle(), 28),
                        createdTime.toLocalDate().toString(),
                        finishedTime.toLocalDate().toString(),
                        timeStr);
            }
        }
    }

    private LocalDateTime getFinishedTime(Card card) {
        // Verificar se está na coluna final
        model.Column column = columnDAO.findById(card.getColumnId());
        if (column != null && column.getType().name().equals("FINAL")) {
            List<CardMovement> movements = movementDAO.findByCardId(card.getId());
            for (CardMovement movement : movements) {
                if (movement.getToColumnId() == column.getId()) {
                    return movement.getMovedAt();
                }
            }
        }
        return null;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();

        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }

    public void generateBlockReport(int boardId) {
        List<BlockHistory> history = historyDAO.findByBoardId(boardId);

        System.out.println("\n=== RELATÓRIO DE BLOQUEIOS ===");
        System.out.printf("%-20s %-8s %-30s %-20s %-15s%n", "Card", "Ação", "Motivo", "Data/Hora", "Tempo Bloqueado");
        System.out.println("-".repeat(95));

        for (BlockHistory item : history) {
            if (item.getAction().equals("BLOCK")) {
                BlockHistory unblock = findUnblockEvent(history, item.getCardId(), item.getActionTime());
                String timeBlocked = "Ainda bloqueado";

                if (unblock != null) {
                    Duration duration = Duration.between(item.getActionTime(), unblock.getActionTime());
                    timeBlocked = formatDuration(duration);
                }

                Card card = cardDAO.findById(item.getCardId());

                System.out.printf("%-20s %-8s %-30s %-20s %-15s%n",
                        truncateString(card != null ? card.getTitle() : "N/A", 18),
                        "BLOQUEIO",
                        truncateString(item.getReason(), 28),
                        item.getActionTime().toLocalDate().toString(),
                        timeBlocked);
            }
        }
    }

    private BlockHistory findUnblockEvent(List<BlockHistory> history, int cardId, LocalDateTime afterTime) {
        for (BlockHistory item : history) {
            if (item.getCardId() == cardId &&
                    item.getAction().equals("UNBLOCK") &&
                    item.getActionTime().isAfter(afterTime)) {
                return item;
            }
        }
        return null;
    }

    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}