import java.util.Scanner;
import service.BoardService;
import service.CardService;
import service.ReportService;
import dao.DatabaseConnection;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BoardService boardService = new BoardService();
    private static CardService cardService = new CardService();
    private static ReportService reportService = new ReportService();

    public static void main(String[] args) {
        // Conectar ao banco de dados
        if (!DatabaseConnection.connect()) {
            System.out.println("Falha ao conectar ao banco de dados. Encerrando...");
            return;
        }

        // Criar tabelas se não existirem
        DatabaseConnection.createTables();

        // Menu principal
        mainMenu();

        // Fechar conexão
        DatabaseConnection.close();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Criar novo board");
            System.out.println("2 - Selecionar board");
            System.out.println("3 - Excluir boards");
            System.out.println("4 - Sair");

            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (choice) {
                case 1:
                    boardService.createNewBoard();
                    break;
                case 2:
                    boardService.selectBoard();
                    if (boardService.getCurrentBoard() != null) {
                        boardMenu();
                    }
                    break;
                case 3:
                    boardService.deleteBoards();
                    break;
                case 4:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void boardMenu() {
        while (true) {
            System.out.println("\n=== MENU DO BOARD ===");
            System.out.println("1 - Mover card para próxima coluna");
            System.out.println("2 - Cancelar card");
            System.out.println("3 - Criar card");
            System.out.println("4 - Bloquear card");
            System.out.println("5 - Desbloquear card");
            System.out.println("6 - Visualizar board");
            System.out.println("7 - Gerar relatório de tempos");
            System.out.println("8 - Gerar relatório de bloqueios");
            System.out.println("9 - Fechar board");

            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (choice) {
                case 1:
                    cardService.moveCard(boardService.getCurrentBoard().getId());
                    break;
                case 2:
                    cardService.cancelCard(boardService.getCurrentBoard().getId());
                    break;
                case 3:
                    cardService.createCard(boardService.getCurrentBoard().getId());
                    break;
                case 4:
                    cardService.blockCard(boardService.getCurrentBoard().getId());
                    break;
                case 5:
                    cardService.unblockCard(boardService.getCurrentBoard().getId());
                    break;
                case 6:
                    boardService.viewBoard(boardService.getCurrentBoard().getId());
                    break;
                case 7:
                    reportService.generateTimeReport(boardService.getCurrentBoard().getId());
                    break;
                case 8:
                    reportService.generateBlockReport(boardService.getCurrentBoard().getId());
                    break;
                case 9:
                    System.out.println("Fechando board...");
                    boardService.setCurrentBoard(null);
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}