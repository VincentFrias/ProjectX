package xadrez;
import java.util.Scanner;

// Matheus - 03/06/2026
// Fiz diversas alterações, principalmente na função Main, para criar um loop de jogo, ler os 
// movimentos do usuário e acionar as funções de movimento do tabuleiro. 
// Também adicionei diversas funções no Board, como printBoard, getPiece, setPiece, ValidPosition 
// e movePiece, para facilitar a implementação do jogo. 


public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Scanner sc = new Scanner(System.in);
        board.fillBoard();
        GameRules rules = new GameRules(board);   // motor de regras (xeque, mate, roque, legalidade)
        BoardPrinter printer = new BoardPrinter(); // exibição no terminal

        boolean running = true;
        boolean WhiteTurn = true;

		// Loop principal do jogo: exibe o tabuleiro, solicita o movimento do usuário, valida e executa 
		// o movimento, e alterna o turno.
        while(running) {
            printer.printBoard(board, WhiteTurn);

            // Fim de jogo: o jogador da vez não consegue escapar do xeque.
            if (rules.isCheckmate(WhiteTurn)) {
                System.out.println((WhiteTurn ? "BRANCAS" : "PRETAS") + " EM XEQUE-MATE. \nFIM DE JOGO.");
                break;
            }
            // Empate por afogamento: sem xeque, mas sem nenhuma jogada legal.
            if (rules.isStalemate(WhiteTurn)) {
                System.out.println("AFOGAMENTO. EMPATE.\n");
                break;
            }

            System.out.println((WhiteTurn) ? "BRANCAS JOGAM" : "PRETAS JOGAM");
            // Alerta de xeque colado no prompt do movimento.
            String prefixoXeque = rules.isInCheck(WhiteTurn) ? "EM XEQUE: " : "";
            System.out.println(prefixoXeque + "DIGITE O MOVIMENTO (EX: e2 e4, d2 d4) ");
            String move = sc.nextLine();
			// Divide a entrada em partes e converte as coordenadas do formato "e2" para índices de matriz.
            String[] parts = move.split(" ");
            // Verifica se a entrada tem o formato correto (duas partes: origem e destino)
            if(parts.length != 2) {
                System.out.println("\nMOVIMENTO INVÁLIDO. TENTE NOVAMENTE.");
                continue;
            }
            
			// Converte as coordenadas do formato "e2" para índices de matriz: 'a' -> 0, 'b' -> 1, ..., 'h' -> 7 
			// e os números de 1 a 8 para índices de 7 a 0 (linha 8) -> 0, linha 1 -> 7). 
            int l1 = 8 - Character.getNumericValue(parts[0].charAt(1));
            int c1 = parts[0].charAt(0) - 'a';
            
			// Mesma conversão para a posição de destino
            int l2 = 8 - Character.getNumericValue(parts[1].charAt(1));
            int c2 = parts[1].charAt(0) - 'a';
            
			// Verifica se as posições são válidas e tenta realizar o movimento
			// Se o movimento for bem-sucedido, inverte o turno. Caso contrário, exibe uma mensagem de erro.
            if(board.ValidPosition(l1, c1) && board.ValidPosition(l2, c2)) {
                if (rules.movePiece(l1, c1, l2, c2, WhiteTurn)) { // Aciona a jogada avaliando o retorno
                    // Só inverte o turno se movePiece retornar true
					WhiteTurn = !WhiteTurn; 
                } else { 
					// Se movePiece retornar false, significa que o movimento foi ilegal ou a peça selecionada não pertence ao jogador atual
                    System.out.println("\nMOVIMENTO ILEGAL OU PEÇA INCORRETA. TENTE NOVAMENTE.");
                }
            } else {
				// Se as posições não forem válidas (fora do tabuleiro), exibe uma mensagem de erro
                System.out.println("\nPOSIÇÃO INVÁLIDA. TENTE NOVAMENTE.");
            }   
        }
		// O loop só termina se a variável "running" for definida como false, o que pode ser implementado 
		// futuramente para condições de vitória ou empate.
        sc.close();
    }
}