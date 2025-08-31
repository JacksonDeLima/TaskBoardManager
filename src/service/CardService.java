package service;

import java.util.List;
import java.util.Scanner;
import dao.CardDAO;
import dao.CardMovementDAO;
import dao.ColumnDAO;
import dao.BlockHistoryDAO;
import model.Card;
import model.CardMovement;
import model.BlockHistory;
import model.ColumnType;

public class CardService {
    private Scanner scanner = new Scanner(System.in);
    private CardDAO cardDAO = new CardDAO();
    private CardMovementDAO movementDAO = new CardMovementDAO();
    private ColumnDAO columnDAO = new ColumnDAO();
    private BlockHistoryDAO historyDAO = new BlockHistoryDAO();

    public void createCard(int boardId) {
        // Encontrar coluna inicial do board
        model.Column initialColumn = columnDAO.findByBoardIdAndType(boardId, ColumnType.INICIAL);

        if (initialColumn == null) {
            System.out.println("Erro: Coluna inicial não encontrada.");
            return;
        }

        System.out.print("Digite o título do card: ");
        String title = scanner.nextLine();

        System.out.print("Digite a descrição do card: ");
        String description = scanner.nextLine();

        Card card = new Card();
        card.setColumnId(initialColumn.getId());
        card.setTitle(title);
        card.setDescription(description);

        int cardId = cardDAO.insert(card);
        if (cardId > 0) {
            // Registrar a movimentação para a coluna inicial
            CardMovement movement = new CardMovement();
            movement.setCardId(cardId);
            movement.setToColumnId(initialColumn.getId());
            movementDAO.insert(movement);

            System.out.println("Card criado com sucesso!");
        } else {
            System.out.println("Erro ao criar card.");
        }
    }

    public void moveCard(int boardId) {
        // Buscar cards não bloqueados e não finalizados/cancelados
        List<Card> cards = getMovableCards(boardId);

        if (cards.isEmpty()) {
            System.out.println("Nenhum card disponível para movimentação.");
            return;
        }

        System.out.println("\nCards disponíveis para movimentação:");
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            model.Column column = columnDAO.findById(card.getColumnId());
            System.out.println((i + 1) + " - " + card.getTitle() + " (na coluna: " + column.getName() + ")");
        }

        System.out.print("Selecione o card (número) ou 0 para voltar: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (choice == 0) return;

        if (choice > 0 && choice <= cards.size()) {
            Card card = cards.get(choice - 1);
            performCardMove(card, boardId);
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    private List<Card> getMovableCards(int boardId) {
        List<Card> allCards = cardDAO.findByBoardId(boardId);
        allCards.removeIf(card ->
                card.isBlocked() ||
                        isInFinalColumn(card.getColumnId()) ||
                        isInCancelColumn(card.getColumnId())
        );
        return allCards;
    }

    private boolean isInFinalColumn(int columnId) {
        model.Column column = columnDAO.findById(columnId);
        return column != null && column.getType() == ColumnType.FINAL;
    }

    private boolean isInCancelColumn(int columnId) {
        model.Column column = columnDAO.findById(columnId);
        return column != null && column.getType() == ColumnType.CANCELAMENTO;
    }

    private void performCardMove(Card card, int boardId) {
        model.Column currentColumn = columnDAO.findById(card.getColumnId());

        if (currentColumn == null) {
            System.out.println("Erro: Coluna atual não encontrada.");
            return;
        }

        // Encontrar próxima coluna
        model.Column nextColumn = columnDAO.findNextColumn(boardId, currentColumn.getOrder());

        if (nextColumn == null) {
            System.out.println("Não há coluna seguinte para mover o card.");
            return;
        }

        // Atualizar coluna do card
        if (cardDAO.updateColumn(card.getId(), nextColumn.getId())) {
            // Registrar movimentação
            CardMovement movement = new CardMovement();
            movement.setCardId(card.getId());
            movement.setFromColumnId(currentColumn.getId());
            movement.setToColumnId(nextColumn.getId());
            movementDAO.insert(movement);

            System.out.println("Card movido para: " + nextColumn.getName());
        } else {
            System.out.println("Erro ao mover card.");
        }
    }

    public void cancelCard(int boardId) {
        // Buscar cards não finalizados/cancelados
        List<Card> cards = getCancelableCards(boardId);

        if (cards.isEmpty()) {
            System.out.println("Nenhum card disponível para cancelamento.");
            return;
        }

        System.out.println("\nCards disponíveis para cancelamento:");
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            model.Column column = columnDAO.findById(card.getColumnId());
            System.out.println((i + 1) + " - " + card.getTitle() + " (na coluna: " + column.getName() + ")");
        }

        System.out.print("Selecione o card (número) ou 0 para voltar: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (choice == 0) return;

        if (choice > 0 && choice <= cards.size()) {
            Card card = cards.get(choice - 1);
            performCardCancel(card, boardId);
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    private List<Card> getCancelableCards(int boardId) {
        List<Card> allCards = cardDAO.findByBoardId(boardId);
        allCards.removeIf(card -> isInFinalColumn(card.getColumnId()) || isInCancelColumn(card.getColumnId()));
        return allCards;
    }

    private void performCardCancel(Card card, int boardId) {
        // Encontrar coluna de cancelamento
        model.Column cancelColumn = columnDAO.findByBoardIdAndType(boardId, ColumnType.CANCELAMENTO);

        if (cancelColumn == null) {
            System.out.println("Erro: Coluna de cancelamento não encontrada.");
            return;
        }

        model.Column currentColumn = columnDAO.findById(card.getColumnId());

        // Atualizar coluna do card
        if (cardDAO.updateColumn(card.getId(), cancelColumn.getId())) {
            // Registrar movimentação
            CardMovement movement = new CardMovement();
            movement.setCardId(card.getId());
            movement.setFromColumnId(currentColumn.getId());
            movement.setToColumnId(cancelColumn.getId());
            movementDAO.insert(movement);

            System.out.println("Card cancelado com sucesso!");
        } else {
            System.out.println("Erro ao cancelar card.");
        }
    }

    public void blockCard(int boardId) {
        // Buscar cards não bloqueados
        List<Card> cards = getUnblockedCards(boardId);

        if (cards.isEmpty()) {
            System.out.println("Nenhum card disponível para bloqueio.");
            return;
        }

        System.out.println("\nCards disponíveis para bloqueio:");
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            model.Column column = columnDAO.findById(card.getColumnId());
            System.out.println((i + 1) + " - " + card.getTitle() + " (na coluna: " + column.getName() + ")");
        }

        System.out.print("Selecione o card (número) ou 0 para voltar: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (choice == 0) return;

        if (choice > 0 && choice <= cards.size()) {
            Card card = cards.get(choice - 1);
            System.out.print("Digite o motivo do bloqueio: ");
            String reason = scanner.nextLine();
            performCardBlock(card, reason, true);
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    public void unblockCard(int boardId) {
        // Buscar cards bloqueados
        List<Card> cards = getBlockedCards(boardId);

        if (cards.isEmpty()) {
            System.out.println("Nenhum card disponível para desbloqueio.");
            return;
        }

        System.out.println("\nCards disponíveis para desbloqueio:");
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            model.Column column = columnDAO.findById(card.getColumnId());
            System.out.println((i + 1) + " - " + card.getTitle() + " (na coluna: " + column.getName() + ")");
        }

        System.out.print("Selecione o card (número) ou 0 para voltar: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (choice == 0) return;

        if (choice > 0 && choice <= cards.size()) {
            Card card = cards.get(choice - 1);
            System.out.print("Digite o motivo do desbloqueio: ");
            String reason = scanner.nextLine();
            performCardBlock(card, reason, false);
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    private List<Card> getUnblockedCards(int boardId) {
        List<Card> allCards = cardDAO.findByBoardId(boardId);
        allCards.removeIf(Card::isBlocked);
        return allCards;
    }

    private List<Card> getBlockedCards(int boardId) {
        List<Card> allCards = cardDAO.findByBoardId(boardId);
        allCards.removeIf(card -> !card.isBlocked());
        return allCards;
    }

    private void performCardBlock(Card card, String reason, boolean block) {
        if (cardDAO.updateBlockStatus(card.getId(), block, reason)) {
            // Registrar no histórico
            BlockHistory history = new BlockHistory();
            history.setCardId(card.getId());
            history.setAction(block ? "BLOCK" : "UNBLOCK");
            history.setReason(reason);
            historyDAO.insert(history);

            System.out.println("Card " + (block ? "bloqueado" : "desbloqueado") + " com sucesso!");
        } else {
            System.out.println("Erro ao " + (block ? "bloquear" : "desbloquear") + " card.");
        }
    }

    public List<Card> getCardsByColumn(int columnId) {
        return cardDAO.findByColumnId(columnId);
    }

    public List<Card> getCardsByBoard(int boardId) {
        return cardDAO.findByBoardId(boardId);
    }
}