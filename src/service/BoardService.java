package service;

import java.util.List;
import java.util.Scanner;
import dao.BoardDAO;
import dao.ColumnDAO;
import model.Board;
import model.Column;
import model.ColumnType;

public class BoardService {
    private Scanner scanner = new Scanner(System.in);
    private BoardDAO boardDAO = new BoardDAO();
    private ColumnDAO columnDAO = new ColumnDAO();
    private Board currentBoard;

    public void createNewBoard() {
        System.out.print("Digite o nome do novo board: ");
        String name = scanner.nextLine();

        Board board = new Board();
        board.setName(name);

        int boardId = boardDAO.insert(board);
        if (boardId > 0) {
            // Criar colunas padrão
            Column[] columns = {
                    new Column(0, boardId, "A Fazer", 1, ColumnType.INICIAL),
                    new Column(0, boardId, "Fazendo", 2, ColumnType.PENDENTE),
                    new Column(0, boardId, "Feito", 3, ColumnType.FINAL),
                    new Column(0, boardId, "Cancelado", 4, ColumnType.CANCELAMENTO)
            };

            for (Column column : columns) {
                columnDAO.insert(column);
            }

            System.out.println("Board '" + name + "' criado com sucesso!");
        } else {
            System.out.println("Erro ao criar board.");
        }
    }

    public void selectBoard() {
        List<Board> boards = boardDAO.findAll();

        if (boards.isEmpty()) {
            System.out.println("Nenhum board encontrado.");
            return;
        }

        System.out.println("\nBoards disponíveis:");
        for (int i = 0; i < boards.size(); i++) {
            System.out.println((i + 1) + " - " + boards.get(i).getName());
        }

        System.out.print("Selecione o board (número) ou 0 para voltar: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (choice == 0) return;

        if (choice > 0 && choice <= boards.size()) {
            currentBoard = boards.get(choice - 1);
            System.out.println("Board '" + currentBoard.getName() + "' selecionado.");
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    public void deleteBoards() {
        List<Board> boards = boardDAO.findAll();

        if (boards.isEmpty()) {
            System.out.println("Nenhum board encontrado.");
            return;
        }

        System.out.println("\nBoards disponíveis para exclusão:");
        for (int i = 0; i < boards.size(); i++) {
            System.out.println((i + 1) + " - " + boards.get(i).getName());
        }

        System.out.print("Selecione o board para excluir (número) ou 0 para voltar: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer

        if (choice == 0) return;

        if (choice > 0 && choice <= boards.size()) {
            Board boardToDelete = boards.get(choice - 1);

            System.out.print("Tem certeza que deseja excluir o board '" + boardToDelete.getName() + "'? (s/n): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("s")) {
                if (boardDAO.delete(boardToDelete.getId())) {
                    System.out.println("Board excluído com sucesso!");
                } else {
                    System.out.println("Erro ao excluir board.");
                }
            }
        } else {
            System.out.println("Seleção inválida.");
        }
    }

    public void viewBoard(int boardId) {
        List<Column> columns = columnDAO.findByBoardId(boardId);
        CardService cardService = new CardService();

        System.out.println("\n=== VISUALIZAÇÃO DO BOARD ===");

        for (Column column : columns) {
            System.out.println("\n--- " + column.getName() + " (" + column.getType() + ") ---");

            List<model.Card> cards = cardService.getCardsByColumn(column.getId());
            if (cards.isEmpty()) {
                System.out.println("  [Nenhum card]");
            } else {
                for (model.Card card : cards) {
                    System.out.println("  • " + card.getTitle() + (card.isBlocked() ? " (BLOQUEADO)" : ""));
                    if (card.getDescription() != null && !card.getDescription().isEmpty()) {
                        System.out.println("    Descrição: " + card.getDescription());
                    }
                }
            }
        }
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Board board) {
        this.currentBoard = board;
    }
}