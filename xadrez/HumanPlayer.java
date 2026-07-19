package xadrez;

import java.util.Scanner;
import xadrez.pieces.Pawn;
import xadrez.pieces.Piece;
import xadrez.pieces.PromotionPiece;

// Jogador humano: lê o lance do teclado e insiste até a entrada estar BEM FORMADA.
// Legalidade não é problema daqui — quem decide isso é o GameRules.
public class HumanPlayer implements Player {

    private final Scanner scanner;

    public HumanPlayer(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Move chooseMove(Board board, GameRules rules, boolean whiteTurn) {
        while (true) {
            System.out.println("DIGITE O MOVIMENTO (EX: e2 e4) OU 'sair' PARA ENCERRAR:");
            String input = scanner.nextLine();

            if (input.trim().equalsIgnoreCase("sair")) {
                return null;
            }

            Move move = Move.parse(input);
            if (move == null) {
                System.out.println("\nENTRADA INVÁLIDA. TENTE NOVAMENTE.");
                continue;
            }

            if (isPromotion(board, move, whiteTurn)) {
                move = move.withPromotion(askPromotion());
            }
            return move;
        }
    }

    // Um peão da cor da vez chegando na última linha.
    private boolean isPromotion(Board board, Move move, boolean whiteTurn) {
        Piece piece = board.getPiece(move.fromRow(), move.fromCol());
        int lastRow = whiteTurn ? 0 : 7;
        return piece instanceof Pawn && piece.isWhite() == whiteTurn && move.toRow() == lastRow;
    }

    private PromotionPiece askPromotion() {
        PromotionPiece choice = null;
        while (choice == null) {
            System.out.println("PROMOÇÃO DE PEÇA:\n Escolha: \n q - Rainha\n r - Torre\n b - Bispo\n n - Cavalo");
            choice = PromotionPiece.fromLetter(scanner.nextLine().trim());
            if (choice == null) {
                System.out.println("OPÇÃO INVÁLIDA.");
            }
        }
        return choice;
    }

    @Override
    public String name() {
        return "Humano";
    }
}