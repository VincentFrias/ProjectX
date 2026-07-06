package xadrez;
import xadrez.pieces.*;

// Board é apenas o ESTADO do tabuleiro: a matriz 8x8 e o acesso a ela.
// As regras do jogo (roque, xeque, xeque-mate, legalidade) ficam em GameRules;
// a exibição no terminal fica em BoardPrinter.
public class Board {
    private Piece[][] board = new Piece[8][8];

    public void fillBoard() {
        // Peças pretas (linhas 0 e 1 da matriz)
        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][3] = new Queen(false);
        board[0][4] = new King(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);
        // Peões pretos (linha 1 da matriz)
        for (int j = 0; j < 8; j++) {
            board[1][j] = new Pawn(false);
        }
        // Peças brancas (linhas 6 e 7 da matriz)
        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][3] = new Queen(true);
        board[7][4] = new King(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);
        // Peões brancos (linha 6 da matriz)
        for (int j = 0; j < 8; j++) {
            board[6][j] = new Pawn(true);
        }
    }

    // Verifica se a posição [l][c] é válida dentro do tabuleiro
    public boolean ValidPosition(int l, int c) {
        if(l < 0 || l > 7 || c < 0 || c > 7) {
            return false;
        }
        return true;
    }

    // Retorna a peça presente na posição [l][c], ou null se não houver peça ou posição inválida
    public Piece getPiece(int l, int c) {
        if(ValidPosition(l, c)) {
            return board[l][c];
        }
        return null;
    }

    // Define a peça presente na posição [l][c] como "piece", se a posição for válida
    public void setPiece(int l, int c, Piece piece) {
        if(ValidPosition(l, c)) {
            board[l][c] = piece;
        }
    }

    // Acesso direto à matriz.
    Piece[][] getBoard() {
        return board;
    }
}
