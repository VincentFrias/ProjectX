package xadrez;
import xadrez.pieces.*;

    // Matheus - 03/06/2026 
    // Fiz diversas alterações, adicionando praticamente todas as funções, que 
    // acredito ser necessário por hora

public class Board {
    private Piece[][] board = new Piece[8][8];
    private check_move checkMove = new check_move();
    
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

    // Tenta mover a peça da posição [l1][c1] para a posição [l2][c2]. 
    // Retorna true se o movimento for bem-sucedido, false caso contrário.
    public boolean movePiece(int l1, int c1, int l2, int c2, boolean whiteTurn) {
        // Verifica se as posições são válidas e se há uma peça na posição de origem
        if(ValidPosition(l1, c1) && ValidPosition(l2, c2) && board[l1][c1] != null) {
            Piece piece = board[l1][c1];
            
            // Verifica se a peça pertence ao jogador que está tentando mover
            if (piece.isWhite() != whiteTurn) {
                return false;
            }
            
            // Verifica se o movimento é válido para a peça
            if (checkMove.canMoveTo(piece, board, new int[]{l1, c1}, new int[]{l2, c2})) {
                board[l2][c2] = piece;
                board[l1][c1] = null;
                piece.setMoved(); 
                return true; 
            }
        }
        // Se qualquer verificação falhar, o movimento é inválido
        return false; 
    }

    // Função usada unicamente para teste de visualização do tabuleiro no terminal, 
    // depois vai ser substituída por uma interface gráfica
    //1 representa as peças brancas
    //2 representa as peças pretas
    public void printBoard(boolean flip) {
    for (int i=0; i<8; i++) {
        System.out.print((8 - i) + " | ");
        for (int j=0; j<8; j++) {
            if(board[i][j] == null) {
                System.out.print("0 ");
            } else {
                if(board[i][j].isWhite()) {

                    System.out.print("1 ");
                }
                else {
                    System.out.print("2 ");
                }
            }
            
        }
        System.out.println("|"); 
    }
    System.out.println("    a b c d e f g h"); 
    }
}
