package xadrez.pieces;

public class Knight extends Piece{
    public Knight(boolean isWhite) {
        // movement_loop = false porque o cavalo tem alcance fixo (salto em L).
        // canJump = true porque o cavalo pula peças no caminho.
        super(isWhite, false, true);

        // 8 saltos em L: combina dx, dy em {-2..+2} e aceita só os pares
        // onde |dx| + |dy| == 3 (forçando um eixo = 1 e o outro = 2).
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (Math.abs(dx) + Math.abs(dy) == 3) {
                    baseMovements.add(new Movement(dx, dy));
                }
            }
        }
    }
}
