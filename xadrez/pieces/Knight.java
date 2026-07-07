package xadrez.pieces;

public class Knight extends Piece{
    public Knight(boolean isWhite) {
        // movement_loop = false porque o cavalo tem alcance fixo (salto em L).
        // canJump = true porque o cavalo pula peças no caminho.
        super(isWhite, false, true);

        // 8 saltos em L: combina dx, dy em {-2..+2} e aceita só os pares
        int[][] moves = {
            {-2, -1}, {-2, 1},
            {-1, -2}, {-1, 2},
            { 1, -2}, { 1, 2},
            { 2, -1}, { 2, 1}
        };
        // Adiciona os movimentos válidos à lista de movimentos base.

        for (int[] move : moves) {
            baseMovements.add(new Movement(move[0], move[1]));
        }
    }
    @Override
    public String toString() { 
        return "c"; 
    }
}
