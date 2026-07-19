package xadrez;
import xadrez.pieces.Piece;

// Exibição do tabuleiro no terminal.
//VERDE representa as peças brancas
//VERMELHO representa as peças pretas
public class BoardPrinter {
    public void printBoard(Board board, boolean flip) {
        String RED = "\u001B[31m";
        String GREEN = "\u001B[32m";
        String RESET = "\u001B[0m";
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " | ");
            for (int j = 0; j < 8; j++) {
                Piece p = board.getPiece(i, j);
                if (p == null) {
                    System.out.print("- ");
                } else if (p.isWhite()) {
                    System.out.print(GREEN + p + " " + RESET);
                } else {
                    System.out.print(RED + p + " " + RESET);
                }
            }
            System.out.println("|");
        }
        System.out.println("    a b c d e f g h");
    }

    // Limpa a tela e devolve o cursor ao topo, para o tabuleiro ser redesenhado
    // sempre no mesmo lugar em vez de rolar. Não apaga o histórico do terminal
    // (\033[3J faria isso) — assim ainda dá para rolar para cima e rever a partida.
    // Depende de o terminal entender ANSI, o mesmo pressuposto que as cores já fazem.
    public void clear() {
        System.out.print("[H[2J");
        System.out.flush();   // sem newline o texto ficaria preso no buffer
    }
}
