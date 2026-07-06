package xadrez;
import xadrez.pieces.Piece;

// Exibição do tabuleiro no terminal.
//1 representa as peças brancas
//2 representa as peças pretas
public class BoardPrinter {
    public void printBoard(Board board, boolean flip) {
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " | ");
            for (int j = 0; j < 8; j++) {
                Piece p = board.getPiece(i, j);
                if (p == null) {
                    System.out.print("0 ");
                } else if (p.isWhite()) {
                    System.out.print("1 ");
                } else {
                    System.out.print("2 ");
                }
            }
            System.out.println("|");
        }
        System.out.println("    a b c d e f g h");
    }
}
