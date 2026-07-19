package xadrez;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        board.fillBoard();
        GameRules rules = new GameRules(board);
        System.out.println("LANCES LEGAIS INICIAIS: " + rules.generateLegalMoves(true).size());
        BoardPrinter printer = new BoardPrinter();
        Scanner sc = new Scanner(System.in);

        System.out.println("ESCOLHA O MODO:");
        System.out.println(" 1 - Humano x Humano");
        System.out.println(" 2 - Humano x Computador (mega facil)");

        Player[] players;   // players[0] = brancas, players[1] = pretas
        if (sc.nextLine().trim().equals("2")) {
            players = new Player[]{ new HumanPlayer(sc), new RandomPlayer() };
        } else {
            players = new Player[]{ new HumanPlayer(sc), new HumanPlayer(sc) };
        }

        boolean whiteTurn = true;

        while (true) {
            printer.printBoard(board, whiteTurn);

            if (rules.isCheckmate(whiteTurn)) {
                System.out.println((whiteTurn ? "BRANCAS" : "PRETAS") + " EM XEQUE-MATE. \nFIM DE JOGO.");
                break;
            }
            if (rules.isStalemate(whiteTurn)) {
                System.out.println("AFOGAMENTO. EMPATE.\n");
                break;
            }

            System.out.println(whiteTurn ? "BRANCAS JOGAM" : "PRETAS JOGAM");
            if (rules.isInCheck(whiteTurn)) {
                System.out.println("EM XEQUE!");
            }

            Player current = players[whiteTurn ? 0 : 1];
            Move move = current.chooseMove(board, rules, whiteTurn);

            if (move == null) {
                System.out.println("PARTIDA ENCERRADA PELO JOGADOR.");
                break;
            }
            
            System.out.println(current.name() + " JOGA: " + move);

            if (rules.movePiece(move.fromRow(), move.fromCol(), move.toRow(), move.toCol(),
                    whiteTurn, move.promotionPiece())) {
                whiteTurn = !whiteTurn;
            } else {
                System.out.println("\nMOVIMENTO ILEGAL OU PEÇA INCORRETA. TENTE NOVAMENTE.");
            }
        }

        sc.close();
    }
}