package xadrez.pieces;

import java.util.List;

public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        // movement_loop = false porque o peão tem alcance fixo (não desliza).
        super(isWhite, false);

        // dir define a direção do avanço:
        //   branco anda "pra frente" (linha +1)
        //   preto anda "pra trás" (linha -1)
        int dir = isWhite ? 1 : -1;

        // Avanço normal: 1 casa pra frente, mesma coluna. Só vale se destino vazio.
        baseMovements.add(new Movement(1 * dir, 0, MoveType.MOVE_ONLY));

        // Capturas diagonais: só valem se destino tiver inimigo.
        baseMovements.add(new Movement(1 * dir,  1, MoveType.CAPTURE_ONLY));
        baseMovements.add(new Movement(1 * dir, -1, MoveType.CAPTURE_ONLY));

        // Avanço duplo: 2 casas pra frente, mesma coluna. Só na primeira jogada
        // e (como todo avanço) só se destino vazio.
        specialMovements.add(new Movement(2 * dir, 0, MoveType.MOVE_ONLY));
    }

}
