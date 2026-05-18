package xadrez;
import xadrez.pieces.*;

public class Board {
    private Piece[][] board = new Piece[8][8];

    public void fillBoard() {
        board[0][0] = new Rook(false);
        board[0][7] = new Rook(false);
    }

    // Temporary function to print in terminal. (Replace for GUI later)
    public void printBoard() {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                System.out.printf((board[i][j] == null) ? "0 " : "1 ");
            }
            System.out.println("");
        }
    }
}
